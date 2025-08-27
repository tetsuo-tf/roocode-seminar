# コードベース構造

## プロジェクト全体構造

```
roocode_seminar/
├── project_plan.md                    # プロジェクト計画書
├── .roo/                             # Roo設定ディレクトリ
├── .serena/                          # Serena設定・メモリディレクトリ
├── .vscode/                          # VSCode設定
└── todo-app/                         # メインアプリケーション
    ├── src/
    ├── target/
    ├── pom.xml
    ├── docker-compose.yml
    ├── init.sql
    └── mvnw
```

## Spring Boot アプリケーション構造

### Java ソースコード構造
```
todo-app/src/main/java/com/example/todoapp/
├── TodoAppApplication.java           # メインアプリケーションクラス
├── config/
│   └── SecurityConfig.java          # Spring Security設定
├── controller/
│   ├── AuthController.java          # 認証関連コントローラー
│   └── TodoController.java          # ToDo関連コントローラー
├── entity/
│   ├── User.java                     # ユーザーエンティティ
│   └── Todo.java                     # ToDoエンティティ
├── repository/
│   ├── UserRepository.java          # ユーザーリポジトリ
│   └── TodoRepository.java          # ToDoリポジトリ
└── service/
    ├── CustomUserDetailsService.java # Spring Security用ユーザー詳細サービス
    ├── UserService.java             # ユーザーサービス
    └── TodoService.java             # ToDoサービス
```

### リソース構造
```
todo-app/src/main/resources/
├── application.yml                   # アプリケーション設定
├── templates/                        # Thymeleafテンプレート
│   ├── layout/
│   │   └── base.html                # ベーステンプレート
│   ├── auth/
│   │   ├── login.html               # ログインページ
│   │   └── register.html            # ユーザー登録ページ
│   ├── todo/
│   │   ├── list.html                # ToDo一覧ページ
│   │   ├── detail.html              # ToDo詳細ページ
│   │   └── form.html                # ToDo作成・編集ページ
│   └── home.html                    # ホームページ
└── static/                          # 静的リソース
    ├── css/
    │   └── app.css                  # アプリケーションCSS
    └── js/
        └── app.js                   # アプリケーションJavaScript
```

### テスト構造
```
todo-app/src/test/java/com/example/todoapp/
├── TodoAppApplicationTests.java      # アプリケーション統合テスト
└── service/
    ├── TodoServiceTest.java         # ToDoサービステスト
    └── UserServiceTest.java         # ユーザーサービステスト
```

## 主要クラスの役割

### エンティティ層
- **User.java**: ユーザー情報を管理するエンティティ
  - フィールド: id, email, password, name, enabled, createdAt, updatedAt
  - リレーション: One-to-Many with Todo
  - バリデーション: Email形式、パスワード長、名前長制限

- **Todo.java**: ToDo情報を管理するエンティティ
  - フィールド: id, title, description, dueDate, completed, completedAt, createdAt, updatedAt
  - リレーション: Many-to-One with User
  - ヘルパーメソッド: isOverdue(), isDueToday(), isDueSoon()

### リポジトリ層
- **UserRepository.java**: ユーザーデータアクセス
- **TodoRepository.java**: ToDoデータアクセス

### サービス層
- **UserService.java**: ユーザー関連ビジネスロジック
- **TodoService.java**: ToDo関連ビジネスロジック
- **CustomUserDetailsService.java**: Spring Security認証用サービス

### コントローラー層
- **AuthController.java**: 認証関連エンドポイント
- **TodoController.java**: ToDo関連エンドポイント

### 設定層
- **SecurityConfig.java**: Spring Security設定
- **application.yml**: アプリケーション設定

## データベース設計

### テーブル構造
```sql
-- users テーブル
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- todos テーブル
CREATE TABLE todos (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date DATE,
    completed BOOLEAN NOT NULL DEFAULT false,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## 設定ファイル

### Maven設定 (pom.xml)
- Spring Boot 3.5.3
- Java 21
- 主要依存関係: Web, JPA, Security, Thymeleaf, PostgreSQL

### Docker設定 (docker-compose.yml)
- PostgreSQL 15-alpine
- PgAdmin 4
- ネットワーク設定
- ボリューム永続化

### アプリケーション設定 (application.yml)
- データベース接続設定
- JPA/Hibernate設定
- Spring Security設定
- Thymeleaf設定
- 環境別プロファイル (dev, test, prod)

## アーキテクチャパターン

### レイヤードアーキテクチャ
1. **Presentation Layer** (Controller)
2. **Business Layer** (Service)
3. **Persistence Layer** (Repository)
4. **Domain Layer** (Entity)

### 依存関係の方向
```
Controller → Service → Repository → Entity
     ↓         ↓          ↓
   View ← DTO/Model ← Entity
```

### セキュリティアーキテクチャ
- Spring Security による認証・認可
- セッションベース認証
- CSRF保護
- パスワードハッシュ化 (BCrypt)

## 開発環境

### 必要なツール
- Java 21
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL 15+

### 開発サーバー
- アプリケーション: http://localhost:8090
- データベース: localhost:5432
- PgAdmin: http://localhost:8080

### 開発フロー
1. Docker Composeでデータベース起動
2. Spring Boot DevToolsで開発サーバー起動
3. ホットリロード機能で効率的な開発
4. テスト実行でコード品質確保
