﻿/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : yxddb

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-06-06 18:19:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `doctors`
-- ----------------------------
DROP TABLE IF EXISTS `doctors`;
CREATE TABLE `doctors` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sex`  int(11) NULL DEFAULT 0 ,
`phone`  int(11) NULL DEFAULT 0 ,
`hospital_no`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`createdAt`  datetime NULL DEFAULT NULL ,
`updatedAt`  datetime NULL DEFAULT NULL ,
`job`  int(11) NULL DEFAULT 0 ,
PRIMARY KEY (`id`),
FOREIGN KEY (`hospital_no`) REFERENCES `hospitals` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `hospital_no` (`hospital_no`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=3

;

-- ----------------------------
-- Table structure for `foot_records`
-- ----------------------------
DROP TABLE IF EXISTS `foot_records`;
CREATE TABLE `foot_records` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`userid`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id' ,
`recordDate`  date NULL DEFAULT NULL COMMENT '扫描时间' ,
`picture`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '扫描图片' ,
`lfootLength`  float NULL DEFAULT NULL COMMENT '足长' ,
`lfootWidth`  float NULL DEFAULT NULL COMMENT '足宽' ,
`lsize`  smallint(11) NULL DEFAULT NULL COMMENT '鞋码' ,
`ltypeWidth`  int(11) NULL DEFAULT NULL COMMENT '型宽' ,
`lfootType`  int(11) NULL DEFAULT NULL COMMENT '足型' ,
`rfootLength`  float NULL DEFAULT NULL ,
`rfootWidth`  float NULL DEFAULT NULL ,
`rsize`  smallint(6) NULL DEFAULT NULL ,
`rtypeWidth`  int(11) NULL DEFAULT NULL ,
`rfootType`  int(11) NULL DEFAULT NULL ,
`footJudgment`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '足型判断' ,
`suggestShoe`  tinyint(11) NULL DEFAULT NULL COMMENT '建议鞋型' ,
`footKnowledge`  int(11) NULL DEFAULT NULL COMMENT '足部知识' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Table structure for `footknowledge`
-- ----------------------------
DROP TABLE IF EXISTS `footknowledge`;
CREATE TABLE `footknowledge` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `hospitals`
-- ----------------------------
DROP TABLE IF EXISTS `hospitals`;
CREATE TABLE `hospitals` (
`id`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
`name`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`createdAt`  datetime NULL DEFAULT NULL ,
`updatedAt`  datetime NULL DEFAULT NULL ,
`host`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`favicon`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `link_configs`
-- ----------------------------
DROP TABLE IF EXISTS `link_configs`;
CREATE TABLE `link_configs` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`url`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址' ,
`picture`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片' ,
`linkName`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接名称' ,
`place`  int(11) NULL DEFAULT NULL COMMENT '显示位置' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`userid`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id' ,
`valid`  tinyint(4) NULL DEFAULT NULL COMMENT '是否有效' ,
`orderid`  int(11) NULL DEFAULT NULL COMMENT '订单id' ,
`contact`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人' ,
`gender`  tinyint(4) NULL DEFAULT NULL COMMENT '联系人性别' ,
`tel`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话' ,
`address`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详细地址' ,
`province`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省' ,
`city`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市' ,
`area`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区' ,
`shoeid`  int(11) NULL DEFAULT NULL COMMENT '鞋子id' ,
`shoeName`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品名' ,
`size`  int(11) NULL DEFAULT NULL COMMENT '尺码' ,
`color`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '颜色' ,
`type`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '鞋型' ,
`price`  int(11) NULL DEFAULT NULL COMMENT '价格' ,
`remark`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注' ,
`createtime`  bigint(20) NULL DEFAULT NULL COMMENT '订单时间' ,
`status`  smallint(6) NULL DEFAULT NULL COMMENT '状态' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=51

;

-- ----------------------------
-- Table structure for `products`
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`name`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称' ,
`smallPic`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小图片' ,
`swipePic`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '轮播图片' ,
`price`  float NULL DEFAULT NULL COMMENT '价格' ,
`intro`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详细介绍' ,
`size`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '尺码(37,38,39)' ,
`color`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '颜色(黑色,白色)' ,
`type`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '鞋型(正常,加宽)' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=4

;

-- ----------------------------
-- Table structure for `swipe_configs`
-- ----------------------------
DROP TABLE IF EXISTS `swipe_configs`;
CREATE TABLE `swipe_configs` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`url`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址' ,
`showTime`  int(11) NOT NULL COMMENT '显示时间' ,
`place`  tinyint(4) NULL DEFAULT NULL COMMENT '显示位置' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=4

;

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`wxid`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`userName`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名' ,
`password`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码' ,
`lastPeriod`  date NULL DEFAULT NULL COMMENT '末次月经时间' ,
`name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字' ,
`height`  int(11) NULL DEFAULT NULL COMMENT '身高(cm)' ,
`weight`  float NULL DEFAULT NULL COMMENT '孕前体重(kg)' ,
`shape`  tinyint(11) NULL DEFAULT NULL COMMENT '体型 (1:偏轻 2:正常 3:偏重 4:肥胖)' ,
`isSingle`  tinyint(1) NULL DEFAULT NULL COMMENT '是否单胎' ,
`contact`  varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人' ,
`gender`  tinyint(1) NULL DEFAULT NULL COMMENT '联系人性别' ,
`tel`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话' ,
`address`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`city`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市' ,
`headUrl`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像' ,
`card_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`subscribe_app`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`subscribe_time`  date NULL DEFAULT NULL ,
`subscribe_status`  tinyint(1) NULL DEFAULT NULL ,
`file_amount`  tinyint(3) NULL DEFAULT NULL ,
`nation`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`date_birth`  date NULL DEFAULT NULL ,
`district`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`province`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`county`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`blood`  varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_email`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_date`  date NULL DEFAULT NULL ,
`doctor_id`  int(11) NULL DEFAULT '-1' COMMENT '所屬醫生' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=25

;

-- ----------------------------
-- Table structure for `weight_records`
-- ----------------------------
DROP TABLE IF EXISTS `weight_records`;
CREATE TABLE `weight_records` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`userid`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id' ,
`week`  int(11) NULL DEFAULT NULL COMMENT '怀孕周数' ,
`recordDate`  date NULL DEFAULT NULL COMMENT '测重时间' ,
`hospital`  smallint(6) NULL DEFAULT NULL COMMENT '医院id' ,
`weight`  float NULL DEFAULT NULL COMMENT '体重(kg)' ,
`result`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评估结果' ,
`standard`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标准体重范围' ,
`tip`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '注意点' ,
`diet`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日常饮食贴士' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `as` (`userid`, `recordDate`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=93

;

-- ----------------------------
-- Table structure for `weightadvice_configs`
-- ----------------------------
DROP TABLE IF EXISTS `weightadvice_configs`;
CREATE TABLE `weightadvice_configs` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`minWeek`  int(11) NULL DEFAULT NULL COMMENT '周数范围' ,
`maxWeek`  int(11) NULL DEFAULT NULL ,
`normal`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '正常提示' ,
`skinny`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '太轻提示' ,
`fat`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '太重提示' ,
`tip_normal`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '正常时饮食小贴士' ,
`tip_skinny`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '太轻时饮食小贴士' ,
`tip_fat`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '太重时饮食小贴士' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=4

;

-- ----------------------------
-- Table structure for `weightrate_configs`
-- ----------------------------
DROP TABLE IF EXISTS `weightrate_configs`;
CREATE TABLE `weightrate_configs` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`status`  int(11) NULL DEFAULT NULL COMMENT '1:偏轻 2:正常 3:偏重 4:肥胖' ,
`rateMin`  float NULL DEFAULT NULL COMMENT '每周体重变化' ,
`rateMax`  float NULL DEFAULT NULL ,
`dRateMin`  float NULL DEFAULT NULL COMMENT '多胎的数值' ,
`dRateMax`  float NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=5

;

-- ----------------------------
-- Table structure for `yxd_basicinfos`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_basicinfos`;
CREATE TABLE `yxd_basicinfos` (
`id`  int(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '表间关联' ,
`mac_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一' ,
`user_id`  int(11) NULL DEFAULT NULL COMMENT '关联userinfo表主键ID' ,
`open_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信用户号' ,
`card_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号' ,
`scene`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码场景值' ,
`ticket`  varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码入场券' ,
`from_app`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联公众号' ,
`from_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联机器码' ,
`analyse`  tinyint(1) NULL DEFAULT 0 COMMENT '是否审核' ,
`name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称' ,
`birth`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生日' ,
`age`  tinyint(3) NULL DEFAULT NULL COMMENT '年龄' ,
`sex`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别' ,
`district`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区' ,
`province`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份' ,
`city`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市' ,
`county`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '县市区' ,
`country`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国籍' ,
`nation`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族' ,
`phone`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话' ,
`email`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱' ,
`date_server`  datetime NULL DEFAULT NULL COMMENT '上传服务器时间' ,
`date_host`  datetime NULL DEFAULT NULL COMMENT '采集本地时间' ,
`sign`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标记' ,
`crowd`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人群' ,
`period`  tinyint(2) NULL DEFAULT NULL COMMENT '孕周' ,
`date_yunfu`  date NULL DEFAULT NULL COMMENT '末次月经时间' ,
`remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`height`  float(6,2) NULL DEFAULT NULL COMMENT '身高' ,
`weight`  float(6,2) NULL DEFAULT NULL COMMENT '体重' ,
`blood`  varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '血型' ,
`hospital_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编码' ,
`hospital_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称' ,
`doctor_name`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生名称' ,
`doctor_id`  int(11) NULL DEFAULT NULL ,
`status`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '相關醫生是否已經打開過' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `mac_id` (`mac_id`) USING BTREE 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_details`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_details`;
CREATE TABLE `yxd_details` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`hospital_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`clinic_dept`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`doctor_name`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`clinic_type`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`count_num`  smallint(5) NULL DEFAULT NULL ,
`open_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`card_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`print_date`  datetime NULL DEFAULT NULL ,
`call_times`  tinyint(1) NULL DEFAULT NULL ,
`start_date`  datetime NULL DEFAULT NULL ,
`end_date`  datetime NULL DEFAULT NULL ,
`status`  varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`repeat_num`  tinyint(1) NULL DEFAULT NULL ,
`record_id`  int(10) UNSIGNED NULL DEFAULT 0 ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_machines`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_machines`;
CREATE TABLE `yxd_machines` (
`id`  smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`hospital_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '医院编号' ,
`machine_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机器编号' ,
`machine_type`  tinyint(1) NULL DEFAULT NULL COMMENT '机器类型' ,
`machine_site`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所处位置' ,
`machine_mac`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机器MAC' ,
`machine_ip`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机器IP' ,
`date`  datetime NOT NULL COMMENT '注册时间' ,
`status`  tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用,1可用,0不可用' ,
`info`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`, `hospital_no`),
UNIQUE INDEX `machine_mac` (`machine_mac`) USING BTREE 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_master`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_master`;
CREATE TABLE `yxd_master` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`hospital_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号' ,
`clinic_dept`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专科名称' ,
`doctor_name`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生名称' ,
`clinic_type`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专科号别' ,
`create_date`  datetime NULL DEFAULT NULL COMMENT '创建更新时间' ,
`status`  varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上下线状态' ,
`count_num`  smallint(5) NULL DEFAULT NULL COMMENT '排队号' ,
`print_date`  datetime NULL DEFAULT NULL COMMENT '最近打印时间' ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_parameters`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_parameters`;
CREATE TABLE `yxd_parameters` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '表间关联' ,
`mac_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一' ,
`left_length`  float(5,2) NULL DEFAULT NULL COMMENT '左脚长' ,
`left_width`  float(5,2) NULL DEFAULT NULL COMMENT '左脚宽' ,
`left_width_41full`  float(5,2) NULL DEFAULT NULL ,
`left_center_angle`  float(5,2) NULL DEFAULT NULL ,
`left_length_90`  float(5,2) NULL DEFAULT NULL ,
`left_length_825`  float(5,2) NULL DEFAULT NULL ,
`left_length_78`  float(5,2) NULL DEFAULT NULL ,
`left_length_725`  float(5,2) NULL DEFAULT NULL COMMENT '左足弓长' ,
`left_length_635`  float(5,2) NULL DEFAULT NULL ,
`left_length_68`  float(5,2) NULL DEFAULT NULL ,
`left_length_41`  float(5,2) NULL DEFAULT NULL ,
`left_length_18`  float(5,2) NULL DEFAULT NULL ,
`left_width_90`  float(5,2) NULL DEFAULT NULL ,
`left_width_78`  float(5,2) NULL DEFAULT NULL ,
`left_width_725`  float(5,2) NULL DEFAULT NULL ,
`left_width_635`  float(5,2) NULL DEFAULT NULL ,
`left_width_41`  float(5,2) NULL DEFAULT NULL ,
`left_width_18`  float(5,2) NULL DEFAULT NULL ,
`left_width_ratio`  float(5,2) NULL DEFAULT NULL ,
`left_width_68`  float(5,2) NULL DEFAULT NULL COMMENT '左脚斜宽' ,
`left_incline_angle`  float(5,2) NULL DEFAULT NULL ,
`left_size_01`  float(5,2) NULL DEFAULT NULL ,
`left_size_02`  float(5,2) NULL DEFAULT NULL ,
`left_size_03`  float(5,2) NULL DEFAULT NULL ,
`left_size_04`  float(5,2) NULL DEFAULT NULL ,
`left_size_05`  float(5,2) NULL DEFAULT NULL ,
`left_size_06`  float(5,2) NULL DEFAULT NULL ,
`left_size_07`  float(5,2) NULL DEFAULT NULL ,
`left_size_08`  float(5,2) NULL DEFAULT NULL ,
`left_size_09`  float(5,2) NULL DEFAULT NULL ,
`left_size_10`  float(5,2) NULL DEFAULT NULL ,
`left_size_11`  float(5,2) NULL DEFAULT NULL ,
`left_size_12`  float(5,2) NULL DEFAULT NULL ,
`left_size_13`  float(5,2) NULL DEFAULT NULL ,
`right_length`  float(5,2) NULL DEFAULT NULL COMMENT '右脚长' ,
`right_width`  float(5,2) NULL DEFAULT NULL COMMENT '右脚宽' ,
`right_width_41full`  float(5,2) NULL DEFAULT NULL ,
`right_center_angle`  float(5,2) NULL DEFAULT NULL ,
`right_length_90`  float(5,2) NULL DEFAULT NULL ,
`right_length_825`  float(5,2) NULL DEFAULT NULL ,
`right_length_78`  float(5,2) NULL DEFAULT NULL ,
`right_length_725`  float(5,2) NULL DEFAULT NULL COMMENT '右足弓长' ,
`right_length_635`  float(5,2) NULL DEFAULT NULL ,
`right_length_68`  float(5,2) NULL DEFAULT NULL ,
`right_length_41`  float(5,2) NULL DEFAULT NULL ,
`right_length_18`  float(5,2) NULL DEFAULT NULL ,
`right_width_90`  float(5,2) NULL DEFAULT NULL ,
`right_width_78`  float(5,2) NULL DEFAULT NULL ,
`right_width_725`  float(5,2) NULL DEFAULT NULL ,
`right_width_635`  float(5,2) NULL DEFAULT NULL ,
`right_width_41`  float(5,2) NULL DEFAULT NULL ,
`right_width_18`  float(5,2) NULL DEFAULT NULL ,
`right_width_ratio`  float(5,2) NULL DEFAULT NULL ,
`right_width_68`  float(5,2) NULL DEFAULT NULL COMMENT '右脚斜宽' ,
`right_incline_angle`  float(5,2) NULL DEFAULT NULL ,
`right_size_01`  float(5,2) NULL DEFAULT NULL ,
`right_size_02`  float(5,2) NULL DEFAULT NULL ,
`right_size_03`  float(5,2) NULL DEFAULT NULL ,
`right_size_04`  float(5,2) NULL DEFAULT NULL ,
`right_size_05`  float(5,2) NULL DEFAULT NULL ,
`right_size_06`  float(5,2) NULL DEFAULT NULL ,
`right_size_07`  float(5,2) NULL DEFAULT NULL ,
`right_size_08`  float(5,2) NULL DEFAULT NULL ,
`right_size_09`  float(5,2) NULL DEFAULT NULL ,
`right_size_10`  float(5,2) NULL DEFAULT NULL ,
`right_size_11`  float(5,2) NULL DEFAULT NULL ,
`right_size_12`  float(5,2) NULL DEFAULT NULL ,
`right_size_13`  float(5,2) NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
UNIQUE INDEX `mac_id` (`mac_id`) USING BTREE 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_patient`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_patient`;
CREATE TABLE `yxd_patient` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`hospital_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`open_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_name`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_nation`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_sex`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_born`  date NULL DEFAULT NULL ,
`pat_age`  tinyint(3) NULL DEFAULT NULL ,
`pat_identity`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`card_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_phone`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`address`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_country`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_province`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_city`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_county`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pat_blood`  varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`meet_flag`  tinyint(1) NULL DEFAULT NULL ,
`period_end_time`  date NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_pictures`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_pictures`;
CREATE TABLE `yxd_pictures` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '表间关联' ,
`mac_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一' ,
`left_dpi`  smallint(3) NULL DEFAULT NULL COMMENT '左脚型图片分辨率' ,
`right_dpi`  smallint(3) NULL DEFAULT NULL COMMENT '右脚型图片分辨率' ,
`left_url`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左脚型图片文件名' ,
`right_url`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右脚型图片文件名' ,
`left_urla`  varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左脚型图片资源路径' ,
`right_urla`  varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右脚型图片资源路径' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `mac_id` (`mac_id`) USING BTREE 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_references`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_references`;
CREATE TABLE `yxd_references` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '表间关联' ,
`mac_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一' ,
`lcircle_01_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_01_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_02_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_02_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_03_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_03_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_725_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_725_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_635_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_635_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_4101_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_4101_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_4102_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_4102_y`  float(8,4) NULL DEFAULT NULL ,
`lbreak_01_x`  float(8,4) NULL DEFAULT NULL ,
`lbreak_01_y`  float(8,4) NULL DEFAULT NULL ,
`lbreak_02_x`  float(8,4) NULL DEFAULT NULL ,
`lbreak_02_y`  float(8,4) NULL DEFAULT NULL ,
`lfoot_top`  smallint(4) NULL DEFAULT NULL ,
`lfoot_bottom`  smallint(4) NULL DEFAULT NULL ,
`lfoot_right`  smallint(4) NULL DEFAULT NULL ,
`lfoot_left`  smallint(4) NULL DEFAULT NULL ,
`lh_turn`  tinyint(1) NULL DEFAULT 0 ,
`lv_turn`  tinyint(1) NULL DEFAULT 0 ,
`lscale`  double NULL DEFAULT NULL ,
`lcircle_18_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_18_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_1801_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_1801_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_1802_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_1802_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_90_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_90_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_825_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_825_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_78_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_78_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_80_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_80_y`  float(8,4) NULL DEFAULT NULL ,
`lcircle_65_x`  float(8,4) NULL DEFAULT NULL ,
`lcircle_65_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_01_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_01_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_02_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_02_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_03_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_03_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_725_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_725_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_635_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_635_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_4101_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_4101_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_4102_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_4102_y`  float(8,4) NULL DEFAULT NULL ,
`rbreak_01_x`  float(8,4) NULL DEFAULT NULL ,
`rbreak_01_y`  float(8,4) NULL DEFAULT NULL ,
`rbreak_02_x`  float(8,4) NULL DEFAULT NULL ,
`rbreak_02_y`  float(8,4) NULL DEFAULT NULL ,
`rfoot_top`  smallint(4) NULL DEFAULT NULL ,
`rfoot_bottom`  smallint(4) NULL DEFAULT NULL ,
`rfoot_right`  smallint(4) NULL DEFAULT NULL ,
`rfoot_left`  smallint(4) NULL DEFAULT NULL ,
`rh_turn`  tinyint(1) NULL DEFAULT 0 ,
`rv_turn`  tinyint(1) NULL DEFAULT 0 ,
`rscale`  double NULL DEFAULT NULL ,
`rcircle_18_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_18_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_1801_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_1801_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_1802_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_1802_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_90_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_90_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_825_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_825_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_78_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_78_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_80_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_80_y`  float(8,4) NULL DEFAULT NULL ,
`rcircle_65_x`  float(8,4) NULL DEFAULT NULL ,
`rcircle_65_y`  float(8,4) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_suggestions`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_suggestions`;
CREATE TABLE `yxd_suggestions` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '表间关联' ,
`mac_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一' ,
`left_foot_size`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左推荐鞋码' ,
`left_foot_width`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左推荐半型宽' ,
`left_foot_width2`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左推荐整型宽' ,
`left_foot_status`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左足弓态势' ,
`left_advise`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左专家意见' ,
`right_foot_size`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右推荐鞋码' ,
`right_foot_width`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右推荐半型宽' ,
`right_foot_width2`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右推荐整型宽' ,
`right_foot_status`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右足弓态势' ,
`right_advise`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右专家意见' ,
`left_para_01`  float(5,2) NULL DEFAULT NULL ,
`left_para_02`  float(5,2) NULL DEFAULT NULL ,
`left_para_03`  float(5,2) NULL DEFAULT NULL ,
`left_para_04`  float(5,2) NULL DEFAULT NULL ,
`left_para_05`  float(5,2) NULL DEFAULT NULL ,
`left_para_06`  float(5,2) NULL DEFAULT NULL ,
`right_para_01`  float(5,2) NULL DEFAULT NULL ,
`right_para_02`  float(5,2) NULL DEFAULT NULL ,
`right_para_03`  float(5,2) NULL DEFAULT NULL ,
`right_para_04`  float(5,2) NULL DEFAULT NULL ,
`right_para_05`  float(5,2) NULL DEFAULT NULL ,
`right_para_06`  float(5,2) NULL DEFAULT NULL ,
`left_foot_type`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '左脚型' ,
`right_foot_type`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '右脚型' ,
`foot_leg`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '腿型' ,
`left_int_size`  smallint(3) NULL DEFAULT NULL ,
`left_int_width`  tinyint(3) NULL DEFAULT NULL ,
`left_int_width2`  tinyint(3) NULL DEFAULT NULL ,
`left_int_status`  tinyint(1) NULL DEFAULT NULL COMMENT '左足势编码' ,
`right_int_size`  smallint(3) NULL DEFAULT NULL ,
`right_int_width`  tinyint(3) NULL DEFAULT NULL ,
`right_int_width2`  tinyint(3) NULL DEFAULT NULL ,
`right_int_status`  tinyint(1) NULL DEFAULT NULL COMMENT '右足态编码' ,
`left_int_type`  tinyint(1) NULL DEFAULT NULL ,
`right_int_type`  tinyint(1) NULL DEFAULT NULL ,
`foot_int_leg`  tinyint(1) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `yxd_wrong_machine_logs`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_wrong_machine_logs`;
CREATE TABLE `yxd_wrong_machine_logs` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`ipaddress`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
`wrong_mac`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
`createdAt`  date NULL DEFAULT NULL ,
`updatedAt`  date NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci

;

