from flask_restful import Resource

#서버 동작 확인용
class Status(Resource):
    def get(self):
        return {'status' : 'success'}