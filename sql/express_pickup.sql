/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3307
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:3307
 Source Schema         : express_pickup

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 13/04/2026 18:39:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_flow
-- ----------------------------
DROP TABLE IF EXISTS `account_flow`;
CREATE TABLE `account_flow`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `type` tinyint(4) NOT NULL COMMENT '类型：1-充值 2-消费 3-收入',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `balance` decimal(10, 2) NOT NULL COMMENT '变动后余额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `related_id` bigint(20) NULL DEFAULT NULL COMMENT '关联ID(订单ID等)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账户流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_flow
-- ----------------------------
INSERT INTO `account_flow` VALUES (1, 2, 1, 1.00, 1.00, '账户充值', NULL, '2026-03-27 12:38:07');
INSERT INTO `account_flow` VALUES (2, 2, 1, 1.00, 2.00, '账户充值', NULL, '2026-03-28 17:30:42');
INSERT INTO `account_flow` VALUES (3, 2, 1, 1.00, 3.00, '账户充值', NULL, '2026-03-28 17:30:49');
INSERT INTO `account_flow` VALUES (4, 2, 1, 1.00, 4.00, '账户充值', NULL, '2026-03-28 17:31:06');
INSERT INTO `account_flow` VALUES (5, 2, 1, 1.00, 5.00, '账户充值', NULL, '2026-03-28 17:31:07');
INSERT INTO `account_flow` VALUES (6, 2, 2, -3.00, 2.00, '订单支付：EX20260328173028405323', 1, '2026-03-28 17:31:29');
INSERT INTO `account_flow` VALUES (7, 6, 3, 2.10, 4.20, '配送收入：EX20260328173028405323', 1, '2026-03-28 17:43:32');
INSERT INTO `account_flow` VALUES (8, 2, 1, 20.00, 22.00, '账户充值', NULL, '2026-03-28 17:44:51');

-- ----------------------------
-- Table structure for delivery_certification
-- ----------------------------
DROP TABLE IF EXISTS `delivery_certification`;
CREATE TABLE `delivery_certification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '认证ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `id_card_front` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证正面照',
  `id_card_back` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证反面照',
  `student_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学生证照片',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '认证状态：0-未认证 1-审核中 2-已通过 3-已拒绝',
  `reject_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `audit_by` bigint(20) NULL DEFAULT NULL COMMENT '审核人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配送员认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of delivery_certification
-- ----------------------------
INSERT INTO `delivery_certification` VALUES (1, 6, '哆啦A梦', '450123192403192437', '20340121', '13733248342', NULL, NULL, NULL, 2, NULL, '2026-03-28 17:41:23', '2026-03-28 17:42:27', 4, '2026-03-28 17:41:23', '2026-03-28 17:41:23');

-- ----------------------------
-- Table structure for dormitory_building
-- ----------------------------
DROP TABLE IF EXISTS `dormitory_building`;
CREATE TABLE `dormitory_building`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '楼栋ID',
  `building_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋名称',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宿舍楼表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dormitory_building
