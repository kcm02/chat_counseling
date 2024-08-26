# ㅇㅁ 상담소

> _**이 프로젝트는 아직 개발중인 프로젝트입니다.**_

## 프로젝트 개요

**ㅇㅁ 상담소**는 실시간 소켓 통신, 데이터 암호화, 매칭 시스템 등으로 익명성을 보장한 멘토링 및 고민 상담 플랫폼입니다. 주요 기능으로는 사용자 가입, 로그인, 채팅방 관리, 실시간 채팅 기능 등이 있습니다.

## 주요 기능

- **사용자 인증**: 이메일 인증을 통한 회원가입, 로그인, JWT 기반의 인증 시스템.
- **채팅 기능**: 채팅방 생성, 채팅 메시지 전송 및 관리, 실시간 채팅 기능.
- **웹 소켓**: 실시간 메시지 전송을 위한 웹 소켓 구현.

## 기술 스택

- **Spring Boot**: 백엔드 서버
- **Spring Security**: 인증 및 권한 관리
- **Spring WebSocket**: 실시간 채팅 기능
- **Hibernate**: ORM
- **MySQL**: 데이터베이스

## 설치 및 실행

### 요구 사항

- JDK 17
- Maven
- MySQL 데이터베이스

### 설정

1. **클론**

   ```bash
   git clone https://github.com/your-repository/secure-login-project.git
   ```

2. **데이터베이스 설정**

   `src/main/resources/application.properties` 파일을 열어 데이터베이스 설정을 수정합니다.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your-database
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

3. **빌드 및 실행**

   Maven을 사용하여 프로젝트를 빌드하고 실행합니다.

   ```bash
   cd secure-login-project
   mvn clean install
   mvn spring-boot:run
   ```


## API 명세

### 사용자 인증

| **엔드포인트**                    | **메서드** | **설명**                           | **요청 형식**                                                        | **응답 형식**                                                      | **상태 코드**                                                                                          |
|-----------------------------------|------------|------------------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| `/api/auth/login`                 | POST       | 사용자를 로그인시키고 JWT를 발급합니다. | `{"username": "exampleUser", "password": "yourpassword"}`             | `{"statusCode": 200, "message": "로그인 성공", "token": "your-jwt-token"}` | 200 OK: 로그인 성공<br>404 NOT FOUND: 사용자 찾을 수 없음<br>401 UNAUTHORIZED: 비밀번호 오류<br>423 LOCKED: 계정 잠금 |
| `/api/auth/logout`                | DELETE     | 사용자의 JWT를 무효화하여 로그아웃합니다. | `Authorization: Bearer your-jwt-token`                                | 없음 (`204 NO CONTENT`)                                          | 204 NO CONTENT: 로그아웃 성공<br>500 INTERNAL SERVER ERROR: 로그아웃 실패                                                        |

### 사용자 관리

| **엔드포인트**                    | **메서드** | **설명**                           | **요청 형식**                                                        | **응답 형식**                                                      | **상태 코드**                                                                                          |
|-----------------------------------|------------|------------------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| `/api/users/signup`               | POST       | 새로운 사용자를 등록합니다.         | `{"username": "exampleUser", "email": "user@example.com", "password": "yourpassword"}` | `{"statusCode": 201, "message": "회원가입 성공", "user": {"id": 1, "username": "exampleUser", "email": "user@example.com"}}` | 201 CREATED: 회원 가입 성공<br>400 BAD REQUEST: 이미 등록된 이메일 |
| `/api/users/{userId}`             | GET        | 사용자의 정보를 조회합니다.          | 없음 (URL 파라미터: `userId`)                                         | `{"statusCode": 200, "message": "정보 조회 성공", "user": {"id": 1, "username": "exampleUser", "email": "user@example.com"}}` | 200 OK: 정보 조회 성공<br>404 NOT FOUND: 사용자를 찾을 수 없음                                                    |
| `/api/users/{userId}`             | PUT        | 사용자의 정보를 수정합니다.          | `{"username": "newUsername", "email": "newemail@example.com"}`        | `{"statusCode": 200, "message": "정보 수정 성공", "user": {"id": 1, "username": "newUsername", "email": "newemail@example.com"}}` | 200 OK: 정보 수정 성공<br>400 BAD REQUEST: 잘못된 요청                                                        |
| `/api/users/{userId}`             | DELETE     | 사용자를 삭제합니다.                 | 없음 (URL 파라미터: `userId`)                                         | `{"statusCode": 204, "message": "회원 삭제 성공"}` | 204 NO CONTENT: 사용자 삭제 성공<br>404 NOT FOUND: 사용자를 찾을 수 없음                                             |
| `/api/users/verify/{token}`       | GET        | 사용자의 이메일 인증을 완료합니다.   | 없음 (URL 파라미터: `token`)                                           | `{"statusCode": 200, "message": "이메일 인증 완료"}` | 200 OK: 인증 성공                                                                                   |

