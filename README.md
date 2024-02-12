# Caffe
## 목차
1. [프로젝트 설명](#프로젝트-설명)
2. [기술 스택](#기술-스택)
3. [프로젝트 구조](#프로젝트-구조)
4. [API 명세](#API-명세)
5. [실행 방법](#실행-방법)

### 1. 프로젝트 설명
1.1 개요
- 카페 메뉴를 관리할 수 있는 웹 서비스 API

1.2 서비스 기능
- 회원가입
- 로그인 및 로그아웃
- 메뉴 등록
- 메뉴 수정
- 메뉴 리스트 조회
- 메뉴 상세 조회
- 메뉴 검색

### 2. 기술 스택
2.1 사용 기술
- kotlin & spring boot
- spring security
- gradle
- jpa
- mysql
- redis
- docker-compose
- kotest

### 3. 프로젝트 구조
#### 3.1 프로젝트 구성도
해당 프로젝트는 hexagonal architecture를 기반으로 구성되어 있습니다.

참조: [hexagonal architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
<img width="775" alt="image" src="https://github.com/yunut/caffe_example/assets/47917032/d0e548ed-2cee-4988-a44f-4de116851924">





#### 3.2 패키지 구조
```
caffe
├── db
│   └── mysql
│       └── data  -- mysql container 데이터 디렉토리
└── src
    ├── main
    │   ├── kotlin
    │   │   └── assignment
    │   │          └── com
    │   │              └── caffe
    │   │                  ├── adapter
    │   │                  │   ├── in
    │   │                  │   │   └── web         -- web layer
    │   │                  │   │      
    │   │                  │   └── out
    │   │                  │       ├── persistence -- persistence(mysql db) layer
    │   │                  │       └── redis       -- redis layer
    │   │                  ├── application
    │   │                  │   ├── domain          -- application service layer
    │   │                  │   └── port            -- application service port
    │   │                  └── common              -- common layer
    │   └── resources
    └── test
        └── kotlin
            └── com
                └── assignment
                    └── caffe
                        ├── domain
                        ├── persistence
                        └── web
```


3.2 DB 스키마
<img width="805" alt="image" src="https://github.com/yunut/caffe_example/assets/47917032/af13bddd-7b31-40d0-be08-d9106bfe330c">


### 4. API 명세
스웨거 사용 시, 특정 환경에서는 동작하지 않는 버그가 있어 스웨거를 사용하지 않고 API 명세를 작성합니다.

- 공통
```json
// 200 OK 
{
   "meta":{
       "code": 200,  
       "message":"ok"  
   }, 
   "data":{
      "products": {
         "id": 1,
         "name": "커피"
      }
   }
}
```
```json
// error 
{
   "meta":{
       "code": 400,
       "message": "error"
  },
  "data": null 
}
```

- 회원가입

```mermaid
sequenceDiagram

client->>web: signup request
web->>server: send request data
server->>databases: exist user
databases-->>server: check
server->>databases: create user
databases-->>server: create
server-->>web: compleate message
web-->>client: send response data
```

POST /signup

Request Body
```json
{
  "phoneNumber": "010-1234-5678",
  "password": "12345678"
}
```

|     이름      |     설명     | 필수 여부 |   타입   |          조건          |
|:-----------:|:----------:| :-------: |:------:|:--------------------:|
| phoneNumber |   휴대폰 번호   | required  | String | xxx-xxxx-xxxx      - |
|  password   |     암호     | required  | String |   8~16자, 영문/숫자 가능    |

response body
```json
200 OK
{
  "meta": {
    "code": 201,
    "message": "User signed up successfully"
  },
  "data": null
}
```

- 로그인
```mermaid
sequenceDiagram

client->>web: signin request
web->>server: send request data
server->>databases: check user info
databases-->>server: check
server->>redis: save refreshToken
redis-->>server: ack
server-->>web: compleate message
web-->>client: send token response
```
POST /signin


request body

|     이름      |     설명     | 필수 여부 |   타입   |          조건          |
|:-----------:|:----------:| :-------: |:------:|:--------------------:|
| phoneNumber |   휴대폰 번호   | required  | String | xxx-xxxx-xxxx      - |
|  password   |     암호     | required  | String |   8~16자, 영문/숫자 가능    |

```json
{
  "phoneNumber": "010-1234-5678",
  "password": "12345678"
}
```
response body
```json
200 OK
{
  "meta": {
    "code": 200,
    "message": "User signed in successfully"
  },
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiZjc3NjRhMy04MmEwLTQzMGQtOTYzOC01YWYyZjcyYTUyZDk6Uk9MRV9VU0VSIiwiaWF0IjoxNzA3NzE5MjE4LCJleHAiOjE3MDc3MzAwMTh9.mNMrLdDq5jMqyv3kXT2p7up9IEkIfJVtuL_oZtiiXqq6UBtp9A-pFXmzrZ1Vah0SUpQ6533zwi92bf7EO2PPuw",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MDc3MTkyMTgsImV4cCI6MTcwNzgwNTYxOH0.csZsSyo_gm2ezS4giBt0JjHd8UoPAcgR2AqldjWqPe-Zo7JJqCnLLtgy2QJpOkIp_DAl4YObg2XRjJ7YpuXU2A"
  }
}
```

- 로그아웃
```mermaid
sequenceDiagram

client->>web: signout request
web->>server: send signout message
server->>redis: remove refreshToken
redis-->>server: ack
server->>redis: add blacklistAccessToken
redis-->>server: ack
server-->>web: compleate message
web-->>client: response status
```

POST /signout

Request Header
```
Authorization: Bearer {accessToken}
```
response body
```json
204 No Content

```

- 메뉴 등록
```mermaid
sequenceDiagram

client->>web: create product request
web->>server: send request data
server->>database: exist data
database-->>server: exist data result
server->>database: insert product data
database-->>server: ack
server->>database: insert consonant data
database-->>server: ack
server-->>web: data id
web-->>client: create product response
```

POST /product/create

Request Header
```
Authorization: Bearer {accessToken}
```
Request Body

|     이름      |   설명   | 필수 여부 |   타입    |      조건       |
|:-----------:|:------:| :-------: |:-------:|:-------------:|
|  category   |  카테고리  | required  | String  |       -       |
|  salePrice  |  판매가   | required  | Integer |     0 초과      |
| orignPrice  |   원가   | required  | Integer |     0 이상      |
|    name     |  상품명   | required  | String  |    100자 이내    |
| description | 상품상세설명 | required  | String  |   1000자 이내    |
| expireDate  |  유통기한  | required  | String  | 2024-01-01 형식 |
|    size     | 상품 사이즈 | required  | String  | SMALL / LARGE |

```json
{
  "category": "COFFEE",
  "salePrice": 3000,
  "orignPrice": 2500,
  "name": "아메리카노",
  "description": "아메리카노는 에스프레소에 물을 타서 만든 커피",
  "expireDate": "2024-01-01",
  "size": "SMALL"
}
```
Response Body
```json
201 Created
{
  "meta": {
    "code": 201,
    "message": "Product create successfully"
  },
  "data": "a7cfe73b-8f89-4bdd-be8f-f410147308e4"
}
```

- 메뉴 수정
```mermaid
sequenceDiagram

client->>web: update product request
web->>server: send request data
server->>database: exist data
database-->>server: exist data result
server->>database: update product data
database-->>server: ack
server->>database: insert consonant data
database-->>server: ack
server-->>web: data id
web-->>client: update product response
```

PATCH /product/{id}

Request Header
```
Authorization: Bearer {accessToken}
```

Request Body

|     이름      |   설명   | 필수 여부 |   타입    |      조건       |
|:-----------:|:------:| :-------: |:-------:|:-------------:|
|  category   |  카테고리  |   | String  |       -       |
|  salePrice  |  판매가   |   | Integer |     0 초과      |
| orignPrice  |   원가   |   | Integer |     0 이상      |
|    name     |  상품명   |   | String  |    100자 이내    |
| description | 상품상세설명 |   | String  |   1000자 이내    |
| expireDate  |  유통기한  |   | String  | 2024-01-01 형식 |
|    size     | 상품 사이즈 |   | String  | SMALL / LARGE |

```json
{
  "orignPrice": 2000
}
```

Response Body
```json
200 OK
{
  "meta": {
    "code": 200,
    "message": "Product update successfully"
  },
  "data": null
}
```

- 메뉴 삭제
```mermaid
sequenceDiagram

client->>web: delete product request
web->>server: send request data
server->>database: delete product
database-->>server: ack
server-->>web: complete message 
web-->>client: delete product response
```
DELETE /product/{id}

Request Header
```
Authorization: Bearer {accessToken}
```

Response Body
```json
200 OK
{
  "meta": {
    "code": 200,
    "message": "Product deleted successfully"
  },
  "data": null
}
```

- 메뉴 리스트 조회
```mermaid
sequenceDiagram

client->>web: list of product request
web->>server: send request data
server->>database: select product
database-->>server: ack
server-->>web: list of product data
web-->>client: list of product response
```

GET /product/list

Request Header
```
Authorization: Bearer {accessToken}
```

Request Parameter

|     이름      |       설명        | 필수 여부 |   타입    |                                               조건                                               |
|:-----------:|:---------------:| :-------: |:-------:|:----------------------------------------------------------------------------------------------:|
|   cursor    |  이전 마지막 상품 데이터  |   | String  |                                               -                                                |
|    size     |      노출 개수      |   | Integer |                                             100 이하                                             |
|    sort     |      정렬 기준      |   | String  |    NAME_ASC, NAME_DESC, CREATED_AT_ASC, UPDATED_AT_ASC, CREATED_AT_DESC,    UPDATED_AT_DESC    |


Response Body
```json
200 OK
{
  "meta": {
    "code": 200,
    "message": "Product list retrieved successfully"
  },
  "data": {
    "products": [
      {
        "id": "92f3d0df-0a36-49e7-92aa-39228fd53bff",
        "category": "category",
        "salePrice": 5000,
        "originPrice": 2000,
        "name": "슈크림 라떼",
        "expireDate": "2024-02-02",
        "size": "SMALL",
        "createdAt": "2024-02-12 06:30:01",
        "updatedAt": "2024-02-12 06:30:01"
      },
      {
        "id": "92f3d0df-0a36-49e7-92aa-39228fd53bff",
        "category": "category",
        "salePrice": 5000,
        "originPrice": 2000,
        "name": "슈크림 라떼",
        "expireDate": "2024-02-02",
        "size": "SMALL",
        "createdAt": "2024-02-12 06:30:01",
        "updatedAt": "2024-02-12 06:30:01"
      }
    ]
  }
}
```

- 메뉴 상세 조회
```mermaid
sequenceDiagram

client->>web: detail of product request
web->>server: send request data
server->>database: select product
database-->>server: ack
server-->>web: detail of product data
web-->>client: detail of product response
```

GET /product/{id}

Request Header
```
Authorization: Bearer {accessToken}
```

Response Body
```json
{
  "meta": {
    "code": 200,
    "message": "Product retrieved successfully"
  },
  "data": {
    "product": {
      "id": "92f3d0df-0a36-49e7-92aa-39228fd53bff",
      "category": "category",
      "salePrice": 5000,
      "originPrice": 2000,
      "name": "슈크림 라떼",
      "description": "설명",
      "expireDate": "2024-02-02",
      "size": "SMALL",
      "createdAt": "2024-02-12 06:30:01",
      "updatedAt": "2024-02-12 06:30:01"
    }
  }
}
```

- 메뉴 검색
```mermaid
sequenceDiagram

client->>web: search product request
web->>server: send request data
server->>database: select product
database-->>server: data
server->>database: select consonant
database-->>server: data
server-->>web: search product data
web-->>client: search product response
```

GET /product/search

Request Header
```
Authorization: Bearer {accessToken}
```

Request Parameter

|   이름    |   설명   | 필수 여부 |   타입    |                                               조건                                               |
|:-------:|:------:| :-------: |:-------:|:----------------------------------------------------------------------------------------------:|
| keyword | 검색 키워드 | required  | String  |                                               -                                                |


Response Body
```json
200 OK
{
  "meta": {
    "code": 200,
    "message": "Product list retrieved successfully"
  },
  "data": {
    "products": [
      {
        "id": "92f3d0df-0a36-49e7-92aa-39228fd53bff",
        "category": "category",
        "salePrice": 5000,
        "originPrice": 2000,
        "name": "슈크림 라떼",
        "expireDate": "2024-02-02",
        "size": "SMALL",
        "createdAt": "2024-02-12 06:30:01",
        "updatedAt": "2024-02-12 06:30:01"
      },
      {
        "id": "92f3d0df-0a36-49e7-92aa-39228fd53bff",
        "category": "category",
        "salePrice": 5000,
        "originPrice": 2000,
        "name": "슈크림 라떼",
        "expireDate": "2024-02-02",
        "size": "SMALL",
        "createdAt": "2024-02-12 06:30:01",
        "updatedAt": "2024-02-12 06:30:01"
      }
    ]
  }
}
```

### 5. 실행 방법
- 프로젝트 root 디렉토리에서 수행합니다.
- mysql container 로딩이 오래 걸려 예외가 발생할 수 있습니다.
```
1. 프로젝트 빌드
 - ./gradlew clean build
 - ./gradlew bootJar  or  ./gradlew build ("DB Conatiner 테스트로 인해 오래 걸림.")

2. 컨테이너 실행
 - make up
```

### 6. TODO
- [x] 상품 리스트 조회, 상품 검색을 검색엔진 으로 변경
  - Elasticsearch 환경 구성
  - pub/sub 구조 구성
  - 상품 등록, 수정, 삭제 시 큐 적재 및 이벤트 발생

- [x] Spring Security에 대한 테스트 코드 작성

