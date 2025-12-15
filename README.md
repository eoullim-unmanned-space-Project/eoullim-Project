# 어울림 (Eoulrim) - 무인공간 대여 서비스

## 프로젝트 개요
**어울림**은 사용자가 다양한 무인 공간을 손쉽게 예약하고 이용할 수 있는 **무인 공간 대여 서비스**입니다.  
회원가입부터 결제, QR코드를 통한 입장까지 원스톱으로 제공하며, 부가 기능으로 Q&A와 리뷰 게시판을 지원합니다.
---
## 🛠️ 기술 스택 (Tech Stack)
본 프로젝트는 **Java 및 Spring 기반**의 웹 서비스를 구축하는 데 중점을 두었으며, 아래와 같은 기술 스택을 활용했습니다.
---
### 1. 백엔드 (Backend)
| 기술 스택 | 로고 | 핵심 역할 |
| :---: | :---: | :--- |
| **Java 17** | ![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=openjdk&logoColor=white) | 애플리케이션의 비즈니스 로직을 구현한 주 언어 |
| **Spring Boot 3.x** | ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) | 빠르고 효율적인 서버 및 RESTful API 개발을 위한 기반 프레임워크 |
| **Spring Security** | ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white) | 사용자 인증 및 권한 관리(인가) 모듈 구현 |

### 2. 데이터베이스 (Database)
| 기술 스택 | 로고 | 핵심 역할 |
| :---: | :---: | :--- |
| **MySQL 8.0** | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white) | 관계형 데이터의 저장, 조회 및 관리 |

### 3. 프론트엔드 (Frontend - View)
| 기술 스택 | 로고 | 핵심 역할 |
| :---: | :---: | :--- |
| **Mustache (SSR)** | ![Mustache](https://img.shields.io/badge/Mustache-5E5C60?style=flat-square&logo=mustache&logoColor=white) | 서버 측 렌더링(SSR)을 통한 동적 뷰(View) 페이지 생성 |

### 4. 형상 관리 및 협업
| 기술 스택 | 로고 | 핵심 역할 |
| :---: | :---: | :--- |
| **Git** | ![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white) | 소스 코드 버전 관리 및 팀원 간의 코드 병합 관리 |
| **Notion** | ![Notion](https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white) | 프로젝트 기획, 문서화 및 회의록 관리 |
---
## 서비스 흐름
1.회원가입 
2.로그인 (JWT 인증)
3.카테고리 선택
4.장소 선택
5.룸 선택
6.날짜 선택 → 해당 날짜의 타임슬롯 조회
7.타임슬롯 선택 → 예약 생성(PENDING_PAYMENT)
8.결제 진행
9.결제 완료 → QR 코드 발급(PAID)
10.QR 코드 스캔 → 무인 공간 입장(USED)
11.이용 종료 처리
12.리뷰 작성 가능
13.Q&A 게시판 이용 가능

## DB 테이블 다이어그램
<img width="3053" height="3014" alt="diagram-export-2025 -12 -12 -오후-2_42_35" src="https://github.com/user-attachments/assets/e6492746-da6b-4810-a521-3dea0b079295" />
