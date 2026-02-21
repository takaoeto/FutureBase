# システム概要設計

## 構成

- Frontend: Next.js (TypeScript)
- Backend: Spring Boot (Java 21 LTS)
- Database: PostgreSQL
- Reverse Proxy: Nginx
- Infra: EC2 + Docker

## アーキテクチャ

Client
↓
Nginx
↓
Spring Boot
↓
PostgreSQL

## 認証

- JWT + メールOTP
