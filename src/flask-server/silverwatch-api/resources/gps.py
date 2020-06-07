from flask_restful import Resource
import pymysql
from flask_restful import reqparse, abort


#set database
DB = 'silver_watch'
USER = 'root'
HOST = 'localhost'
PASSWORD = 'capstone19'

parser = reqparse.RequestParser()
parser.add_argument('watch_id', type = str)#스마트워치 아이디
parser.add_argument('latitude', type = str) #위도
parser.add_argument('longitude', type = str)#경도

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