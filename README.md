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

## 데이터 베이스 테이블 구조
<img width="562" alt="image" src="https://github.com/dudwns9280/wanted-pre-onboarding-backend/assets/73771805/431efdbc-6e3b-4d1b-a790-2f92fcb400f7">

## API 동작 데모 영상

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


[Overriding vs Overloading](#overriding-vs-overloading)

