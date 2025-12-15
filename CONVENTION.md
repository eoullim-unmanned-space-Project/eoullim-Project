# 🔖Team Convention
프로젝트 기본 팀 컨벤션 문서
---
## 01. Branch 컨벤션
| 브랜치명 | 설명 | 예시 |
|----------|-------|-------|
| main | 배포용 브랜치 | main |
| dev | 통합(개발)용 브랜치 | devlop |
| feature/ | 기능 개발 브랜치 | feature/login |
| bugfix/ | 버그 수정 브랜치 | bugfix/nav-bar |
| refactor/ | 리팩토링 브랜치 | refactor/homepage-ui |
---

## 02. Commit 컨벤션
| 타입 | 설명 | 예시 |
|------|------|------|
| feat | 새로운 기능 추가 | feat: 로그인 기능 구현 |
| fix | 버그 수정 | fix: 결제 오류 해결 |
| style | 코드 스타일 변경 (포맷팅, 세미콜론 등) | style: 코드 포맷 정리 |
| refactor | 리팩토링 (기능 변화 없음) | refactor: ReservationService 구조 개선 |
---

## 03. Commit 메시지 
**커밋 메시지 형식**
**예시**
- feat: 회원가입 API 구현
- fix: 결제 실패 에러 해결
- refactor: 가독성 향상
---

## 04. Issue 컨벤션
| 타입 | 설명 | 예시 |
|------|------|------|
| feat | 기능 개발 | feat: 예약 기능 개발 |
| fix | 버그 수정 | fix: 로그인 오류 수정 |
| docs | 문서 작업 | docs: API 문서 업데이트 |
| refactor | 리팩토링 | refactor: 결제 모듈 리팩토링 |
---

## 05. Issue 제목 
- [feat] 예약 기능 개발
- [feat] 결제 기능 개발
- [fix] 로그인 실패 문제 해결

## 06. Issue 내용
- User 엔티티 작성 완료. JPA 매핑 및 Lombok 적용.
