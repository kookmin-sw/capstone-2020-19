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

    
class WearAll(Resource):
    def get(self):
        db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
        cusor = db.cursor(pymysql.cursors.DictCursor)
        try: 
            sql = "SELECT * FROM watch_wear;"
            cusor.execute(sql)
            rows = cusor.fetchall()
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