# 최영준

## 애플리케이션 실행 방법

### docker-compose 실행 방법
```
git clone https://github.com/dudwns9280/wanted-pre-onboarding-backend.git
cd docker
docker-compose build
docker-compose up -d
```

### ec2로 배포된 서버
서버 주소 : http://ec2-15-165-160-0.ap-northeast-2.compute.amazonaws.com:18080  
AWS 구성 환경 :  
<img width="750" alt="image" src="https://github.com/dudwns9280/wanted-pre-onboarding-backend/assets/73771805/d5ca1058-93ec-43b7-b70a-252e01cb539a">
- Dockerfile을 통해 java jar 파일 생성 후 실행 가능한 환경 설정.
- docker-compose.yml 파일을 통해 세부 설정 및 container끼리 의존도 설정.
- 프리티어인 t2.micro를 사용하기에 서버 구동하다가 cpu 풀 현상이 확인되어 swapmemory 4gb로 설정하여 사용.

## 데이터 베이스 테이블 구조
<img width="562" alt="image" src="https://github.com/dudwns9280/wanted-pre-onboarding-backend/assets/73771805/431efdbc-6e3b-4d1b-a790-2f92fcb400f7">

## API 동작 데모 영상
[API 데모 영상_최영준](https://drive.google.com/file/d/1DMC3NsN-W-zTlGAV863aAzjQDRhwlbVZ/view?usp=drive_link)

## 구현 방법 및 이유에 대한 간략한 설명
### 과제 1. 사용자 회원가입 엔드포인트
1) 이메일과 비밀번호 유효성 검증
- @Valid 어노테이션을 통해 request 단에서 유효성 검증 진행
- @Email과 @Size를 활용한 이메일, 비밀번호 유효성 검증.
- @Valid 어노테이션을 사용한 이유
  - 유효성 검증을 하는 코드를 삽입하면 비지니스 로직이 가려진다고 생각함
  - 구현 및 에러처리가 간단.
2) 비밀번호 암호화 저장
- BCryptPasswordEncoder를 Spring Security의 bean에 등록하여 사용.
- BCryptPasswordEncoder를 사용하여 password 암호화 진행.
- 사용 이유
  1) 복호화가 불가능한 단방향 해시 함수이기 때문에

### 과제 2. 사용자 로그인 엔드포인트
1) 사용자 조회
- email을 통해 db에서 해당 user 객체를 찾아 반환
- 반환된 user 객체에서 입력된 password와 암호화된 password matches 진행.
- password가 정상적이면 JWT 토큰 발급
2) JWT 토큰 발급
- TokenProvider를 통해 User email 정보를 통해 accessToken과 refreshToken 생성. 
- accessToken이 만료됐을 시 refreshToken을 통해 accessToken을 발급하는 코드는 처리하지 않음. 
- jwt 검증 시 토큰 검증과 토큰에 담긴 email 정보를 통해 db의 user 정보를 가져오는 CustomUserDetail 구현
- CustomUserDetail의 authorities는 user로 고정.
### 과제 3. 새로운 게시글을 생성하는 엔드포인트
1) 게시글 생성
- 헤더에 jwt accessToken을 포함하였고 게시글 제목과 본문을 받아 게시글 생성.
- @AuthenticationPrincipal 을 통해 jwt token에서 user 정보를 가져옴.
- 해당 user를 게시글 작성자로 mapping.
- 구현 이유:
  - 로그인 시에만 발급된 accessToken이 있기에 token에 user정보를 빼온다는건 해당 유저가 로그인했다고 할 수 있기 때문.
  - 문제점은 accessToken의 경우 이미 발급된 토큰에 대해서 제어할 수 없기 때문에 accessToken이 탈취당했을 시 해당 사용자가 로그인한것과 같은 효과를 낸다.
  - 따라서 임시 방편으로 accessToken을 10분주기로 설정함
  - 추후 개선안으로는 로그인 된 사용자를 redis에 token정보를 넣어서 관리한다면 로그인 체크가 가능하다고 생각함.

