# Wanted-pre-onboarding-backend

## 지원자의 성명
## 윤주호

---

## 클라우드 배포 환경

<img src = aws.png>

## AWS 배포 IP 주소

```text
 3.84.61.37
```


---
## 애플리케이션의 실행 방법
```text
$ ./gradlew clean build -x test
$ sudo docker-compose up -d --build
```
## 엔드포인트 호출 방법
| HTTP Method | End point | Description |
| --- | --- | --- |
| POST | /auth/signup | 회원가입 |
| POST | /auth/login | 로그인 |
| POST | /post/save | 게시물 등록 |
| GET | /post/search/{postId} | 게시물 조회 |
| POST | /post/update/{postId} | 게시물 수정 |
| GET | /post/searchAll | 게시물 목록 |
| POST | /post/delete/{postId} | 게시물 삭제 |

---

## 데이터베이스 테이블 구조

<img src = erd.png>

- 게시판 -> 사용자<br> 다대일 단방향으로 구성 (N:1)
- 한명의 사용자가 여러개의 게시글을 작성할수 있으므로 N:1의 관계로 표현
 

---
## 구현한 API의 동작을 촬영한 데모 영상 링크

[API동작 데모영상](https://youtu.be/hv3pd69gbJc)

---

## 구현 방법 및 이유에 대한 간략한 설명
## 과제 1. 사용자 회원가입 엔드포인트
- 회원가입 시 전달된 JSON 타입을 `UserRequest`객체로 변환해 회원가입을 진행합니다<br>
이때, `Validation`을 통해 객체가 이메일 조건과 패스워드 조건에 부합한지 검증합니다
- 검증이 완료된 객체는 `Service`로 넘어가 `PasswordEncoder`로 비밀번호를 암호화한 후 `Entity`로 변환하여 DB에 저장합니다

## 과제 2. 사용자 로그인 엔드포인트
- 로그인 시 전달된 JSON 타입을 `UserRequest`객체로 변환해 회원가입을 진행합니다<br>
이때, `Validation`을 통해 객체가 이메일 조건과 패스워드 조건에 부합한지 검증합니다
- 사용자 인증을 수행한 후 JWT를 생성, 발행합니다 <br>해당 JWT가 유효한 시간은 yml파일에서 추후 수정 가능합니다
- 유효한 시간이 지나 더이상 유효하지 않은 JWT로는 사용자 인증이 불가하며,<br> 새롭게 JWT를 발행받아야 합니다

## 과제 3. 새로운 게시글을 생성하는 엔드포인트
- 현재 사용자가 새로운 게시글을 생성하려고 할때, `Controller`는 새로운 게시물 정보와, 현재사용자 정보를 받아옵니다
- 사용자 정보와 새로운 게시글 정보를 기반으로 `Post` 객체를 생성 후 DB에 저장합니다

## 과제 4. 게시글 목록을 조회하는 엔드포인트
- 게시글 목록을 조회할 때 `Param`으로 몇번째 페이지인지를 의미하는 page와 <br>해당 페이지에 존재하는 데이터의 개수를 의미하는 size를 전달하여 Pagenation기능을 수행합니다
- page, size의 Default 값을 고정, 설정 해놓지 않아 사용자가 희망하는 page, size를 전달할 수 있게끔 구성하였습니다


## 과제 5. 특정 게시글을 조회하는 엔드포인트
- 특정 게시글을 조회할 때에는 조회하고자 하는 게시물의 id값을 `Param`으로 전달합니다
- 해당하는 게시글이 존재할 시 게시글을 전달합니다

## 과제 6. 특정 게시글을 수정하는 엔드포인트
- 특정 게시글을 수정할 때에는 작성자와 현재 사용자가 같은지 검증하는 과정이 필요해,<br> DB에서 수정하고픈 게시글의 작성자와 현재 접속한 사용자가 동일한 데이터`(id, email, password)`를 가지고 있는지 판단합니다
- 작성자가 아닌 다른 사용자가 수정하려고 할 시 예외를 발생합니다

## 과제 7. 특정 게시글을 삭제하는 엔드포인트
- 특정 게시글을 삭제할 때에는 작성자와 현재 사용자가 같은지 검증하는 과정이 필요해,<br> DB에서 삭제하고픈 게시글의 작성자와 현재 접속한 사용자가 동일한 데이터`(id, email, password)`를 가지고 있는지 판단합니다
- 작성자가 아닌 다른 사용자가 삭제하려고 할 시 예외를 발생합니다

---

## API 명세(request/response 포함)
## 1. 회원가입
### 1.1 Request
- **POST** `/auth/signup`
    ```json
  {
      "email" : "test@test.com",
      "password" : "12345678"
  }
  ```
### 1.2 Response
- **200 OK**
    ```json
    test@test.com
    ```
- **401 UnAuthorized**
  - 이메일, 비밀번호 유효성 검사 실패
    ```json
    {
        "timestamp": "2023-08-11T10:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/auth/signup"
    }
    ```


## 2. 로그인
### 2.1 Request
- **POST** `/auth/login`
    ```json
    {
      "email" : "test@test.com",
      "password" : "12345678"
    }
    ```
### 2.2 Response
- **200 OK**
    ```json
    {
      "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY5MTg0ODM2MH0.IZcDD1gCP7Qn3t7vLQ9r2FyjPvPuHiHR5c5BXE_CjcfWFsn3yAv0Yvz_hcj4v1j3MglJw0_n7j-voDySXXqOxQ"
    }
    ```
- **401 Unauthorized**
  - 등록된 사용자가 아닌 경우
    ```json
    {
        "timestamp": "2023-08-11T11:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/auth/login"
    }
    ```
## 3. 게시물 등록
### 3.1 Request
- **POST** `/post/save`
- **Headers** 
  - `Authorization: Bearer AccessToken `
- **Body**
    ```json
    {
        "title" : "title",
        "content" : "content"   
    }
    ```
### 3.2 Response
- **200 OK**
    ```json
    새로운 게시글이 생성되었습니다
    ```
- **401 Unauthorized**
  - 인증된 사용자가 아닌 경우
    ```json
    {
        "timestamp": "2023-08-11T10:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/post/save"
    }
    ```


## 3. 게시물 조회
### 3.1 Request
- **GET** `/post/search/{postId}`
- **Headers** 
  - `Authorization: Bearer AccessToken `
### 3.2 Response
- **200 OK**
    ```json
    {
      "id": 1,
      "title": "newTitle",
      "content": "newContent"
    }
    ```
- **500 Internal Server Error**
  - 등록되지 않는 게시물 조회 요청
    ```json
    {
      "timestamp": "2023-08-11T14:03:45.709+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/post/search/5"
    }
    ```  
- **401 Unauthorized**
  - 인증된 사용자가 아닌 경우
    ```json
    {
        "timestamp": "2023-08-11T13:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/post/search/5"
    }
    ```

## 4. 게시물 목록 조회
### 4.1 Request
- **GET** `/post/searchAll`
  - params
    - page
    - size
- **Headers** 
  - `Authorization: Bearer AccessToken `
### 4.2 Response
- **200 OK**
  ```json
  {
    "posts": [
        {
            "id": 1,
            "title": "newTitle"
        },
        {
            "id": 2,
            "title": "newTitle"
        },
        {
            "id": 3,
            "title": "newTitle2"
        }
    ],
    "pageInfo": {
        "size": 5,
        "page": 1,
        "totalElements": 3,
        "totalPages": 1
    }
  }
  ```

- **400 Bad Request**
  - params 전달하지 않은 경우
    ```json
    {
      "timestamp": "2023-08-11T14:11:04.344+00:00",
      "status": 400,
      "error": "Bad Request",
      "path": "/post/searchAll"
    }
    ```
- **401 Unauthorized**
  - 인증된 사용자가 아닌 경우
    ```json
    {
        "timestamp": "2023-08-11T13:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/post/searchAll"
    }
    ```

## 5. 게시물 수정
### 5.1 Request
- **POST** `/post/update/{postId}`
- **Headers** 
  - `Authorization: Bearer AccessToken `
- **Body**  
  ```json
  {
      "title" : "newTitle1",
      "content" : "newContent1"
  }
  ```

### 5.2 Response
- **200 OK**
  ```json
  게시글이 수정되었습니다
  ```
- **500 Internal Server Error**
  - 등록되지 않는 게시물 조회 요청
    ```json
    {
      "timestamp": "2023-08-11T14:15:16.089+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/post/update/5"
    }
    ```  
- **500 Internal Server Error**
  - 작성자와 로그인한 사용자가 다른 경우
    ```json
    {
      "timestamp": "2023-08-11T14:18:34.704+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/post/update/1"
    }
    ```
- **401 Unauthorized**
  - 인증된 사용자가 아닌 경우
    ```json
    {
        "timestamp": "2023-08-11T13:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/post/update/1"
    }
    ```


## 6. 게시물 삭제
### 6.1 Request
- **POST** `/post/delete/{postId}`
- **Headers** 
  - `Authorization: Bearer AccessToken `
### 6.2 Response
- **200 OK**
  ```json
  게시글이 삭제되었습니다
  ```
- **500 Internal Server Error**
  - 등록되지 않는 게시물 조회 요청
    ```json
    {
      "timestamp": "2023-08-11T15:48:49.854+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/post/delete/5"
    }
    ```  
- **500 Internal Server Error**
  - 작성자와 로그인한 사용자가 다른 경우
    ```json
    {
      "timestamp": "2023-08-11T15:51:29.854+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/post/delete/3"
    }
    ```
- **401 Unauthorized**
  - 인증된 사용자가 아닌 경우
    ```json
    {
        "timestamp": "2023-08-11T13:17:55.440+00:00",
        "status": 401,
        "error": "Unauthorized",
        "path": "/post/delete/5"
    }
    ```