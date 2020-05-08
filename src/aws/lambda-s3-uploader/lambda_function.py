import json
import boto3
import datetime
import uuid

class Upload:
    def __init__(self, data, is_walk):
        # now = datetime.datetime.now()
        self.is_walk = True if is_walk == 1 else False
        self.file_name = str(uuid.uuid4()) + ("-walk" if self.is_walk else "-fall")
        self.walk_bucket = "silverwatch-walk"
        self.fall_bucket = "silverwatch-fall"
        self.data = data
        
        
    def upload_file(self):
        print("Upload File")
        s3 = boto3.client('s3')
        try:
            response = s3.upload_file('/tmp/' + self.file_name, self.walk_bucket if self.is_walk else self.fall_bucket, self.file_name)
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
    upload = Upload(event["data"], event["isWalk"])
    return upload.main()

