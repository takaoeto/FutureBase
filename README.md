# FutureBase - Backend

Spring Boot 3.x / Java 21 で実装されたタスク管理APIです。

## 技術スタック

| 項目 | 採用技術 |
|------|---------|
| フレームワーク | Spring Boot 3.2.x |
| 言語 | Java 21 |
| 認証 | JWT (HS256) + メールOTP |
| DB | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| セキュリティ | Spring Security 6 |
| パスワード | BCrypt |
| メール | Spring Mail (開発時: MailHog) |

---

## 起動方法

### Docker Compose（推奨）

```bash
# 起動（DB + App + MailHog）
docker-compose up --build

# バックグラウンド起動
docker-compose up -d --build
```

起動後:
- API: http://localhost:8080
- MailHog Web UI（OTP確認）: http://localhost:8025

### ローカル起動（PostgreSQL別途必要）

```bash
# DB作成
createdb futurebase

# スキーマ適用
psql -d futurebase -f src/main/resources/schema.sql

# 起動
./mvnw spring-boot:run
```

---

## 環境変数

| 変数名 | 説明 | デフォルト |
|--------|------|----------|
| DB_URL | PostgreSQL接続URL | jdbc:postgresql://localhost:5432/futurebase |
| DB_USERNAME | DBユーザー | futurebase |
| DB_PASSWORD | DBパスワード | futurebase |
| JWT_SECRET | JWT署名シークレット（本番は必ず変更） | - |
| MAIL_HOST | SMTPホスト | smtp.gmail.com |
| MAIL_PORT | SMTPポート | 587 |
| MAIL_USERNAME | SMTPユーザー | - |
| MAIL_PASSWORD | SMTPパスワード | - |
| FRONT_ORIGIN | フロントエンドオリジン（CORS） | http://localhost:3000 |

---

## APIエンドポイント一覧

### 認証

| Method | Path | 説明 |
|--------|------|------|
| POST | /api/auth/register | ユーザー登録（OTP送信） |
| POST | /api/auth/otp/verify | 登録OTP検証（アカウント有効化） |
| POST | /api/auth/login | ログイン（OTP送信） |
| POST | /api/auth/login/otp/verify | ログインOTP検証（JWT発行） |
| POST | /api/auth/logout | ログアウト |

### プロジェクト（要JWT）

| Method | Path | 説明 |
|--------|------|------|
| GET | /api/projects | 一覧取得（ページング） |
| POST | /api/projects | 新規作成 |
| PUT | /api/projects/{id} | 更新 |
| DELETE | /api/projects/{id} | 削除 |

### タスク（要JWT）

| Method | Path | 説明 |
|--------|------|------|
| GET | /api/projects/{id}/tasks | 一覧取得（ページング） |
| POST | /api/projects/{id}/tasks | 新規作成 |
| PUT | /api/tasks/{id} | 更新 |
| DELETE | /api/tasks/{id} | 削除 |

---

## 認証フロー

```
【登録】
1. POST /api/auth/register  → メールにOTP送信
2. POST /api/auth/otp/verify → アカウント有効化

【ログイン】
1. POST /api/auth/login → メールにOTP送信
2. POST /api/auth/login/otp/verify → JWT取得

【API利用】
Authorization: Bearer {token}
```

---

## プロジェクト構成

```
src/main/java/com/futurebase/
├── FuturebaseApplication.java
├── config/
│   └── SecurityConfig.java          # Spring Security設定・CORS
├── controller/
│   ├── AuthController.java
│   ├── ProjectController.java
│   └── TaskController.java
├── dto/
│   ├── request/
│   │   ├── AuthRequest.java         # Register/Login/OtpVerify
│   │   └── DomainRequest.java       # Project/Task CRUD
│   └── response/
│       └── ApiResponse.java         # 全レスポンスDTO
├── entity/
│   ├── User.java
│   ├── Project.java
│   ├── Task.java
│   ├── TaskStatus.java
│   └── OtpToken.java
├── exception/
│   ├── AppException.java            # カスタム例外（内部クラス）
│   ├── ErrorCode.java
│   └── GlobalExceptionHandler.java  # @RestControllerAdvice
├── filter/
│   └── JwtFilter.java               # JWT検証フィルター
├── repository/
│   ├── UserRepository.java
│   ├── ProjectRepository.java
│   ├── TaskRepository.java
│   └── OtpRepository.java
├── security/
│   └── JwtProvider.java             # JWT生成・検証
└── service/
    ├── AuthService.java
    ├── OtpService.java
    ├── ProjectService.java
    └── TaskService.java
```

---

## OTP開発時の確認方法

MailHog を使うと実際にメールを送らずOTPを確認できます。

1. `docker-compose up` で起動
2. http://localhost:8025 を開く
3. 登録/ログイン後、受信メールにOTPが表示される

---

## セキュリティ設計

- **パスワード**: BCryptでハッシュ化
- **OTP**: BCryptでハッシュ化、5分有効、5回失敗でアカウントロック
- **JWT**: HS256、有効期限30分、ステートレス運用
- **CSRF**: JWTを使用するため無効化
- **CORS**: 許可Origin設定で管理
