# ToDo Application

Spring Boot を使用したモダンなToDo管理アプリケーションです。Material Design を採用したユーザーフレンドリーなインターフェースで、効率的なタスク管理を実現します。

## 概要

このアプリケーションは以下の機能を提供します：

### 主な機能
- **ToDo管理**: タスクの作成、編集、削除、完了状態の切り替え
- **検索・フィルタリング**: タイトル検索、完了状態でのフィルタリング
- **期限管理**: 期限日の設定、期限切れタスクの表示
- **統計情報**: 完了率、タスク数の統計表示
- **ユーザー認証**: セキュアなログイン機能

### 設定可能な項目
- **タイトル** (必須、最大200文字)
- **詳細説明** (任意、最大1000文字)
- **期限日** (任意、今日以降の日付)
- **完了状態** (完了/未完了)

## 技術スタック

- **Backend**: Spring Boot 3.5.3
- **Frontend**: Thymeleaf + Material Design
- **Database**: PostgreSQL 15
- **Security**: Spring Security 6
- **Build Tool**: Maven
- **Java Version**: 21

## 必要な環境

- Java 21 以上
- Docker & Docker Compose
- Maven 3.6 以上

## セットアップと起動方法

### 1. リポジトリのクローン
```bash
git clone <repository-url>
cd todo-app
```

### 2. データベースの起動
Docker Compose を使用してPostgreSQLデータベースを起動します：

```bash
docker-compose up -d
```

これにより以下のサービスが起動します：
- **PostgreSQL**: ポート 5432
- **pgAdmin**: ポート 8080 (データベース管理ツール)

### 3. アプリケーションの起動

#### Maven を使用する場合
```bash
./mvnw spring-boot:run
```

#### IDE を使用する場合
`TodoAppApplication.java` の main メソッドを実行してください。

### 4. アプリケーションへのアクセス

アプリケーションが起動したら、以下のURLでアクセスできます：

- **アプリケーション**: http://localhost:8090
- **pgAdmin**: http://localhost:8080

## ログイン情報

### アプリケーション
初回起動時は新規ユーザー登録を行ってください。

### pgAdmin (データベース管理)
- **Email**: admin@todoapp.com
- **Password**: admin

### データベース接続情報
- **Host**: localhost
- **Port**: 5432
- **Database**: todoapp
- **Username**: todouser
- **Password**: todopass

## 開発環境

### テストの実行
```bash
./mvnw test
```

### 本番環境での起動
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## プロジェクト構造

```
todo-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/todoapp/
│   │   │   ├── controller/     # コントローラー
│   │   │   ├── entity/         # エンティティ
│   │   │   ├── repository/     # リポジトリ
│   │   │   ├── service/        # サービス
│   │   │   └── config/         # 設定
│   │   └── resources/
│   │       ├── templates/      # Thymeleafテンプレート
│   │       ├── static/         # 静的ファイル
│   │       └── application.yml # 設定ファイル
│   └── test/                   # テストコード
├── docker-compose.yml          # Docker設定
├── init.sql                    # DB初期化スクリプト
└── pom.xml                     # Maven設定
```

## API エンドポイント

- `GET /` - ホームページ
- `GET /todos` - ToDo一覧
- `GET /todos/new` - ToDo作成フォーム
- `POST /todos` - ToDo作成
- `GET /todos/{id}/edit` - ToDo編集フォーム
- `POST /todos/{id}` - ToDo更新
- `POST /todos/{id}/delete` - ToDo削除
- `POST /todos/{id}/toggle` - 完了状態切り替え
- `GET /todos/overdue` - 期限切れToDo一覧

## トラブルシューティング

### データベース接続エラー
1. Docker Compose でPostgreSQLが起動しているか確認
2. ポート5432が他のプロセスで使用されていないか確認

### アプリケーション起動エラー
1. Java 21がインストールされているか確認
2. Maven依存関係が正しくダウンロードされているか確認

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 作者

TodoApp Team - Version 1.0.0
