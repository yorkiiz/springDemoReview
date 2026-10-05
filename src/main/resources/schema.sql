CREATE TABLE IF NOT EXISTS `ums_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(64) DEFAULT '' COMMENT '昵称',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
    `role` VARCHAR(32) NOT NULL DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `pms_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
    `price` DECIMAL(10,2) NOT NULL COMMENT '销售价格',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
    `description` TEXT COMMENT '商品描述',
    `image` VARCHAR(500) DEFAULT '' COMMENT '商品图片URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_price` (`price`),
    KEY `idx_name` (`name`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE IF NOT EXISTS `oms_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_sn` VARCHAR(64) NOT NULL COMMENT '订单编号(业务唯一)',
    `member_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `pay_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '实付金额',
    `pay_type` TINYINT DEFAULT NULL COMMENT '支付方式：1-支付宝，2-微信',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已取消，3-已退款',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_sn` (`order_sn`),
    KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `oms_order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `order_sn` VARCHAR(64) NOT NULL COMMENT '订单编号',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(255) NOT NULL COMMENT '商品名称(快照)',
    `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品单价(快照)',
    `quantity` INT NOT NULL COMMENT '购买数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

CREATE TABLE IF NOT EXISTS `oms_pay_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `order_sn` VARCHAR(64) NOT NULL COMMENT '订单编号',
    `trade_no` VARCHAR(128) DEFAULT NULL COMMENT '第三方支付流水号',
    `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    `pay_type` TINYINT NOT NULL COMMENT '支付方式：1-支付宝，2-微信',
    `pay_status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0-处理中，1-成功，2-失败',
    `callback_content` TEXT COMMENT '回调原始内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_trade_no` (`trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付流水表';

INSERT IGNORE INTO `ums_member` (`username`, `password`, `nickname`, `role`, `status`)
VALUES ('admin', '$2a$10$hbl6y3FbqMjWmBsSQhodRu6CIoMQ18fyugbkMdU1qyjUaMr5wn/fK', '管理员', 'ADMIN', 1);

INSERT IGNORE INTO `pms_product` (`name`, `price`, `stock`, `description`, `image`, `status`) VALUES
('iPhone 18 Pro 256GB', 9999.00, 100, '苹果最新旗舰，A20芯片，钛金属机身', '/images/iphone18pro.jpg', 1),
('HUAWEI MATE 80 256GB', 5349.00, 120, '华为旗舰，XMAGE影像系统', '/images/huawei-mate80.jpg', 1),
('小米15 128GB', 3598.00, 150, '小米性价比旗舰，骁龙8系芯片', '/images/xiaomi15.jpg', 1),
('OPPO Find X10 256GB', 5499.00, 90, 'OPPO旗舰，哈苏影像系统', '/images/oppo-findx10.jpg', 1),
('VIVO X300 256GB', 2899.00, 130, 'VIVO蔡司影像手机', '/images/vivo-x300.jpg', 1),
('MI 红米 Turbo 5 128GB', 2599.00, 200, '红米性能旗舰，天玑芯片', '/images/mi-redmi-turbo5.jpg', 1),
('小米18 Pro 256GB', 7999.00, 80, '小米高端旗舰，徕卡光学镜头', '/images/xiaomi18pro.jpg', 1),
('VIVO S60 256GB', 3899.00, 110, 'VIVO轻薄自拍旗舰', '/images/vivo-s60.jpg', 1),
('iPhone 18 Pro Max 256GB', 11999.00, 70, '苹果超大屏旗舰，6.9英寸屏幕', '/images/iphone18pro.jpg', 1),
('HUAWEI MATE 80 Pro 256GB', 6999.00, 75, '华为Mate系列影像旗舰', '/images/huawei-mate80.jpg', 1),
('OPPO Reno 13 256GB', 3299.00, 130, 'OPPO Reno系列，人像拍照', '/images/oppo-findx10.jpg', 1),
('小米15 Pro 256GB', 4599.00, 80, '小米Pro级性能旗舰', '/images/xiaomi15.jpg', 1),
('VIVO X300 Pro 256GB', 3999.00, 75, 'VIVO X系列Pro旗舰', '/images/vivo-x300.jpg', 1),
('MI 红米 Turbo 5 Pro', 3299.00, 120, '红米Turbo Pro性能版', '/images/mi-redmi-turbo5.jpg', 1),
('iPhone 18 128GB', 6999.00, 150, '苹果标准版，轻薄设计', '/images/iphone18pro.jpg', 1),
('小米18 Ultra', 6999.00, 50, '小米影像机皇，1英寸大底', '/images/xiaomi18pro.jpg', 1),
('HUAWEI Pura 80 256GB', 4999.00, 100, '华为Pura系列，时尚影像旗舰', '/images/huawei-mate80.jpg', 1),
('VIVO X Fold 4', 8999.00, 35, 'VIVO折叠屏旗舰', '/images/vivo-x300.jpg', 1),
('OPPO Find X10 Pro 256GB', 6999.00, 55, 'OPPO Find Pro影像旗舰', '/images/oppo-findx10.jpg', 1),
('小米Civi 5 256GB', 2699.00, 160, '小米轻薄自拍手机', '/images/xiaomi15.jpg', 1),
('iPhone 18 Pro 512GB', 11499.00, 80, '苹果旗舰大容量版，512GB存储', '/images/iphone18pro.jpg', 1),
('VIVO S60 Pro', 4599.00, 65, 'VIVO S系列Pro版', '/images/vivo-s60.jpg', 1),
('HUAWEI MATE 80 RS', 11999.00, 25, '华为Mate RS非凡大师版', '/images/huawei-mate80.jpg', 1),
('MI 红米 Note 15 Pro', 1999.00, 250, '红米Note系列性价比之王', '/images/mi-redmi-turbo5.jpg', 1),
('OPPO K15 256GB', 2199.00, 180, 'OPPO性价比之选', '/images/oppo-findx10.jpg', 1),
('小米18 标准版 256GB', 4299.00, 120, '小米数字系列标准版', '/images/xiaomi18pro.jpg', 1),
('VIVO iQOO 15 256GB', 3999.00, 90, 'iQOO电竞旗舰', '/images/vivo-s60.jpg', 1),
('iPhone 18 Pro Max 512GB', 13499.00, 45, '苹果超大屏旗舰大容量版', '/images/iphone18pro.jpg', 1),
('HUAWEI MATE 80 512GB', 5999.00, 90, '华为旗舰大容量版', '/images/huawei-mate80.jpg', 1),
('OPPO Reno 13 Pro', 4299.00, 85, 'OPPO Reno Pro人像旗舰', '/images/oppo-findx10.jpg', 1),
('小米15 256GB', 3899.00, 130, '小米性价比之选', '/images/xiaomi15.jpg', 1),
('VIVO X Flip 2', 5999.00, 45, 'VIVO竖折折叠屏手机', '/images/vivo-x300.jpg', 1),
('MI 红米 Turbo 5 256GB', 2899.00, 170, '红米Turbo大容量版', '/images/mi-redmi-turbo5.jpg', 1),
('iPhone 18 Pro 1TB', 13999.00, 50, '苹果旗舰顶配版，1TB超大存储', '/images/iphone18pro.jpg', 1),
('小米18 Pro 512GB', 8699.00, 60, '小米旗舰大容量版', '/images/xiaomi18pro.jpg', 1),
('HUAWEI Pura 80 Pro', 6499.00, 65, '华为Pura Pro影像旗舰', '/images/huawei-mate80.jpg', 1),
('VIVO Y200 128GB', 1599.00, 200, 'VIVO入门长续航手机', '/images/vivo-s60.jpg', 1),
('OPPO Find X10 512GB', 6199.00, 70, 'OPPO旗舰大容量版', '/images/oppo-findx10.jpg', 1),
('小米15 512GB', 4299.00, 100, '小米性能旗舰大容量版', '/images/xiaomi15.jpg', 1),
('iPhone 18 Pro Max 1TB', 15999.00, 30, '苹果超大屏旗舰顶配版', '/images/iphone18pro.jpg', 1),
('VIVO X300 512GB', 3299.00, 100, 'VIVO影像手机大容量版', '/images/vivo-x300.jpg', 1),
('HUAWEI MATE 80 Pro 512GB', 7699.00, 55, '华为Mate Pro大容量版', '/images/huawei-mate80.jpg', 1),
('小米18 Pro 1TB', 9999.00, 35, '小米旗舰顶配版', '/images/xiaomi18pro.jpg', 1),
('OPPO Find X10 Pro 512GB', 7699.00, 40, 'OPPO Find Pro顶配版', '/images/oppo-findx10.jpg', 1),
('VIVO S60 512GB', 4299.00, 85, 'VIVO S系列大容量版', '/images/vivo-s60.jpg', 1),
('小米15 Pro 512GB', 4999.00, 60, '小米Pro旗舰大容量版', '/images/xiaomi15.jpg', 1),
('VIVO X300 Pro 512GB', 4499.00, 55, 'VIVO X Pro大容量版', '/images/vivo-x300.jpg', 1),
('小米Civi 5 512GB', 2999.00, 120, '小米自拍手机大容量版', '/images/xiaomi15.jpg', 1),
('VIVO iQOO 15 Pro', 4799.00, 60, 'iQOO Pro级电竞手机', '/images/vivo-s60.jpg', 1),
('小米18 标准版 512GB', 4799.00, 90, '小米数字系列大容量版', '/images/xiaomi18pro.jpg', 1);
