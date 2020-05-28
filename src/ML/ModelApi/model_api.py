import torch
from torch import nn
from torch.nn import functional as F
from torch.utils.data import TensorDataset, DataLoader
from torch.optim.lr_scheduler import _LRScheduler
from torch.utils.tensorboard import SummaryWriter
import numpy as np
import pandas as pd
from multiprocessing import cpu_count
from pathlib import Path
from flask import Flask, request, jsonify
import json 
import time

seed = 1
np.random.seed(seed)
torch.cuda.set_device(0)

ROOT = Path.cwd().parent/'ML'
SAMPLE = ROOT/'sample_submission.csv'
TRAIN = ROOT/'train_x.csv'
TARGET = ROOT/'train_y.csv'
TEST = ROOT/'test_x.csv'
RESULT = ROOT/'test_y.csv'

ID_COLS = ['series_id', 'measurement_num']

x_cols = {
    'series_id' : np.uint32,
    'measurement_num': np.uint32,
    'x' : np.float32,
    'y' : np.float32,
    'z' : np.float32
}

y_cols = {
    'series_id' : np.uint32,
    'activity' : str
}

class LSTMClassifier(nn.Module):
    """simple version"""
    
    def __init__(self, input_dim, hidden_dim, layer_dim, output_dim):
        super().__init__()
        self.hidden_dim = hidden_dim
        self.layer_dim = layer_dim
        self.rnn = nn.LSTM(input_dim, hidden_dim, layer_dim, batch_first=True)
        self.fc = nn.Linear(hidden_dim, output_dim)
        self.batch_size = None
        self.hidden = None
        
    def forward(self, x):
        h0, c0 = self.init_hidden(x)
        out, (hn, cn) = self.rnn(x, (h0, c0))
        out = self.fc(out[:, -1, :])
        return out
    
    def init_hidden(self, x):
        h0 = torch.zeros(self.layer_dim, x.size(0), self.hidden_dim)
        c0 = torch.zeros(self.layer_dim, x.size(0), self.hidden_dim)
        return [t.cuda() for t in (h0, c0)]
    
device = torch.device("cuda")
model = LSTMClassifier(3, 10, 3, 2)
model.load_state_dict(torch.load('best.pth', map_location="cuda:0"))
model.to(device)

def create_datasets(X, y, test_size=0.2, dropcols = ID_COLS, time_dim_first=False):
    enc = LabelEncoder()
    y_enc = enc.fit_transform(y)
    X_grouped = create_grouped_array(X)
    if time_dim_first:
        X_grouped = X_grouped.transpose(0, 2, 1)
    
    X_train, X_valid, y_train, y_valid = train_test_split(X_grouped, y_enc, test_size = 0.1)
    X_train, X_valid = [torch.tensor(arr, dtype=torch.float32) for arr in (X_train, X_valid)]
    y_train, y_valid = [torch.tensor(arr, dtype=torch.float32) for arr in (y_train, y_valid)]

    train_ds = TensorDataset(X_train, y_train)
    valid_ds = TensorDataset(X_valid, y_valid)
    
    return train_ds, valid_ds, enc

def create_grouped_array(data, group_col='series_id', drop_cols=ID_COLS):
        X_grouped = np.row_stack([
            group.drop(columns=drop_cols).values[None]
            for _, group in data.groupby(group_col)
        ])
        
        return X_grouped

def create_test_dataset(X, drop_cols=ID_COLS):
    X_grouped = np.row_stack([
        group.drop(columns=drop_cols).values[None]
        for _, group in X.groupby('series_id')
    ])
    X_grouped = torch.tensor(X_grouped.transpose(0, 2, 1)).float()
    y_fake = torch.tensor([0] * len(X_grouped)).long()
    return TensorDataset(X_grouped, y_fake)
    
def create_loaders(train_ds, valid_ds, bs=512, jobs=0):
    train_dl = DataLoader(train_ds, bs, shuffle=True, num_workers=jobs)
    valid_dl = DataLoader(valid_ds, bs, shuffle=False, num_workers=jobs)
    
    return train_dl, valid_dl

def accuracy(output, target):
    return(output.argmax(dim=1) == target).float().mean().item()

def get_prediction(x_tst):
    test_dl = DataLoader(create_test_dataset(x_tst), batch_size=64, shuffle=False)
    test = []

    for batch, _ in test_dl:
        batch = batch.permute(0, 2, 1)
        out = model(batch.cuda())
        y_hat = F.log_softmax(out, dim=1).argmax(dim=1)
        test += y_hat.tolist()
    return test

app = Flask(__name__)


@app.route('/', methods=['POST',])
def data_post():
    if request.method == 'POST':
        text = request.data.decode("utf-8")
        p_dict = eval(text)
        data = p_dict['data']
        df = pd.DataFrame([x.split(',') for x in data.split('\n')[1:-1]], columns=[x for x in data.split('\n')[0].split(',')])
        df_len = len(df)
        df.insert(0, "measurement_num", [i for i in range(df_len)])
        df.insert(0, "series_id", [0] * df_len)
        df = df.astype({'series_id': np.uint32, 'measurement_num': np.uint32, 'x':np.float32, 'y':np.float32, 'z':np.float32})
        df.dtypes
        # result = 0:fall / 1:walk
        pred_start_time = time.time()
        result = get_prediction(df)
        pred_end_time = time.time()
        print(p_dict['id'], result[0])
        print('prediction takes ', pred_end_time - pred_start_time, 'seconds')
        return json.dumps({'status' : 200, 'result': result[0]})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)


