-- 快递代取平台数据库初始化脚本
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS express_pickup DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE express_pickup;

-- 1. 用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    role_id BIGINT DEFAULT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role_id (role_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 角色表
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码：admin-超级管理员 delivery-配送员 user-普通用户',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 3. 配送员认证表
CREATE TABLE delivery_certification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '认证ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    id_card VARCHAR(18) NOT NULL COMMENT '身份证号',
    student_id VARCHAR(20) DEFAULT NULL COMMENT '学号',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    id_card_front VARCHAR(255) DEFAULT NULL COMMENT '身份证正面照',
    id_card_back VARCHAR(255) DEFAULT NULL COMMENT '身份证反面照',
    student_card VARCHAR(255) DEFAULT NULL COMMENT '学生证照片',
    status TINYINT DEFAULT 0 COMMENT '认证状态：0-未认证 1-审核中 2-已通过 3-已拒绝',
    reject_reason VARCHAR(255) DEFAULT NULL COMMENT '拒绝原因',
    submit_time DATETIME DEFAULT NULL COMMENT '提交时间',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    audit_by BIGINT DEFAULT NULL COMMENT '审核人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送员认证表';

-- 4. 快递站点表
CREATE TABLE express_station (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '站点ID',
    station_name VARCHAR(100) NOT NULL COMMENT '站点名称',
    address VARCHAR(255) DEFAULT NULL COMMENT '站点地址',
    contact_phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='快递站点表';

-- 5. 包裹规格表
CREATE TABLE package_spec (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格ID',
    spec_name VARCHAR(50) NOT NULL COMMENT '规格名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '规格描述',
    extra_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '加价金额',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='包裹规格表';

-- 6. 宿舍楼表
CREATE TABLE dormitory_building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    building_name VARCHAR(50) NOT NULL COMMENT '楼栋名称',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍楼表';

-- 7. 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '下单用户ID',
    delivery_id BIGINT DEFAULT NULL COMMENT '配送员ID',
    station_id BIGINT NOT NULL COMMENT '快递站点ID',
    spec_id BIGINT NOT NULL COMMENT '包裹规格ID',
    pickup_code VARCHAR(50) NOT NULL COMMENT '取件码',
    building_id BIGINT NOT NULL COMMENT '宿舍楼ID',
    room_number VARCHAR(20) NOT NULL COMMENT '房间号',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    base_price DECIMAL(10,2) DEFAULT 2.00 COMMENT '基础价格',
    extra_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '规格加价',
    total_price DECIMAL(10,2) NOT NULL COMMENT '订单总价',
    delivery_income DECIMAL(10,2) DEFAULT 0.00 COMMENT '配送员收入(订单总价的70%)',
    platform_income DECIMAL(10,2) DEFAULT 0.00 COMMENT '平台收入(订单总价的30%)',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-待接单 1-配送中 2-已完成 3-已取消',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态：0-未支付 1-已支付',
    pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
    receive_time DATETIME DEFAULT NULL COMMENT '接单时间',
    complete_time DATETIME DEFAULT NULL COMMENT '完成时间',
    cancel_time DATETIME DEFAULT NULL COMMENT '取消时间',
    cancel_reason VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_delivery_id (delivery_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 8. 通知公告表
CREATE TABLE notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    publish_date DATE DEFAULT NULL COMMENT '发布日期',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    create_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_is_top (is_top),
    INDEX idx_publish_date (publish_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- 9. 账户流水表
CREATE TABLE account_flow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流水ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type TINYINT NOT NULL COMMENT '类型：1-充值 2-消费 3-收入',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    balance DECIMAL(10,2) NOT NULL COMMENT '变动后余额',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    related_id BIGINT DEFAULT NULL COMMENT '关联ID(订单ID等)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户流水表';

-- 初始化数据

-- 插入角色
INSERT INTO sys_role (id, role_name, role_code, description) VALUES
(1, '超级管理员', 'admin', '系统超级管理员，拥有所有权限'),
(2, '配送员', 'delivery', '配送员角色，可以接单配送'),
(3, '普通用户', 'user', '普通用户角色，可以下单');

-- 插入管理员账号 (密码: admin123)
INSERT INTO sys_user (id, username, password, nickname, phone, role_id, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', '13800138000', 1, 1);

-- 插入快递站点
INSERT INTO express_station (station_name, address, sort_order, status) VALUES
('菜鸟驿站', '学校东门菜鸟驿站', 1, 1),
('顺丰速运', '学校南门顺丰网点', 2, 1),
('京东快递', '学校北门京东站点', 3, 1),
('中通快递', '学校西门中通网点', 4, 1),
('圆通快递', '学校东门圆通站点', 5, 1);

-- 插入包裹规格
INSERT INTO package_spec (spec_name, description, extra_price, sort_order, status) VALUES
('小件', '小型包裹，如文件、小饰品等', 0.00, 1, 1),
('中件', '中型包裹，如衣服、鞋子等', 1.00, 2, 1),
('大件', '大型包裹，如被子、大箱子等', 3.00, 3, 1),
('超大件', '超大包裹，需特殊处理', 5.00, 4, 1);

-- 插入宿舍楼
INSERT INTO dormitory_building (building_name, sort_order, status) VALUES
('1号楼', 1, 1),
('2号楼', 2, 1),
('3号楼', 3, 1),
('4号楼', 4, 1),
('5号楼', 5, 1),
('6号楼', 6, 1),
('7号楼', 7, 1),
('8号楼', 8, 1);

-- 插入通知公告
INSERT INTO notice (title, content, publish_date, is_top, status, create_by) VALUES
('平台正式上线通知', '欢迎使用快递代取平台！本平台旨在为同学们提供便捷的快递代取服务。请大家遵守平台规则，文明使用。', CURDATE(), 1, 1, 1),
('配送员招募公告', '我们正在招募配送员！如果你有空闲时间，欢迎申请成为配送员，赚取额外收入。申请路径：个人中心-配送员认证', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 0, 1, 1),
('关于服务费用的说明', '平台基础服务费为2元，根据包裹规格会有额外加价。配送员可获得订单总价的70%作为收入。', DATE_SUB(CURDATE(), INTERVAL 2 DAY), 0, 1, 1);
