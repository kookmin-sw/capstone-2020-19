import pymysql
from flask_restful import Resource
from flask_restful import reqparse, abort
from pyfcm import FCMNotification


#set database
DB = 'silver_watch'
USER = 'root'
HOST = 'localhost'
PASSWORD = 'capstone19'

parser = reqparse.RequestParser()
parser.add_argument('watch_id', type = str)#스마트워치 아이디
parser.add_argument('fall_result', type=str) # 낙상 감지 결과

class Push(Resource):
    def post(self):
        try:
            args = parser.parse_args()
            watch_id = args['watch_id']
            fall_result = args['fall_result']
            db = pymysql.connect(host=HOST, user=USER, password=PASSWORD,charset='utf8', db=DB)
            cusor = db.cursor(pymysql.cursors.DictCursor)
            wear_sql = "SELECT * FROM watch_wear WHERE watch_id=%s"
            cusor.execute(wear_sql, (watch_id))
            wear_result = cusor.fetchone()
            if wear_result["wear"] == 1:
                search_sql = "SELECT * FROM watch_user WHERE watch_id=%s"
                cusor.execute(search_sql, (watch_id))
                res = cusor.fetchone()
                name = res["name"]
                print(watch_id, fall_result)
                push_service = FCMNotification(api_key="AAAAFWjNWao:APA91bGA4SV9-2Uy0OuaHE0rtHDKG9ld_CSlygKrl1kyDn-1pn78yTVB_0fxx2Pjg3vthMkBbetI0qI-ZInMYPhTsGTDCVA72ElIZq35BZ68rFtrDskmY1Pc-CP5I3miuOu0411wVajK")
                registration_id = "fIqUj9dG7zk:APA91bEBS5WJLjaPd3wjFeD4Wrb8xaGotpFpZUkqWyw2Bc6TU7PlKEFDsPcoIQpqqYBTL1B9_sxQEsZ07gunQlqL4Jm_dD1BKs8pdevJDOoSI6mjGbwPQ8bqexT-x_-MJ9EYefjn1wmw"
                message_title = "실버워치 낙상 알림"
                message_body = f"{name} 낙상 감지됨."
                result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
                cusor.close()
                db.close()

            return {"status" : 1, "fall_result": fall_result}
        except Exception as e:
            print(e)
            return {"status": 0}
        
