# API詳細設計

## 共通エラー

{
"code": "ERROR_CODE",
"message": "エラーメッセージ",
"details": [],
"traceId": "xxxx"
}

---

## Register

POST /api/auth/register

## OTP Verify

POST /api/auth/otp/verify

## Login

POST /api/auth/login

## Login OTP Verify

POST /api/auth/login/otp/verify

## Logout

POST /api/auth/logout
※ クライアントでtoken削除

---

## Projects一覧

GET /api/projects?page=0&size=20

200 OK
{
"items": [],
"page": 0,
"size": 20,
"total": 0
}

---

## Tasks一覧

GET /api/projects/{id}/tasks?page=0&size=50
