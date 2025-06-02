# newsFeed
---
## 사용한 기술

- 언어: Java17
- 프레임워크: Spring Boot 3.4.5
- 데이터베이스: MySQL
- 버전 관리: Git
---

## ERD

<img src="https://d2sqqdb3t4xrq5.cloudfront.net/upload/vXwW6vTZY3HY4DAwH/YmNtYW9KbnFrWnNlcG81U0dfcG5wdzRwMlFvZWRIOXR1OG4ucG5n" alt=""/>


---

## 📘 API 명세 - NewsFeed

이 문서는 NewsFeed 프로젝트의 RESTful API 엔드포인트와 요청/응답 형식을 설명합니다.

---

## 🔐 1. 회원 관리

| Method | URL                  | 설명   | Status Code | Request Body                                                                       | Response                                |
| ------ | -------------------- | ---- | ----------- | ---------------------------------------------------------------------------------- | --------------------------------------- |
| POST   | `/api/auth/signup`   | 회원가입 | 201 Created | `{ "email": "user@example.com", "password": "Password123!", "username": "사용자이름" }` |                                         |
| POST   | `/api/auth/login`    | 로그인  | 200 OK      | `{ "email": "user@example.com", "password": "Password123!" }`                      | `{ "token": "JWT_TOKEN", "userId": 1 }` |
| DELETE | `/api/auth/withdraw` | 회원탈퇴 | 200 OK      | `{ "password": "Password123!" }`                                                   |                                         |

---

## 🙍‍♂️ 2. 프로필 관리

| Method | URL                                       | 설명      | Status Code | Request Body                                                              | Response                                                                                                                                       |
| ------ | ----------------------------------------- | ------- | ----------- | ------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| GET    | `/api/users/{userId}`                     | 프로필 조회  | 200 OK      |                                                                           | `{ "userId": 1, "username": "사용자이름", "email": "user@example.com", "profileImage":"이미지URL", "bio" : "자기소개" }`                                   |
| GET    | `/api/users/search/username?username=홍길동` | 사용자명 검색 | 200 OK      | `username=사용자명&page=0&size=10`                                            | `{[ { "userId": 1, "username": "사용자이름", "email": "user@example.com", "profileImage":"이미지URL", "bio" : "자기소개", "followersCount": "0" } ], 페이징}` |
| PUT    | `/api/users/profile`                      | 프로필 수정  | 200 OK      | `{ "username": "새이름", "profileImage": "이미지 URL", "bio" : "자기소개" }`        | `{ "username": "사용자이름", "email": "user@example.com", "profileImage":"이미지URL", "bio" : "자기소개" }`                                                |
| PUT    | `/api/users/password`                     | 비밀번호 변경 | 200 OK      | `{ "currentPassword": "Password123!", "newPassword": "newPassword123!" }` |                                                                                                                                                |

---

## 📰 3. 뉴스피드 게시물

| Method | URL                   | 설명        | Status Code | Request Body                                                    | Response                                                           |
| ------ | --------------------- | --------- | ----------- | --------------------------------------------------------------- | ------------------------------------------------------------------ |
| POST   | `/api/posts`          | 게시물 작성    | 201 Created | `{ "title": "제목", "content": "내용", "imageUrl": "이미지 URL" }`     |                                                                    |
| GET    | `/api/posts`          | 뉴스피드 조회   | 200 OK      | Query: `page=1&size=10`                                         | `{ "posts":[...], "totalPages": 5, "currentPage": 1 }`             |
| GET    | `/api/posts/{postId}` | 게시물 상세 조회 | 200 OK      |                                                                 | `{ "postId": 1, "title": "제목", "content": "내용", "author": "작성자" }` |
| PUT    | `/api/posts/{postId}` | 게시물 상세 수정 | 200 OK      | `{ "title": "수정제목", "content": "수정내용", "imageUrl": "이미지 URL" }` |                                                                    |
| DELETE | `/api/posts/{postId}` | 게시물 상세 삭제 | 200 OK      |                                                                 |                                                                    |

---

