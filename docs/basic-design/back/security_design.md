# セキュリティ設計

## 認証方式

- JWT認証を採用
- AuthorizationヘッダーにBearerトークンを付与

## 認可

- プロジェクトはownerのみ操作可能
- JWTからuserIdを取得しowner_idと照合する

## パスワード

- BCryptでハッシュ化
