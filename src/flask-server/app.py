import sys
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

DB = ''
USER = ''
HOST = ''
PASSWORD = ''

class CheckWriteWatchID(Resource):
    parser.add_argument('watch_id', type = str)
    #시계 고유 번호 중복 여부 확인 GET
    def post(self, watch_id):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)

        return True
    
#서버 동작 확인용
class Status(Resource):
    def get(self):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        return {'status' : 'success'}

class GetBattery(Resource):
    parser.add_argument('battery', type = str)
    def get(self):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        return "battery"

class Get_gps(Resource):
    parser.add_argument('gps', type = str)
    def get(self):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        return "gps"

api.add_resource(Check_write_watch_id, '/watch_id')
api.add_resource(Get_battery, '/battery')
api.add_resource(Get_gps, '/gps')
api.add_resource(Status, '/status')

if __name__ == '__main__':
    app.run(debug=True)
