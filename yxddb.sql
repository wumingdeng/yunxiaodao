/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : yxddb

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-06-02 09:36:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `footknowledge`
-- ----------------------------
DROP TABLE IF EXISTS `footknowledge`;
CREATE TABLE `footknowledge` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of footknowledge
-- ----------------------------

-- ----------------------------
-- Table structure for `foot_records`
-- ----------------------------
DROP TABLE IF EXISTS `foot_records`;
CREATE TABLE `foot_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(128) DEFAULT NULL COMMENT '用户id',
  `recordDate` date DEFAULT NULL COMMENT '扫描时间',
  `picture` text COMMENT '扫描图片',
  `lfootLength` float DEFAULT NULL COMMENT '足长',
  `lfootWidth` float DEFAULT NULL COMMENT '足宽',
  `lsize` smallint(11) DEFAULT NULL COMMENT '鞋码',
  `ltypeWidth` int(11) DEFAULT NULL COMMENT '型宽',
  `lfootType` int(11) DEFAULT NULL COMMENT '足型',
  `rfootLength` float DEFAULT NULL,
  `rfootWidth` float DEFAULT NULL,
  `rsize` smallint(6) DEFAULT NULL,
  `rtypeWidth` int(11) DEFAULT NULL,
  `rfootType` int(11) DEFAULT NULL,
  `footJudgment` text COMMENT '足型判断',
  `suggestShoe` tinyint(11) DEFAULT NULL COMMENT '建议鞋型',
  `footKnowledge` int(11) DEFAULT NULL COMMENT '足部知识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of foot_records
-- ----------------------------
INSERT INTO `foot_records` VALUES ('1', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '2017-05-12', '111', '30', '10', '38', '12', '1', '30', '11', '38', '11', '1', '1', '3', '1');

-- ----------------------------
-- Table structure for `hospitals`
-- ----------------------------
DROP TABLE IF EXISTS `hospitals`;
CREATE TABLE `hospitals` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` int(11) DEFAULT NULL COMMENT '医院名称',
  `city` int(11) DEFAULT NULL COMMENT '城市',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hospitals
-- ----------------------------

-- ----------------------------
-- Table structure for `link_configs`
-- ----------------------------
DROP TABLE IF EXISTS `link_configs`;
CREATE TABLE `link_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text NOT NULL COMMENT '地址',
  `picture` text NOT NULL COMMENT '图片',
  `linkName` varchar(11) DEFAULT NULL COMMENT '链接名称',
  `place` int(11) DEFAULT NULL COMMENT '显示位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of link_configs
-- ----------------------------
INSERT INTO `link_configs` VALUES ('1', 'www.baidu.com', 'static/assets/shoe/link/ico_01.png', '百度', '0');

-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(128) DEFAULT NULL COMMENT '用户id',
  `valid` tinyint(4) DEFAULT NULL COMMENT '是否有效',
  `orderid` int(11) DEFAULT NULL COMMENT '订单id',
  `contact` varchar(11) DEFAULT NULL COMMENT '联系人',
  `gender` tinyint(4) DEFAULT NULL COMMENT '联系人性别',
  `tel` varchar(16) DEFAULT NULL COMMENT '电话',
  `address` text COMMENT '详细地址',
  `province` varchar(15) DEFAULT NULL COMMENT '省',
  `city` varchar(15) DEFAULT NULL COMMENT '市',
  `area` varchar(15) DEFAULT NULL COMMENT '区',
  `shoeid` int(11) DEFAULT NULL COMMENT '鞋子id',
  `shoeName` varchar(11) DEFAULT NULL COMMENT '品名',
  `size` int(11) DEFAULT NULL COMMENT '尺码',
  `color` varchar(11) DEFAULT NULL COMMENT '颜色',
  `type` varchar(11) DEFAULT NULL COMMENT '鞋型',
  `price` int(11) DEFAULT NULL COMMENT '价格',
  `remark` text COMMENT '备注',
  `createtime` bigint(20) DEFAULT NULL COMMENT '订单时间',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('1', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '1', '1', '老王', '1', '18954875125', '通往天堂的地铁', '福建', '厦门', '思明区', '1', '孕妇蹦极专用鞋', '40', '黑色', '加宽', '399', '不甜不要钱', '1484223595', '0');
INSERT INTO `orders` VALUES ('2', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '1', '1', '老王', '1', '18954875125', '通往天堂的地铁', '福建', '厦门', '思明区', '1', '孕妇蹦极专用鞋', '40', '黑色', '加宽', '399', '不甜不要钱', '1484223595', '1');
INSERT INTO `orders` VALUES ('3', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '1', '1', '老王', '1', '18954875125', '通往天堂的地铁', '福建', '厦门', '思明区', '1', '孕妇蹦极专用鞋', '40', '黑色', '加宽', '399', '不甜不要钱', '1484223595', '2');
INSERT INTO `orders` VALUES ('4', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '1', '1', '老王', '1', '18954875125', '通往天堂的地铁', '福建', '厦门', '思明区', '1', '孕妇蹦极专用鞋', '40', '黑色', '加宽', '399', '不甜不要钱', '1484223595', '3');
INSERT INTO `orders` VALUES ('5', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '1', '1', '老王', '1', '18954875125', '通往天堂的地铁', '福建', '厦门', '思明区', '1', '孕妇蹦极专用鞋', '40', '黑色', '加宽', '399', '不甜不要钱', '1484223595', '4');
INSERT INTO `orders` VALUES ('6', null, null, null, '长长的', '0', '18888888888', '大使馆收到风', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '白色', '加宽', '879', '百分点', '1495507759', '0');
INSERT INTO `orders` VALUES ('7', null, null, null, '26w', '0', '18888888111', 'qwe', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '38', '黑色', '加宽', '879', '部分代表', '1495510172', '0');
INSERT INTO `orders` VALUES ('8', null, null, null, '26w', '0', '18888888111', 'qwe', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '38', '黑色', '加宽', '879', '部分代表', '1495511123', '0');
INSERT INTO `orders` VALUES ('9', null, null, null, '纷纷过', '1', '18888888111', '但是饭否的', '福建', '厦门', '湖里区', '3', '孕妇保健鞋3', '40', '白色', '正常', '879', '饿过广东 v否', '1495524124', '0');
INSERT INTO `orders` VALUES ('10', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536982', '0');
INSERT INTO `orders` VALUES ('11', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536982', '0');
INSERT INTO `orders` VALUES ('12', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536982', '0');
INSERT INTO `orders` VALUES ('13', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536982', '0');
INSERT INTO `orders` VALUES ('14', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536988', '0');
INSERT INTO `orders` VALUES ('15', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536988', '0');
INSERT INTO `orders` VALUES ('16', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536988', '0');
INSERT INTO `orders` VALUES ('17', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495536988', '0');
INSERT INTO `orders` VALUES ('18', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537018', '0');
INSERT INTO `orders` VALUES ('19', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537018', '0');
INSERT INTO `orders` VALUES ('20', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537022', '0');
INSERT INTO `orders` VALUES ('21', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537022', '0');
INSERT INTO `orders` VALUES ('22', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537026', '0');
INSERT INTO `orders` VALUES ('23', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537026', '0');
INSERT INTO `orders` VALUES ('24', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537026', '0');
INSERT INTO `orders` VALUES ('25', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537030', '0');
INSERT INTO `orders` VALUES ('26', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537030', '0');
INSERT INTO `orders` VALUES ('27', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537032', '0');
INSERT INTO `orders` VALUES ('28', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537032', '0');
INSERT INTO `orders` VALUES ('29', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537033', '0');
INSERT INTO `orders` VALUES ('30', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537033', '0');
INSERT INTO `orders` VALUES ('31', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537035', '0');
INSERT INTO `orders` VALUES ('32', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537035', '0');
INSERT INTO `orders` VALUES ('33', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537035', '0');
INSERT INTO `orders` VALUES ('34', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537035', '0');
INSERT INTO `orders` VALUES ('35', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537039', '0');
INSERT INTO `orders` VALUES ('36', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537039', '0');
INSERT INTO `orders` VALUES ('37', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537039', '0');
INSERT INTO `orders` VALUES ('38', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '37', '黑色', '加宽', '879', null, '1495537039', '0');
INSERT INTO `orders` VALUES ('39', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '红色', '加宽', '879', null, '1495604528', '0');
INSERT INTO `orders` VALUES ('40', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '红色', '加宽', '879', null, '1495604554', '0');
INSERT INTO `orders` VALUES ('41', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '红色', '加宽', '879', null, '1495604565', '0');
INSERT INTO `orders` VALUES ('42', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '红色', '加宽', '879', null, '1495604603', '0');
INSERT INTO `orders` VALUES ('43', null, null, null, '13355555555', '0', '13333333333', '这么好', '福建', '厦门', '思明区', '3', '孕妇保健鞋3', '40', '红色', '正常', '879', null, '1495609849', '0');
INSERT INTO `orders` VALUES ('44', null, null, null, '13355555555', '0', '13333333333', '这么好', '福建', '厦门', '思明区', '3', '孕妇保健鞋3', '40', '红色', '正常', '879', null, '1495609849', '0');
INSERT INTO `orders` VALUES ('45', null, null, null, '杜子腾', '0', '15682536522', '突兀图腾柱', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '40', '黑色', '加宽', '879', null, '1495609983', '0');
INSERT INTO `orders` VALUES ('46', null, null, null, '你', '0', '13333333333', '吧', '福建', '厦门', '思明区', '3', '孕妇保健鞋3', '39', '灰色', '加宽', '879', 'l l l', '1495626159', '0');
INSERT INTO `orders` VALUES ('47', null, null, null, '你', '0', '13333333333', '吧', '福建', '厦门', '思明区', '3', '孕妇保健鞋3', '37', '黑色', '正常', '879', null, '1495681433', '0');
INSERT INTO `orders` VALUES ('48', null, null, null, 'dfd', '0', '18888888888', '电风扇的大股东', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '白色', '正常', '879', '但是发生大发的撒', '1495688742', '0');
INSERT INTO `orders` VALUES ('49', null, null, null, 'dfd', '0', '18888888888', '电风扇的大股东', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '白色', '正常', '879', '但是发生大发的撒', '1495688775', '0');
INSERT INTO `orders` VALUES ('50', null, null, null, 'dfd', '0', '18888888888', '电风扇的大股东', '福建', '厦门', '思明区', '1', '孕妇保健鞋', '39', '白色', '正常', '879', '但是发生大发的撒', '1495688801', '0');

-- ----------------------------
-- Table structure for `products`
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(11) NOT NULL DEFAULT '' COMMENT '名称',
  `smallPic` text NOT NULL COMMENT '小图片',
  `swipePic` text COMMENT '轮播图片',
  `price` float DEFAULT NULL COMMENT '价格',
  `intro` text COMMENT '详细介绍',
  `size` text COMMENT '尺码(37,38,39)',
  `color` text COMMENT '颜色(黑色,白色)',
  `type` text COMMENT '鞋型(正常,加宽)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES ('1', '孕妇保健鞋', 'https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png', 'static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg', '879', 'just do it', '37,38,39,40', '黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp', '正常,加宽');
INSERT INTO `products` VALUES ('2', '孕妇保健鞋2', 'https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png', 'static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg', '879', 'just do it', '37,38,39,40,41', '黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp', '正常,加宽');
INSERT INTO `products` VALUES ('3', '孕妇保健鞋3', 'https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png', 'static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg', '879', 'just do it', '37,38,39,40,41', '黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp', '正常,加宽');

-- ----------------------------
-- Table structure for `swipe_configs`
-- ----------------------------
DROP TABLE IF EXISTS `swipe_configs`;
CREATE TABLE `swipe_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text NOT NULL COMMENT '地址',
  `showTime` int(11) NOT NULL COMMENT '显示时间',
  `place` tinyint(4) DEFAULT NULL COMMENT '显示位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of swipe_configs
