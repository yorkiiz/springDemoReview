CREATE TABLE IF NOT EXISTS `ums_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(64) DEFAULT '' COMMENT '昵称',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
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
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
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

INSERT IGNORE INTO `ums_member` (`username`, `password`, `nickname`, `status`)
VALUES ('admin', '$2a$10$hbl6y3FbqMjWmBsSQhodRu6CIoMQ18fyugbkMdU1qyjUaMr5wn/fK', '管理员', 1);

INSERT IGNORE INTO `pms_product` (`name`, `price`, `stock`, `description`, `status`) VALUES
('iPhone 16', 5999.00, 100, '苹果手机', 1),
('MacBook Pro', 12999.00, 50, '苹果笔记本电脑', 1),
('AirPods Pro', 1899.00, 200, '无线耳机', 1);
