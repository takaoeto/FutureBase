# FutureBase API設計書

## 共通仕様

- 認証方式：JWT認証
- Base URL：/api
- レスポンス形式：application/json
- ステータスコード
  - 200 OK
  - 201 Created
  - 400 Bad Request
  - 401 Unauthorized
  - 404 Not Found
  - 500 Internal Server Error

---

# 認証

## ログイン

POST /api/auth/login

### Request

{
"email": "test@example.com",
"password": "password"
}

### Response

200 OK

{
"token": "jwt-token-string"
}

---

# プロジェクト

## プロジェクト一覧取得

GET /api/projects

### Response

200 OK

[
{
"id": 1,
"name": "FutureBase開発"
}
]

---

## プロジェクト新規作成

POST /api/projects

### Request

{
"name": "FutureBase開発"
}

### Response

201 Created

{
"id": 1,
"name": "FutureBase開発"
}

※ プロジェクト作成時、ログインユーザーをowner_idとして保存する。

---

## プロジェクト更新

PUT /api/projects/{projectId}

### Request

{
"name": "FutureBase改修"
}

### Response

200 OK

{
"id": 1,
"name": "FutureBase改修"
}

---

## プロジェクト削除

DELETE /api/projects/{projectId}

### Response

200 OK

{
"message": "Deleted successfully"
}

---

# タスク

## タスク一覧取得

GET /api/projects/{projectId}/tasks

### Response

200 OK

[
{
"id": 1,
"title": "API設計書作成",
"status": "TODO"
}
]

---

## タスク新規作成

POST /api/projects/{projectId}/tasks

### Request

{
"title": "API設計書作成",
"status": "TODO"
}

### Response

201 Created

{
"id": 1,
"title": "API設計書作成",
"status": "TODO"
}

---

## タスク更新

PUT /api/tasks/{taskId}

### Request

{
"title": "API設計書修正",
"status": "DOING"
}

### Response

200 OK

{
"id": 1,
"title": "API設計書修正",
"status": "DOING"
}

---

## タスク削除

DELETE /api/tasks/{taskId}

### Response

200 OK

{
"message": "Deleted successfully"
}
