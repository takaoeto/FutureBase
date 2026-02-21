# システム概要設計

## 1. 構成

Frontend: Next.js (TypeScript SPA)
Backend: Spring Boot 3.x (Java21)
Database: PostgreSQL
ReverseProxy: Nginx
Infra: EC2 + Docker

---

## 2. アーキテクチャ

Client
↓
Nginx
↓
Spring Boot
↓
PostgreSQL

---

## 3. 認証設計

### フロー

登録:
Register → OTP → 有効化

ログイン:
Login → OTP → JWT発行

### JWT仕様

- アルゴリズム: HS256
- 有効期限: 30分
- ペイロード:
  - sub: userId
  - email
  - iat
  - exp

- Refreshトークン: 未実装

---

## 4. 認可設計

### 原則

- ownerのみアクセス可能

### 他人projectId指定時

- 存在しない場合 → 404
- 存在するがownerでない → 403

---

## 5. フロント設計

- JWTはlocalStorage保存
- 401受信 → token削除 → loginへ
- 403受信 → エラー表示

---

## 6. CORS設計

許可Origin:

- 本番ドメイン
- localhost

---

## 7. CSRF

JWT使用のため無効化
