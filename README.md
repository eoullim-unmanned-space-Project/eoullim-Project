# 🚀 어울림 (Eoulrim) - 무인공간 대여 서비스

<p align="center">
  <img width="500" 
  height="400" alt="Image"
  src="https://github.com/user-attachments/assets/0ac8498e-0360-49ff-9eb4-e6ed6cad6021"
/>
</p>

## 프로젝트 소개

**어울림(Eoulrim)** 은 **무인 공간 대여 서비스**로,  
다음과 같은 특징을 제공합니다.

- **비대면 예약 시스템**  
  사용자는 원하는 공간과 시간을 선택해  
  실시간으로 예약 가능 여부를 확인할 수 있습니다.

- **간편 결제 & 자동 처리**  
  결제 완료 후 별도의 절차 없이  
  즉시 이용이 가능한 상태로 전환됩니다.

- **QR 코드 기반 출입**  
  예약 완료 시 발급되는 **QR 코드**를 통해  
  현장 키오스크 또는 출입 시스템에서 바로 입장할 수 있습니다.

- **빠르고 효율적인 공간 이용**  
  직원 대기 없이 예약부터 입장까지  
  모든 과정을 한 번에 처리할 수 있습니다.

⏩**서비스 플로우**
```
회원가입/로그인 → 카테고리/장소 선택 → 룸 및 날짜 선택 
→ 타임슬롯 예약 (실시간 동기화) → 결제 → QR 코드 발급 
→ 공간 입장 → 이용 완료 → 리뷰 작성 (AI 필터링)
```
⏰**프로젝트 기간**
 -  **2025-12-11 ~ 2026-01-21 (30일)**

## 왜 어울림인가?
**🤖 AI 기반 지능형 서비스**
- 실시간 비속어 필터링: Gemini AI를 활용한 비속어 필터링을 지원합니다.
- 지능형 Q&A 챗봇: 24/7 AI 챗봇 지원합니다.
- 개인화 콘텐츠: AI 기반 오늘의 운세 제공합니다.

**⚡ 실시간 동기화 시스템**
- 중복예약 원천 차단: SSE(Server Sent Event)기반 타임슬롯 실시간 업데이트를 지원합니다.
- 즉각적인 상태 반영: 예약/취소 시 모든 클라이언트 동시 업데이트를 지원합니다.
- 1:1실시간 상담: SSE(Server Sent Event)기반 실시간 채팅 지원합니다.

**🔐 안전한 결제 시스템**
- 포트원(아임포트)연동: 카카오페이 등 결제 수단을 제공합니다.
- 자동 환불 처리: 주문 취소 시 API 기반의 자동 환불 처리를 통해 즉각적인 환불 프로세스를 구현했습니다.
- 결제 무결성 검증: (위변조 방지): 서버 사이드 결제 무결성 검증 로직을 통해 결제 금액 및 상태 위변조를 원천적으로 차단합니다.
- QR 코드 인증: QR 코드 인증 방식을 적용하여 안전한 공간 출입 관리를 제공합니다.

**🎨 사용자 중심 설계**
- 카카오 소셜 로그인(OAuth2.0): 간편한 회원가입 & 로그인
- 직관적인 예약 프로세스: 복잡한 새로고침 없이, SSE(Server Sent Event)를 활용한 실시간 예약 반영으로 즉각적인 상태 확인

## 팀 소개
   
<table align="center">
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/482bfc07-58ff-4c94-940e-43a2dd31e83f" width="150" height="200" style="display: block; object-fit: full; border-radius: 50%;"/><br />
      <b>김승민</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/beaead54-2d5d-4d03-bd1e-bd5be05c6f39" width="150" height="200" style="display: block; object-fit: cover; border-radius: 50%;"/><br />
      <b>박진영</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/fa2bafaa-d5ce-439c-a5fc-f5dabff2d4ec" width="150" height="200" style="display: block; object-fit: cover; border-radius: 50%;"/><br />
      <b>이상은</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/594cd046-dcd8-4403-a091-d1c6aa2856fe" width="150" height="200" style="display: block; object-fit: cover; border-radius: 50%;"/><br />
      <b>최광섭</b>
    </td>
  </tr>
