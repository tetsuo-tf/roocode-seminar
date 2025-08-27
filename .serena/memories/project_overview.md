# Spring Boot ToDoアプリケーション プロジェクト概要

## プロジェクトの目的
Spring Boot 3.5.3とMaterial Design Liteを使用したモダンなToDoアプリケーションの開発。
ユーザー認証機能付きの個人用タスク管理システム。

## 技術スタック
- **フレームワーク**: Spring Boot 3.5.3
- **言語**: Java 21
- **データベース**: PostgreSQL 15+ (開発環境), H2 (テスト環境)
- **セキュリティ**: Spring Security 6.x
- **テンプレートエンジン**: Thymeleaf
- **UI/UX**: Material Design Lite
- **ビルドツール**: Maven
- **コンテナ**: Docker Compose
- **開発ツール**: Spring Boot DevTools

## 主要な依存関係
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-thymeleaf
- spring-boot-starter-validation
- thymeleaf-extras-springsecurity6
- postgresql (runtime)
- spring-boot-devtools (development)
- JUnit 5 + Spring Boot Test (testing)

## アプリケーション構成
- **ポート**: 8090
- **データベース**: localhost:5432/todoapp
- **PgAdmin**: localhost:8080
- **コンテキストパス**: /

## 主要機能
1. ユーザー認証・認可 (Spring Security)
2. ユーザー登録機能
3. ToDoのCRUD操作
4. ページング機能 (10件/ページ)
5. レスポンシブデザイン
6. バリデーション機能
7. セキュリティ対策 (CSRF, XSS対策)

## プロジェクト構造
```
todo-app/
├── src/main/java/com/example/todoapp/
│   ├── TodoAppApplication.java (メインクラス)
│   ├── config/ (設定クラス)
│   ├── controller/ (コントローラー層)
│   ├── entity/ (エンティティ層)
│   ├── repository/ (リポジトリ層)
│   └── service/ (サービス層)
├── src/main/resources/
│   ├── templates/ (Thymeleafテンプレート)
│   ├── static/ (静的リソース)
│   └── application.yml (設定ファイル)
└── src/test/ (テストコード)
