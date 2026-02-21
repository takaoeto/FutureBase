# フロントエンド状態詳細設計

## AuthContext

保持:

- token
- isAuthenticated

---

## ProjectsPage

state:

- projects: Project[]
- loading: boolean

---

## ProjectDetailPage

state:

- project: Project
- tasks: Task[]
- loading: boolean

---

## Task状態遷移

1 → TODO
2 → DOING
3 → DONE

左右ボタンで status を変更し PUT /api/tasks/{id} 実行