</table>


## 기술 스택

### 백엔드
![Java](https://img.shields.io/badge/Java_17-ED8A00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### 프론트엔드
![Mustache](https://img.shields.io/badge/Mustache-FF530F?style=for-the-badge&logo=mustache&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

### API
![SOLAPI](https://img.shields.io/badge/SOLAPI-FF6F00?style=for-the-badge&logo=messagebird&logoColor=white) 
![PortOne](https://img.shields.io/badge/PortOne_KakaoPay-FF5D5D?style=for-the-badge&logo=p&logoColor=white)
![Kakao](https://img.shields.io/badge/Kakao_Login-FFCD00?style=for-the-badge&logo=kakaotalk&logoColor=black)
![Gemini](https://img.shields.io/badge/Gemini_AI-8E75C2?style=for-the-badge&logo=googlegemini&logoColor=white)
![Gmail](https://img.shields.io/badge/Gmail-EA4335?style=for-the-badge&logo=gmail&logoColor=white)

### 협업 툴
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

## 채택한 기술과 이슈 & 깃 브랜치 전략
🎨 **Mustache**
- 로직 없는 템플릿: View와 비즈니스 로직 완전 분리
- XSS자동 방지: HTML 이스케이핑 기본 적용
- 경량성: 빠른 렌더링 속도

 ⚡**SSE (Server-Sent Events)**
- 단방향 실시간 통신: 서버 -> 클라이언트 데이터 푸시에 최적
- WebSocket 대비 간단한 구현: HTTP 프로토콜 기반
- 자동 재연결: 네트워크 끊김 시 자동 복구
- 작용
  - 타임슬롯 실시간 업데이트
  - 1:1 상담 메시지

🔐 **Session기반 인증&인가**
- **서버 측 강력한 제어**: 세션 저장소를 통해 비정상적인 접근 발생 시 즉각적인 세션 무효화 가능
- **CSRF(Cross-Site Request Forgery)방어**
  - CSRF 토큰 적용: 상태 변경 요청(POST,PUT,DELETE)에 대해 서버가 발급한 고유번호 <code>X-CSRF-TOKEN 헤더</code>를 검증하여 위조요청 차단
  - SameSite 쿠키 설정: 외부 사이트에서의 쿠키 전송을 제한하여 보안성 강화
- **Spring Security기반 권한 관리**
  - 계층적 권한 제어:<code>@PreAuthorize</code>를 사용한 <code>ROLE_ADMIN</code>,<code>ROLE_USER</code> API별 권한 명시
  - 보안 필터 체인(Filter Chain): 인증되지 않은 사용자의 보호 자원 접근을 원천 차단하는 표준 보안 프로세스 준수

🤖 **Gemini AI**
- 다국어 지원: 한국어 자연어 처리 우수
- 빠른 응답 속도: 실시간 필터링에 적합
- 무료 티어: 개발 단계에서 비용 절감

⚫ **깃 브랜치전략**

👉 [컨벤션 문서 (CONVENTION.md)](https://github.com/eoullim-unmanned-space-Project/eoullim-Project/blob/develop/CONVENTION.md)

## 시작하기
사전 요구사항
- **Java**: 17이상
- **MySQL**: 8.0이상
- **Gradle**: 8.x
- **IDE**: IntelliJ IDEA 권장

**⚙️ 환경설정**

#### 1. 데이터베이스 설정

```sql
CREATE DATABASE eoullim CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'eoullim_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON eoullim.* TO 'eoullim_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 2. 환경 변수 설정

`src/main/resources/application-local.yml` 생성:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eoullim?serverTimezone=Asia/Seoul
    username: eoullim_user
    password: your_password
    
  # 카카오 로그인
  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: ${KAKAO_CLIENT_ID}
            client-secret: ${KAKAO_CLIENT_SECRET}

# 포트원 결제
portone:
  api-key: ${PORTONE_API_KEY}
  api-secret: ${PORTONE_API_SECRET}

# Gemini AI
gemini:
  api-key: ${GEMINI_API_KEY}

# Gmail SMTP
spring:
  mail:
    username: ${GMAIL_USERNAME}
    password: ${GMAIL_APP_PASSWORD}

# SOLAPI (문자 발송)
solapi:
  api-key: ${SOLAPI_API_KEY}
  api-secret: ${SOLAPI_API_SECRET}

# Gemini
gemini:
  api-key: ${GEMINI_API_KEY}
```

#### 3. 환경 변수 등록

**Linux/Mac**:
```bash
export KAKAO_CLIENT_ID=your_kakao_client_id
export KAKAO_CLIENT_SECRET=your_kakao_secret
export PORTONE_API_KEY=your_portone_key
export PORTONE_API_SECRET=your_portone_secret
export GEMINI_API_KEY=your_gemini_key
export GMAIL_USERNAME=your_email@gmail.com
export GMAIL_APP_PASSWORD=your_app_password
export SOLAPI_API_KEY=your_solapi_key
export SOLAPI_API_SECRET=your_solapi_secret
```

**Windows**:
```cmd
set KAKAO_CLIENT_ID=your_kakao_client_id
set KAKAO_CLIENT_SECRET=your_kakao_secret
...
```

### 실행

#### 개발 환경

```bash
# 빌드
./gradlew clean build

# 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### 프로덕션 환경

```bash
# JAR 빌드
./gradlew clean build -x test
# 실행
java -jar -Dspring.profiles.active=prod build/libs/eoullim-0.0.1-SNAPSHOT.jar
```

### 접속
- **애플리케이션**: http://localhost:8080
- **관리자 페이지**: http://localhost:8080/admin/dashboard
- **기본 관리자 계정**: 
  - ID: `admin`
  - PW: `admin1234!` (최초 로그인 후 변경 필수)

## API 엔드포인트

### 인증 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/auth/signup` | 회원가입 화면 | |
| POST | `/auth/signup` | 회원가입 처리 | |
| GET | `/api/users/check-login-id` | 아이디 중복 확인 | |
| GET | `/auth/login` | 로그인 화면 | |
| POST | `/auth/login` | 로그인 처리 | |
| DELETE | `/api/auth/session` | 로그아웃 | |
| GET | `/auth/find-password` | 비밀번호 찾기 화면 | |
| POST | `/api/auth/password-reset/code` | 비밀번호 찾기 인증번호 발송 | |
| POST | `/api/auth/password-reset/verify` | 비밀번호 찾기 인증번호 확인 | |
| GET | `/auth/reset-password` | 비밀번호 재설정 화면 | |
| PUT | `/api/auth/password-reset` | 비밀번호 재설정 처리 | |

### 사용자 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/user/kakao` | 카카오 소셜 로그인 콜백 | |
| GET | `/user/profile` | 프로필 조회 화면 | |
| GET | `/user/profile/edit` | 프로필 수정 화면 | |
| PUT | `/api/users/profile` | 프로필 수정 처리 | |
| DELETE | `/api/users/profile/image` | |
| POST | `/api/user/withdraw` | 회원 탈퇴 | |
| GET | `/user/verify-password` | 비밀번호 확인 화면 | |
| GET | `/user/bookings` | 예약 내역 화면 | |
| GET | `/user/reviews` | 리뷰 목록 화면 | |
| GET | `/user/qna` | 내 Q&A 목록 화면 | |
| POST | `/user/qna` | Q&A 작성 (마이페이지) | 수정 요망 (RESTful: POST `/api/qaas`) |
| GET | `/user/qna/{id}` | 내 Q&A 상세 화면 | |
| GET | `/user/qna/{id}/edit` | 내 Q&A 수정 화면 | |
| POST | `/user/qna/{id}/edit` | 내 Q&A 수정 처리 | 수정 요망 (RESTful: PUT `/api/qaas/{id}`) |
| POST | `/user/qna/{id}/delete` | 내 Q&A 삭제 | 수정 요망 (RESTful: DELETE `/api/qaas/{id}`) |

### 사용자 API

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| POST | `/api/email/send` | 이메일 인증번호 발송 | |
| POST | `/api/email/verify` | 이메일 인증번호 확인 | |
| POST | `/api/verify-password` | 비밀번호 확인 | |
| POST | `/api/auth/find-id/code` | 아이디 찾기 인증번호 발송 | |
| POST | `/api/auth/find-id/verify` | 아이디 찾기 인증번호 확인 | |
| POST | `/api/auth/find-id` | 아이디 찾기 결과 조회 | |
| GET | `/api/user/search` | 예약 내역 검색 | 수정 요망 (RESTful: GET `/api/users/bookings?code=&status=`) |
| GET | `/api/user/payment` | 결제 상세 조회 | 수정 요망 (RESTful: GET `/api/users/payments?code=`) |
| POST | `/api/user/refund` | 환불 요청 | 수정 요망 (RESTful: POST `/api/users/refunds`) |
| POST | `/api/user/use-qrCode/{id}` | QR 코드 사용 처리 | 수정 요망 (RESTful: PATCH `/api/users/bookings/{id}/use-qr`) |

### 예약 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/booking/detail` | 예약 상세 화면 | |
| GET | `/booking/complete` | 예약 완료 화면 | |
| POST | `/api/calculate-amount` | 예약 금액 계산 | 수정 요망 (RESTful: GET `/api/bookings/calculate-amount`) |
| POST | `/api/bookings` | 예약 생성 | |

### 결제 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| POST | `/api/payment/prepare` | 결제 준비 | |
| POST | `/api/payment/complete` | 결제 완료 | |
| POST | `/api/payment/cancel` | 결제 취소 | 수정 요망 (RESTful: POST `/api/payments/{id}/cancel`) |

### 장소 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/public` | 메인 페이지 (신규 장소 4개) | |
| GET | `/public/map` | 장소 목록 (지도) | |
| POST | `/api/admin/place` | 장소 생성 (관리자) | |
| PUT | `/api/admin/place/{placeId}` | 장소 수정 (관리자) | |
| DELETE | `/api/admin/place/{placeId}` | 장소 삭제 (관리자) | |

### 룸 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/public/place/{placeId}/room` | 장소별 룸 목록 | |
| POST | `/api/admin/room` | 룸 생성 처리 (관리자) | |
| PUT | `/api/admin/room/{roomId}` | 룸 수정 처리 (관리자) | |
| DELTE | `/api/admin/room/{roomId}` | 룸 삭제 (관리자) | 

### 타임슬롯 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/api/sse/{roomId}/timeslot-connect` | 타임슬롯 실시간 업데이트 (SSE) | |

### 아이템 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| POST | `Scheduled` | 아이템 실시간 업데이트 (SSE) | |

### 리뷰 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/rooms/{roomId}/reviews/new` | 리뷰 작성 화면 | |
| POST | `/rooms/{roomId}/reviews` | 리뷰 작성 처리 | |
| POST | `/rooms/reviews/{reviewId}` | 리뷰 수정 처리 | |
| GET | `/user/reviews` | 사용자 리뷰 목록 조회 | |
| GET | `/api/user/reviews` | 사용자 리뷰 목록 조회 | |
| DELETE | `/api/user/reviews/{reviewId}` | 사용자 리뷰 삭제 | |

### Q&A 관련

| HTTP Method | URL | 설명 |
|------------|-----|------|
| GET | `/public/qnas` | Q&A 전체 조회 |
| GET | `/public/qnas/{qnaId}` | Q&A 전체 상세 조회 |
| POST | `/user/qna` | Q&A 작성 처리 |
| GET | `/user/qna` | 마이페이지 Q&A 조회 |
| GET | `/user/qna/{qnaId}` | 마이페이지 Q&A 상세 조회 |
| GET | `/user/qna/{qnaId}/edit` | 마이페이지 Q&A 수정 처리 |
| POST | `/qna/{qnaId}/update` | 마이페이지 Q&A 수정 처리 |
| POST | `/user/qna/{qnaId}/delete` | Q&A 삭제 처리 |

### 공지사항 관련

| HTTP Method | URL | 설명 |
|------------|-----|------|
| GET | `/notices` | 공지사항 목록 화면 |
| GET | `/notices/{noticeId}` | 공지사항 상세 화면 |

### 알림 관련

| HTTP Method | URL | 설명 |
|------------|-----|------|
| GET | `/notifications` | 알림 목록 화면 |
| GET | `/notifications/qr` | QR 코드 화면 |

### 관리자 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/admin/dashboard` | 관리자 대시보드 | |
| GET | `/admin/refund` | 환불 목록 화면 | |
| GET | `/admin/refund/detail/{id}` | 환불 상세 조회 | |
| POST | `/api/refund/rejection/{id}` | 환불 거절 | 수정 요망 (RESTful: PATCH `/api/admin/refunds/{id}/reject`) |
| POST | `/api/refund/approve/{id}` | 환불 승인 | 수정 요망 (RESTful: PATCH `/api/admin/refunds/{id}/approve`) |
| GET | `/admin/place` | 관리자 장소 목록 화면 | |
| GET | `/admin/users` | 사용자 목록 화면 | |
| GET | `/admin/users/{userId}` | 사용자 상세 조회 | |
| PATCH | `/admin/user/{userId}/suspend` | 사용자 정지 | |
| PATCH | `/admin/user/{userId}/restore` | 사용자 복구 | |
| GET | `/admin/qna` | 관리자 Q&A 목록 화면 | |
| GET | `/admin/qna/{qaaId}` | 관리자 Q&A 상세 화면 | |
| POST | `/admin/qna/{qaaId}/delete` | 관리자 Q&A 삭제 | |
| POST | `/admin/qna/{qaaId}/comments/new` | Q&A 댓글 작성 | |
| POST | `/admin/comments/{qaaId}/delete` | Q&A 댓글 삭제 | |
| GET | `/admin/notices` | 관리자 공지사항 목록 화면 | |
| GET | `/admin/notices/{noticeId}` | 관리자 공지사항 상세 화면 | |
| GET | `/admin/notices/new` | 공지사항 작성 화면 | |
| POST | `/admin/notices/new` | 공지사항 작성 처리 | |
| GET | `/admin/notices/{noticeId}/edit` | 공지사항 수정 화면 | |
| POST | `/admin/notices/{noticeId}/edit` | 공지사항 수정 처리 | |
| POST | `/admin/notices/{noticeId}/delete` | 공지사항 삭제 | |
| GET | `/admin/reviews` | 관리자 리뷰 목록 | |
| DELETE | `/api/admin/reviews/{reviewId}` | 관리자 리뷰 삭제 | |

### 메인 페이지

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/public` | 메인 페이지 | |

## 데이터베이스 구조

주요 테이블:
- users: 사용자 정보
- roles, user_roles: 권한 관리
- events: 이벤트 정보
- places: 장소 정보
- rooms: 룸 정보
- time_slots: 시간대 정보
- bookings: 예약 정보
- payments: 결제 정보
- payment_refunds: 환불 정보
- reviews: 리뷰 정보
- qaas: Q&A 정보
- comments: 댓글 정보
- notices: 공지사항 정보
- notifications: 알림 정보
- inquirychatroom: 채팅방 정보
- inquiry_chat: 채팅 정보

## 프로젝트 구조

```
eoullim-Project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/eoullimback/
│   │   │       ├── _common/          # 공통 모듈
│   │   │       ├── admin/            # 관리자 기능
│   │   │       ├── event/            # 이벤트 기능
│   │   │       ├── booking/          # 예약 기능
│   │   │       ├── booking_auto/     # 예약 자동화
│   │   │       ├── comment/          # 댓글 기능
│   │   │       ├── item/             # 아이템 관리
│   │   │       ├── notice/           # 공지사항
│   │   │       ├── notification/     # 알림
│   │   │       ├── payment/          # 결제
│   │   │       ├── payment_refund/   # 환불
│   │   │       ├── place/            # 장소 관리
│   │   │       ├── qaa/              # Q&A
│   │   │       ├── review/           # 리뷰
│   │   │       ├── room/             # 룸 관리
│   │   │       ├── timeslot/         # 타임슬롯
│   │   │       ├── timeslot_auto/    # 타임슬롯 자동화
│   │   │       └── user_auth/        # 사용자 인증
│   │   └── resources/
│   │       ├── application.yml       # 설정 파일
│   │       ├── templates/            # Mustache 템플릿
│   │       └── static/               # 정적 리소스
│   └── test/                         # 테스트 코드
└── build.gradle                      # Gradle 빌드 설정
```

## 아키텍쳐
🛠️ 시스템 아키텍쳐
```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Client    │◄────►│ Spring Boot  │◄────►│   MySQL     │
│  (Browser)  │      │  Application │      │  Database   │
└─────────────┘      └──────────────┘      └─────────────┘
       │                    │
       │                    ├──────► Gemini AI (비속어 필터링, Q&A, 운세)
       │                    │
       │                    ├──────► PortOne (결제)
       │                    │
       │                    ├──────► Kakao (소셜 로그인)
       │                    │
       │                    ├──────► Gmail API (이메일 발송)
       │                    │
       │                    └──────► SOLAPI (SMS 발송)
       │
       └────── SSE ─────────┘ (실시간 타임슬롯, 채팅)
```

## 주요 기능
<p align="center">
  <img src="eoullim-Project/assets/map.gif" width="45%" />
</p>
<p align="center">
  <img src="eoullim-Project/assets/place_room.gif" width="45%" />
</p>

### 🫥 사용자 관리
- **다양한 인증 방식**
  - 일반 회원가입/로그인
  - 카카오 소셜 로그인
  - 이메일 인증 시스템
- **프로필 관리**: 정보 수정, 프로필 이미지 업로드/삭제
- **비밀번호 관리**: 분실 시 이메일 인증을 통한 재설정
- **회원 탈퇴**: 안전한 계정 삭제

<p align="center">
  <img src="https://github.com/user-attachments/assets/5783a9af-ba4f-4cea-bc40-6f927b4b7d62" width="45%" />
</p>

### 📆 예약 시스템
- **실시간 타임슬롯 조회**: SSE 기반 동시성 제어
- **중복 예약 방지**: 낙관적 락을 통한 동시 요청 처리
- **자동 금액 계산**: 시간대별 요금 자동 산출
- **예약 상태 관리**
  - `READY`: 결제 대기
  - `SUCCESS`: 결제 완료
  - `COMPLETED`: 사용 완료
  - `CANCELLED`: 취소
  - `FAILED`: 실패
  - `REFUNDED`: 환불 완료
- **QR 코드 발급**: 결제 완료 시 자동 생성 및 발송
- **예약 내역 관리**: 조회, 검색, 상태별 필터링



### 💳 결제 시스템
- **포트원 결제 연동**
  - 카카오페이
  - 신용카드
  - 기타 간편결제
- **결제 프로세스**
  1.결제 준비 (사전 검증)
  2.결제 진행
  3.결제 완료 확인
  4.QR 코드 발급
- **환불 관리**
  - 사용자 환불 요청
  - 관리자 승인/거절
  - 자동 환불 처리
 
<p align="center">
  <img src="eoullim-Project/assets/booking.gif" width="45%" />
</p>

### 💬 커뮤니티 기능
- **리뷰 시스템**
  - AI 기반 실시간 비속어 필터링 (Gemini AI)
  - 별점 평가
  - 사진 첨부
  - 수정/삭제 기능
- **Q&A 게시판**
  - 질문 작성 및 관리
  - 관리자 댓글 기능
  - 검색 및 필터링
- **공지사항**
  - 관리자 전용 작성
  - 중요 공지 상단 고정

<p align="center">
  <img src="https://github.com/user-attachments/assets/993190c2-08a7-4f47-8e3c-7a8f08627398" width="45%" />
  <img src="https://github.com/user-attachments/assets/0b601852-422d-4b29-8fc3-6c0b187192f6" width="45%" />
  <img src="https://github.com/user-attachments/assets/7efe107d-2b1d-4bd2-b426-e43db0298c70" width="45%" />
  <img src="https://github.com/user-attachments/assets/85d26bde-fd78-487b-9248-c7dd70642771" width="45%" />
</p>

### 🤖 AI 기능
- **비속어 실시간 필터링**: Gemini AI를 통한 리뷰 품질 관리
- **지능형 Q&A 도우미**: 자주 묻는 질문 자동 응답
- **오늘의 운세**: 개인화된 AI 운세 콘텐츠

### 🎧 고객 지원
- **실시간 1:1 상담**: SSE 기반 관리자 채팅
- **AI 챗봇**: 24/7 자동 응답 시스템
- **알림 시스템**: 예약, 결제, 환불 상태 알림

<p align="center">
  <img src="https://github.com/user-attachments/assets/9fda74e1-1de2-4626-b49b-7d84eea69ef6" width="45%" />
</p>

### 🧑🏻‍💼 관리자 기능
- **대시보드**
  - 회원 통계
  - 결제/환불 현황
  - 예약 현황
- **장소/룸 관리**: CRUD 작업
- **사용자 관리**
  - 계정 정지/복구
  - 사용자 상세 조회
- **환불 관리**: 승인/거절 처리
- **콘텐츠 관리**
  - 리뷰 삭제
  - Q&A 답변
  - 공지사항 작성
 
<p align="center">
  <img src="https://github.com/user-attachments/assets/a59a8941-4c9b-4db4-add5-f98062a0defd" width="45%" />
  <img src="https://github.com/user-attachments/assets/885a22fb-70e0-4a1d-93bd-18c5a466bc2e" width="45%" />
</p>

## ⚙️프로젝트를 진행하면서 기술적 도전과 해결 경험 (각자 파트)
- **김승민**
  - **1. 문제 상황**
     - 보안 강화를 위해 프로젝트에 CSRF(Cross-Site Request Forgery) 방어 체계를 도입했습니다. 하지만 적용 직후, 기존에 잘 작동하던 <code>POST</code>,<code>PUT</code>,<code>DELETE</code> 등 상태를 변경하는 모든 HTTP 메서드가 403 Forbidden 에러를 뱉으며 차단되는 현상이 발생했습니다. 특히 비동기(Fetch) 요청에서 데이터 처리가 아예 불가능해지는 상황에 직면했습니다.
  - **2. 원인 분석**
    - CSRF 토큰 누락: 서버는 보안을 위해 매 요청마다 유효한 토큰을 요구하지만, 클라이언트의 비동기 요청 헤더에는 해당 토큰이 포함되지 않아 서버가 이를 인식하지 못하는게 이유였습니다.
    - 브라우저 동작의 한계: 일반적인 Form 전송과 달리 JS를 이용한 비동기 통신은 브라우저가 자동으로 토큰을 헤더에 삽입해주지 않기 때문에 헤더에 직접 넣어줘야하는 필요함을 파악했습니다.
  - **3. 해결과정**
    - 토큰의 전역 관리: HTML의 헤더 부분의 <code><meta></code>태그를 활용하여 서버로부터 발급받은 **CSRF_토큰**과 **CSRF_헤더** name과 content를 통해 값을 배치했습니다. 이를 통해 페이지 어디에서든 JS로 토큰에 접근할 수 있는 환경을 만들었습니다.
    - 공통 인증 유틸 함수: <code>POST</code>,<code>PUT</code>,<code>DELETE</code> Fetch 요청 전 헤더에 함수를 호출하여 자동으로 주입하는 방식을 사용하였습니다.
  - **4. 느낀점**
    - CSRF 보안을 적용하면서 처음에 모든 요청에 403 Forbidden 오류가 발생하고 동작을 하지않아 몹시 당황했지만, 이 과정을 통해 웹 보안의 핵심 원리인 '요청의 신뢰성 확인' 과정을 깊이 있게 이해할 수 있었습니다. 단순히 기능을 구현하는 것을 넘어, 서버가 클라이언트의 정당성을 어떻게 검증하는지 파악하는 계기가 되었습니다 
- **이상은**
  - 비동기 처리
    - 페이지마다 경로를 구현하여 머스태치에 넣어주는 과정에서 한 페이지에 두개의 경로가 들어가는 상황이 발생했습니다. 처음엔 커트롤단에 합쳐 머스태치에 넣어 줄려하였지만 경로가 달라 하나를 포기하거나 새로운 페이지를 추가해야했습니다.
    - 하지만 두가지의 경우와 달리 꼭 두개의 경로가 한 페이지에 나와야 하는 상황이었습니다.
    - 이를 해결하기 위해 자바스크립트를 학습하고 비동기 처리를 이해하였습니다. 처음에 어려웠지만 consolo.log를 찍어보며 데이터의 흐름을 파악하고 계속 시도하며 문제점을 하나씩 줄여 나갔습니다.
    - 그 결과 한 페이지에 두개의 경로가 나오도록 하였고 이 경험을 통해 비동기를 한층 더 이해하고 접목 시킬 수 있었습니다.

- **최광섭**
  - OAuth 기반 소셜 로그인 및 인증/보안 강화
    - 사용자 편의성을 높이기 위해 카카오 소셜 로그인 기능을 구현했으며, 기존 세션 기반 인증만으로는 보안과 통합 관리에 한계가 있었다. 또한 일반 로그인과 소셜 로그인 사용자가 공통 인증 토큰으로 권한을 관리할 수 있는 구조 설계가 필요했다.
    - 이를 해결하기 위해 OAuth 2.0 기반 로그인 흐름과 공통 인증 토큰 발급 구조를 설계하고, Spring Security와 CSRF 토큰 기반 인증을 적용하여 보안성을 강화했다. 인증 과정에서 발생한 매핑 오류와 토큰 검증 문제는 단계적으로 점검하고 예외 처리를 적용하며 해결했다.
    - 그 결과 소셜 로그인과 일반 로그인이 통합된 안정적인 인증 구조를 구축할 수 있었고, 보안성과 사용자 편의성을 동시에 확보했다.
  
- **박진영**
  - 외부 API 연동 한계와 서비스 안정성 개선
      - 공간 대여 서비스의 반복 문의를 해결하기 위해 챗봇과 결제 알림 기능을 구현했다. 초기에는 외부 AI API에 의존하면서 요금 제한과 401/429 오류로 서비스가 불안정했고, 결제 완료 문자 알림 또한 데이터 누락으로 전송 실패 문제가 발생했다.
      - 이를 해결하기 위해 Rule 기반 응답과 AI 응답을 분리한 하이브리드 챗봇 구조를 설계하고, 의도 분류 및 비동기 처리로 응답 지연과 API 의존도를 줄였다. 또한 결제부터 알림까지의 데이터 흐름을 점검해 필수 값 검증과 예외 처리를 강화했다.
      - 그 결과 외부 API 장애 상황에서도 기본 기능을 유지할 수 있었으며, 챗봇 응답 안정성과 결제 알림 신뢰도를 함께 개선할 수 있었다.

---
## 노션(문서화) 링크
https://www.notion.so/2c567018e56c81cbb053ea5c6a68bf71
    
