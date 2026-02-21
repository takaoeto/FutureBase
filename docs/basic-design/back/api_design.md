# API基本設計

## プロジェクトAPI

| Method | URL                | 内容     |
| ------ | ------------------ | -------- |
| GET    | /api/projects      | 一覧取得 |
| POST   | /api/projects      | 作成     |
| PUT    | /api/projects/{id} | 更新     |
| DELETE | /api/projects/{id} | 削除     |

---

## タスクAPI

| Method | URL                      | 内容     |
| ------ | ------------------------ | -------- |
| GET    | /api/projects/{id}/tasks | 一覧取得 |
| POST   | /api/projects/{id}/tasks | 作成     |
| PUT    | /api/tasks/{id}          | 更新     |
| DELETE | /api/tasks/{id}          | 削除     |
