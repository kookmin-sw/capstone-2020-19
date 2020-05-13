from flask import Flask, request
from flask_restful import Resource, Api
from flask_restful import reqparse, abort
from flask_cors import CORS
import pymysql
import json

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

parser = reqparse.RequestParser()
parser.add_argument('battery', type = str)
parser.add_argument('gps', type = str)
parser.add_argument('watch_id', type = str)

class Check_write_watch_id(Resource):
    #시계 번호 추가
    def post(self, watch_id):
        return 0

    #시계 고유 번호 중복 여부 확인 GET
    def get(self, watch_id):
        f = open("watch_id", "r")
        while(True):
            line = f.readline()
            if not line: break
            if watch_id == line:
                return False
        post(self, watch_id)
        return True
    
class Status(Resource):
    def post(self):
        return {'status' : 'success'}

class Get_battery(Resource):
    def get(self):

        return "battery"

class Get_gps(Resource):
    def get(self):
        return "gps"

api.add_resource(Check_write_watch_id, '/watch_id')
api.add_resource(Get_battery, '/battery')
api.add_resource(Get_gps, '/gps')
api.add_resource(Status, '/usr')

if __name__ == '__main__':
    app.run(debug=True)
