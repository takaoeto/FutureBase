# フロントエンド API対応表

## 認証

POST /api/auth/login
→ login.tsx

---

## プロジェクト

GET /api/projects
→ projects/index.tsx

POST /api/projects
→ projects/new.tsx

DELETE /api/projects/{id}
→ projects/index.tsx

---

## タスク

GET /api/projects/{id}/tasks
→ projects/[id].tsx

POST /api/projects/{id}/tasks
→ projects/[id].tsx

PUT /api/tasks/{id}
→ projects/[id].tsx