-- ----------------------------
INSERT INTO `dormitory_building` VALUES (1, '1号楼', 1, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (2, '2号楼', 2, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (3, '3号楼', 3, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (4, '4号楼', 4, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (5, '5号楼', 5, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (6, '6号楼', 6, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (7, '7号楼', 7, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `dormitory_building` VALUES (8, '8号楼', 8, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');

-- ----------------------------
-- Table structure for express_station
-- ----------------------------
DROP TABLE IF EXISTS `express_station`;
CREATE TABLE `express_station`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '站点ID',
  `station_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点地址',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '快递站点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of express_station
-- ----------------------------
INSERT INTO `express_station` VALUES (1, '菜鸟驿站', '学校东门菜鸟驿站', NULL, 1, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `express_station` VALUES (2, '顺丰速运', '学校南门顺丰网点', NULL, 2, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `express_station` VALUES (3, '京东快递', '学校北门京东站点', NULL, 3, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `express_station` VALUES (4, '中通快递', '学校西门中通网点', NULL, 4, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `express_station` VALUES (5, '圆通快递', '学校东门圆通站点', NULL, 5, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `express_station` VALUES (6, '中国邮政快递', '学校北门快递站点', '13800012334', 6, 1, '2026-04-02 01:36:41', '2026-04-02 01:36:41');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `publish_date` date NULL DEFAULT NULL COMMENT '发布日期',
  `is_top` tinyint(4) NULL DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_top`(`is_top` ASC) USING BTREE,
  INDEX `idx_publish_date`(`publish_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '平台正式上线通知', '欢迎使用快递代取平台！本平台旨在为同学们提供便捷的快递代取服务。请大家遵守平台规则，文明使用。', '2026-03-27', 1, 1, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `notice` VALUES (2, '配送员招募公告', '我们正在招募配送员！如果你有空闲时间，欢迎申请成为配送员，赚取额外收入。申请路径：个人中心-配送员认证', '2026-03-26', 0, 1, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `notice` VALUES (3, '关于服务费用的说明', '平台基础服务费为2元，根据包裹规格会有额外加价。配送员可获得订单总价的70%作为收入。', '2026-03-25', 0, 1, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '下单用户ID',
  `delivery_id` bigint(20) NULL DEFAULT NULL COMMENT '配送员ID',
  `station_id` bigint(20) NOT NULL COMMENT '快递站点ID',
  `spec_id` bigint(20) NOT NULL COMMENT '包裹规格ID',
  `pickup_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '取件码',
  `building_id` bigint(20) NOT NULL COMMENT '宿舍楼ID',
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `base_price` decimal(10, 2) NULL DEFAULT 2.00 COMMENT '基础价格',
  `extra_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '规格加价',
  `total_price` decimal(10, 2) NOT NULL COMMENT '订单总价',
  `delivery_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '配送员收入(订单总价的70%)',
  `platform_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '平台收入(订单总价的30%)',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '订单状态：0-待接单 1-配送中 2-已完成 3-已取消',
  `pay_status` tinyint(4) NULL DEFAULT 0 COMMENT '支付状态：0-未支付 1-已支付',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '接单时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消原因',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_delivery_id`(`delivery_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'EX20260328173028405323', 2, 6, 2, 2, '0164', 6, '707', '', 2.00, 1.00, 3.00, 2.10, 0.90, 2, 1, '2026-03-28 17:31:29', '2026-03-28 17:42:52', '2026-03-28 17:43:32', NULL, NULL, '2026-03-28 17:30:29', '2026-03-28 17:43:32');
INSERT INTO `orders` VALUES (2, 'EX20260328174424060732', 2, NULL, 4, 3, '1384', 2, '317', '', 2.00, 3.00, 5.00, 3.50, 1.50, 0, 0, NULL, NULL, NULL, NULL, NULL, '2026-03-28 17:44:24', '2026-03-28 17:44:24');

-- ----------------------------
-- Table structure for package_spec
-- ----------------------------
DROP TABLE IF EXISTS `package_spec`;
CREATE TABLE `package_spec`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `spec_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格描述',
  `extra_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '加价金额',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '包裹规格表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of package_spec
-- ----------------------------
INSERT INTO `package_spec` VALUES (1, '小件', '小型包裹，如文件、小饰品等', 0.00, 1, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `package_spec` VALUES (2, '中件', '中型包裹，如衣服、鞋子等', 1.00, 2, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `package_spec` VALUES (3, '大件', '大型包裹，如被子、大箱子等', 3.00, 3, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `package_spec` VALUES (4, '超大件', '超大包裹，需特殊处理', 5.00, 4, 1, '2026-03-27 01:43:57', '2026-03-27 01:43:57');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码：admin-超级管理员 delivery-配送员 user-普通用户',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', '系统超级管理员，拥有所有权限', '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `sys_role` VALUES (2, '配送员', 'delivery', '配送员角色，可以接单配送', '2026-03-27 01:43:57', '2026-03-27 01:43:57');
INSERT INTO `sys_role` VALUES (3, '普通用户', 'user', '普通用户角色，可以下单', '2026-03-27 01:43:57', '2026-03-27 01:43:57');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '账户余额',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (2, 'user', '$2a$10$jhbfKB6ROTE7fqRoP3OJ6u7TCijIpZYz/J51JHWmSaEiA/fUgRlMK', 'test1', NULL, '13754533421', NULL, 22.00, 1, 3, '2026-03-27 02:57:32', '2026-03-28 17:44:50');
INSERT INTO `sys_user` VALUES (4, 'admin', '$2a$10$bRzRC7aby8ibryt.5aZc4OopF4jfCPk2Q4MrSuqRK/YTaY67WFKpu', '管理员', NULL, '13800138000', NULL, 0.00, 1, 1, '2026-03-27 11:08:24', '2026-03-27 11:08:52');
INSERT INTO `sys_user` VALUES (5, 'use1', '$2a$10$ZgMLOgDI.ylCUI4D4cUevuzgZBPmiMpX.5o9hUdBGMTh26u4lfRL2', 'fast', NULL, '17000134542', NULL, 0.00, 1, 2, '2026-03-28 01:47:13', '2026-03-28 01:47:37');
INSERT INTO `sys_user` VALUES (6, 'user2', '$2a$10$0pJ3CDWXWJJV./ndt5.ok.7En5O/Q8o0jcpe2ssL08zxj7zXEszuG', 'test2', NULL, '13224347384', '13224347384@test.com', 2.10, 1, 2, '2026-03-28 17:27:36', '2026-03-28 17:43:32');

SET FOREIGN_KEY_CHECKS = 1;