-- ----------------------------
INSERT INTO `swipe_configs` VALUES ('1', '/static/assets/shoe/swipe/swipe.jpg', '3', '1');
INSERT INTO `swipe_configs` VALUES ('2', '/static/assets/shoe/swipe/swipe1.jpg', '3', '2');
INSERT INTO `swipe_configs` VALUES ('3', '/static/assets/shoe/swipe/swipe2.jpg', '3', '3');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `wxid` varchar(128) DEFAULT NULL,
  `userName` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `lastPeriod` date DEFAULT NULL COMMENT '末次月经时间',
  `name` varchar(20) DEFAULT NULL COMMENT '名字',
  `height` int(11) DEFAULT NULL COMMENT '身高(cm)',
  `weight` float DEFAULT NULL COMMENT '孕前体重(kg)',
  `shape` tinyint(11) DEFAULT NULL COMMENT '体型 (1:偏轻 2:正常 3:偏重 4:肥胖)',
  `isSingle` tinyint(1) DEFAULT NULL COMMENT '是否单胎',
  `contact` varchar(11) DEFAULT NULL COMMENT '联系人',
  `gender` tinyint(1) DEFAULT NULL COMMENT '联系人性别',
  `tel` varchar(16) DEFAULT NULL COMMENT '电话',
  `address` varchar(32) DEFAULT NULL COMMENT '地址',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `headUrl` varchar(256) DEFAULT NULL COMMENT '头像',
  `card_id` varchar(20) DEFAULT NULL,
  `subscribe_app` varchar(20) DEFAULT NULL,
  `subscribe_time` date DEFAULT NULL,
  `subscribe_status` tinyint(1) DEFAULT NULL,
  `file_amount` tinyint(3) DEFAULT NULL,
  `nation` varchar(10) DEFAULT NULL,
  `date_birth` date DEFAULT NULL,
  `district` varchar(10) DEFAULT NULL,
  `province` varchar(10) DEFAULT NULL,
  `county` varchar(10) DEFAULT NULL,
  `blood` varchar(3) DEFAULT NULL,
  `user_email` varchar(36) DEFAULT NULL,
  `user_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', null, null, '2017-01-01', '陈钊文', '160', '48', '2', '0', 'dfd', '0', '18888888888', '电风扇的大股东', '厦门', 'http://wx.qlogo.cn/mmopen/ee0w56iagUJSSuicGhKBWWmBn0voibuwZ1cVwQpdriaejasF2KFC6VMuMicnoO8dXZiaQxibk1TjQBxlATv3VKKauKbh3GBqRPbicXAV/0', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `users` VALUES ('2', 'onmcQ0309iaFOBhQAqA59K_2Ybgc', null, null, null, '音源', null, null, null, null, null, '0', null, null, null, 'http://wx.qlogo.cn/mmopen/PiajxSqBRaEKkkdY1iaSdxM83CMRFict3TO5d1vOwBoRs8fvr3bGvbn6AeBKoiadUMm8ic2AlanDSZx1LSCyMgHCRzUXZEX242hcKc5eiaEaVcxBg/0', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `users` VALUES ('3', 'onmcQ07m8lSlejzIBofU85KQrXTY', null, null, '2017-02-19', '吴朝曦', '100', '100', null, '1', null, '0', null, null, null, 'http://wx.qlogo.cn/mmopen/ee0w56iagUJRn37xiaa1Rz76B5pxCbvaKWqzaA9IxXSKzCcmuDa5tkrahRicmJwknecfk0PdcyqUX6XsZTpK9XJxJSq4jw97SHz/0', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `users` VALUES ('4', '123', null, null, '2011-01-01', '吴朝曦', '120', '52', null, '1', null, '0', null, null, null, 'http://wx.qlogo.cn/mmopen/ee0w56iagUJRn37xiaa1Rz76B5pxCbvaKWqzaA9IxXSKzCcmuDa5tkrahRicmJwknecfk0PdcyqUX6XsZTpK9XJxJSq4jw97SHz/0', null, null, null, null, null, null, '1983-01-01', null, null, null, null, null, null);
INSERT INTO `users` VALUES ('5', '321', null, null, null, '吴朝曦', null, null, null, '1', null, '0', null, null, null, 'http://wx.qlogo.cn/mmopen/ee0w56iagUJRn37xiaa1Rz76B5pxCbvaKWqzaA9IxXSKzCcmuDa5tkrahRicmJwknecfk0PdcyqUX6XsZTpK9XJxJSq4jw97SHz/0', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `users` VALUES ('8', '31212', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `users` VALUES ('14', 'orzDvv0yDNiYzvwT0v7nyUB8Ek3I', null, null, '2017-02-09', '大名', '174', null, null, null, null, '0', null, '大名水水', null, null, '33392023920', null, null, null, '2', '俄羅斯', '1990-03-20', null, null, null, null, null, null);
INSERT INTO `users` VALUES ('15', 'orzDvv1HMI1MbOOz0zHy1XHxL974', null, null, '2017-04-01', '大名', '177', null, null, null, null, '0', null, '大名水水', null, null, '33392023920', null, null, null, null, '俄羅斯', '1990-03-20', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `weightadvice_configs`
-- ----------------------------
DROP TABLE IF EXISTS `weightadvice_configs`;
CREATE TABLE `weightadvice_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `minWeek` int(11) DEFAULT NULL COMMENT '周数范围',
  `maxWeek` int(11) DEFAULT NULL,
  `normal` text COMMENT '正常提示',
  `skinny` text COMMENT '太轻提示',
  `fat` text COMMENT '太重提示',
  `tip_normal` text COMMENT '正常时饮食小贴士',
  `tip_skinny` text COMMENT '太轻时饮食小贴士',
  `tip_fat` text COMMENT '太重时饮食小贴士',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weightadvice_configs
