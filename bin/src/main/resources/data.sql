-- 1. まず categories に挿入
INSERT INTO categories (name) VALUES ('デフォルト');

-- 2. 次に regions に挿入
INSERT INTO regions (name) VALUES ('東京');

-- 3. 最後に users を挿入（category_id=1, region_id=1 が存在する状態で）
INSERT INTO users (name, email, password, category_id, region_id)
VALUES ('山田太郎', 'yamada@example.com', 'hashed_password_here', 1, 1);