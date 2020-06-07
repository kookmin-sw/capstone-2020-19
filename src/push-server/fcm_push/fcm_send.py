import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
from firebase_admin import datetime
import requests
import json

# cred = credentials.Certificate('fcm-example-193a8-firebase-adminsdk-w8o5u-2d53e8a7b1.json')
# default_app = firebase_admin.initialize_app(cred)

# # This registration token comes from the client FCM SDKs.
# topic = 'capstone-19'

# # See documentation on defining a message payload.
# message = messaging.Message(
#     android=messaging.AndroidConfig(
#         ttl=datetime.timedelta(seconds=3600),
#         priority='normal',
#         notification=messaging.AndroidNotification(
#             title='USER_NAME',
#             body='낙상알림',
#             sound='default'
#         ),
#     ),
#     data={
#         'watch_id': 'user_name',
#         'injury': '1',
#     },
#     webpush=messaging.WebpushConfig(
#         notification=messaging.WebpushNotification(
#             title='SilverWatch',
#             body='낙상알림',
#             icon='',
#         ),
#     ),
#     topic=topic
    
# )

# # Send a message to the device corresponding to the provided
# # registration token.
# response = messaging.send(message)
# # Response is a message ID string.
# print('Successfully sent message:', response)



def send_fcm_notification(ids, title, body):
    # fcm 푸시 메세지 요청 주소
    url = 'https://fcm.googleapis.com/fcm/send'
    
    # 인증 정보(서버 키)를 헤더에 담아 전달
    headers = {
        'Authorization': 'key=AAAAtuDaTyo:APA91bH3P0dx4rB6Oqret0_dUmwm6mVf4oHcChgmKkKnpiz3G89zv_sH9VJvvivo8rfeefZdjfn30gNow3p2kyqqBg-SMsSDGuCOT1CSNT1PSaVCEO4RzWsVK0v23mHHLAnzKooWmQuj',
        'Content-Type': 'application/json; UTF-8',
    }

    # 보낼 내용과 대상을 지정
    content = {
        'registration_ids': ids,
        'notification': {
            'title': title,
            'body': body
        }
    }

    # json 파싱 후 requests 모듈로 FCM 서버에 요청
    requests.post(url, data=json.dumps(content), headers=headers)