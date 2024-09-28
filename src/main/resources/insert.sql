insert into d2csgame.tbl_user (id, created_by, created_date, last_modified_by, last_modified_date, email, password, username, role_id)
values  (1, null, '2024-09-24 13:36:14.000000', null, '2024-09-24 13:36:18.000000', 'vtuyenlam1998@gmail.com', '123', 'tuyenlam987', null);

insert into d2csgame.tbl_tag (id, created_by, created_date, last_modified_by, last_modified_date, name)
values  (1, 'admin', '2024-09-13 15:07:24.000000', 'admin', '2024-09-13 15:07:24.000000', 'Epic'),
        (2, 'admin', '2024-09-13 15:07:24.000000', 'admin', '2024-09-13 15:07:24.000000', 'Legendary'),
        (3, 'admin', '2024-09-13 15:07:24.000000', 'admin', '2024-09-13 15:07:24.000000', 'Immortal');

insert into d2csgame.tbl_character (id, created_by, created_date, last_modified_by, last_modified_date, name, attribute)
values  (1, 'admin', '2024-09-13 15:07:10.000000', 'admin', '2024-09-13 15:07:10.000000', 'Warrior', 'STRENGTH'),
        (2, 'admin', '2024-09-13 15:07:10.000000', 'admin', '2024-09-13 15:07:10.000000', 'Mage', 'INTELLIGENCE'),
        (3, 'admin', '2024-09-13 15:07:10.000000', 'admin', '2024-09-13 15:07:10.000000', 'Rubick', 'INTELLIGENCE'),
        (4, null, '2024-09-14 08:41:07.602430', null, '2024-09-14 08:43:02.504798', 'Bane', 'INTELLIGENCE'),
        (5, null, '2024-09-14 08:43:22.505488', null, '2024-09-14 08:43:46.361698', 'Enigma', 'INTELLIGENCE');

insert into d2csgame.tbl_image (id, created_by, created_date, last_modified_by, last_modified_date, action_id, action_type, file_path, is_primary)
values  (1, null, null, null, null, 1, 'PRODUCT', '/images/gauntlet.png', false),
        (2, null, null, null, null, 1, 'PRODUCT', '/images/axe.png', true),
        (3, null, null, null, null, 0, 'CHARACTER', '/images/warrior.png', true),
        (4, null, null, null, null, 1, 'CHARACTER', '/images/mage.png', false),
        (12, null, '2024-09-14 07:12:29.666227', null, '2024-09-14 07:12:29.666227', 3, 'PRODUCT', 'D:\\upload\\product\\20240914_141227884_ff8688ad5d74587e63755c492c196510.jpg', false),
        (13, null, '2024-09-14 07:14:03.130335', null, '2024-09-14 07:14:03.130335', 3, 'PRODUCT', 'D:\\upload\\product\\20240914_141403124_ff8688ad5d74587e63755c492c196510.jpg', false),
        (15, null, '2024-09-14 07:22:00.114875', null, '2024-09-14 07:22:00.114875', 3, 'PRODUCT', 'D:\\upload\\product\\17262985201073_ff8688ad5d74587e63755c492c196510.jpg', true);

insert into d2csgame.tbl_notification (id, created_by, created_date, last_modified_by, last_modified_date, created_at, message, recipient, action, href, is_read, event_type, user_id)
values  (1, null, '2024-09-27 02:21:16.895843', null, '2024-09-27 02:21:16.895843', '2024-09-27 02:21:16.896843', '123', 'Lâm', null, null, null, null, null),
        (2, null, '2024-09-27 02:21:37.720521', null, '2024-09-27 02:21:37.720521', '2024-09-27 02:21:37.721516', 'Lên', null, null, null, null, null, null);


-- Chèn dữ liệu vào bảng product
insert into d2csgame.tbl_product (id, created_by, created_date, last_modified_by, last_modified_date, demo, name, price, product_type, character_id, description)
values  (1, 'admin', '2024-09-13 14:56:49.000000', 'admin', '2024-09-13 14:56:49.000000', null, 'Gauntlet', 99.99, 'PIECE', 1, 'Gauntlet with 9999 power slap'),
        (2, 'admin', '2024-09-13 14:56:49.000000', 'admin', '2024-09-13 14:56:49.000000', null, 'Axe', 149.99, 'SET', 2, 'The weapon which 1 hit 1 kill'),
        (3, null, '2024-09-14 02:30:25.107073', null, '2024-09-14 02:49:52.694725', 'https://youtu.be/JNFbzKGQDrk', 'Diviner Embrace', 42000, 'SET', 3, 'Diviner Embrace là set cao cấp của Rubick với thiết kế độc đáo và ấn tượng mang tới một phong các hoàn toàn khác biệt.

Set không chỉ có các chi tiết được thiết kế chỉnh chu mà các phần họa tiết đều được chú ý thêm hiệu ứng ánh sáng vào giúp nổi bật và gây ấn tượng mạnh hơn.

Đặc biệt Diviner Embrace còn dễ dàng kết hợp hay phối với các Immortal hay Arcana của Rubick đều rất phù hợp.

Set Diviner Embrace đang được bán trên shop D2SET.NET và có thể trade được ngay.');

ALTER TABLE tbl_product MODIFY  description VARCHAR(500);

insert into d2csgame.product_tag (product_id, tag_id)
values  (1, 1),
        (3, 1),
        (1, 2),
        (3, 3);

insert into d2csgame.tbl_cart (id, created_by, created_date, last_modified_by, last_modified_date, user_id)
values  (2, null, '2024-09-24 09:40:11.928171', null, '2024-09-24 09:40:11.928171', 1);

insert into d2csgame.tbl_cart_item (id, created_by, created_date, last_modified_by, last_modified_date, price, quantity, cart_id, product_id)
values  (5, null, '2024-09-24 09:40:11.934170', null, '2024-09-24 09:40:11.934170', 149.99, 4, 2, 2),
        (6, null, '2024-09-24 09:46:19.015072', null, '2024-09-24 09:46:19.015072', 42000, 10, 2, 3);

insert into d2csgame.tbl_order (id, created_by, created_date, last_modified_by, last_modified_date, order_date, payment_date, status, total_amount, user_id)
values  (1, null, '2024-09-24 09:11:57.813167', null, '2024-09-24 09:11:57.813167', '2024-09-24 09:11:59.674231', null, 'PAID', 168599.96, 1);

insert into d2csgame.tbl_order_item (id, created_by, created_date, last_modified_by, last_modified_date, price, quantity, order_id, product_id)
values  (1, null, '2024-09-24 09:12:08.054637', null, '2024-09-24 09:12:08.054637', 149.99, 4, 1, 2),
        (2, null, '2024-09-24 09:12:03.635646', null, '2024-09-24 09:12:03.635646', 42000, 4, 1, 3);
