# 어울림 (Eoulrim) - 무인공간 대여 서비스

## 프로젝트 개요

**어울림**은 사용자가 다양한 무인 공간을 손쉽게 예약하고 이용할 수 있는 **스마트 무인 공간 대여 서비스**입니다.  
기본적인 예약 및 결제 시스템에 **Gemini AI**를 결합하여 지능형 고객 응대와 건전한 리뷰 환경을 구축하였으며, 실시간 타임슬롯 동기화(SSE)와 QR코드 기반의 출입 통제 시스템을 통해 진정한 무인 운영 솔루션을 제공합니다.

## 기술 스택

### 백엔드
- **Java 17**: 애플리케이션의 비즈니스 로직을 구현한 주 언어
- **Spring Boot 3.4.12**: 빠르고 효율적인 서버 및 RESTful API 개발을 위한 기반 프레임워크
- **Spring Data JPA**: 데이터베이스 접근 및 ORM 구현
- **Spring Security**: 사용자 인증 및 권한 관리 (비밀번호 암호화, CSRF Token 기반 보안통신 구현)
- **Spring Mail**: 이메일 인증 기능

### 데이터베이스
- **MySQL 8.0**: 관계형 데이터의 저장, 조회 및 관리

### 프론트엔드
- **Mustache (SSR)**: 서버 측 렌더링을 통한 동적 뷰 페이지 생성
- **JavaScript**: 클라이언트 사이드 상호작용

### 외부 연동
- **포트원 (아임포트)**: 결제 시스템 연동
- **카카오 OAuth**: 소셜 로그인 연동
- **제미나이(Gemini AI)**: AI 콘텐츠 및 챗봇 연동
- **솔라피 (SOLAPI)**: 문자 전송 및 인증 연동  

### 형상 관리 및 협업
- **Git**: 소스 코드 버전 관리 및 팀원 간의 코드 병합 관리
- **Notion**: 프로젝트 기획, 문서화 및 회의록 관리

## 서비스 흐름

1. 회원가입
2. 로그인 (세션 기반 인증)
3. 카테고리 선택
4. 장소 선택
5. 룸 선택
6. 날짜 선택 → 해당 날짜의 타임슬롯 조회
7. 타임슬롯 선택 → 예약 생성(PENDING_PAYMENT)
8. 결제 진행
9. 결제 완료 → QR 코드 발급(PAID)
10. QR 코드 스캔 → 무인 공간 입장(USED)
11. 이용 종료 처리
12. 리뷰 작성 가능 (AI 실시간 비속어 필터링)
13. Q&A 게시판 이용 가능
14. 오늘의 운세 (AI 오늘의 운세)
15. 챗(1:1 상담 & Q&A 도우미)

## 주요 기능

### 사용자 관리
- 회원가입 및 로그인 (일반, 카카오 소셜 로그인)
- 프로필 관리 (수정, 삭제)
- 비밀번호 찾기 및 재설정 
- 이메일 인증
- 회원 탈퇴

### 예약 시스템
- 장소 및 룸 조회
- 타임슬롯 선택 및 예약 생성 (SSE 기반 중복 예약 방지)
- 예약 금액 자동 계산
- 예약 상태 관리 (PENDING, CONFIRMED, CANCELED, REFUNDED)
- 결제 후 QR CODE 전송
- 예약 내역 조회 및 검색

### 결제 시스템
- 포트원(아임포트) 결제 연동
- 결제 준비 및 완료 처리
- 결제 취소
- 환불 요청 및 관리

### 게시판 기능
- Q&A 게시판 (CRUD)
- 리뷰 작성 및 관리 (Gemini AI 연동을 통한 비속어 실시간 필터링)
- 공지사항
- 댓글 기능 (관리자)

### 고객 지원 기능
- AI 오늘의 운세 (Gemini AI 기반 개인화 콘텐츠 제공)
- AI 챗봇 (Gemini AI 기반 Q&A 도우미)
- 챗봇 (SSE 기반 1:1 상담)

### 관리자 기능
- 대시보드 (회원 및 결제 통계)
- 장소 및 룸 관리
- 사용자 관리 (정지, 복구)
- 환불 승인/거절
- 공지사항 관리

## API 엔드포인트

### 인증 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/auth/signup` | 회원가입 화면 | |
| POST | `/auth/signup` | 회원가입 처리 | |
| GET | `/auth/signup-check-login-id` | 아이디 중복 확인 | 수정 요망 (RESTful: GET `/api/users/check-login-id`) |
| GET | `/auth/login` | 로그인 화면 | |
| POST | `/auth/login` | 로그인 처리 | |
| GET | `/auth/logout` | 로그아웃 | 수정 요망 (RESTful: DELETE `/api/auth/session`) |
| GET | `/auth/find-password` | 비밀번호 찾기 화면 | |
| POST | `/auth/find-password/send-code` | 비밀번호 찾기 인증번호 발송 | 수정 요망 (RESTful: POST `/api/auth/password-reset/code`) |
| POST | `/auth/verify-password-code` | 비밀번호 찾기 인증번호 확인 | 수정 요망 (RESTful: POST `/api/auth/password-reset/verify`) |
| GET | `/auth/reset-password` | 비밀번호 재설정 화면 | |
| POST | `/auth/reset-password` | 비밀번호 재설정 처리 | 수정 요망 (RESTful: PUT `/api/auth/password-reset`) |

