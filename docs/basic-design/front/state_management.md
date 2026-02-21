# 状態管理設計（基本）

## 管理方針

- 認証状態 → React Context
- APIデータ → useState + useEffect
- 将来的に react-query 導入可能

---

## 管理対象

| 状態             | 管理方法     |
| ---------------- | ------------ |
| JWT              | localStorage |
| ログイン状態     | AuthContext  |
| プロジェクト一覧 | useState     |
| タスク一覧       | useState     |