-- ----------------------------
INSERT INTO `weightadvice_configs` VALUES ('1', '1', '12', '由于胎儿生长速度较慢，母体的相关组织增长变化也不明显，所需营养较为有限。因此，不必强求补充大量的营养，但这个阶段是胎儿生长发育最重要的时期，某种营养素的缺乏或过量，会引起胎儿早期发育障碍和畸形，此时需注意营养全面，烹调时应做到食物清淡爽口，避免刺激性强的食物。如有呕吐不可禁食，吐后仍要吃一些易消化的食物。', '\"原因一：孕吐反应\r对策：为了缓解孕吐反应所带来的不适，建议孕妈咪在孕早期的饮食不要太油腻，多吃清淡的食物，像烤馍片、烤面包、苏打饼等食品有助缓解孕吐症状，所以孕吐比较厉害的孕妈咪可以买些来吃哦。饮食要营养均衡，像鸡蛋、绿叶蔬菜、豆类食品、动物肝脏、坚果、水果等食物都要各自摄取一些。有的孕妈咪孕吐实在严重就不强求说像常人那样有规律性的饮食，也不必为了宝宝而强制自己进食，都吐出来话也没什么营养可以吸收，进食的数量、种类、次数可以根据孕妈咪自身的食欲好坏来调整，少吃多餐，从整体上保证进食量。\r原因二：食欲不振\r对策：食欲不好的孕妈咪可以适当进行一些户外运动，这样可促进肠胃消化吸收，让食欲大增。然后再搭配清淡可口、富含营养、容易消化的饮食原则增加进食量。口味方面，孕妈咪也不必太忌讳哪些东西不能吃，以符合孕妈咪的饮食习惯和爱好为主，但不得接触酒类。\r原因三：胎儿的原因\r对策：想要增加体重，孕妈咪除了要保证正常的进食之外，还要记得补充动物肝脏、绿叶蔬菜等食物哦，这些食物都含有丰富的叶酸。\"', '\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"', null, null, null);
INSERT INTO `weightadvice_configs` VALUES ('2', '13', '27', '胎儿生长发育迅速，母体也发生了极大变化，要增加热量，给予足够的蛋白质，增加动物性食品、植物油、维生素及微量元素的摄入。膳食要荤素兼备、粗细搭配，同时摄取足够的粮谷类食物，每天膳食中粮谷类需有300克至450克，除大米、面粉外，还可选用B族维生素类和氨基酸丰富的杂粮，如小米、玉米、麦片等；每日肉、蛋、禽、鱼类动物性食物或豆类及其制品需有200克、动物内脏(肝)50克(每周一至两次)，蔬菜500克，水果200克、植物油30克至40克。孕中期每餐摄取量可因孕妇食欲增加而有所增加，但随着妊娠的进展，子宫不断增大，胃部会受到挤压，使得孕妇餐后出现饱胀感。为此，可增加每日的餐次，但每次的食量要适度，切忌盲目过量进食或大吃甜食，避免孕妇因肥胖或血糖过高导致妊娠期糖尿病的发生。', '\"一、孕吐反应\r措施：这时的饮食宜清淡、不要太油腻，烤面包片、烤馍片、苏打饼干等可以缓解孕吐症状，但同时也要平衡饮食，补充一定的叶酸，如动物的肝脏、鸡蛋、豆类、绿叶蔬菜、水果以及坚果。孕妇，孕吐反应比较严重的饮食不必像常人那样强调规律性，更不可强制进食，进食的餐次、数量、种类及时间应根据孕妇的食欲和反应轻重进行调整，采取少食多餐的办法，保证进食量。\r二、食欲不振\r措施：合理调配饮食，孕妇的饮食应以富含营养、清淡可口、容易消化为原则。在口味方面，不必太忌讳，可以尽可能照顾孕妇的饮食习惯和爱好。酒类应绝对禁止。\"', '\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"', null, null, null);
INSERT INTO `weightadvice_configs` VALUES ('3', '28', '40', '\"1. 多吃鲫鱼、鲤鱼、萝卜和冬瓜等食物，有助于缓解水肿症状。\r2. 多吃含有丰富胶原蛋白的食品，如猪蹄等，有助于增加皮肤的弹性。\r3. 多吃鸡肉、鱼肉等易于消化吸收且含丰富蛋白质的食物。\r4. 吃一些动物的内脏，如心、肝、肾等，可满足多种无机盐和维生素的需要。\r5. 经常吃一些富含碘的食物，如海带、鱿鱼、紫菜等海洋植物。\r6. 多选用芹菜和莴苣等含有丰富的维生素和矿物质的食物。\r7. 多吃些花生、芝麻、豌豆、菠菜等含各种维生素和不饱和脂肪酸的食物，以避免胎儿发育异常和肌肉萎缩。\r不要过多摄入碳水化合物（主食）和饱和脂肪（例如奶油、油炸食品），避免胎儿过大。可以多吃一些优质蛋白，比如鱼、虾类的食物，另外要吃新鲜的蔬菜和水果，补充各种维生素和微量元素。\"', '可以多吃些含有糖分的食物，如甘蔗汁、果汁等。不过，当发现宝宝体重不足时，最好先检查是不是胎 盘或其他功能有问题，如果是因为疾病因素造成胎儿体重不 足，只要控制病情，宝宝的体重自然就会跟上。其次饮食要均衡，宝宝体重才会标准，可着重增加蛋 白质、氣基酸的摄取，至于油脂类并不需要额外摄取，因为 日常饮食中的油脂已经很多了。', '\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"', null, null, null);

