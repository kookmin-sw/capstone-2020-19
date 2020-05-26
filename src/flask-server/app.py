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
parser.add_argument('watch_battery', type = str)
parser.add_argument('latitude', type = str)
parser.add_argument('longtitude', type = str)

#set database
DB = 'silver_watch'
USER = 'root'
HOST = 'localhost'
PASSWORD = '1234qwer'

#db에 watch_id가 존재하는지 확인
#저장할때 4개의 테이블에 다 저장되니까, parent table인 watch_user에서만 확인해도 된다
class CheckID(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "SELECT watch_id FROM watch_user WHERE watch_id = %s;"
            res = cusor.execute(sql, (watch_id))
            print(res)
            cusor.close()
            db.commit()
            db.close()
            if res:
                #기능이 수행되고, 디비에 watch_id가 존재할때
                return {"status": 1, "register_result": 1}
        except Exception:
            #기능이 제대로 수행하지 않을때
            return {"status": 0, "register_result": 0}

#db에 watch id가 있는지 확인하고, 없으면 db에 저장
#db에 존재하면 return false
#watch_user, watch_gps, watch_battery, watch_wear에 저장
class SetWatchID(Resource):
    #시계 고유 번호 저장
    def post(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "INSERT INTO watch_user(watch_id) VALUES(%s)" 
            cusor.execute(sql, (watch_id))
            sql = "insert INTO watch_gps(watch_id) values(%s)"
            cusor.execute(sql, (watch_id))
            sql = "insert into watch_battery(watch_id) values(%s)"
            cusor.execute(sql, (watch_id))
            sql = "insert into watch_wear(watch_id) values(%s)"
            cusor.execute(sql, (watch_id))
            cusor.close()
            db.commit()
            db.close()
            return {"status":1}
        except Exception :
            cusor.close()
            db.commit()
            db.close()
            return {"status":0}
    
#서버 동작 확인용
class Status(Resource):
    def get(self):
        return {'status' : 'success'}

class Battery(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "SELECT watch_battery FROM watch_battery WHERE watch_id = %s;"
            cusor.execute(sql, (watch_id))
            rows = cusor.fetchone()
            print(rows)
            result = rows['watch_battery']
            cusor.close()
            db.commit()
            db.close()
            return {"status": "1", "battery": result}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : "0"}

    def post(self):
        args = parser.parse_args()
        watch_battery = args['watch_battery']
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "update watch_battery set watch_battery= %s WHERE watch_id = %s;"
            cusor.execute(sql, (watch_battery, watch_id))
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

class Gps(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "SELECT gps FROM watch_user WHERE watch_id = %s;"
            cusor.execute(sql, watch_id)
            rows = cusor.fetchone()
            result = rows['gps']
            #print(result)
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1, "gps" : result}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

    def post(self):
        args = parser.parse_args()
        gps = args['gps']
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "update watch_gps set gps= %s WHERE watch_id = %s;"
            cusor.execute(sql, (gps, watch_id))
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

api.add_resource(SetWatchID, '/set_watch_id')
api.add_resource(Battery, '/battery')
api.add_resource(Gps, '/gps')
api.add_resource(Status, '/status')
api.add_resource(CheckID, '/check_watch_id')

if __name__ == '__main__':
    app.run(debug=True)
