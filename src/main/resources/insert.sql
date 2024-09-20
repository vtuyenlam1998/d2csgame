-- Chèn dữ liệu vào bảng product
INSERT INTO tbl_product (name, description, price, product_type, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    ('Gauntlet', 'Powerful glove with magic abilities', 99.99, 'PIECE', 'admin', NOW(), 'admin', NOW()),
    ('Axe', 'Axe with sharp blade', 149.99, 'SET', 'admin', NOW(), 'admin', NOW());

-- Lấy ID của các product vừa chèn để sử dụng trong bảng image
SET @product1_id = LAST_INSERT_ID(); -- Lấy ID của sản phẩm mới nhất (Axe)
SET @product2_id = LAST_INSERT_ID() - 1; -- ID của sản phẩm trước đó (Gauntlet)

-- Chèn dữ liệu vào bảng image
INSERT INTO tbl_image (action_id, action_type, is_primary, file_path)
VALUES
    (@product2_id, 'PRODUCT', TRUE, '/images/gauntlet.png'),
    (@product1_id, 'PRODUCT', FALSE, '/images/axe.png');

-- Chèn dữ liệu vào bảng character
INSERT INTO tbl_character (name, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    ('Warrior', 'admin', NOW(), 'admin', NOW()),
    ('Mage', 'admin', NOW(), 'admin', NOW());

-- Lấy ID của các character vừa chèn để sử dụng trong bảng image
SET @character1_id = LAST_INSERT_ID(); -- Lấy ID của character mới nhất (Mage)
SET @character2_id = LAST_INSERT_ID() - 1; -- ID của character trước đó (Warrior)

-- Chèn dữ liệu vào bảng image
INSERT INTO tbl_image (action_id, action_type, is_primary, file_path)
VALUES
    (@character2_id, 'CHARACTER', TRUE, '/images/warrior.png'),
    (@character1_id, 'CHARACTER', FALSE, '/images/mage.png');

-- Chèn dữ liệu vào bảng tag
INSERT INTO tbl_tag (name, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    ('Epic', 'admin', NOW(), 'admin', NOW()),
    ('Legendary', 'admin', NOW(), 'admin', NOW());

-- Lấy ID của các tag vừa chèn để sử dụng trong bảng product_tag (nếu cần)
SET @tag1_id = LAST_INSERT_ID(); -- Lấy ID của tag mới nhất (Legendary)
SET @tag2_id = LAST_INSERT_ID() - 1; -- ID của tag trước đó (Epic)

-- Nếu bạn có bảng kết nối nhiều-mối quan hệ giữa Product và Tag, chẳng hạn như `product_tag`:
-- Thay đổi các giá trị của product_id và tag_id phù hợp với dữ liệu của bạn.

-- Giả sử bạn có bảng product_tag (có thể đã được định nghĩa trước):
-- Chèn dữ liệu vào bảng product_tag (chỉ nếu có bảng này)
-- INSERT INTO product_tag (product_id, tag_id)
-- VALUES
--     (1, @tag2_id),  -- Gán tag Epic cho product với ID 1
--     (2, @tag1_id);  -- Gán tag Legendary cho product với ID 2
ALTER TABLE tbl_product MODIFY  description VARCHAR(500);
