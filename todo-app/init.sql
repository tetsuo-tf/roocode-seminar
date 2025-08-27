-- データベース初期化スクリプト
-- PostgreSQL用のToDo アプリケーション初期設定

-- 必要に応じて拡張機能を有効化
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- タイムゾーンの設定
SET timezone = 'Asia/Tokyo';

-- データベースの文字エンコーディング確認
SHOW server_encoding;

-- 初期データベースの準備完了
SELECT 'Database initialization completed' AS status;
