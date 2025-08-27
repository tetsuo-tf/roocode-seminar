# 推奨開発コマンド

## 基本的な開発コマンド

### プロジェクトのビルドと実行
```bash
# プロジェクトディレクトリに移動
cd todo-app

# Mavenを使用したビルド
./mvnw clean compile

# アプリケーションの実行
./mvnw spring-boot:run

# パッケージ作成
./mvnw clean package

# テスト実行
./mvnw test

# 特定のテストクラス実行
./mvnw test -Dtest=TodoServiceTest

# テストスキップしてビルド
./mvnw clean package -DskipTests
```

### Docker環境の管理
```bash
# Docker Composeでデータベース起動
docker-compose up -d

# データベースのみ起動
docker-compose up -d postgres

# PgAdminも含めて起動
docker-compose up -d postgres pgadmin

# コンテナ停止
docker-compose down

# ボリューム含めて削除
docker-compose down -v

# ログ確認
docker-compose logs postgres
```

### データベース操作
```bash
# PostgreSQLに直接接続
docker exec -it todo-postgres psql -U todouser -d todoapp

# データベースの初期化
docker-compose down -v && docker-compose up -d
```

## 開発ツール

### Maven関連
```bash
# 依存関係の確認
./mvnw dependency:tree

# 依存関係の更新チェック
./mvnw versions:display-dependency-updates

# プラグインの更新チェック
./mvnw versions:display-plugin-updates
```

### Spring Boot DevTools
- ファイル変更時の自動再起動が有効
- LiveReload機能でブラウザ自動更新
- ポート8090でアプリケーション起動

## システムコマンド (macOS)

### ファイル操作
```bash
# ファイル検索
find . -name "*.java" -type f

# 内容検索
grep -r "TodoService" src/

# ディレクトリ一覧
ls -la

# ファイル内容表示
cat src/main/resources/application.yml
```

### プロセス管理
```bash
# ポート使用状況確認
lsof -i :8090
lsof -i :5432

# プロセス終了
kill -9 <PID>
```

### Git操作
```bash
# 状態確認
git status

# 変更をステージング
git add .

# コミット
git commit -m "メッセージ"

# ブランチ確認
git branch

# ログ確認
git log --oneline
```

## アクセスURL
- **アプリケーション**: http://localhost:8090
- **PgAdmin**: http://localhost:8080
  - Email: admin@todoapp.com
  - Password: admin
- **H2 Console** (テスト時): http://localhost:8090/h2-console