### 사용자 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/user/kakao` | 카카오 소셜 로그인 콜백 | |
| GET | `/user/profile` | 프로필 조회 화면 | |
| GET | `/user/profile/edit` | 프로필 수정 화면 | |
| POST | `/user/profile` | 프로필 수정 처리 | 수정 요망 (RESTful: PUT `/api/users/profile`) |
| POST | `/user/profile-delete` | 프로필 이미지 삭제 | 수정 요망 (RESTful: DELETE `/api/users/profile/image`) |
| POST | `/user/leave` | 회원 탈퇴 | 수정 요망 (RESTful: DELETE `/api/users`) |
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
| POST | `/api/find/email/send` | 아이디 찾기 인증번호 발송 | 수정 요망 (RESTful: POST `/api/auth/find-id/code`) |
| POST | `/api/find/email/verify` | 아이디 찾기 인증번호 확인 | 수정 요망 (RESTful: POST `/api/auth/find-id/verify`) |
| POST | `/api/find/login-id` | 아이디 찾기 결과 조회 | 수정 요망 (RESTful: GET `/api/auth/find-id`) |
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
| GET | `/admin/reviews` | 관리자 리뷰 목록 | |
| GET | `/api/admin/reviews` | 관리자 리뷰 목록 | |
| DELETE | `/api/admin/reviews/{reviewId}` | 관리자 리뷰 삭제 | |

### Q&A 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/public/qnas` | Q&A 전체 조회 | |
| GET | `/public/qnas/{qnaId}` | Q&A 전체 상세 조회 | |
| POST | `/user/qna` | Q&A 작성 처리 | |
| GET | `/user/qna` | 마이페이지 Q&A 조회 | |
| GET | `/user/qna/{qnaId}` | 마이페이지 Q&A 상세 조회 | |
| GET | `/user/qna/{qnaId}/edit` | 마이페이지 Q&A 수정 처리 | |
| POST | `/qna/{qnaId}/update` | 마이페이지 Q&A 수정 처리 | |
| POST | `/user/qna/{qnaId}/delete` | Q&A 삭제 처리 | |

### 공지사항 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/notices` | 공지사항 목록 화면 | |
| GET | `/notices/{noticeId}` | 공지사항 상세 화면 | |
| GET | `/admin/notices` | 관리자 공지사항 목록 화면 | |
| GET | `/admin/notices/{noticeId}` | 관리자 공지사항 상세 화면 | |
| GET | `/admin/notices/new` | 공지사항 작성 화면 (관리자) | |
| POST | `/admin/notices/new` | 공지사항 작성 처리 (관리자) | |
| GET | `/admin/notices/{noticeId}/edit` | 공지사항 수정 화면 (관리자) | |
| POST | `/admin/notices/{noticeId}/edit` | 공지사항 수정 처리 (관리자) | |
| POST | `/admin/notices/{noticeId}/delete` | 공지사항 삭제 (관리자) | |

### 댓글 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| POST | `/admin/qna/{qaaId}/comments/new` | Q&A 댓글 작성 (관리자) | |
| POST | `/admin/comments/{qaaId}/delete` | 댓글 삭제 (관리자) | |

### 알림 관련

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/notifications` | 알림 목록 화면 | |
| GET | `/notifications/qr` | QR 코드 화면 | |

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

### 메인 페이지

| HTTP Method | URL | 설명 | 비고 |
|------------|-----|------|------|
| GET | `/main/main` | 메인 페이지 | |

## 데이터베이스 구조

주요 테이블:
- users: 사용자 정보
- roles, user_roles: 권한 관리
- event: 이벤트 정보
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

## 실행 방법

1. MySQL 데이터베이스 생성 및 설정
2. `application.yml` 파일에 데이터베이스 연결 정보 설정
3. 프로젝트 빌드: `./gradlew build`
4. 애플리케이션 실행: `./gradlew bootRun`
5. 브라우저에서 `http://localhost:8080` 접속

## 주요 특징

- 세션 기반 인증 시스템
- 포트원(아임포트) 결제 연동
- 카카오 소셜 로그인 지원
- 이메일 인증 기능
- 실시간 타임슬롯 업데이트 (SSE)
- 예약 자동 해제 스케줄러
- 관리자 대시보드 및 관리 기능
- 파일 업로드 및 이미지 관리
- AI 기반 실시간 비속어 필터링 (Gemini AI)
- 지능형 Q&A 도우미 및 개인화 운세 (Gemini AI)
- 실시간 1:1 상담 (SSE)


