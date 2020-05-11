from flask import Flask
from flask_restful import Resource, Api
from flask_restful import reqparse, abort
from flask_cors import CORS
import pymysql
import json

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

@app.route("/ping", methods=['GET'])
def ping():
    return "pong"

parser = reqparse.RequestParser()
parser.add_argument('battery', type = int)
parser.add_argument('gps', type = tuple) #위도, 경도를 저장해야함
parser.add_argument('watch_id', type = str)

class Check_write_watch_id(Resource):
    #시계 번호 추가
    def add_watch_id(watch_id):
        f = open("watch_id", "a")
        f.write(watch_id)
        f.close()

    #시계 고유 번호 중복 여부 확인
    def check_watch_id(watch_id):
        f = open("watch_id", "r")
        while(Ture):
            line = f.readline()
            if not line: break
            if watch_id == line:
                return False
        add_watch_id(watch_id)
        return True
    
class Status(Resource):
    def post(self):
        return {'status' : 'success'}


api.add_resource(Check_write_watch_id, '/watch_id')
#api.add_resource(check_battery, '/battery')
#api.add_resource(get_gps, '/gps')
api.add_resource(Status, '/usr')

if __name__ == '__main__':
    app.run(debug=True)
