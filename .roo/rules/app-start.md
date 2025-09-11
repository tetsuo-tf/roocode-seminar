# Spring Boot ToDoアプリケーション 起動ガイド

## 📋 概要

このToDoアプリケーションは、開発環境ではH2データベース、本番環境ではPostgreSQLを使用するように設定されています。

## 🚀 起動方法

### 開発環境（H2データベース）- 推奨

```bash
# プロジェクトディレクトリに移動
cd todo-app

# アプリケーション起動（devプロファイルがデフォルト）
./mvnw spring-boot:run
```

**アクセス先:**
- アプリケーション: http://localhost:8090
- H2コンソール: http://localhost:8090/h2-console

**H2コンソール接続設定:**
- JDBC URL: `jdbc:h2:file:./data/todoapp;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1`
- User Name: `sa`
- Password: （空白）

### 本番環境（PostgreSQL）

```bash
# PostgreSQLコンテナ起動
cd todo-app
docker-compose up -d

# アプリケーション起動（prodプロファイル）
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

**アクセス先:**
- アプリケーション: http://localhost:8090
- pgAdmin: http://localhost:8080

**pgAdmin接続設定:**
- Email: admin@todoapp.com
- Password: admin

### テスト実行

```bash
cd todo-app
./mvnw test
```

## 🗄️ データベース設定

### 開発環境（H2）
- **タイプ**: ファイルベースH2データベース
- **場所**: `./data/todoapp.mv.db`
- **特徴**: データが永続化される、Dockerが不要

### 本番環境（PostgreSQL）
- **タイプ**: PostgreSQL 15
- **接続**: localhost:5432
- **データベース**: todoapp
- **ユーザー**: todouser
- **パスワード**: todopass

### テスト環境（H2）
- **タイプ**: インメモリH2データベース
- **特徴**: テスト実行時のみ存在、テスト終了後に削除

## 🔧 プロファイル詳細

| プロファイル | データベース | 用途 | 起動コマンド |
|-------------|-------------|------|-------------|
| `dev` (デフォルト) | H2ファイル | 開発 | `./mvnw spring-boot:run` |
| `prod` | PostgreSQL | 本番 | `./mvnw spring-boot:run -Dspring-boot.run.profiles=prod` |
| `test` | H2インメモリ | テスト | `./mvnw test` |

## 📁 データファイル

### H2データベースファイル
```
todo-app/
├── data/
│   ├── todoapp.mv.db      # H2データベースファイル
│   └── todoapp.trace.db   # H2トレースファイル（必要に応じて）
```

### PostgreSQLデータ
```
todo-app/
├── postgres_data/         # Dockerボリューム（自動作成）
```

## 🛠️ トラブルシューティング

### H2データベースが見つからない場合
初回起動時は正常です。アプリケーションが自動的にデータベースファイルを作成します。

### PostgreSQL接続エラー
```bash
# PostgreSQLコンテナの状態確認
docker-compose ps

# PostgreSQLコンテナ再起動
docker-compose restart postgres
```

### ポート競合エラー
アプリケーションはポート8090を使用します。他のアプリケーションが使用している場合は停止してください。

### データベースリセット

**H2データベース:**
```bash
# データファイル削除
rm -rf todo-app/data/
```

**PostgreSQL:**
```bash
# コンテナとボリューム削除
cd todo-app
docker-compose down -v
docker-compose up -d
```

## 🔐 認証情報

### アプリケーション
- **管理者ユーザー**: admin
- **パスワード**: admin

### H2コンソール
- **ユーザー**: sa
- **パスワード**: （空白）

### pgAdmin
- **Email**: admin@todoapp.com
- **パスワード**: admin

### PostgreSQL
- **ユーザー**: todouser
- **パスワード**: todopass
- **データベース**: todoapp

## 📝 開発時の推奨フロー

1. **開発開始**: `./mvnw spring-boot:run` でH2環境で開発
2. **データ確認**: H2コンソールでデータベース状態を確認
3. **テスト実行**: `./mvnw test` で機能テスト
4. **本番確認**: 必要に応じてprodプロファイルで動作確認
5. **コミット**: 変更をGitにコミット

## 🚪 アプリケーション停止

```bash
# Ctrl+C でアプリケーション停止

# PostgreSQLコンテナ停止（必要に応じて）
cd todo-app
docker-compose down
