DROP DATABASE IF EXISTS eoullim_db;

CREATE DATABASE IF NOT EXISTS eoullim_db;

USE eoullim_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS payment_logs;
DROP TABLE IF EXISTS payment_refunds;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS timeslots;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS place_files;
DROP TABLE IF EXISTS places;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS qaas;
DROP TABLE IF EXISTS file_infos;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  login_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
  password VARCHAR(255) NOT NULL COMMENT '사용자 PW',
  name VARCHAR(50) NOT NULL COMMENT '사용자 이름',
  phone VARCHAR(30) NULL COMMENT '휴대폰 번호',
  email VARCHAR(255) NOT NULL COMMENT 'Email 주소',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '사용자 계정 생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '사용자 계정(프로필) 수정일',
  UNIQUE KEY `uk_users_login_id` (login_id),
  UNIQUE KEY `uk_users_email` (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 정보 테이블';

CREATE TABLE IF NOT EXISTS roles (
  role_name VARCHAR(30) PRIMARY KEY COMMENT '사용자 권한'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='권한 코드(USER, MANAGER, ADMIN)';

CREATE TABLE IF NOT EXISTS user_roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL COMMENT '사용자',
  role_name VARCHAR(30) NOT NULL COMMENT '사용자 권한',
  CONSTRAINT `fk_user_roles_user_id` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_user_roles_role_name` FOREIGN KEY (role_name) REFERENCES roles(role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 권한 매핑';

CREATE TABLE file_infos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  original_name VARCHAR(255) NOT NULL COMMENT '원본 파일명',
  stored_name VARCHAR(255) NOT NULL COMMENT 'UUID가 적용된 파일명',
  content_type VARCHAR(255) COMMENT '타입',
  file_size BIGINT COMMENT '파일 사이즈',
  file_path VARCHAR(255) NOT NULL COMMENT '서버 내 실제 경로',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='파일 정보 테이블';

CREATE TABLE qaas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL COMMENT '작성자',
  title VARCHAR(50) NOT NULL COMMENT 'Q&A 제목',
  content TEXT NOT NULL COMMENT 'Q&A 내용',
  view_count BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  INDEX `idx_qaas_user` (user_id),
  CONSTRAINT `fk_qaas_user_id` FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Q&A 테이블';

CREATE TABLE comments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  comment TEXT NOT NULL COMMENT '댓글',
  user_id BIGINT NOT NULL COMMENT '관리자 Q&A 응답',
  qaa_id BIGINT NOT NULL COMMENT 'Q&A',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  INDEX `idx_comments_user` (user_id),
  CONSTRAINT `fk_comments_user_id` FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT `fk_comments_qaa_id` FOREIGN KEY (qaa_id) REFERENCES qaas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Q&A 댓글 테이블';

CREATE TABLE places (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL COMMENT '장소 이름',
  address VARCHAR(100) NOT NULL COMMENT '장소 주소',
  latitude DECIMAL(10,8) NOT NULL COMMENT '위도',
  longitude DECIMAL(11,8) NOT NULL COMMENT '경도',
  open_time DATETIME NOT NULL COMMENT '오픈시간',
  close_time DATETIME NOT NULL COMMENT '마감시간',
  status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' COMMENT '상태(SCHEDULED, OPEN, CLOSED)',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='장소 테이블';

CREATE TABLE place_files (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  place_id BIGINT NOT NULL COMMENT '장소',
  file_id BIGINT NOT NULL COMMENT '파일',
  display_order INT DEFAULT 0 COMMENT '파일순서',
  CONSTRAINT `fk_place_files_place_files` FOREIGN KEY(place_id) REFERENCES places(id) ON DELETE CASCADE,
  CONSTRAINT `fk_place_files_file_info` FOREIGN KEY(file_id) REFERENCES file_infos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='장소 파일 테이블';

CREATE TABLE rooms (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  place_id BIGINT NOT NULL COMMENT '장소',
  name VARCHAR(150) NOT NULL COMMENT '방 이름',
  content VARCHAR(255) NOT NULL COMMENT '방 내용',
  category VARCHAR(100) NOT NULL COMMENT '방 종류',
  capacity_policy VARCHAR(20) NOT NULL DEFAULT 'PER_SLOT' COMMENT '시간 타입(PER_DAY, PER_SLOT)',
  CHECK (category IN('PARTY','STUDY','PRACTICE')),
  CHECK (capacity_policy IN('PER_DAY','PER_SLOT')),
  CONSTRAINT `fk_rooms_place` FOREIGN KEY(place_id) REFERENCES places(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='시간 슬롯 테이블';

CREATE TABLE time_slots (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_id BIGINT NOT NULL COMMENT '방 번호',
  start_time DATETIME(6) NOT NULL COMMENT '시작시간',
  end_time DATETIME(6) NOT NULL COMMENT '종료시간',
  capacity INT NOT NULL COMMENT '인원 수 지정',
  reserved INT NOT NULL DEFAULT 0 COMMENT '예약인원',
  status VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '슬롯 상태',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  CONSTRAINT `fk_timeslots_room` FOREIGN KEY (room_id) REFERENCES rooms(id),
  CHECK (status IN('OPEN','CLOSED','CANCELED')),
  CHECK (reserved >= 0 AND reserved <= capacity),
  UNIQUE KEY `uk_time_slots_room_id` (room_id),
  UNIQUE KEY `uk_time_slots_start_time` (start_time),
  INDEX `idx_time_slots_start_time` (start_time),
  INDEX `idx_time_slots_end_timee` (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='시간 슬롯 테이블';

CREATE TABLE items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  time_slot_id BIGINT NOT NULL COMMENT '시간대',
  title VARCHAR(50) NOT NULL COMMENT '상품 이름',
  context VARCHAR(150) NOT NULL COMMENT '상품 설명',
  price INT NOT NULL DEFAULT 0 COMMENT '상품 가격',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  CONSTRAINT `fk_items_time_slot` FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='아이템 테이블';

CREATE TABLE bookings (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL COMMENT '사용자',
  time_slot_id BIGINT NOT NULL COMMENT '타임슬롯',
  item_id BIGINT NOT NULL COMMENT '아이템',
  qty INT NOT NULL COMMENT '인원체크',
  amount BIGINT NOT NULL COMMENT '가격',
  booking_date DATE NOT NULL COMMENT '예약 날짜',
  cancelled_at DATETIME(6) NOT NULL  COMMENT '취소일',
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '상태',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  UNIQUE KEY `uk_user_time_slot_item` (user_id, time_slot_id, item_id),
  CONSTRAINT `fk_bookings_user` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_bookings_time_slot` FOREIGN KEY (time_slot_id) REFERENCES time_slots(id),
  CONSTRAINT `fk_bookings_item` FOREIGN KEY (item_id) REFERENCES items(id),
  CHECK (qty > 0),
  CHECK (status IN('PENDING','CONFIRMED','CANCELED','REFUNDED')),
  INDEX `idx_bookings_user` (user_id, status),
  INDEX `idx_bookings_time_slot` (time_slot_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='예약 테이블';

CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL COMMENT '결제한 사용자 ID',
  booking_id BIGINT NOT NULL COMMENT '예약주문 ID',
  order_id VARCHAR(100) NOT NULL COMMENT '예약주문 ID',
  payment_key VARCHAR(100) NOT NULL COMMENT '결제 키 (PG 또는 모의 PG 트랜잭션 키)',
  amount BIGINT NOT NULL COMMENT '결제 금액(포인트)',
  method VARCHAR(30) NOT NULL COMMENT '결제 수단 (KAKAO_PAY, TOSS_PAY등)',
  status VARCHAR(30) NOT NULL COMMENT '결제 상태',
  product_code VARCHAR(50) NOT NULL COMMENT '상품 코드',
  product_name VARCHAR(100) NOT NULL COMMENT '상품 이름',
  failure_code VARCHAR(50) NULL COMMENT '결제 실패 코드',
  failure_message VARCHAR(255) NULL COMMENT '결제 실패 사유',
  requested_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '결제 요청 시간',
  approved_at DATETIME(6) NULL COMMENT '결제 승인 시간',
  cancelled_at DATETIME(6) NULL COMMENT '결제 취소/환불 시간',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  CHECK (status IN('READY','SUCCESS','FAILED','CANCELLED','REFUNDED')),
  UNIQUE KEY `uk_payments_booking_id` (booking_id),
  UNIQUE KEY `uk_payments_payment_key` (payment_key),
  INDEX `idx_payments_user_id` (user_id),
  INDEX `idx_payments_order_id` (order_id),
  CONSTRAINT `fk_payments_user` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_payments_booking` FOREIGN KEY (booking_id) REFERENCES bookings(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='결제 테이블';

CREATE TABLE payment_refunds (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payment_id BIGINT NOT NULL COMMENT '원 결제 ID',
  amount BIGINT NOT NULL COMMENT '환불 금액',
  reason VARCHAR(255) NULL COMMENT '환불 사유',
  status VARCHAR(30) NOT NULL COMMENT '환불 상태 (REQUESTED, COMPLETED, FAILED)',
  failure_code VARCHAR(50) NULL COMMENT '환불 실패 코드',
  failure_message VARCHAR(255) NULL COMMENT '환불 실패 사유',
  requested_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '환불 요청 시간',
  completed_at DATETIME(6) NULL COMMENT '완료 시간',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CHECK (status IN('REQUESTED','COMPLETED','FAILED')),
  INDEX `idx_payment_refunds_payment_id` (payment_id),
  CONSTRAINT `fk_payment_refunds_payment` FOREIGN KEY (payment_id) REFERENCES payments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='결제 환불 테이블';

CREATE TABLE payment_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payment_id BIGINT NOT NULL COMMENT '결제 ID',
  payment_refund_id BIGINT NULL COMMENT '환불 결제 ID (환불 시 연결)',
  action_type VARCHAR(20) NOT NULL COMMENT 'PAYMENT | REFUND',
  amount BIGINT NOT NULL COMMENT '금액',
  status VARCHAR(20) NOT NULL COMMENT 'PENDING | COMPLETED | FAILED',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CHECK (action_type IN('PAYMENT','REFUND')),
  CHECK (status IN('PENDING','COMPLETED','FAILED')),
  CONSTRAINT `fk_payment_log_payment` FOREIGN KEY (payment_id) REFERENCES payments(id),
  CONSTRAINT `fk_payment_log_refund` FOREIGN KEY (payment_refund_id) REFERENCES payment_refunds(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='결제/환불 로그';

CREATE TABLE notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL COMMENT '대상 사용자',
  payment_id BIGINT NOT NULL COMMENT '결제 ID (결제 완료 이벤트와 연결)',
  type VARCHAR(20) NOT NULL COMMENT '알림(PAYMENT, REFUND, CANCEL, BOOKING)',
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '전송 상태(PENDING, SENT, FAILED)',
  message VARCHAR(255) NOT NULL COMMENT '전송 메시지',
  qr_code VARCHAR(40) NOT NULL COMMENT 'QR 코드 토큰',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  sent_at DATETIME(6) NULL COMMENT '전송 시각',
  CHECK (type IN('PAYMENT','REFUND','CANCEL','BOOKING')),
  CHECK (status IN('PENDING','SENT','FAILED')),
  CONSTRAINT `fk_notification_user` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_notification_payment` FOREIGN KEY (payment_id) REFERENCES payments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 결제 알림 테이블';

CREATE TABLE reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5) COMMENT '별점',
  content TEXT NOT NULL COMMENT '리뷰 내용',
  user_id BIGINT NOT NULL COMMENT '사용자 ID',
  room_id BIGINT NOT NULL COMMENT '숙소 ID',
  payment_id BIGINT NOT NULL COMMENT '결제 완료 ID',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  UNIQUE KEY `uk_review_once` (payment_id),
  INDEX `idx_review_room` (room_id, rating),
  CONSTRAINT `fk_review_user_id` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_review_room_id` FOREIGN KEY (room_id) REFERENCES rooms(id),
  CONSTRAINT `fk_review_payment_id` FOREIGN KEY (payment_id) REFERENCES payments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰 테이블';

CREATE TABLE notices (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL COMMENT '전달할 사용자',
  title VARCHAR(50) NOT NULL COMMENT '공지사항 제목',
  content TEXT NOT NULL COMMENT '공지사항 내용',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  CONSTRAINT `fk_notices_user` FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='대여시설 공지사항';