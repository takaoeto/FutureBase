# テーブル設計

## users

| カラム名   | 型           | 備考   |
| ---------- | ------------ | ------ |
| id         | BIGINT       | PK     |
| name       | VARCHAR(255) |        |
| email      | VARCHAR(255) | UNIQUE |
| password   | VARCHAR(255) |        |
| created_at | DATETIME     |        |

---

## projects

| カラム名   | 型           | 備考         |
| ---------- | ------------ | ------------ |
| id         | BIGINT       | PK           |
| name       | VARCHAR(255) |              |
| owner_id   | BIGINT       | FK(users.id) |
| created_at | DATETIME     |              |
| updated_at | DATETIME     |              |

---

## tasks

| カラム名    | 型           | 備考            |
| ----------- | ------------ | --------------- |
| id          | BIGINT       | PK              |
| project_id  | BIGINT       | FK(projects.id) |
| title       | VARCHAR(255) |                 |
| description | TEXT         |                 |
| status      | BIGINT       | 1/2/3           |
| created_at  | DATETIME     |                 |
| updated_at  | DATETIME     |                 |
