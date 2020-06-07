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
parser.add_argument('watch_battery', type = str)#스마트워치 배터리

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