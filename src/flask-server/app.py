import sys
from flask import Flask, request
from flask_restful import Resource, Api
from flask_restful import reqparse, abort
from flask_cors import CORS
import pymysql
import json
from datetime import datetime as dt

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

parser = reqparse.RequestParser()
parser.add_argument('watch_id', type = str)#스마트워치 아이디
parser.add_argument('watch_battery', type = str)#스마트워치 배터리
parser.add_argument('latitude', type = str) #위도
parser.add_argument('longitude', type = str)#경도
parser.add_argument('datetime', type = str)#시간정보
parser.add_argument('wear', type = str) #스마트워치 착용여부
parser.add_argument('name', type = str) #스마트워치 사용자 이름
parser.add_argument('phone_number', type=str) #휴대폰 번호

#set database
DB = 'silver_watch'
USER = 'root'
HOST = 'localhost'
PASSWORD = 'capstone19'
#PASSWORD = '1234qwer'

#db table structure
#watch_user:
#   watch_id: varchar(100) notNull Unique
#   name: varchar(45)
#   phone_number(45)
#watch_gps:
#   id: int(11) notNull autoincrease
#   watch_id: varchar(100) notNull foreignkey
#   latitude: varchar(45) 
#   longitude: varchar(45)
#   time: datetime
#watch_battery:
#   id: int(11) notNull autoincrease
#   watch_id: varchar(100) notNull foreignkey
#   battery: varchar(45)
#   time: datetime
#watch_wear:
#   id: int(11) notNull autoincrease
#   watch_id: varchar(100) notNull foreignkey
#   wear: bool
#   time: datetime

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
            else:
                #기능이 수행되고, 디비에 watch_id가 없을 때
                return {"status" : 1, "reguster_result" : 0}
        except Exception:
            #기능이 제대로 수행하지 않을때
            return {"status": 0, "register_result": 0}

class GetInfomation(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "SELECT * FROM watch_user WHERE watch_id = %s;"
            cusor.execute(sql, (watch_id))
            rows = cusor.fetchone()
            name_result = rows['name']
            phone_number_result = rows['phone_number']
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1, "name" : name_result, "phone_number" : phone_number_result}
        except Exception:
            return {"status":0}


#db에 watch id가 있는지 확인하고, 없으면 db에 저장
#db에 존재하면 return false
#watch_user, watch_gps, watch_battery, watch_wear에 저장
class SetWatchID(Resource):
    #시계 고유 번호 저장
    def post(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        name = args['name']
        phone_number = args['phone_number']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "INSERT INTO watch_user(watch_id, name, phone_number) VALUES(%s, %s, %s)" 
            cusor.execute(sql, (watch_id, name, phone_number))
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

#배터리 기능
class Battery(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "SELECT watch_battery, time FROM watch_battery WHERE watch_id = %s;"
            cusor.execute(sql, (watch_id))
            rows = cusor.fetchone()
            # print(rows)
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
            sql = "update watch_battery set watch_battery= %s, time=now() WHERE watch_id = %s;"
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

#gps 기능
#latitude, longtitude 사용
class Gps(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "SELECT * FROM watch_gps WHERE watch_id = %s;"
            cusor.execute(sql, watch_id)
            rows = cusor.fetchone()
            latitude_result = rows['latitude']
            longitude_result = rows['longitude']
            #print(result)
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1, "latitude" : latitude_result, "longitude" : longitude_result}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

    def post(self):
        args = parser.parse_args()
        latitude = args['latitude']
        longitude = args['longitude']
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            #gps는 지속적인 기록이 필요하기때문에 insert를 사용한다(update를 사용하게되면 지속적인 기록이 아니라 기존 기록이 갱신되기만 함)
            sql = "INSERT INTO watch_gps(watch_id, latitude, longitude, time) values(%s, %s, %s, now())"
            cusor.execute(sql, (watch_id, latitude, longitude))
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

class CheckWear(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "SELECT * from watch_wear where watch_id = %s"
            cusor.execute(sql, (watch_id))
            rows = cusor.fetchone()
            wear_result = rows['wear']
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1, "wear" : wear_result}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

    def post(self):
        args = parser.parse_args()
        wear = args['wear']
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "UPDATE watch_wear set wear=%s, time=now() where watch_id=%s"
            cusor.execute(sql, (wear, watch_id))
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}

def send_fcm_notification(ids, title, body):
    url = 'https://fcm.googleapis.com/fcm/send'

    headers = {
        'Authorization' : 'key=<AAAAlErUq0o:APA91bFRvgzk2pc_QIXklpZUrAE52jYstBl7WuVb7ykv388RBUwfkDZEowJimgM5dj0uuxRdPySOmKNTCEthpLR6cVqTKGB05GLGFR8xFAStLDBaG4tKrjjpZdNWCdeKrUWZNlYMGX2n>'
        'Content-type' : 'application/json; UTF-8'
    }

    content = {
        'registeration_ids' : ids,
        'notification' : {
            'title' : title,
            'body' : body
        }
    }

    requests.post(url, data=json.dumps(content), headers=headers)


api.add_resource(SetWatchID, '/set_watch_id')
api.add_resource(Battery, '/battery')
api.add_resource(Gps, '/gps')
api.add_resource(Status, '/status')
api.add_resource(CheckID, '/check_watch_id')
api.add_resource(CheckWear, '/wear')
api.add_resource(GetInfomation, '/get_information')

if __name__ == '__main__':
    app.run(debug=True)
