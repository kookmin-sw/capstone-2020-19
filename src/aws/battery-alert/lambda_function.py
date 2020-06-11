import json
import requests
from pyfcm import FCMNotification

def lambda_handler(event, context):
    r = requests.get("http://203.246.112.155:5000/battery_all")
    battery_results = r.json()["result"]
    print(battery_results)
    
    for battery_result in battery_results:
        if isinstance(battery_result["watch_battery"], str):
            battery_result["watch_battery"] = int(battery_result["watch_battery"])
        # print(battery_result["watch_battery"], type(battery_result["watch_battery"]))
        if isinstance(battery_result["watch_battery"], int) and battery_result["watch_battery"] <= 20:
            push_service = FCMNotification(api_key="AAAAFWjNWao:APA91bGA4SV9-2Uy0OuaHE0rtHDKG9ld_CSlygKrl1kyDn-1pn78yTVB_0fxx2Pjg3vthMkBbetI0qI-ZInMYPhTsGTDCVA72ElIZq35BZ68rFtrDskmY1Pc-CP5I3miuOu0411wVajK")
            registration_id = "fIqUj9dG7zk:APA91bEBS5WJLjaPd3wjFeD4Wrb8xaGotpFpZUkqWyw2Bc6TU7PlKEFDsPcoIQpqqYBTL1B9_sxQEsZ07gunQlqL4Jm_dD1BKs8pdevJDOoSI6mjGbwPQ8bqexT-x_-MJ9EYefjn1wmw"
            message_title = "실버워치 배터리 알림"
            message_body = "배터리 부족 감지됨."
            result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
            
    # TODO implement
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }

