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

class Information(Resource):
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