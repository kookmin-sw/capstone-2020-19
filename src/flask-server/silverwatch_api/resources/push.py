from flask_restful import Resource
from flask_restful import reqparse, abort


parser = reqparse.RequestParser()
parser.add_argument('watch_id', type = str)#스마트워치 아이디
parser.add_argument('fall_result', type=str) # 낙상 감지 결과

class Push(Resource):
    def post(self):
        try:
            args = parser.parse_args()
            watch_id = args['watch_id']
            fall_result = args['fall_result']
            return {"status" : 1, "fall_result": fall_result}
        except Exception as e:
            print(e)
            return {"status": 0}
        