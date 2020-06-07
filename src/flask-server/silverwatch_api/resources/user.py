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
parser.add_argument('name', type = str) #스마트워치 사용자 이름
parser.add_argument('phone_number', type=str) #휴대폰 번호

#db에 watch id가 있는지 확인하고, 없으면 db에 저장
#db에 존재하면 return false
#watch_user, watch_gps, watch_battery, watch_wear에 저장
class User(Resource):
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
        except Exception as e:
            #기능이 제대로 수행하지 않을때
            print(e)
            return {"status": 0, "register_result": 0}

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
            return {"status": 1}
        except Exception as e:
            print(e)
            cusor.close()
            db.commit()
            db.close()
            return {"status": 0}