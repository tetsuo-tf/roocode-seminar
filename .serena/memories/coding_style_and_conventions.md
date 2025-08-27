# コーディングスタイルと規約

## Java コーディング規約

### 命名規則
- **クラス名**: PascalCase (例: `TodoService`, `UserController`)
- **メソッド名**: camelCase (例: `findByUserId`, `createTodo`)
- **変数名**: camelCase (例: `userId`, `todoList`)
- **定数**: UPPER_SNAKE_CASE (例: `MAX_PAGE_SIZE`)
- **パッケージ名**: 小文字 + ドット記法 (例: `com.example.todoapp.service`)

### ファイル構成
```
com.example.todoapp/
├── TodoAppApplication.java (メインクラス)
├── config/ (設定クラス)
├── controller/ (コントローラー層)
├── entity/ (エンティティ層)
├── repository/ (リポジトリ層)
└── service/ (サービス層)
```

### アノテーション使用パターン

#### エンティティクラス
```java
@Entity
@Table(name = "table_name")
@EntityListeners(AuditingEntityListener.class)
public class EntityName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

#### バリデーション
```java
@NotBlank(message = "日本語エラーメッセージ")
@Size(max = 200, message = "文字数制限メッセージ")
@Email(message = "有効なメールアドレスを入力してください")
```

#### サービスクラス
```java
@Service
@Transactional(readOnly = true)
public class ServiceName {

    @Transactional
    public EntityType create(EntityType entity) {
        // 作成処理
    }
}
```

### Javadocコメント規約
```java
/**
 * クラスの説明
 *
 * @author TodoApp Team
 * @version 1.0
 */
public class ClassName {

    /**
     * メソッドの説明
     *
     * @param param パラメータの説明
     * @return 戻り値の説明
     * @throws ExceptionType 例外の説明
     */
    public ReturnType methodName(ParamType param) {
        // 実装
    }
}
```

## Spring Boot 規約

### レイヤー構成
1. **Controller層**: HTTPリクエスト処理、バリデーション
2. **Service層**: ビジネスロジック、トランザクション管理
3. **Repository層**: データアクセス
4. **Entity層**: データモデル

### トランザクション管理
- サービス層で `@Transactional` を使用
- 読み取り専用の場合は `@Transactional(readOnly = true)`
- クラスレベルで読み取り専用、メソッドレベルで書き込み可能に設定

### エラーハンドリング
- カスタム例外クラスの作成
- `@ControllerAdvice` でグローバル例外処理
- 適切なHTTPステータスコードの返却

## データベース規約

### テーブル命名
- 複数形を使用 (例: `users`, `todos`)
- スネークケース (例: `created_at`, `updated_at`)

### カラム命名
- 主キー: `id`
- 外部キー: `{テーブル名}_id` (例: `user_id`)
- 作成日時: `created_at`
- 更新日時: `updated_at`

### インデックス
- 外部キーには自動的にインデックス作成
- 検索条件に使用するカラムにインデックス追加

## テスト規約

### テストクラス命名
- `{対象クラス名}Test` (例: `TodoServiceTest`)

### テストメソッド命名
- `{メソッド名}_{条件}_{期待結果}` (例: `createTodo_ValidInput_ReturnsCreatedTodo`)

### テストデータ
- `@TestConfiguration` でテスト用設定
- H2インメモリデータベース使用
- `application-test.yml` でテスト環境設定

## フロントエンド規約

### Thymeleafテンプレート
- `layout/base.html` をベーステンプレート
- フラグメント機能を活用
- セキュリティ関連は `th:sec` 属性使用

### CSS/JavaScript
- Material Design Lite使用
- レスポンシブデザイン対応
- アクセシビリティ考慮

## セキュリティ規約

### Spring Security
- CSRF保護有効
- セッション管理適切に設定
- パスワードハッシュ化 (BCrypt)

### バリデーション
- サーバーサイドバリデーション必須
- XSS対策 (Thymeleafの自動エスケープ)
- SQLインジェクション対策 (JPA使用)

## 設定ファイル規約

### application.yml
- 環境別設定 (dev, test, prod)
- 機能別にセクション分け
- コメントで説明追加

### プロファイル使用
- `spring.profiles.active` で環境切り替え
- 本番環境では適切なセキュリティ設定