-- ----------------------------
-- Table structure for `weightrate_configs`
-- ----------------------------
DROP TABLE IF EXISTS `weightrate_configs`;
CREATE TABLE `weightrate_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `status` int(11) DEFAULT NULL COMMENT '1:偏轻 2:正常 3:偏重 4:肥胖',
  `rateMin` float DEFAULT NULL COMMENT '每周体重变化',
  `rateMax` float DEFAULT NULL,
  `dRateMin` float DEFAULT NULL COMMENT '多胎的数值',
  `dRateMax` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weightrate_configs
-- ----------------------------
INSERT INTO `weightrate_configs` VALUES ('1', '1', '0.44', '0.58', '0.66', '0.87');
INSERT INTO `weightrate_configs` VALUES ('2', '2', '0.35', '0.5', '0.53', '0.75');
INSERT INTO `weightrate_configs` VALUES ('3', '3', '0.23', '0.33', '0.46', '0.66');
INSERT INTO `weightrate_configs` VALUES ('4', '4', '0.17', '0.27', '0.34', '0.54');

-- ----------------------------
-- Table structure for `weight_records`
-- ----------------------------
DROP TABLE IF EXISTS `weight_records`;
CREATE TABLE `weight_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(128) DEFAULT NULL COMMENT '用户id',
  `week` int(11) DEFAULT NULL COMMENT '怀孕周数',
  `recordDate` date DEFAULT NULL COMMENT '测重时间',
  `hospital` smallint(6) DEFAULT NULL COMMENT '医院id',
  `weight` float DEFAULT NULL COMMENT '体重(kg)',
  `result` varchar(6) DEFAULT NULL COMMENT '评估结果',
  `standard` varchar(32) DEFAULT NULL COMMENT '标准体重范围',
  `tip` text COMMENT '注意点',
  `diet` text COMMENT '日常饮食贴士',
  PRIMARY KEY (`id`),
  UNIQUE KEY `as` (`userid`,`recordDate`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weight_records
-- ----------------------------
INSERT INTO `weight_records` VALUES ('78', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '21', '2017-05-23', null, '55', '正常', '52.77kg-56.75kg', '胎儿生长发育迅速，母体也发生了极大变化，要增加热量，给予足够的蛋白质，增加动物性食品、植物油、维生素及微量元素的摄入。膳食要荤素兼备、粗细搭配，同时摄取足够的粮谷类食物，每天膳食中粮谷类需有300克至450克，除大米、面粉外，还可选用B族维生素类和氨基酸丰富的杂粮，如小米、玉米、麦片等；每日肉、蛋、禽、鱼类动物性食物或豆类及其制品需有200克、动物内脏(肝)50克(每周一至两次)，蔬菜500克，水果200克、植物油30克至40克。孕中期每餐摄取量可因孕妇食欲增加而有所增加，但随着妊娠的进展，子宫不断增大，胃部会受到挤压，使得孕妇餐后出现饱胀感。为此，可增加每日的餐次，但每次的食量要适度，切忌盲目过量进食或大吃甜食，避免孕妇因肥胖或血糖过高导致妊娠期糖尿病的发生。', null);
INSERT INTO `weight_records` VALUES ('92', 'onmcQ08YlxYrpJYJgc7lJVIXAkt4', '21', '2017-05-24', null, '65', '偏重', '52.77kg-56.75kg', '\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"', null);

-- ----------------------------
-- Table structure for `yxd_basicinfos`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_basicinfos`;
CREATE TABLE `yxd_basicinfos` (
  `id` int(16) unsigned NOT NULL AUTO_INCREMENT COMMENT '表间关联',
  `mac_id` varchar(40) DEFAULT NULL COMMENT '唯一',
  `user_id` int(11) DEFAULT NULL COMMENT '关联userinfo表主键ID',
  `open_id` varchar(40) DEFAULT NULL COMMENT '微信用户号',
  `card_id` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `scene` varchar(10) DEFAULT NULL COMMENT '二维码场景值',
  `ticket` varchar(120) DEFAULT NULL COMMENT '二维码入场券',
  `from_app` varchar(20) DEFAULT NULL COMMENT '关联公众号',
  `from_id` varchar(20) DEFAULT NULL COMMENT '关联机器码',
  `analyse` tinyint(1) DEFAULT '0' COMMENT '是否审核',
  `name` varchar(10) DEFAULT NULL COMMENT '名称',
  `birth` varchar(10) DEFAULT NULL COMMENT '生日',
  `age` tinyint(3) DEFAULT NULL COMMENT '年龄',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `district` varchar(10) DEFAULT NULL COMMENT '地区',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `county` varchar(10) DEFAULT NULL COMMENT '县市区',
  `country` varchar(10) DEFAULT NULL COMMENT '国籍',
  `nation` varchar(10) DEFAULT NULL COMMENT '民族',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `email` varchar(30) DEFAULT NULL COMMENT '邮箱',
  `date_server` datetime DEFAULT NULL COMMENT '上传服务器时间',
  `date_host` datetime DEFAULT NULL COMMENT '采集本地时间',
  `sign` varchar(10) DEFAULT NULL COMMENT '标记',
  `crowd` varchar(10) DEFAULT NULL COMMENT '人群',
  `period` tinyint(2) DEFAULT NULL COMMENT '孕周',
  `date_yunfu` date DEFAULT NULL COMMENT '末次月经时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `height` float(6,2) DEFAULT NULL COMMENT '身高',
  `weight` float(6,2) DEFAULT NULL COMMENT '体重',
  `blood` varchar(3) DEFAULT NULL COMMENT '血型',
  `hospital_no` varchar(20) DEFAULT NULL COMMENT '医院编码',
  `hospital_name` varchar(20) DEFAULT NULL COMMENT '医院名称',
  `clinic_dept` varchar(20) DEFAULT NULL COMMENT '专科名称',
  `doctor_name` varchar(10) DEFAULT NULL COMMENT '医生名称',
  `clinic_type` varchar(10) DEFAULT NULL COMMENT '专科号别',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac_id` (`mac_id`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_basicinfos
-- ----------------------------
INSERT INTO `yxd_basicinfos` VALUES ('44', '0A002700001720170601153508', '14', 'orzDvv0yDNiYzvwT0v7nyUB8Ek3I', '33392023920', '33602855', 'gQEo8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyX0NpUTlyTE1iTF8xMExsTk5wMTIAAgSvwy9ZAwSAUQEA', 'yxd', '0A0027000017', '0', '大名', '1990-3-20', '27', '女', 'null', 'null', 'null', 'null', 'null', '俄羅斯', null, null, '2017-06-01 16:01:18', '2017-06-01 15:35:35', null, null, '13', '2017-03-01', null, '175.00', '70.50', null, 'J180', '解放军第180医院', 'null', 'null', 'null');
INSERT INTO `yxd_basicinfos` VALUES ('45', '0A002700001720170601173753', '14', 'orzDvv0yDNiYzvwT0v7nyUB8Ek3I', '33392023920', '536221881', 'gQH97zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAySF9kdjhtTE1iTF8xM1FOTk5wMXIAAgR04C9ZAwSAUQEA', 'yxd', '0A0027000017', '0', '大名', '1990-3-20', '27', '女', 'null', 'null', 'null', 'null', 'null', '俄羅斯', null, null, '2017-06-01 17:38:22', '2017-06-01 17:38:12', null, null, '16', '2017-02-09', null, '174.00', '70.50', null, 'J180', '解放军第180医院', 'null', 'null', 'null');

-- ----------------------------
-- Table structure for `yxd_clinic`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_clinic`;
CREATE TABLE `yxd_clinic` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hospital_no` varchar(20) NOT NULL COMMENT '医院编号',
  `clinic_code` varchar(10) DEFAULT NULL COMMENT '专科代码',
  `clinic_dept` varchar(20) NOT NULL COMMENT '专科名称',
  `pid` smallint(5) NOT NULL DEFAULT '0' COMMENT '上级专科ID',
  `flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为挂号专科',
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_clinic
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_details`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_details`;
CREATE TABLE `yxd_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hospital_no` varchar(20) DEFAULT NULL,
  `clinic_dept` varchar(20) DEFAULT NULL,
  `doctor_name` varchar(10) DEFAULT NULL,
  `clinic_type` varchar(10) DEFAULT NULL,
  `count_num` smallint(5) DEFAULT NULL,
  `open_id` varchar(40) DEFAULT NULL,
  `card_id` varchar(20) DEFAULT NULL,
  `pat_name` varchar(20) DEFAULT NULL,
  `print_date` datetime DEFAULT NULL,
  `call_times` tinyint(1) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `repeat_num` tinyint(1) DEFAULT NULL,
  `record_id` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_details
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_display`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_display`;
CREATE TABLE `yxd_display` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hospital_no` varchar(20) DEFAULT NULL COMMENT '医院编号',
  `clinic_dept` varchar(20) DEFAULT NULL COMMENT '专科名称',
  `doctor_name` varchar(10) DEFAULT NULL COMMENT '医生名称',
  `clinic_type` varchar(10) DEFAULT NULL COMMENT '专科号别',
  `current_num` smallint(5) DEFAULT NULL COMMENT '当前叫号',
  `repeat_num` smallint(5) DEFAULT NULL COMMENT '复诊号',
  `display_date` datetime DEFAULT NULL COMMENT '显示日期',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_display
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_doctor`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_doctor`;
CREATE TABLE `yxd_doctor` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hospital_no` varchar(20) NOT NULL COMMENT '医院编号',
  `doctor_name` varchar(10) NOT NULL COMMENT '医生姓名',
  `clinic_dept` varchar(20) NOT NULL COMMENT '专科名称',
  `clinic_type` varchar(10) NOT NULL COMMENT '专科号别',
  `password` varchar(20) DEFAULT NULL COMMENT '可用作登录密码',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `date` datetime NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_doctor
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_hospital`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_hospital`;
CREATE TABLE `yxd_hospital` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hospital_no` varchar(20) NOT NULL COMMENT '医院编号',
  `worktime` varchar(5) NOT NULL DEFAULT '08:00' COMMENT '医院上班时间',
  `hospital_name` varchar(20) NOT NULL COMMENT '医院名称',
  `hospital_alias` varchar(20) DEFAULT NULL COMMENT '医院别名',
  `hospital_phone` varchar(20) DEFAULT NULL COMMENT '医院电话',
  `hosptial_web` varchar(100) DEFAULT NULL COMMENT '医院官网',
  `hospital_addr` varchar(100) DEFAULT NULL COMMENT '医院地址',
  `db_method` varchar(10) DEFAULT NULL COMMENT '数据库接入方式',
  `db_type` varchar(10) DEFAULT NULL COMMENT '数据库类型',
  `db_ip` varchar(15) DEFAULT NULL COMMENT '数据库IP地址',
  `db_port` varchar(4) DEFAULT NULL COMMENT '数据库端口',
  `db_name` varchar(20) DEFAULT NULL COMMENT '数据库名称',
  `db_user` varchar(10) DEFAULT NULL COMMENT '数据库用户名',
  `db_psw` varchar(20) DEFAULT NULL COMMENT '数据库密码',
  `wechat_url` varchar(150) NOT NULL COMMENT '微信Token接口',
  `login_url` varchar(150) DEFAULT NULL COMMENT '网页登录接口',
  `verify_url` varchar(150) NOT NULL COMMENT '扫描验证接口',
  `userinfo_url` varchar(150) NOT NULL COMMENT '用户信息接口',
  `clinic_url` varchar(150) NOT NULL COMMENT '挂号服务接口',
  `upload_url` varchar(150) NOT NULL COMMENT '图片上传接口',
  `data_url` varchar(150) NOT NULL COMMENT '数据上传接口',
  `space_day` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '脚型采集最小间隔天数',
  `app_name` varchar(20) DEFAULT NULL COMMENT '关联公众号',
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hospital_no` (`hospital_no`),
  UNIQUE KEY `hospital_name` (`hospital_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_hospital
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_login`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_login`;
CREATE TABLE `yxd_login` (
  `mac_id` varchar(40) NOT NULL,
  `open_id` varchar(20) DEFAULT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`mac_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_login
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_machine`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_machine`;
CREATE TABLE `yxd_machine` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hospital_no` varchar(20) NOT NULL COMMENT '医院编号',
  `machine_no` varchar(20) NOT NULL COMMENT '机器编号',
  `machine_type` tinyint(1) DEFAULT NULL COMMENT '机器类型',
  `machine_site` varchar(20) DEFAULT NULL COMMENT '所处位置',
  `machine_mac` varchar(20) DEFAULT NULL COMMENT '机器MAC',
  `machine_ip` varchar(20) DEFAULT NULL COMMENT '机器IP',
  `date` datetime NOT NULL COMMENT '注册时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `info` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `machine_mac` (`machine_mac`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_machine
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_master`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_master`;
CREATE TABLE `yxd_master` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hospital_no` varchar(20) DEFAULT NULL COMMENT '医院编号',
  `clinic_dept` varchar(20) DEFAULT NULL COMMENT '专科名称',
  `doctor_name` varchar(10) DEFAULT NULL COMMENT '医生名称',
  `clinic_type` varchar(10) DEFAULT NULL COMMENT '专科号别',
  `create_date` datetime DEFAULT NULL COMMENT '创建更新时间',
  `status` varchar(1) DEFAULT NULL COMMENT '上下线状态',
  `count_num` smallint(5) DEFAULT NULL COMMENT '排队号',
  `print_date` datetime DEFAULT NULL COMMENT '最近打印时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_master
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_parameters`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_parameters`;
CREATE TABLE `yxd_parameters` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表间关联',
  `mac_id` varchar(40) DEFAULT NULL COMMENT '唯一',
  `left_length` float(5,2) DEFAULT NULL COMMENT '左脚长',
  `left_width` float(5,2) DEFAULT NULL COMMENT '左脚宽',
  `left_width_41full` float(5,2) DEFAULT NULL,
  `left_center_angle` float(5,2) DEFAULT NULL,
  `left_length_90` float(5,2) DEFAULT NULL,
  `left_length_825` float(5,2) DEFAULT NULL,
  `left_length_78` float(5,2) DEFAULT NULL,
  `left_length_725` float(5,2) DEFAULT NULL COMMENT '左足弓长',
  `left_length_635` float(5,2) DEFAULT NULL,
  `left_length_68` float(5,2) DEFAULT NULL,
  `left_length_41` float(5,2) DEFAULT NULL,
  `left_length_18` float(5,2) DEFAULT NULL,
  `left_width_90` float(5,2) DEFAULT NULL,
  `left_width_78` float(5,2) DEFAULT NULL,
  `left_width_725` float(5,2) DEFAULT NULL,
  `left_width_635` float(5,2) DEFAULT NULL,
  `left_width_41` float(5,2) DEFAULT NULL,
  `left_width_18` float(5,2) DEFAULT NULL,
  `left_width_ratio` float(5,2) DEFAULT NULL,
  `left_width_68` float(5,2) DEFAULT NULL COMMENT '左脚斜宽',
  `left_incline_angle` float(5,2) DEFAULT NULL,
  `left_size_01` float(5,2) DEFAULT NULL,
  `left_size_02` float(5,2) DEFAULT NULL,
  `left_size_03` float(5,2) DEFAULT NULL,
  `left_size_04` float(5,2) DEFAULT NULL,
  `left_size_05` float(5,2) DEFAULT NULL,
  `left_size_06` float(5,2) DEFAULT NULL,
  `left_size_07` float(5,2) DEFAULT NULL,
  `left_size_08` float(5,2) DEFAULT NULL,
  `left_size_09` float(5,2) DEFAULT NULL,
  `left_size_10` float(5,2) DEFAULT NULL,
  `left_size_11` float(5,2) DEFAULT NULL,
  `left_size_12` float(5,2) DEFAULT NULL,
  `left_size_13` float(5,2) DEFAULT NULL,
  `right_length` float(5,2) DEFAULT NULL COMMENT '右脚长',
  `right_width` float(5,2) DEFAULT NULL COMMENT '右脚宽',
  `right_width_41full` float(5,2) DEFAULT NULL,
  `right_center_angle` float(5,2) DEFAULT NULL,
  `right_length_90` float(5,2) DEFAULT NULL,
  `right_length_825` float(5,2) DEFAULT NULL,
  `right_length_78` float(5,2) DEFAULT NULL,
  `right_length_725` float(5,2) DEFAULT NULL COMMENT '右足弓长',
  `right_length_635` float(5,2) DEFAULT NULL,
  `right_length_68` float(5,2) DEFAULT NULL,
  `right_length_41` float(5,2) DEFAULT NULL,
  `right_length_18` float(5,2) DEFAULT NULL,
  `right_width_90` float(5,2) DEFAULT NULL,
  `right_width_78` float(5,2) DEFAULT NULL,
  `right_width_725` float(5,2) DEFAULT NULL,
  `right_width_635` float(5,2) DEFAULT NULL,
  `right_width_41` float(5,2) DEFAULT NULL,
  `right_width_18` float(5,2) DEFAULT NULL,
  `right_width_ratio` float(5,2) DEFAULT NULL,
  `right_width_68` float(5,2) DEFAULT NULL COMMENT '右脚斜宽',
  `right_incline_angle` float(5,2) DEFAULT NULL,
  `right_size_01` float(5,2) DEFAULT NULL,
  `right_size_02` float(5,2) DEFAULT NULL,
  `right_size_03` float(5,2) DEFAULT NULL,
  `right_size_04` float(5,2) DEFAULT NULL,
  `right_size_05` float(5,2) DEFAULT NULL,
  `right_size_06` float(5,2) DEFAULT NULL,
  `right_size_07` float(5,2) DEFAULT NULL,
  `right_size_08` float(5,2) DEFAULT NULL,
  `right_size_09` float(5,2) DEFAULT NULL,
  `right_size_10` float(5,2) DEFAULT NULL,
  `right_size_11` float(5,2) DEFAULT NULL,
  `right_size_12` float(5,2) DEFAULT NULL,
  `right_size_13` float(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac_id` (`mac_id`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_parameters
-- ----------------------------
INSERT INTO `yxd_parameters` VALUES ('42', '0A002700001720170601155458', '231.15', '83.72', '73.05', '6.01', '208.03', '190.70', '180.30', '167.58', '146.78', '158.68', '94.77', '41.61', '36.65', '46.90', '35.84', '47.88', '36.90', '60.15', '1.39', '86.27', '4.92', null, null, null, null, null, null, null, null, null, null, null, null, null, '227.66', '85.12', '75.15', '5.00', '204.90', '187.82', '177.58', '165.05', '144.56', '155.24', '93.34', '40.98', '42.99', '43.12', '40.77', '44.35', '37.76', '61.58', '1.38', '87.55', '3.70', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `yxd_parameters` VALUES ('45', '0A002700001720170601173753', '231.15', '83.72', '73.05', '6.76', '208.03', '190.70', '180.30', '167.58', '146.78', '158.68', '94.77', '41.61', '36.65', '46.90', '35.84', '47.88', '36.90', '60.26', '1.39', '86.27', '4.92', null, null, null, null, null, null, null, null, null, null, null, null, null, '227.66', '85.12', '75.15', '5.45', '204.90', '187.82', '177.58', '165.05', '144.56', '155.24', '93.34', '40.98', '42.99', '43.12', '40.77', '44.35', '37.76', '62.02', '1.37', '87.55', '3.70', null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `yxd_patient`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_patient`;
CREATE TABLE `yxd_patient` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `hospital_no` varchar(20) DEFAULT NULL,
  `open_id` varchar(40) DEFAULT NULL,
  `pat_name` varchar(10) DEFAULT NULL,
  `pat_nation` varchar(10) DEFAULT NULL,
  `pat_sex` varchar(2) DEFAULT NULL,
  `pat_born` date DEFAULT NULL,
  `pat_age` tinyint(3) DEFAULT NULL,
  `pat_identity` varchar(8) DEFAULT NULL,
  `card_id` varchar(20) DEFAULT NULL,
  `pat_phone` varchar(20) DEFAULT NULL,
  `address` varchar(40) DEFAULT NULL,
  `pat_country` varchar(10) DEFAULT NULL,
  `pat_province` varchar(10) DEFAULT NULL,
  `pat_city` varchar(10) DEFAULT NULL,
  `pat_county` varchar(10) DEFAULT NULL,
  `pat_blood` varchar(3) DEFAULT NULL,
  `meet_flag` tinyint(1) DEFAULT NULL,
  `period_end_time` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_patient
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_pictures`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_pictures`;
CREATE TABLE `yxd_pictures` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表间关联',
  `mac_id` varchar(40) DEFAULT NULL COMMENT '唯一',
  `left_dpi` smallint(3) DEFAULT NULL COMMENT '左脚型图片分辨率',
  `right_dpi` smallint(3) DEFAULT NULL COMMENT '右脚型图片分辨率',
  `left_url` varchar(50) DEFAULT NULL COMMENT '左脚型图片文件名',
  `right_url` varchar(50) DEFAULT NULL COMMENT '右脚型图片文件名',
  `left_urla` varchar(250) DEFAULT NULL COMMENT '左脚型图片资源路径',
  `right_urla` varchar(250) DEFAULT NULL COMMENT '右脚型图片资源路径',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac_id` (`mac_id`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_pictures
-- ----------------------------
INSERT INTO `yxd_pictures` VALUES ('42', '0A002700001720170601155458', '100', '100', '0A002700001720170601155458L.jpg', '0A002700001720170601155458R.jpg', 'http://jessielaura.asia:80/uploads/201706/0A002700001720170601155458L.jpg', 'http://jessielaura.asia:80/uploads/201706/0A002700001720170601155458R.jpg');
INSERT INTO `yxd_pictures` VALUES ('45', '0A002700001720170601173753', '100', '100', '0A002700001720170601173753L.jpg', '0A002700001720170601173753R.jpg', 'http://127.0.0.1:8097/upload/20170504/0A002700001720170601173753L.jpg', 'http://127.0.0.1:8097/upload/20170504/0A002700001720170601173753R.jpg');

-- ----------------------------
-- Table structure for `yxd_references`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_references`;
CREATE TABLE `yxd_references` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表间关联',
  `mac_id` varchar(40) DEFAULT NULL COMMENT '唯一',
  `lcircle_01_x` float(8,4) DEFAULT NULL,
  `lcircle_01_y` float(8,4) DEFAULT NULL,
  `lcircle_02_x` float(8,4) DEFAULT NULL,
  `lcircle_02_y` float(8,4) DEFAULT NULL,
  `lcircle_03_x` float(8,4) DEFAULT NULL,
  `lcircle_03_y` float(8,4) DEFAULT NULL,
  `lcircle_725_x` float(8,4) DEFAULT NULL,
  `lcircle_725_y` float(8,4) DEFAULT NULL,
  `lcircle_635_x` float(8,4) DEFAULT NULL,
  `lcircle_635_y` float(8,4) DEFAULT NULL,
  `lcircle_4101_x` float(8,4) DEFAULT NULL,
  `lcircle_4101_y` float(8,4) DEFAULT NULL,
  `lcircle_4102_x` float(8,4) DEFAULT NULL,
  `lcircle_4102_y` float(8,4) DEFAULT NULL,
  `lbreak_01_x` float(8,4) DEFAULT NULL,
  `lbreak_01_y` float(8,4) DEFAULT NULL,
  `lbreak_02_x` float(8,4) DEFAULT NULL,
  `lbreak_02_y` float(8,4) DEFAULT NULL,
  `lfoot_top` smallint(4) DEFAULT NULL,
  `lfoot_bottom` smallint(4) DEFAULT NULL,
  `lfoot_right` smallint(4) DEFAULT NULL,
  `lfoot_left` smallint(4) DEFAULT NULL,
  `lh_turn` tinyint(1) DEFAULT '0',
  `lv_turn` tinyint(1) DEFAULT '0',
  `lscale` double DEFAULT NULL,
  `lcircle_18_x` float(8,4) DEFAULT NULL,
  `lcircle_18_y` float(8,4) DEFAULT NULL,
  `lcircle_1801_x` float(8,4) DEFAULT NULL,
  `lcircle_1801_y` float(8,4) DEFAULT NULL,
  `lcircle_1802_x` float(8,4) DEFAULT NULL,
  `lcircle_1802_y` float(8,4) DEFAULT NULL,
  `lcircle_90_x` float(8,4) DEFAULT NULL,
  `lcircle_90_y` float(8,4) DEFAULT NULL,
  `lcircle_825_x` float(8,4) DEFAULT NULL,
  `lcircle_825_y` float(8,4) DEFAULT NULL,
  `lcircle_78_x` float(8,4) DEFAULT NULL,
  `lcircle_78_y` float(8,4) DEFAULT NULL,
  `lcircle_80_x` float(8,4) DEFAULT NULL,
  `lcircle_80_y` float(8,4) DEFAULT NULL,
  `lcircle_65_x` float(8,4) DEFAULT NULL,
  `lcircle_65_y` float(8,4) DEFAULT NULL,
  `rcircle_01_x` float(8,4) DEFAULT NULL,
  `rcircle_01_y` float(8,4) DEFAULT NULL,
  `rcircle_02_x` float(8,4) DEFAULT NULL,
  `rcircle_02_y` float(8,4) DEFAULT NULL,
  `rcircle_03_x` float(8,4) DEFAULT NULL,
  `rcircle_03_y` float(8,4) DEFAULT NULL,
  `rcircle_725_x` float(8,4) DEFAULT NULL,
  `rcircle_725_y` float(8,4) DEFAULT NULL,
  `rcircle_635_x` float(8,4) DEFAULT NULL,
  `rcircle_635_y` float(8,4) DEFAULT NULL,
  `rcircle_4101_x` float(8,4) DEFAULT NULL,
  `rcircle_4101_y` float(8,4) DEFAULT NULL,
  `rcircle_4102_x` float(8,4) DEFAULT NULL,
  `rcircle_4102_y` float(8,4) DEFAULT NULL,
  `rbreak_01_x` float(8,4) DEFAULT NULL,
  `rbreak_01_y` float(8,4) DEFAULT NULL,
  `rbreak_02_x` float(8,4) DEFAULT NULL,
  `rbreak_02_y` float(8,4) DEFAULT NULL,
  `rfoot_top` smallint(4) DEFAULT NULL,
  `rfoot_bottom` smallint(4) DEFAULT NULL,
  `rfoot_right` smallint(4) DEFAULT NULL,
  `rfoot_left` smallint(4) DEFAULT NULL,
  `rh_turn` tinyint(1) DEFAULT '0',
  `rv_turn` tinyint(1) DEFAULT '0',
  `rscale` double DEFAULT NULL,
  `rcircle_18_x` float(8,4) DEFAULT NULL,
  `rcircle_18_y` float(8,4) DEFAULT NULL,
  `rcircle_1801_x` float(8,4) DEFAULT NULL,
  `rcircle_1801_y` float(8,4) DEFAULT NULL,
  `rcircle_1802_x` float(8,4) DEFAULT NULL,
  `rcircle_1802_y` float(8,4) DEFAULT NULL,
  `rcircle_90_x` float(8,4) DEFAULT NULL,
  `rcircle_90_y` float(8,4) DEFAULT NULL,
  `rcircle_825_x` float(8,4) DEFAULT NULL,
  `rcircle_825_y` float(8,4) DEFAULT NULL,
  `rcircle_78_x` float(8,4) DEFAULT NULL,
  `rcircle_78_y` float(8,4) DEFAULT NULL,
  `rcircle_80_x` float(8,4) DEFAULT NULL,
  `rcircle_80_y` float(8,4) DEFAULT NULL,
  `rcircle_65_x` float(8,4) DEFAULT NULL,
  `rcircle_65_y` float(8,4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_references
-- ----------------------------
INSERT INTO `yxd_references` VALUES ('42', '0A002700001720170601155458', '158.0000', '41.0000', '176.0000', '656.0000', '219.0000', '34.0000', '259.0000', '203.5333', '36.0000', '265.9265', '264.0000', '398.9205', '68.0000', '404.6570', '77.0000', '404.3936', '176.0000', '401.4961', '74', '1086', '567', '125', '0', '0', '0.6818181818181818', '147.5653', '445.3177', '250.0000', '533.3142', '90.0000', '554.9086', '258.0000', '94.9325', '54.0000', '147.4587', '36.0000', '175.9190', '257.0000', '157.0358', '257.0000', '250.1474', '155.0000', '37.0000', '132.0000', '655.0000', '75.0000', '34.0000', '38.0000', '202.8145', '267.0000', '267.0365', '37.0000', '397.7242', '241.0000', '405.3164', '119.0000', '400.7760', '221.5000', '404.5907', '62', '1062', '528', '90', '0', '0', '0.69', '158.3312', '444.5403', '51.0000', '532.6771', '217.0000', '553.4458', '36.0000', '94.4364', '262.0000', '149.2634', '267.0000', '177.2989', '39.0000', '156.4361', '40.0000', '249.3050');
INSERT INTO `yxd_references` VALUES ('45', '0A002700001720170601173753', '158.0000', '41.0000', '176.0000', '656.0000', '219.0000', '34.0000', '259.0000', '203.5333', '36.0000', '265.9265', '264.0000', '398.9205', '68.0000', '404.6570', '77.0000', '404.3936', '176.0000', '401.4961', '74', '1086', '567', '125', '0', '0', '0.6818181818181818', '144.7825', '445.5226', '250.0000', '532.1168', '90.0000', '555.8477', '258.0000', '94.9325', '54.0000', '147.4587', '36.0000', '175.9190', '257.0000', '157.0358', '257.0000', '250.1474', '155.0000', '37.0000', '132.0000', '655.0000', '75.0000', '34.0000', '38.0000', '202.8145', '267.0000', '267.0365', '37.0000', '397.7242', '241.0000', '405.3164', '119.0000', '400.7760', '221.5000', '404.5907', '62', '1062', '528', '90', '0', '0', '0.69', '159.9969', '444.6520', '50.0000', '531.7832', '217.0000', '554.0106', '36.0000', '94.4364', '262.0000', '149.2634', '267.0000', '177.2989', '39.0000', '156.4361', '40.0000', '249.3050');

-- ----------------------------
-- Table structure for `yxd_suggestions`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_suggestions`;
CREATE TABLE `yxd_suggestions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表间关联',
  `mac_id` varchar(40) DEFAULT NULL COMMENT '唯一',
  `left_foot_size` varchar(10) DEFAULT NULL COMMENT '左推荐鞋码',
  `left_foot_width` varchar(10) DEFAULT NULL COMMENT '左推荐半型宽',
  `left_foot_width2` varchar(10) DEFAULT NULL COMMENT '左推荐整型宽',
  `left_foot_status` varchar(10) DEFAULT NULL COMMENT '左足弓态势',
  `left_advise` varchar(255) DEFAULT NULL COMMENT '左专家意见',
  `right_foot_size` varchar(10) DEFAULT NULL COMMENT '右推荐鞋码',
  `right_foot_width` varchar(10) DEFAULT NULL COMMENT '右推荐半型宽',
  `right_foot_width2` varchar(10) DEFAULT NULL COMMENT '右推荐整型宽',
  `right_foot_status` varchar(10) DEFAULT NULL COMMENT '右足弓态势',
  `right_advise` varchar(255) DEFAULT NULL COMMENT '右专家意见',
  `left_para_01` float(5,2) DEFAULT NULL,
  `left_para_02` float(5,2) DEFAULT NULL,
  `left_para_03` float(5,2) DEFAULT NULL,
  `left_para_04` float(5,2) DEFAULT NULL,
  `left_para_05` float(5,2) DEFAULT NULL,
  `left_para_06` float(5,2) DEFAULT NULL,
  `right_para_01` float(5,2) DEFAULT NULL,
  `right_para_02` float(5,2) DEFAULT NULL,
  `right_para_03` float(5,2) DEFAULT NULL,
  `right_para_04` float(5,2) DEFAULT NULL,
  `right_para_05` float(5,2) DEFAULT NULL,
  `right_para_06` float(5,2) DEFAULT NULL,
  `left_foot_type` varchar(10) DEFAULT NULL COMMENT '左脚型',
  `right_foot_type` varchar(10) DEFAULT NULL COMMENT '右脚型',
  `foot_leg` varchar(10) DEFAULT NULL COMMENT '腿型',
  `left_int_size` smallint(3) DEFAULT NULL,
  `left_int_width` tinyint(3) DEFAULT NULL,
  `left_int_width2` tinyint(3) DEFAULT NULL,
  `left_int_status` tinyint(1) DEFAULT NULL COMMENT '左足势编码',
  `right_int_size` smallint(3) DEFAULT NULL,
  `right_int_width` tinyint(3) DEFAULT NULL,
  `right_int_width2` tinyint(3) DEFAULT NULL,
  `right_int_status` tinyint(1) DEFAULT NULL COMMENT '右足态编码',
  `left_int_type` tinyint(1) DEFAULT NULL,
  `right_int_type` tinyint(1) DEFAULT NULL,
  `foot_int_leg` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_suggestions
-- ----------------------------
INSERT INTO `yxd_suggestions` VALUES ('42', '0A002700001720170601155458', '230', '1.0型基宽', '一型', '正常足弓', null, '230', '1.0型基宽', '一型', '正常足弓', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '3', null, null, null, '3', null, null, null);
INSERT INTO `yxd_suggestions` VALUES ('45', '0A002700001720170601173753', '230', '1.0型基宽', '一型', '正常足弓', null, '230', '1.0型基宽', '一型', '正常足弓', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '3', null, null, null, '3', null, null, null);

-- ----------------------------
-- Table structure for `yxd_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_userinfo`;
CREATE TABLE `yxd_userinfo` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '可作为userid',
  `open_id` varchar(40) DEFAULT NULL COMMENT '微信用户号',
  `card_id` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `subscribe_app` varchar(20) DEFAULT NULL COMMENT '关注公众号名称',
  `subscribe_time` datetime DEFAULT NULL COMMENT '关注公众号时间',
  `subscribe_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '公众号关注状态',
  `file_amount` tinyint(3) NOT NULL DEFAULT '0' COMMENT '记录总数',
  `name` varchar(10) DEFAULT NULL COMMENT '名称',
  `sex` varchar(1) DEFAULT NULL COMMENT '性别',
  `nation` varchar(10) DEFAULT NULL COMMENT '民族',
  `date_birth` date DEFAULT NULL COMMENT '出生日期',
  `address` varchar(40) DEFAULT NULL COMMENT '地址',
  `district` varchar(10) DEFAULT NULL COMMENT '地区',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `county` varchar(10) DEFAULT NULL COMMENT '县市区',
  `height` float(6,2) DEFAULT NULL COMMENT '身高',
  `weight` float(6,2) DEFAULT NULL COMMENT '体重',
  `blood` varchar(3) DEFAULT NULL COMMENT '血型',
  `date_yunfu` date DEFAULT NULL COMMENT '末次月经',
  `user_account` varchar(20) DEFAULT NULL COMMENT '账号名称',
  `user_pwd` varchar(20) DEFAULT NULL COMMENT '账号密码',
  `user_phone` varchar(15) DEFAULT NULL COMMENT '注册电话',
  `user_email` varchar(30) DEFAULT NULL COMMENT '注册邮箱',
  `user_date` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `open_id` (`open_id`),
  UNIQUE KEY `card_id` (`card_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_userinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_verify`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_verify`;
CREATE TABLE `yxd_verify` (
  `mac_id` varchar(40) NOT NULL DEFAULT '' COMMENT '唯一',
  `scene` varchar(10) DEFAULT NULL COMMENT '二维码场景值',
  `ticket` varchar(120) DEFAULT NULL COMMENT '二维码入场券',
  `open_id` varchar(40) DEFAULT NULL COMMENT '监测：微信用户号',
  `create_date` datetime DEFAULT NULL COMMENT '二维码生成时间',
  `scan_date` datetime DEFAULT NULL COMMENT '二维码扫描时间',
  PRIMARY KEY (`mac_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_verify
-- ----------------------------

-- ----------------------------
-- Table structure for `yxd_wechat`
-- ----------------------------
DROP TABLE IF EXISTS `yxd_wechat`;
CREATE TABLE `yxd_wechat` (
  `app_name` varchar(20) NOT NULL COMMENT '公众号',
  `app_id` varchar(20) DEFAULT NULL COMMENT '开发ID',
  `app_secret` varchar(40) DEFAULT NULL COMMENT '开发密钥',
  `qrcode_url` varchar(150) DEFAULT NULL COMMENT '公众号固定二维码',
  `time` datetime DEFAULT NULL COMMENT 'token更新时间',
  `access_token` varchar(250) DEFAULT NULL COMMENT 'access_token',
  PRIMARY KEY (`app_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yxd_wechat
-- ----------------------------
