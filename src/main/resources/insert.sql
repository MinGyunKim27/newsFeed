-- 개발용****************************
-- DROP DATABASE newsfeed_db;
-- *********************************
CREATE DATABASE IF NOT EXISTS newsfeed_db;

use newsfeed_db;

CREATE TABLE IF NOT EXISTS `users` (
                         `id`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY	COMMENT '유저 고유 식별자',
                         `email`	VARCHAR(255)	NOT NULL UNIQUE COMMENT '이메일',
                         `password`	VARCHAR(255)	NOT NULL	COMMENT '비밀번호',
                         `username`	VARCHAR(100)	NOT NULL	COMMENT '유저이름',
                         `profile_image`	VARCHAR(500)	NULL	COMMENT '이미지',
                         `bio`	TEXT	NULL	COMMENT '자기소개',
                         `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                         `updated_at`	TIMESTAMP	NOT NULL	COMMENT '수정일'
);

CREATE TABLE IF NOT EXISTS `post` (
                        `id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT '게시글 고유 식별자',
                        `user_id`	BIGINT	NOT NULL	COMMENT '유저 고유 식별자',
                        `title`	VARCHAR(200)	NOT NULL	COMMENT '제목',
                        `content`	TEXT	NOT NULL	COMMENT '내용',
                        `image_url`	VARCHAR(500)	NULL	COMMENT '업로드 사진',
                        `like_count` BIGINT NULL COMMENT '좋아요 수',
                        `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                        `updated_at`	TIMESTAMP	NOT NULL	COMMENT '수정일',
                        foreign key (user_id) references users(id)
);

CREATE TABLE IF NOT EXISTS `like` (
                        `id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT '좋아요 고유 식별자',
                        `user_id`	BIGINT	NOT NULL	COMMENT '유저 고유 식별자',
                        `post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
                        `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                        `updated_at`	TIMESTAMP	NULL	COMMENT '수정일',
                        UNIQUE KEY `uk_user_post` (`user_id`, `post_id`) comment '한 사용자가 여러 게시글에 좋아요 중복 금지',
                        foreign key (user_id) references users(id),
                        foreign key (post_id) references post(id)
);

CREATE TABLE IF NOT EXISTS `comment` (
                           `id`	BIGINT	AUTO_INCREMENT PRIMARY KEY	COMMENT '댓글 고유 식별자',
                           `post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
                           `user_id`	BIGINT	NOT NULL	COMMENT '유저 고유 식별자',
                           `content`	TEXT	NOT NULL	COMMENT '댓글',
                           `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                           `updated_at`	TIMESTAMP	NOT NULL	COMMENT '수정일',
                           foreign key (user_id) references users(id),
                           foreign key (post_id) references post(id)
);

CREATE TABLE IF NOT EXISTS `follows` (
                          `id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT '팔로우 고유 식별자',
                          `follower_id`	BIGINT	NOT NULL	COMMENT '팔로워아이디',
                          `following_id`	BIGINT	NOT NULL	COMMENT '팔로잉아이디',
                          `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                          `updated_at`	TIMESTAMP	NULL	COMMENT '수정일',
                          UNIQUE KEY `uk_follower_following` (`follower_id`, `following_id`) comment '동일한 요청값 금지',
                          foreign key (follower_id) references users(id),
                          foreign key (following_id) references users(id)
);

CREATE TABLE IF NOT EXISTS `image`  (
                            `id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT '이미지 고유 식별자',
                            `image_url` VARCHAR(500) NULL COMMENT '이미지 주소값',
                            `post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자'
)