### 과제 4. 게시글 목록을 조회하는 엔드포인트
- jwt accessToken이 유효한지만 체크.
- page, size를 @RequestParam으로 받아 구현.
- JPA의 pageable을 통한 전체 게시글 조회 시 페이징 처리 기능 구현
- JPA pageable을 사용한 이유
  - Mysql의 limit 기능을 통해 구현할 수 있지만 그렇게 되면 비지니스 로직이 복잡해짐.
  - page처리를 구현 시 간편하게 사용할 수 있기 때문
### 과제 5. 특정 게시글을 조회하는 엔드포인트
- jwt accessToken이 유효한지만 체크.
- 게시글의 id를 @PathParam으로 받아서 db에서 조회.
###  과제 6. 특정 게시글을 수정하는 엔드포인트
- CustomUserDetail을 통해 JWT token에서 user정보를 가져와 조회
- post id와 수정할 내용을 받음.
- post id를 통해 게시글 조회 후 jwt token의 user와 비교하여 작성자 확인.
- 작성자라면 수정.
- CustomUserDetail을 사용한 이유
  - accessToken에 있는 user 정보를 통해 Spring Security에서 db에 접근하여 user 정보를 가져올 수 있기 때문에
  - 만약 CustomUserDetail을 사용하지 않는다면 user 정보를 프론트에서 직접 전달받아야하는데
  - 전달받은 유저와 전달받은 accessToken이 해당 유저가 발급받은 accessToken인지 검증하지 못함.
### 과제 7. 특정 게시글을 삭제하는 엔드포인트
- CustomUserDetail을 통해 JWT token에서 user정보를 가져와 조회
- post id를 받아 게시글 조회 후 jwt token의 user와 비교하여 작성자 확인.
- 작성자라면 삭제.
## API 명세서

## 기본 정보

### BASE URL : http://15.165.160.0:18080

---

## 전체 간단 목록

| 구분 | URL | method |
| --- | --- | --- |
| 회원 가입 | http://15.165.160.0:18080/users/signup | POST |
| 로그인 | http://15.165.160.0:18080/users/signin | POST |
| 게시글 생성 | http://15.165.160.0:18080/posts | POST |
| 게시글 목록 조회 | http://15.165.160.0:18080/posts?page=1&size=4 | GET |
| 특정 게시글 조회 | http://15.165.160.0:18080/posts/{id} | GET |
| 특정 게시글 수정 | http://15.165.160.0:18080/posts/{id} | PATCH |
| 특정 게시글 삭제 | http://15.165.160.0:18080/posts/{id} | DELETE |

---

## User

### 회원 가입
url : http://15.165.160.0:18080/users/signup  
method : POST  
**Request Body**

```
{
    "email": "dudwns9280@naver.com",
    "password" : "12345678"
}
```

**Response**

```
200
```

**Error : 400**

- 이메일 @ 없을 시

    ```
    {
        "status": "BAD_REQUEST",
        "message": "올바른 이메일 형식을 입력해주세요."
    }
    ```

- 비밀번호 8자리 미만일 시

    ```
    {
        "status": "BAD_REQUEST",
        "message": "비밀번호는 8자리 이상으로 입력해주세요"
    }
    ```

- 동일한 이메일의 유저가 db상에 이미 존재할 경우

    ```
    {
        "status": "BAD_REQUEST",
        "message": "이미 존재하는 이메일입니다"
    }
    ```

### 로그인
url : http://15.165.160.0:18080/users/signin  
method : POST  
**Request Body**

```
{
    "email": "dudwns9280@naver.com",
    "password" : "12345678"
}
```

**Response : 200**

```
{
    "id": 3,
    "email": "dudwns9280@naver1.com",
    "jwtToken": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdXRob3JpemF0aW9uIiwiZW1haWwiOiJkdWR3bnM5MjgwQG5hdmVyMS5jb20iLCJleHAiOjE2OTE2NjUyODR9.ZG8ER4i4zeDB8SUpXykgm4jQ7WasymsKPwT2UbJdC9Y",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImR1ZHduczkyODBAbmF2ZXIxLmNvbSIsImV4cCI6MTY5MTkyMzg4NH0.3Bi3I68bW32uk1_ReD7nrKEaBeTzSG9l5gqDX4M1MK0"
    }
}
```

