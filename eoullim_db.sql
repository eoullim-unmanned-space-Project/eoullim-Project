DROP DATABASE IF EXISTS eoullim_db;

CREATE DATABASE IF NOT EXISTS eoullim_db;

USE eoullim_db;

SELECT * FROM time_slots;
SELECT * FROM items;
SELECT * FROM places;	
SELECT * FROM rooms;

SELECT * FROM rooms WHERE place_id = 2;

SELECT * FROM time_slots;
SELECT * FROM users;
SELECT * FROM rooms;
SELECT * FROM user_roles;	
SELECT * FROM bookings;
SELECT * FROM payments;	
SELECT * FROM roles;
SELECT * FROM payment_refunds;
SELECT * FROM reviews;

DELETE FROM payments WHERE id =1;
DELETE FROM bookings WHERE id = 1;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS payment_logs;
DROP TABLE IF EXISTS payment_refunds;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS time_slots;
DROP TABLE IF EXISTS room_images;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS places;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS qaas;
DROP TABLE IF EXISTS file_infos;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  login_id VARCHAR(50) NOT NULL COMMENT '사용자 ID',
  password VARCHAR(255) NOT NULL COMMENT '사용자 PW',
  name VARCHAR(50) NOT NULL COMMENT '사용자 이름',
  phone VARCHAR(30) NULL COMMENT '휴대폰 번호',
  email VARCHAR(255) NOT NULL COMMENT 'Email 주소',
  profile_image VARCHAR(255) NULL COMMENT '사용자 프로필',
  status VARCHAR(20) NOT NULL COMMENT '사용자 상태',
  
  provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL',

  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '사용자 계정 생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '사용자 계정(프로필) 수정일',
  withdrawn_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '사용자 탈퇴일',

  CHECK(status IN('ACTIVE', 'WITHDRAWN', 'SUSPENDED')),

  UNIQUE KEY `uk_users_login_id` (login_id),
  UNIQUE KEY `uk_users_email` (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 정보 테이블';

select * from users WHERE id = 1;

CREATE TABLE IF NOT EXISTS user_roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  user_id BIGINT NOT NULL COMMENT '사용자',
  role_name VARCHAR(30) NOT NULL COMMENT '사용자 권한',
  
UNIQUE KEY `uk_user_role` (user_id, role_name)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 권한 매핑';

INSERT INTO user_roles (user_id, role_name)
VALUES (1, 'ADMIN');


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
  
  user_id BIGINT NOT NULL COMMENT '댓글 작성자(관리자)',
  qaa_id BIGINT NOT NULL COMMENT 'Q&A',
  
  content TEXT NOT NULL COMMENT '댓글',

  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  
  INDEX `idx_comments_user` (user_id),
  INDEX `idx_comments_qaa` (qaa_id),
  CONSTRAINT `fk_comments_user_id` FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT `fk_comments_qaa_id` FOREIGN KEY (qaa_id) REFERENCES qaas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Q&A 댓글 테이블';

CREATE TABLE places (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  
  name VARCHAR(50) NOT NULL COMMENT '장소 이름',
  address VARCHAR(100) NOT NULL COMMENT '장소 주소',
  latitude DECIMAL(10,8) NOT NULL COMMENT '위도',
  longitude DECIMAL(11,8) NOT NULL COMMENT '경도',
  category VARCHAR(100) NOT NULL COMMENT '방 종류',
  profile_image VARCHAR(500) COMMENT '장소 사진',

  CHECK (category IN('PARTY','STUDY','PRACTICE')),
  
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='장소 테이블';

CREATE TABLE rooms (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  place_id BIGINT NOT NULL COMMENT '장소',
  
  name VARCHAR(150) NOT NULL COMMENT '방 이름',
  content VARCHAR(255) NOT NULL COMMENT '방 내용',
  max_capacity INT NOT NULL COMMENT '최대 인원 수 지정',
  default_price int NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '상태(OPEN, CLOSED)',
  room_image VARCHAR(500) COMMENT '방 사진',

  CHECK (status IN('OPEN', 'CLOSED')),
  
  CONSTRAINT `fk_rooms_place` FOREIGN KEY(place_id) REFERENCES places(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='방 테이블';

CREATE TABLE room_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    room_id BIGINT NOT NULL,
    
    CONSTRAINT fk_room_images_room FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE time_slots (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  room_id BIGINT NOT NULL COMMENT '방 번호',
  
  slot_month CHAR(7) NOT NULL COMMENT '생성된 월',
  start_time DATETIME(6) NOT NULL COMMENT '시작시간',
  end_time DATETIME(6) NOT NULL COMMENT '종료시간',
  capacity INT NOT NULL COMMENT '인원 수 지정',
  status VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '슬롯 상태',
  
  holdexpired_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '대기된 시간',
  
  CHECK (status IN('OPEN','CLOSED','HOLD')),
  CONSTRAINT `fk_timeslots_room` FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,

  UNIQUE KEY `uk_time_slots_room_start` (room_id, slot_Month, start_time),
  
  INDEX `idx_time_slots_slot_Month` (slot_Month),
  INDEX `idx_time_slots_start_time` (start_time),
  INDEX `idx_time_slots_end_time` (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='시간 슬롯 테이블';

CREATE TABLE items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  
  time_slot_id BIGINT NOT NULL COMMENT '시간대',

  price INT NOT NULL DEFAULT 0 COMMENT '상품 가격',
  
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',

  CONSTRAINT `fk_items_time_slot` FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='아이템 테이블';

CREATE TABLE bookings (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  
  user_id BIGINT NOT NULL COMMENT '사용자 ID',
  time_slot_id BIGINT NOT NULL COMMENT '타임슬롯 ID',
  room_id BIGINT NOT NULL COMMENT '룸 ID',

  item_snapshot_price BIGINT NOT NULL COMMENT '아이템 가격 저장본',
  booking_code VARCHAR(255) NOT NULL COMMENT '예약 코드',
  booking_date DATE NOT NULL COMMENT '이용 예정 날짜',
  booking_time DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '예약시간',
  qty INT NOT NULL COMMENT '인원체크',
  amount BIGINT NOT NULL COMMENT '가격',
  cancelled_at DATETIME(6) NULL COMMENT '취소일', 
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '상태',
  
  UNIQUE KEY `uk_room_date_slot` (room_id, booking_date, time_slot_id),
  
  CONSTRAINT `fk_bookings_user` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_bookings_time_slot` FOREIGN KEY (time_slot_id) REFERENCES time_slots(id),
  CONSTRAINT `fk_bookings_room` FOREIGN KEY (room_id) REFERENCES rooms(id),

  CHECK (qty > 0),
  CHECK (status IN('PENDING','CONFIRMED','CANCELED','REFUNDED')),
  
  INDEX `idx_bookings_user` (user_id, status),
  INDEX `idx_bookings_time_slot` (time_slot_id),
  INDEX `idx_bookings_code` (booking_code )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='예약 테이블';

CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  
  user_id BIGINT NOT NULL COMMENT '결제한 사용자 ID',
  booking_id BIGINT NOT NULL COMMENT '예약주문 ID',
  
  order_id VARCHAR(100) NOT NULL COMMENT '주문 코드 ID',
  payment_key VARCHAR(100) NOT NULL COMMENT '결제 키 (PG 또는 모의 PG 트랜잭션 키)',
  imp_uid VARCHAR(100) NULL COMMENT '포트원 발급 키',
  amount BIGINT NOT NULL COMMENT '결제 금액(포인트)',
  method VARCHAR(30) NOT NULL COMMENT '결제 수단 (KAKAO_PAY, TOSS_PAY등)',
  status VARCHAR(30) NOT NULL COMMENT '결제 상태',
  product_name VARCHAR(100) NOT NULL COMMENT '상품 이름',
  failure_code VARCHAR(50) NULL COMMENT '결제 실패 코드',
  failure_message VARCHAR(255) NULL COMMENT '결제 실패 사유',
  requested_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '결제 요청 시간',
  approved_at DATETIME(6) NULL COMMENT '결제 승인 시간',
  cancelled_at DATETIME(6) NULL COMMENT '결제 취소/환불 시간',
  
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  
  CHECK (status IN('READY','SUCCESS','FAILED','CANCELLED','REFUNDED', 'COMPLETED')),

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
  
  CHECK (status IN('REQUESTED','COMPLETED','FAILED', 'REJECTED')),
  
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
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '전송 상태(PENDING, SENT, FAILED, COMPLETED)',
  message VARCHAR(255) NOT NULL COMMENT '전송 메시지',
  qr_code VARCHAR(100) NULL COMMENT 'QR 코드 토큰',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  sent_at DATETIME(6) NULL COMMENT '전송 시각',
  
  INDEX `idx_notifications_user_id` (user_id),
  INDEX `idx_notifications_payment_id` (payment_id),
  
  CONSTRAINT `fk_notifications_user` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_notifications_payment` FOREIGN KEY (payment_id) REFERENCES payments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 결제 알림 테이블';

CREATE TABLE reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  
  user_id BIGINT NOT NULL COMMENT '사용자 ID',
  room_id BIGINT NOT NULL COMMENT '숙소 ID',
  payment_id BIGINT NOT NULL COMMENT '결제 완료 ID',
  
  rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5) COMMENT '별점',
  content TEXT NOT NULL COMMENT '리뷰 내용',
  
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
  
  UNIQUE KEY `uk_review_once` (payment_id),
  
  INDEX `idx_review_room` (room_id, rating),
  
  CONSTRAINT `fk_reviews_user_id` FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT `fk_reviews_room_id` FOREIGN KEY (room_id) REFERENCES rooms(id),
  CONSTRAINT `fk_reviews_payment_id` FOREIGN KEY (payment_id) REFERENCES payments(id)
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

-- --------------------------
USE eoullim_db;

INSERT INTO places (name, address, latitude, longitude, category, profile_image, created_at, updated_at)
VALUES
('해운대 파티룸', '부산광역시 해운대구 우동 123-45', 35.1631, 129.1631, 'PARTY', null, NOW(), NOW()),
('광안리 스터디하우스', '부산광역시 수영구 광안동 67-12', 35.1534, 129.1186, 'STUDY', null, NOW(), NOW()),
('부산 연습실', '부산광역시 부산진구 부전동 88-9', 35.1650, 129.0605, 'PRACTICE', null, NOW(), NOW()),
('서면 파티앤펀', '부산광역시 부산진구 부전동 42-1', 35.1595, 129.0606, 'PARTY', null, NOW(), NOW()),
('조용한 공부방 부산', '부산광역시 남구 대연동 21-7', 35.1363, 129.0883, 'STUDY', null, NOW(), NOW());
SELECT * FROM places;

INSERT INTO rooms (place_id, name, content, max_capacity, default_price, status, room_image) VALUES
(1, '스탠다드룸', '기본적인 편의시설을 갖춘 방', 2, 50000, 'OPEN', NULL),
(1, '디럭스룸', '넓고 쾌적한 방', 3, 80000, 'OPEN', NULL),
(1, '스위트룸', '럭셔리 스위트룸, 침대 2개', 4, 150000, 'CLOSED', NULL),
(2, '커넥팅룸', '2개의 방이 연결된 구조', 6, 120000, 'OPEN', NULL),
(3, '패밀리룸', '가족 단위 숙박에 적합', 5, 100000, 'OPEN', NULL);
SELECT * FROM rooms;
SELECT * FROM room_images;