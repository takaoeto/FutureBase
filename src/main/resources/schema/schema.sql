-- ============================================================
-- FutureBase schema.sql
-- DDL + DML（開発用初期データ）
-- ============================================================
-- ============================================================
-- DDL
-- ============================================================
-- ユーザーテーブル
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT FALSE,
  locked BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
-- プロジェクトテーブル
CREATE TABLE IF NOT EXISTS projects (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  owner_id BIGINT NOT NULL REFERENCES users(id),
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
-- タスクテーブル
CREATE TABLE IF NOT EXISTS tasks (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  status VARCHAR(20) NOT NULL CHECK (status IN ('TODO', 'DOING', 'DONE')),
  project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
-- OTPトークンテーブル
CREATE TABLE IF NOT EXISTS otp_tokens (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  otp_hash VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  consumed_at TIMESTAMP,
  attempt_count INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
-- インデックス
CREATE INDEX IF NOT EXISTS idx_projects_owner_id ON projects(owner_id);
CREATE INDEX IF NOT EXISTS idx_tasks_project_id ON tasks(project_id);
CREATE INDEX IF NOT EXISTS idx_otp_tokens_user_id ON otp_tokens(user_id);
-- ============================================================
-- DML（開発用初期データ）
-- ============================================================
-- パスワードはすべて "password1234" の BCrypt ハッシュ
-- BCrypt生成例: BCrypt.hashpw("password1234", BCrypt.gensalt())
-- ============================================================
-- ---- users ----
-- user1: 有効化済み・ロックなし（通常ログイン可能）
-- user2: 有効化済み・ロックなし（別オーナー確認用）
-- user3: 未有効化（OTP未検証の状態を再現）
-- user4: ロック済み（ロック状態を再現）
INSERT INTO users (
    id,
    email,
    password_hash,
    enabled,
    locked,
    created_at
  )
VALUES (
    1,
    'user1@example.com',
    '$2a$10$wSD3B1uXoO8GETKdOIHE6.5BObm2ORtPzW3PcwJc4WkHkNqjfP0ga',
    TRUE,
    FALSE,
    '2026-02-01 09:00:00'
  ),
  (
    2,
    'user2@example.com',
    '$2a$10$wSD3B1uXoO8GETKdOIHE6.5BObm2ORtPzW3PcwJc4WkHkNqjfP0ga',
    TRUE,
    FALSE,
    '2026-02-02 09:00:00'
  ),
  (
    3,
    'user3@example.com',
    '$2a$10$wSD3B1uXoO8GETKdOIHE6.5BObm2ORtPzW3PcwJc4WkHkNqjfP0ga',
    FALSE,
    FALSE,
    '2026-02-03 09:00:00'
  ),
  (
    4,
    'user4@example.com',
    '$2a$10$wSD3B1uXoO8GETKdOIHE6.5BObm2ORtPzW3PcwJc4WkHkNqjfP0ga',
    TRUE,
    TRUE,
    '2026-02-04 09:00:00'
  ) ON CONFLICT (id) DO NOTHING;
-- シーケンスをINSERT済みの最大値に合わせる
SELECT setval(
    'users_id_seq',
    (
      SELECT MAX(id)
      FROM users
    )
  );
-- ---- projects ----
-- user1 owns: プロジェクトA（タスクあり）、プロジェクトB（タスクなし）
-- user2 owns: プロジェクトC（403確認用）
INSERT INTO projects (id, name, owner_id, created_at)
VALUES (1, 'プロジェクトA', 1, '2026-02-05 10:00:00'),
  (2, 'プロジェクトB', 1, '2026-02-06 10:00:00'),
  (3, 'プロジェクトC', 2, '2026-02-07 10:00:00') ON CONFLICT (id) DO NOTHING;
SELECT setval(
    'projects_id_seq',
    (
      SELECT MAX(id)
      FROM projects
    )
  );
-- ---- tasks ----
-- プロジェクトA配下にTODO/DOING/DONEの3ステータスを用意
-- プロジェクトBはタスクなし（空一覧の確認用）
-- プロジェクトCはuser2所有（403確認用）
INSERT INTO tasks (id, title, status, project_id, created_at)
VALUES -- プロジェクトA（user1所有）
  (
    1,
    '要件定義書を作成する',
    'DONE',
    1,
    '2026-02-08 10:00:00'
  ),
  (
    2,
    '基本設計書を作成する',
    'DONE',
    1,
    '2026-02-08 11:00:00'
  ),
  (
    3,
    '詳細設計書を作成する',
    'DOING',
    1,
    '2026-02-09 10:00:00'
  ),
  (
    4,
    'バックエンド実装',
    'DOING',
    1,
    '2026-02-10 10:00:00'
  ),
  (
    5,
    'フロントエンド実装',
    'TODO',
    1,
    '2026-02-11 10:00:00'
  ),
  (
    6,
    'E2Eテスト実施',
    'TODO',
    1,
    '2026-02-12 10:00:00'
  ),
  (
    7,
    'デプロイ作業',
    'TODO',
    1,
    '2026-02-13 10:00:00'
  ),
  -- プロジェクトC（user2所有・403確認用）
  (
    8,
    'タスク from user2',
    'TODO',
    3,
    '2026-02-14 10:00:00'
  ) ON CONFLICT (id) DO NOTHING;
SELECT setval(
    'tasks_id_seq',
    (
      SELECT MAX(id)
      FROM tasks
    )
  );
-- ---- otp_tokens ----
-- 消費済みOTPを履歴として残す（otp_hash は "123456" の BCrypt ハッシュ）
INSERT INTO otp_tokens (
    id,
    user_id,
    otp_hash,
    expires_at,
    consumed_at,
    attempt_count,
    created_at
  )
VALUES (
    1,
    1,
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFDS',
    '2026-02-01 09:10:00',
    '2026-02-01 09:05:00',
    0,
    '2026-02-01 09:00:00'
  ) ON CONFLICT (id) DO NOTHING;
SELECT setval(
    'otp_tokens_id_seq',
    (
      SELECT MAX(id)
      FROM otp_tokens
    )
  );