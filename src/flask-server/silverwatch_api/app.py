import sys
from os.path import abspath, dirname
sys.path.insert(0, dirname(dirname(abspath(__file__))))
from flask import Flask, request
from flask_restful import Resource, Api
from flask_cors import CORS
import pymysql
import json
from datetime import datetime as dt
from silverwatch_api.resources.battery import Battery
from silverwatch_api.resources.gps import Gps
from silverwatch_api.resources.push import Push
from silverwatch_api.resources.status import Status
from silverwatch_api.resources.user import User
from silverwatch_api.resources.wear import Wear

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

#set database
DB = 'silver_watch'
USER = 'root'
HOST = 'localhost'
PASSWORD = 'capstone19'
#PASSWORD = '1234qwer'

#db table structure
#watch_user:
#   watch_id: varchar(100) notNull Unique
#   name: varchar(45)
#   phone_number(45)
#watch_gps:
#   id: int(11) notNull autoincrease
#   watch_id: varchar(100) notNull foreignkey
#   latitude: varchar(45) 
#   longitude: varchar(45)
#   time: datetime
#watch_battery:
#   id: int(11) notNull autoincrease
#   watch_id: varchar(100) notNull foreignkey
#   battery: varchar(45)
#   time: datetime
#watch_wear:
#   id: int(11) notNull autoincrease
#   watch_id: varchar(100) notNull foreignkey
#   wear: bool
#   time: datetime


api.add_resource(User, '/user')
api.add_resource(Battery, '/battery')
api.add_resource(Gps, '/gps')
api.add_resource(Push, '/push')
api.add_resource(Status, '/')
api.add_resource(Wear, '/wear')

if __name__ == '__main__':
    app.run(debug=True)
