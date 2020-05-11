from flask import Flask
from flask_restful import Resource, Api
from flask_restful import reqparse, abort
from flask_cors import CORS
import pymysql
import json

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

@app.route("/ping", methods=['GET'])
def ping():
    return "pong"

parser = reqparse.RequestParser()
parser.add_argument('battery', type = int)
parser.add_argument('gps', type = tuple)
parser.add_argument('watch_id', type = str)

#시계 번호 추가
def add_watch_id(watch_id):
    f = open("watch_id", "a")
    f.write(watch_id)
    f.close()

#시계 고유 번호 중복 여부 확인
def check_watch_id(watch_id):
    f = open("watch_id", "r")
    while(Ture):
        line = f.readline()
        if not line: break
        if watch_id == line:
            return False
    add_watch_id(watch_id)
    return True

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=True)
