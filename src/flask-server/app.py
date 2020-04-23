from flask import Flask, jsonify, request
import bcrypt

app = Flask(__name__)
app.users = {}
app.id_count = 1

@app.route("/ping", methods=['GET'])
def ping():
    return "pong"

@app.route("/sign-up", methods=['POST'])
def sign_up():
    new_user = request.json
    new_user['password'] = bcrypt.hashpw(
            new_user['password'd].encode('UTF-8'),
            bcrypt.gensalt()
            )

    new_user_id = app.database.execute(text("""
    INSERT INTO users(
        name,
        address,
        profile,
        hased_password)
    VALUE(
        :name,
        :address,
        :profile,
        )
        """
        ), new_user).lastrowid
    new_user_info = get_user(new_user_id)

    return jsonify(new_user_info)

    return jsonify(new_user)