**Response : 400**
- 이메일 @ 없을 시
    ```
    {
        "status": "BAD_REQUEST",
        "message": "올바른 이메일 형식을 입력해주세요."
    }
    ```
- 비밀번호 8자리 미만일 시
    ```
    {
        "status": "BAD_REQUEST",
        "message": "비밀번호는 8자리 이상으로 입력해주세요"
    }
    ```
- 비밀번호가 틀렸을 시

    ```
    {
        "status": "BAD_REQUEST",
        "message": "아이디 혹은 비밀번호 확인 요망"
    }
    ```
**Response: 404**
- email로 유저를 찾지 못한 경우
    ```
    {
        "status": "NOT_FOUND",
        "message": "찾을 수 없습니다."
    }
    ```
---
# **post**

### 게시글 생성
url : http://15.165.160.0:18080/posts  
method : POST  
**Authorization : Bearer Token**  
```
  Token
```
**Request Body**

```
{
    "title":"테스트 게시물",
    "content":"테스트 입니다."
}
```

**Response : 200**

```
{
    "id": 1,
    "title": "테스트 게시물",
    "content": "테스트 입니다.",
    "writer": "dudwns9280@naver.com"
}
```

**Response : 401**
- jwt 토큰이 만료되었을 때

    ```
    {
        "status": "401",
        "message": "토큰이 만료되었습니다."
    }
    ```

**Response : 403**
- jwt 토큰이 위/변조 되었을 때

    ```
    {
        "status": "403",
        "message": "토큰이 위조/변조 되었습니다. 다시 발급받아주세요"
    }
    ```
이후로 jwt 만료는 생략.

### 게시글 목록 조회
url : http://15.165.160.0:18080/posts  
method : GET  
**Authorization : Bearer Token** 
```
  Token
```
**Request Param**
```
?page=1&size=4
```

**Response : 200**

```
{
    "postResponseList": [
        {
            "id": 2,
            "title": "테스트 게시물",
            "content": "테스트 입니다.",
            "writer": "dudwns9280@naver.com"
        },
        {
            "id": 3,
            "title": "테스트 게시물",
            "content": "테스트 입니다.",
            "writer": "dudwns9280@naver.com"
        },
        {
            "id": 4,
            "title": "테스트 게시물",
            "content": "테스트 입니다.",
            "writer": "dudwns9280@naver.com"
        },
        {
            "id": 5,
            "title": "테스트 게시물 2",
            "content": "테스트 입니다. 2",
            "writer": "dudwns9280@naver.com"
        }
    ],
    "totalPage": 2
}
```
### 특정 게시글 조회
url : http://15.165.160.0:18080/posts/{id}  
method : GET  
**Authorization : Bearer Token**
```
  Token
```
**Response : 200**

```
{
    "id": 5,
    "title": "테스트 게시물 2",
    "content": "테스트 입니다. 2",
    "writer": "dudwns9280@naver.com"
}
```
### 특정 게시글 수정
url : http://15.165.160.0:18080/posts/{id}  
method : PATCH  
**Authorization : Bearer Token**
```
  Token
```
**Request Body**

```
{
    "title":"테스트 게시물 수정",
    "content":"테스트 입니다...12#!@#"
}
```

**Response : 200**

```
{
    "id": 2,
    "title": "테스트 게시물 수정",
    "content": "테스트 입니다...12#!@#",
    "writer": "dudwns9280@naver.com"
}
```

**Response : 401**
- 게시물 작성자가 아닌 다른 유저의 jwt 토큰을 넘기면서 수정할 경우.
```
{
  "status": "UNAUTHORIZED",
  "message": "게시글을 수정할 권한이 없습니다."
}
```
### 특정 게시글 삭제
url : http://15.165.160.0:18080/{id}  
method : DELETE  
**Authorization : Bearer Token**  
```
  Token
```

**Response : 200**

**Response : 401**
- 게시물 작성자가 아닌 다른 유저의 jwt 토큰을 넘기면서 삭제할 경우.
```
{
    "status": "UNAUTHORIZED",
    "message": "게시글을 삭제할 권한이 없습니다."
}
```

