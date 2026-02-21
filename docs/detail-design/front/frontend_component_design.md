# フロントエンド詳細設計（コンポーネント）

## 1. LoginPage

入力:

- email
- password

処理:

- login()
- JWT保存
- /projects遷移

---

## 2. ProjectsPage

表示:

- プロジェクト一覧

処理:

- GET /api/projects
- 削除機能

---

## 3. NewProjectPage

入力:

- name

処理:

- POST /api/projects
- 成功後 /projects遷移

---

## 4. ProjectDetailPage

表示:

- タスク一覧（カンバン）

処理:

- GET /api/projects/{id}/tasks
- タスク追加
- タスク移動
