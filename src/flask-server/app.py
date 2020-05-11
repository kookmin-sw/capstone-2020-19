from flask import Flask, jsonify, request
from flask_restful import Resource, Api
from flask_restful import reqparse, abort
from flask_cors import CORS
import pymysql
import json

app = Flask(__name__)
api = Api(app)
cors = CORS(app)
app.users = {}
app.id_count = 1

@app.route("/ping", methods=['GET'])
def ping():
    return "pong"

@app.route("/sign-up", methods = ['POST'])
def sign_up():
    new_user = request.json
    new_user["id"] = app.id_count
    app.users[app.id_count] = new_user
    app.id_count = app.id_count + 1

    return jsonify(new_user)

