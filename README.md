# Spring Boot ToDoアプリケーション

Spring Boot 3.5.3とMaterial Design Liteを使用したモダンなToDoアプリケーションです。

## 📋 プロジェクト概要

このプロジェクトは、ユーザー認証機能付きのToDoアプリケーションです。Spring Securityによるセキュアな認証システムと、Material Design LiteベースのレスポンシブUIを提供します。

## 🛠️ 技術スタック

- **Backend**: Java 17 + Spring Boot 3.5.3
- **Security**: Spring Security 6.x
- **Database**: PostgreSQL 15+ (開発環境はDocker Compose)
- **ORM**: Spring Data JPA
- **Template Engine**: Thymeleaf
- **UI Framework**: Material Design Lite
- **Build Tool**: Maven
- **Containerization**: Docker Compose

## 📦 必要な環境・依存関係

### 前提条件
- Java 17以上
- Maven 3.6以上
- Docker & Docker Compose

### 主要な依存関係
- spring-boot-starter-web (3.5.3)
- spring-boot-starter-data-jpa (3.5.3)
- spring-boot-starter-security (3.5.3)
- spring-boot-starter-thymeleaf (3.5.3)
- spring-boot-starter-validation (3.5.3)
- postgresql (最新ドライバー)

## 🚀 インストール・セットアップ手順

### 1. リポジトリのクローン
```bash
git clone https://github.com/tetsuo-tf/roocode-seminar.git
cd roocode-seminar
```

### 2. データベースの起動（Docker Compose）
```bash
cd todo-app
docker-compose up -d
```

### 3. アプリケーションのビルドと起動
```bash
# Mavenでビルド
./mvnw clean compile

# アプリケーションの起動
./mvnw spring-boot:run
```

### 4. アプリケーションへのアクセス
ブラウザで以下のURLにアクセスしてください：
```
http://localhost:8090
```

## 💻 使用方法

### 基本機能
1. **ユーザー登録**: 新規アカウントの作成
2. **ログイン/ログアウト**: セキュアな認証システム
3. **ToDo管理**:
   - ToDo項目の作成、編集、削除
   - 完了状態の管理
   - 期限日の設定
4. **ページング**: 大量のToDo項目の効率的な表示

### 画面構成
- ログイン画面
- ユーザー登録画面
- ToDo一覧画面（ページング付き）
- ToDo作成・編集画面

## 🏗️ プロジェクト構造

```
todo-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/todoapp/
│   │   │   ├── TodoAppApplication.java
│   │   │   ├── config/SecurityConfig.java
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.yml
│   └── test/
├── docker-compose.yml
└── pom.xml
```

## 🧪 テスト実行

```bash
# 単体テストの実行
./mvnw test

# 統合テストの実行
./mvnw verify
```

## 🔧 開発環境

### データベース設定
- **Host**: localhost
- **Port**: 5432
- **Database**: todoapp
- **Username**: todouser
- **Password**: todopass

### 開発用ツール
- Spring Boot DevTools（ホットリロード）
- H2インメモリデータベース（テスト用）

## 📝 API仕様

このアプリケーションはThymeleafベースのWebアプリケーションですが、主要なエンドポイントは以下の通りです：

- `GET /` - ホーム画面
- `GET /login` - ログイン画面
- `GET /register` - ユーザー登録画面
- `GET /todos` - ToDo一覧
- `POST /todos` - ToDo作成
- `PUT /todos/{id}` - ToDo更新
- `DELETE /todos/{id}` - ToDo削除

## 🤝 コントリビューション

1. このリポジトリをフォーク
2. 機能ブランチを作成 (`git checkout -b feature/amazing-feature`)
3. 変更をコミット (`git commit -m 'Add some amazing feature'`)
4. ブランチにプッシュ (`git push origin feature/amazing-feature`)
5. プルリクエストを作成

## 📄 ライセンス

このプロジェクトはMITライセンスの下で公開されています。詳細は[LICENSE](LICENSE)ファイルを参照してください。

## 👨‍💻 作成者

**Hiroshi ISHITATE** - [tetsuo-tf](https://github.com/tetsuo-tf)

- Company: Techfirm, Inc.
- Email: tetsuo@techfirm.co.jp
- Website: https://www.techfirm.co.jp/

## 🙏 謝辞

このプロジェクトは学習目的で作成されました。Spring Boot、Material Design Lite、その他のオープンソースプロジェクトの開発者の皆様に感謝いたします。
