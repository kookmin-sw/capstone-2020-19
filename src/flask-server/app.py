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
parser.add_argument('watch_id', type = str)
parser.add_argument('battery', type = str)
parser.add_argument('gps', type = str)

#set database
DB = 'silver_watch'
USER = 'root'
HOST = 'localhost'
PASSWORD = '1234qwer'

#db에 watch id가 있는지 확인하고, 없으면 db에 저장
#db에 존재하면 return false
class SetWatchID(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        sql = "SELECT id FROM watch_user WHERE id == %s;"
        cusor.execute(sql, (watch_id))
        cusor.close()
        db.commit()
        db.close()
        
    #시계 고유 번호 중복 여부 확인 후 저장
    def post(self, watch_id):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        sql = "SELECT id FROM watch_user WHERE id == %s;"
        res = cusor.execute(sql, (watch_id))
        if(res == "NULL"):
            cusor.close()
            db.commit()
            db.close()
            return False
        else: 
            sql = "INSERT INTO watch_user(id) VALUES(%s)"
            cusor.execute(sql, watch_id)
            cusor.close()
            db.commit()
            db.close()
            return True
    
#서버 동작 확인용
class Status(Resource):
    def get(self):
        return {'status' : 'success'}

class GetBattery(Resource):
    def get(self):
        args = parser.parse_args()
        battery = args['battery']
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        sql = "SELECT battery FROM watch_user WHERE id == %s"
        cusor.execute(sql, (watch_id))
        rows = cusor.fetchall()
        print(rows)
        cusor.close()
        db.commit()
        db.close()
        

class GetGps(Resource):
    def get(self):
        args = parser.parse_args()
        gps = args['gps']
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        sql = "SELECT gps FROM watch_user WHERE id == %s"
        res = cusor.execute(sql, watch_id)
        cusor.close()
        db.commit()
        db.close()
        return "gps"

api.add_resource(SetWatchID, '/watch_id')
api.add_resource(GetBattery, '/battery')
api.add_resource(GetGps, '/gps')
api.add_resource(Status, '/status')

if __name__ == '__main__':
    app.run(debug=True)
