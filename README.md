# Welcome to Silver Watch

#### 노인을 위한 웨어러블 케어 시스템 어플 제작

**팀페이지 주소** -> https://kookmin-sw.github.io/capstone-2020-19

---

![Screen Shot 2020-03-23 at 1 26 33 PM](https://user-images.githubusercontent.com/13490996/77281266-ee65be80-6d09-11ea-995f-7f05512f1f6d.png)



### 1. 프로젝트 소개

- 고령화시대에 접어 들면서 노인에서 가장 자주 일어나는 사고는 바로 낙상사고다. 2016년부터 소비자위해감시 시스템에 수집된 65세 이상 고령자의 안전사고 2만 2천여견 중, 낙상사고가 56%에 달한다. 이 가운데 44%는 골절 증세를 겪어, 심각한 부상으로 이어진다.
- 또한 65세이상 낙상사고는 4년새 52% 증가로 급등하고 대부분의 경우는 집이다. 사고 발생 시 노약자의 가족에게 즉시 알림을 보내 도움을 받을 수 있게 하고자한다.
- 더불어 혼자 생활하는 독거 노인은 도움을 받을 수 있는 방법이 한정적이기 때문에, 지역 사회복지사나 알림서비스에 동의한 주변 이웃에게 자동으로 알림을 보내 즉각 처치를 받을 수 있게 한다.

- 우리 팀은 Wear OS 기반의 웨어러블 기기인 스마트 워치를 노인이 착용하여 낙상을 감지하고 이를 보호자에게 알리는 서비스를 제공하고자 한다.
- 더불어, 일반적인 노인 뿐만 아니라 치매노인의 케어를 위해 GPS센서가 달린 스마트 워치를 이용하여 치매노인의 기존 행동반경을 기록하여 갑작스런 외출이나 실종 시 바로 보호자의 관리자 어플에 경고를 보내 즉각적인 대응을 할 수 있도록 한다. 또한 HR센서를 이용해 착용 여부를 판단하여 데이터 기록이 끊기지 않도록 알려준다.
- 여기서 보호자는 노인의 가족뿐만 아니라 지역 사회복지사도 포함으로, 가족이 없는 독거 노인을 케어하는 데에도 도움이 될 수 있다. 그렇기에 HR 센서를 이용하여 노인들의 하루 활동량을 확인하고, 활동량이 급격히 감소했을 때 역시 관리자 어플에 알림을 보낸다.

보호가 필요한 노인에게 즉각적인 도움을 줄 수 있는 스마트 워치 어플과 노인의 생활 데이터를 열람할 수 있는 관리자 어플, 그리고 머신러닝을 이용해 노인의 낙상사고 패턴을 인식하고 파악하는 노인 종합 보호 시스템을 만들고자 한다.
언제나 관리자가 보호 대상을 케어할 수 있고 수동적 연락이 아닌 자동 경고 알림으로 실시간 서비스를 제공하는 종합 노인 보호 시스템을 만들고자 한다.

### 2. Abstract 

- As our society has started to become aging society, fall accidents are the most
occurring accident that happpen to elderly. According to the data gathered by CISS 
(Consumer Injury Surveillance System) since 2016, 56% of accidents that happened
to elderly were fall accidents. 44% of elderly who underwent these fall accidents
experienced bone fracture which led to severe injury.
- Also, fall accidents that happened to elderly were increase by 52% in last 4 years,
and were mostly happened at home. Therefore, we intend to send emergency alarms
to family members of the older people in case of fall accident.
- Furthermore, we are planning to send alarms automatically to 
local social welfare workers or neighbors who agreed to receive alarms.
Because the help that elderly(who are alone) can get is very limited. 

- Silver Watch team is planning to provide service that senses fall accident of elderly
and sends notification to their guardian. The service will be made with 'Wear OS'
based smart watch.
- Additionally, our service provides care system for dementia patients. By using 
smart watches that include GPS sensor, whenever patients go out of the safe-zone,
(which is set by collecting patients's pattern)notifications are sent to guardian's 
application. With this service, users are capable of taking immediate actions in case
of missing or abrupt changes of their dementia patients. Additionally, by using HR sensor catches whether the user is wearing the watch or not which enable the system to keep data flows consistently.

- In this case, guardian does not only mean elderly people's family. It includes local social welfare workers. In order to care for elderly who are alone without family. Therefore we utilize HR sensor to keep track of elderly people's daily exercise rate. If exercise rate abruptly drop down, alarms are sent to their guardians.

In this project we are going to make smart watch application that gives immediate aid to elderly people who are in need, administrator application that provides elderly people's life pattern, and finally all-around care system for elderly that is capable of recognizing and identifying fall accident patterns using machine learning.
An all-around care system for elderly that allows guardians to take care of their elderly or patients. A system that provides service which automatically, not passively, sends out notifications to users.

### 3. 소개 영상

TBD

### 4. 팀 소개

#### 팀장

##### 오윤재

<img width="159" alt="younjae" src="https://user-images.githubusercontent.com/13490996/77280988-346e5280-6d09-11ea-93e5-e0b394669801.png" style="zoom:150%;" > 

```
이메일: younjae@kookmin.ac.kr
역할: PM / API 서버 개발
```

#### 팀원

##### 박현서

<img width="154" alt="hyeonseo" src="https://user-images.githubusercontent.com/13490996/77280974-2e787180-6d09-11ea-881c-f337352b0ebe.png" style="zoom:150%;" >  

```
이메일: reinstate12@kookmin.ac.kr
역할: Android 관리자 앱 개발
```

##### 송지영

<img width="153" alt="jiyeong" src="https://user-images.githubusercontent.com/13490996/77280985-333d2580-6d09-11ea-8fdf-56b18a39d4d6.png" style="zoom:150%;" >  

```
이메일: songjjy0108@kookmin.ac.kr
역할: 센서 데이터 분석 및 처리 / 동작 감지 알고리즘 개발
```

##### 이수정

<img width="155" alt="sujeong" src="https://user-images.githubusercontent.com/13490996/77280987-33d5bc00-6d09-11ea-987c-60e74260934e.png" style="zoom:150%;" > 

```
이메일: ekfqlc0001@kookmin.ac.kr
역할: 센서 데이터 분석 및 처리 / 동작 감지 알고리즘 개발
```

##### 정지현

<img width="165" alt="jihyeon" src="https://user-images.githubusercontent.com/13490996/77280982-320bf880-6d09-11ea-95a0-16608cd152cf.png" style="zoom:150%;" >  

```
이메일: xcm1321@kookmin.ac.kr
역할: Android Wear 앱 개발 / 서버 인프라 관리
```

##### 사드

<img width="138" alt="saad" src="https://user-images.githubusercontent.com/13490996/77280986-33d5bc00-6d09-11ea-8e47-c45f6045576b.png" style="zoom:150%;" >   

```
이메일: slik.isgr8@gmail.com
역할: API 서버 개발
```

### 5. 사용법

TBD

### 6. 기타

None
