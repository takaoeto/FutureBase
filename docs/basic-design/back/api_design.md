# API一覧

## 認証

POST /api/auth/register
POST /api/auth/otp/verify
POST /api/auth/otp/resend
POST /api/auth/login
POST /api/auth/login/otp/verify
POST /api/auth/logout

## プロジェクト

GET /api/projects
POST /api/projects
PUT /api/projects/{id}
DELETE /api/projects/{id}

## タスク

GET /api/projects/{id}/tasks
POST /api/projects/{id}/tasks
PUT /api/tasks/{id}
DELETE /api/tasks/{id}
