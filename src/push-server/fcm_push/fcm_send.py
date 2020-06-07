import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
from firebase_admin import datetime

cred = credentials.Certificate('silverwatch-808dd-firebase-adminsdk-yniz6-ef9ea779b1.json')
default_app = firebase_admin.initialize_app(cred)

# This registration token comes from the client FCM SDKs.
topic = 'capstone-19'

# See documentation on defining a message payload.
message = messaging.Message(
    android=messaging.AndroidConfig(
        ttl=datetime.timedelta(seconds=3600),
        priority='normal',
        notification=messaging.AndroidNotification(
            title='USER_NAME',
            body='낙상알림',
            sound='default'
        ),
    ),
    data={
        'watch_id': 'user_name',
        'injury': '1',
    },
    webpush=messaging.WebpushConfig(
        notification=messaging.WebpushNotification(
            title='SilverWatch',
            body='낙상알림',
            icon='',
        ),
    ),
    topic=topic
    
)

# Send a message to the device corresponding to the provided
# registration token.
response = messaging.send(message)
# Response is a message ID string.
print('Successfully sent message:', response)