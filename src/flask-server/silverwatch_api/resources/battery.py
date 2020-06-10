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
            sql = "SELECT wb.id, wb.watch_id, wb.watch_battery, wb.time, wu.name, wu.phone_number FROM watch_battery as wb JOIN watch_user as wu ON wb.watch_id = wu.watch_id; WHERE wb.watch_id = %s;"
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


class BatteryAll(Resource):
    def get(self):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "SELECT wb.id, wb.watch_id, wb.watch_battery, wb.time, wu.name, wu.phone_number FROM watch_battery as wb JOIN watch_user as wu ON wb.watch_id = wu.watch_id;"
            cusor.execute(sql)
            rows = cusor.fetchall()
            for i in range(len(rows)):
                try:
                    rows[i]["time"] = rows[i]["time"].strftime("%Y/%m/%d %H:%M:%S")
                except:
                    rows[i]["time"] = "null"
            print(rows)
            #print(result)
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 1, "result": rows}
        except Exception:
            cusor.close()
            db.commit()
            db.close()
            return {"status" : 0}