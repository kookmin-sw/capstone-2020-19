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
parser.add_argument('wear', type = str) #스마트워치 착용여부

class Wear(Resource):
    def get(self):
        args = parser.parse_args()
        watch_id = args['watch_id']
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try:
            sql = "SELECT ww.id, ww.watch_id, ww.wear, ww.time, wu.name, wu.phone_number FROM watch_wear as ww JOIN watch_user as wu ON ww.watch_id = wu.watch_id WHERE ww.watch_id = %s;"
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

    
class WearAll(Resource):
    def get(self):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "SELECT ww.id, ww.watch_id, ww.wear, ww.time, wu.name, wu.phone_number FROM watch_wear as ww JOIN watch_user as wu ON ww.watch_id = wu.watch_id;"
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