import json
import boto3
import datetime
import uuid

class Upload:
    def __init__(self, data):
        now = datetime.datetime.now()
        self.file_name = str(now.strftime("%Y-%m-%d-%H:%M:%S"))
        self.bucket_name = "silverwatch-data"
        self.data = data
        
    def upload_file(self):
        print("Upload File")
        s3 = boto3.client('s3')
        try:
            response = s3.upload_file('/tmp/' + self.file_name, self.bucket_name, self.file_name)
        except Exception as e:
            print(e)
            return False
        return True
    
    def data_to_file(self):
        f = open("/tmp/" + self.file_name, 'w')
        f.write(self.data)
        f.close()
        
    def main(self):
        self.data_to_file()
        return self.upload_file()
        
def lambda_handler(event, context):
    upload = Upload(event["data"])
    return upload.main()

