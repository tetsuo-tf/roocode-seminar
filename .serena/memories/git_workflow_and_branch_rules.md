# Git ワークフローとブランチ作成ルール

## ワークフロー戦略

### GitHub Flow採用
このプロジェクトでは**GitHub Flow**に準拠したブランチ戦略を採用しています。

- **mainブランチ**: 本番環境にデプロイ可能な安定版
- **機能ブランチ**: 各機能・修正用の作業ブランチ
- **短期間での開発サイクル**: 機能完成後は速やかにマージ・削除

## ブランチ命名規則

### 基本形式
```
<type>/<description>
```

### ブランチタイプ
- `feature/` - 新機能の追加
- `fix/` - バグ修正
- `hotfix/` - 緊急修正
- `docs/` - ドキュメントの更新
- `refactor/` - リファクタリング
- `test/` - テストの追加・修正
- `chore/` - その他の作業（設定変更、依存関係更新など）

### 命名例
```bash
feature/add-user-authentication
feature/todo-pagination
feature/add-readme
fix/login-validation-error
fix/csrf-token-issue
hotfix/security-vulnerability
docs/update-readme
docs/api-documentation
refactor/service-layer-cleanup
refactor/controller-optimization
test/add-integration-tests
test/todo-service-unit-tests
chore/update-dependencies
chore/docker-configuration
```

## ブランチ作成手順

### 1. 最新のmainブランチから作成
```bash
git checkout main
git pull origin main
git checkout -b <type>/<description>
```

### 2. 作業完了後のプッシュ
```bash
git add .
git commit -m "<type>: <description>"
git push origin <type>/<description>
```

### 3. プルリクエスト作成
- GitHubでプルリクエストを作成
- 適切なタイトルと説明を記載
- 関連するissueがある場合は`Closes #<issue-number>`を記載
- レビュアーを指定

## コミットメッセージ規則

### 形式
```
<type>: <description>
```

### タイプ
- `feat` - 新機能
- `fix` - バグ修正
- `docs` - ドキュメント更新
- `style` - コードスタイル修正（機能に影響しない）
- `refactor` - リファクタリング
- `test` - テスト追加・修正
- `chore` - その他の作業

### コミットメッセージ例
```bash
feat: ユーザー認証機能を追加
feat: ToDoのページング機能を実装
fix: ログインバリデーションエラーを修正
fix: CSRFトークンの問題を解決
docs: README.mdにセットアップ手順を追加
refactor: サービス層のコードを整理
test: TodoServiceの単体テストを追加
chore: 依存関係を最新版に更新
```

## ブランチ管理ルール

### 基本ルール
1. **mainブランチ**: 本番環境にデプロイ可能な安定版
2. **機能ブランチ**: 各機能・修正用の作業ブランチ
3. **ブランチの寿命**: 機能完成後は速やかにマージ・削除
4. **レビュー必須**: すべてのプルリクエストはレビューを経てマージ
5. **テスト通過**: マージ前にすべてのテストが通過していること

### マージ戦略
- **Squash and merge**: 機能ブランチの複数コミットを1つにまとめてマージ
- **コミット履歴の整理**: 意味のあるコミット単位でマージ

## 禁止事項

### 絶対に避けるべき行為
- ❌ mainブランチへの直接コミット
- ❌ 他人のブランチへの直接プッシュ
- ❌ 長期間放置されたブランチの維持
- ❌ 意味のないコミットメッセージ（例: "fix", "update", "wip"など）
- ❌ レビューなしでのマージ
- ❌ テスト失敗状態でのマージ

## プルリクエストのベストプラクティス

### プルリクエストタイトル
```
<type>: <簡潔な説明>
```

### プルリクエスト説明テンプレート
```markdown
## 概要
変更内容の簡潔な説明

## 変更内容
- 変更点1
- 変更点2
- 変更点3

## 関連Issue
Closes #<issue-number>

## チェックリスト
- [ ] テストが通過している
- [ ] コードレビューを受けた
- [ ] ドキュメントを更新した（必要に応じて）

## テスト
- [ ] 単体テスト
- [ ] 統合テスト
- [ ] 手動テスト
```

## 緊急時の対応

### Hotfixブランチ
```bash
# 緊急修正の場合
git checkout main
git pull origin main
git checkout -b hotfix/critical-security-fix
# 修正作業
git commit -m "hotfix: 重要なセキュリティ問題を修正"
git push origin hotfix/critical-security-fix
# 即座にプルリクエスト作成・レビュー・マージ
```

## ツールとの連携

### GitHub Actions
- プルリクエスト作成時に自動テスト実行
- mainブランチマージ時にデプロイメント実行

### ブランチ保護ルール
- mainブランチへの直接プッシュを禁止
- プルリクエストレビュー必須
- ステータスチェック必須（テスト通過）

これらのルールに従うことで、プロジェクトの品質と開発効率を維持し、チーム開発を円滑に進めることができます。