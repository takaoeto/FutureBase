# フロントエンド基本設計（概要）

## 1. 技術スタック

- Next.js (Pages Router)
- TypeScript
- React
- Axios（API通信）
- JWT認証（localStorage保存）

---

## 2. アーキテクチャ方針

UI層とAPI通信層を分離する。

pages
↓
components
↓
hooks
↓
libs(apiClient)

---

## 3. 設計原則

- APIレスポンス型はTypeScriptで定義する
- ロジックとUIを分離する
- 認証状態はContextで管理する