## 🤝 4. 친구 관리

| Method | URL                                     | 설명     | Status Code | Request Body | Response                                                         |
| ------ | --------------------------------------- | ------ | ----------- | ------------ | ---------------------------------------------------------------- |
| POST   | `/api/users/follows/{userId}`           | 팔로우    | 201 Created |              |                                                                  |
| DELETE | `/api/users/follows/{userId}`           | 언팔로우   | 200 OK      |              |                                                                  |
| GET    | `/api/users/{userId}/follows/following` | 팔로잉 목록 | 200 OK      |              | `{[ { "userId": 2, "username": "친구1" } ], "followsCount": "0"}`  |
| GET    | `/api/users/{userId}/follows/followers` | 팔로워 목록 | 200 OK      |              | `{[ { "userId": 3, "username": "친구2" } ], "followsCount" : "0"}` |

---

## 💬 5. 댓글 (도전 기능)

| Method | URL                                           | 설명    | Status Code | Request Body                                   | Response                                                                                                                         |
| ------ | --------------------------------------------- | ----- | ----------- | ---------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| POST   | `/api/posts/{postId}/comments`                | 댓글 작성 | 201 Created | `{ "content": "댓글 내용" }`                       | `{ "author" : "작성자", "content" : "댓글 내용", "createdAt" : 작성일 }`                                                                   |
| GET    | `/api/posts/{postId}/comments?page=0&size=10` | 댓글 조회 | 200 OK      | `@RequestParam page=0 기본 값 0 size =10 기본 값 10` | `{ "comments": [...], "pageNumber" : 0, "pageSize" : 10, "totalComment" : 15, "totalPage" : 2, "first" : true, "last" : false }` |
| GET    | `/api/posts/{postId}/comments/count`          | 댓글 조회 | 200 OK      |                                                | Long타입 변수                                                                                                                        |
| PUT    | `/api/comments/{commentId}`                   | 댓글 수정 | 200 OK      | `{ "content": "수정 댓글" }`                       | `{ "author" : "작성자", "content" : "댓글 내용", "createdAt" : 작성일 }`                                                                   |
| DELETE | `/api/comments/{commentId}`                   | 댓글 삭제 | 200 OK      |                                                |                                                                                                                                  |

---

## ❤️ 6. 좋아요 (도전 기능)

| Method | URL                               | 설명           | Status Code | Request Body | Response                                                                         |
| ------ | --------------------------------- | ------------ | ----------- | ------------ | -------------------------------------------------------------------------------- |
| POST   | `/api/posts/{postId}/likes`       | 게시물 좋아요      | 201 Created |              |                                                                                  |
| GET    | `/api/posts/{postId}/likes`       | 게시물 좋아요 조회   | 200 OK      |              | `{ [{ "likeId" : 1, "userId" : 1, "userName" : "박진", "profileImage" : "--" }] }` |
| GET    | `/api/posts/{postId}/likes/count` | 게시물 좋아요 총 갯수 | 200 OK      |              | Long타입 변수                                                                        |
| DELETE | `/api/posts/{postId}/likes`       | 게시물 좋아요 취소   | 200 OK      |              |                                                                                  |

---

## 🔎 7. 검색

| Method | URL                                       | 설명      | Status Code | Request Body                   | Response      |
| ------ | ----------------------------------------- | ------- | ----------- | ------------------------------ | ------------- |
| GET    | `/api/users/search/username?username=홍길동` | 사용자명 검색 | 200 OK      | `username=사용자명&page=0&size=10` | 검색 결과 + 페이징처리 |
| GET    | `/api/users/search/email`                 | 이메일 검색  | 200 OK      | `email=이메일&page=0&size=10`     | 검색 결과 + 페이징처리 |
| GET    | `/api/posts/search/title`                 | 게시글 검색  | 200 OK      | `title=제목&page=0&size=10`      | 검색 결과 + 페이징처리 |

---

## 🔑 인증 헤더

모든 API요청 시 (회원가입, 로그인 제외):

```http
Authorization: Bearer {JWT_TOKEN}
```