### 채팅 관리

| **엔드포인트**                    | **메서드** | **설명**                           | **요청 형식**                                                        | **응답 형식**                                                      | **상태 코드**                                                                                          |
|-----------------------------------|------------|------------------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| `/api/chatrooms`                  | POST       | 새로운 채팅방을 생성합니다.           | `{"chatRoomName": "General Chat"}`                                     | `{"statusCode": 201, "message": "채팅방 생성 성공", "chatRoom": {"id": 1, "chatRoomName": "General Chat"}}` | 201 CREATED: 채팅방 생성 성공<br>400 BAD REQUEST: 잘못된 요청                                                        |
| `/api/chatrooms/{chatRoomId}`     | GET        | 채팅방의 세부 정보를 조회합니다.      | 없음 (URL 파라미터: `chatRoomId`)                                      | `{"statusCode": 200, "message": "채팅방 정보 조회 성공", "chatRoom": {"id": 1, "chatRoomName": "General Chat"}}` | 200 OK: 채팅방 정보 조회 성공<br>404 NOT FOUND: 채팅방을 찾을 수 없음                                                    |
| `/api/messages`                  | POST       | 채팅방에 메시지를 전송합니다.          | `{"chatRoomId": 1, "senderId": 1, "content": "Hello!", "messageType": "TEXT"}` | `{"statusCode": 201, "message": "메시지 전송 성공", "chatMessage": {"id": 1, "content": "Hello!"}}` | 201 CREATED: 메시지 전송 성공<br>400 BAD REQUEST: 잘못된 요청                                                        |
| `/api/messages/{messageId}`      | PUT        | 메시지를 수정합니다.                 | `{"content": "Updated content"}`                                       | `{"statusCode": 200, "message": "메시지 수정 성공", "chatMessage": {"id": 1, "content": "Updated content"}}` | 200 OK: 메시지 수정 성공<br>404 NOT FOUND: 메시지를 찾을 수 없음                                                        |
| `/api/messages/{messageId}`      | DELETE     | 메시지를 삭제합니다.                 | 없음 (URL 파라미터: `messageId`)                                       | `{"statusCode": 204, "message": "메시지 삭제 성공"}` | 204 NO CONTENT: 메시지 삭제 성공<br>404 NOT FOUND: 메시지를 찾을 수 없음                                                        |

### 웹 소켓

- **연결 URL**: `ws://localhost:8080/ws/chat?roomId={roomId}`

  실시간으로 채팅방에 메시지를 주고받을 수 있습니다. `roomId`는 채팅방 ID를 지정합니다.

## 설정 및 실행

1. **클론**

   ```bash
   git clone https://github.com/your-repository/secure-login-project.git
   ```

2. **데이터베이스 설정**

   `src/main/resources/application.properties` 파일을 열어 데이터베이스 설정을 수정합니다.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your-database
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

3. **빌드 및 실행**

   Maven을 사용하여 프로젝트를 빌드하고 실행합니다.

   ```bash
   cd secure-login-project
   mvn clean install
   mvn spring-boot:run
   ```
