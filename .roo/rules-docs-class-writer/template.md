# <ClassName> 設計書

- **FQCN**: <package>.<ClassName>
- **所在**: `<relative_path>`  （例: `src/main/java/com/example/user/UserService.java`）
- **役割要約**: 2〜3文でクラスの責務を要約。
- **設計要約**: 採用パターン/重要な設計判断を一行ずつ。

## 1. 責務（Responsibility）
- 主責務:
- 副次責務:
- 非責務（やらないこと）:

## 2. 公開API（メソッド一覧）
| 可視性 | シグネチャ | 入力 | 出力 | 例外 | 前提/事後条件 | 計算量 | スレッド安全性 |
|---|---|---|---|---|---|---|---|
| public | `User findById(Long id)` | `id: Long` | `User` | `NotFoundException` | id≠null / ユーザ存在 | O(1) | 不変 |

（必要行だけ増やす。内部メソッドは別表でも可）

## 3. フィールド/状態
| 可視性 | フィールド | 型 | 初期値 | 不変性 | 役割 |
|---|---|---|---|---|---|

- 不変条件（クラス不変量）:
- ミューテーションポイント:

## 4. 依存関係
- 直接依存（内部 / 外部ライブラリ）:
- 依存方向: このクラス → 相手 / 相手 → このクラス
- 同期/非同期、I/O有無、トランザクション境界:

### 4.1 関連図（Mermaid）
```mermaid
classDiagram
  ClassA <|-- ClassB
  ClassA --> RepositoryX : uses
  ClassA ..> EventY : publishes
