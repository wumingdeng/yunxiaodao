# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.7.16)
# Database: yxdDB
# Generation Time: 2017-05-25 06:08:51 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table foot_records
# ------------------------------------------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `foot_records` WRITE;
/*!40000 ALTER TABLE `foot_records` DISABLE KEYS */;

INSERT INTO `foot_records` (`id`, `userid`, `recordDate`, `picture`, `lfootLength`, `lfootWidth`, `lsize`, `ltypeWidth`, `lfootType`, `rfootLength`, `rfootWidth`, `rsize`, `rtypeWidth`, `rfootType`, `footJudgment`, `suggestShoe`, `footKnowledge`)
VALUES
	(1,'onmcQ08YlxYrpJYJgc7lJVIXAkt4','2017-05-12','111',30,10,38,12,1,30,11,38,11,1,'1',3,1);

/*!40000 ALTER TABLE `foot_records` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table footKnowledge
# ------------------------------------------------------------

DROP TABLE IF EXISTS `footKnowledge`;

CREATE TABLE `footKnowledge` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table hospitals
# ------------------------------------------------------------

DROP TABLE IF EXISTS `hospitals`;

CREATE TABLE `hospitals` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` int(11) DEFAULT NULL COMMENT '医院名称',
  `city` int(11) DEFAULT NULL COMMENT '城市',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table link_configs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `link_configs`;

CREATE TABLE `link_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text NOT NULL COMMENT '地址',
  `picture` text NOT NULL COMMENT '图片',
  `linkName` varchar(11) DEFAULT NULL COMMENT '链接名称',
  `place` int(11) DEFAULT NULL COMMENT '显示位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `link_configs` WRITE;
/*!40000 ALTER TABLE `link_configs` DISABLE KEYS */;

INSERT INTO `link_configs` (`id`, `url`, `picture`, `linkName`, `place`)
VALUES
	(1,'www.baidu.com','static/assets/shoe/link/ico_01.png','百度',0);

/*!40000 ALTER TABLE `link_configs` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table orders
# ------------------------------------------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;

INSERT INTO `orders` (`id`, `userid`, `valid`, `orderid`, `contact`, `gender`, `tel`, `address`, `province`, `city`, `area`, `shoeid`, `shoeName`, `size`, `color`, `type`, `price`, `remark`, `createtime`, `status`)
VALUES
	(1,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',1,1,'老王',1,'18954875125','通往天堂的地铁','福建','厦门','思明区',1,'孕妇蹦极专用鞋',40,'黑色','加宽',399,'不甜不要钱',1484223595,0),
	(2,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',1,1,'老王',1,'18954875125','通往天堂的地铁','福建','厦门','思明区',1,'孕妇蹦极专用鞋',40,'黑色','加宽',399,'不甜不要钱',1484223595,1),
	(3,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',1,1,'老王',1,'18954875125','通往天堂的地铁','福建','厦门','思明区',1,'孕妇蹦极专用鞋',40,'黑色','加宽',399,'不甜不要钱',1484223595,2),
	(4,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',1,1,'老王',1,'18954875125','通往天堂的地铁','福建','厦门','思明区',1,'孕妇蹦极专用鞋',40,'黑色','加宽',399,'不甜不要钱',1484223595,3),
	(5,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',1,1,'老王',1,'18954875125','通往天堂的地铁','福建','厦门','思明区',1,'孕妇蹦极专用鞋',40,'黑色','加宽',399,'不甜不要钱',1484223595,4),
	(6,NULL,NULL,NULL,'长长的',0,'18888888888','大使馆收到风','福建','厦门','思明区',1,'孕妇保健鞋',39,'白色','加宽',879,'百分点',1495507759,0),
	(7,NULL,NULL,NULL,'26w',0,'18888888111','qwe','福建','厦门','思明区',1,'孕妇保健鞋',38,'黑色','加宽',879,'部分代表',1495510172,0),
	(8,NULL,NULL,NULL,'26w',0,'18888888111','qwe','福建','厦门','思明区',1,'孕妇保健鞋',38,'黑色','加宽',879,'部分代表',1495511123,0),
	(9,NULL,NULL,NULL,'纷纷过',1,'18888888111','但是饭否的','福建','厦门','湖里区',3,'孕妇保健鞋3',40,'白色','正常',879,'饿过广东 v否',1495524124,0),
	(10,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536982,0),
	(11,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536982,0),
	(12,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536982,0),
	(13,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536982,0),
	(14,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536988,0),
	(15,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536988,0),
	(16,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536988,0),
	(17,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495536988,0),
	(18,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537018,0),
	(19,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537018,0),
	(20,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537022,0),
	(21,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537022,0),
	(22,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537026,0),
	(23,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537026,0),
	(24,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537026,0),
	(25,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537030,0),
	(26,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537030,0),
	(27,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537032,0),
	(28,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537032,0),
	(29,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537033,0),
	(30,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537033,0),
	(31,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537035,0),
	(32,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537035,0),
	(33,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537035,0),
	(34,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537035,0),
	(35,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537039,0),
	(36,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537039,0),
	(37,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537039,0),
	(38,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',37,'黑色','加宽',879,NULL,1495537039,0),
	(39,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',39,'红色','加宽',879,NULL,1495604528,0),
	(40,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',39,'红色','加宽',879,NULL,1495604554,0),
	(41,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',39,'红色','加宽',879,NULL,1495604565,0),
	(42,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',39,'红色','加宽',879,NULL,1495604603,0),
	(43,NULL,NULL,NULL,'13355555555',0,'13333333333','这么好','福建','厦门','思明区',3,'孕妇保健鞋3',40,'红色','正常',879,NULL,1495609849,0),
	(44,NULL,NULL,NULL,'13355555555',0,'13333333333','这么好','福建','厦门','思明区',3,'孕妇保健鞋3',40,'红色','正常',879,NULL,1495609849,0),
	(45,NULL,NULL,NULL,'杜子腾',0,'15682536522','突兀图腾柱','福建','厦门','思明区',1,'孕妇保健鞋',40,'黑色','加宽',879,NULL,1495609983,0),
	(46,NULL,NULL,NULL,'你',0,'13333333333','吧','福建','厦门','思明区',3,'孕妇保健鞋3',39,'灰色','加宽',879,'l l l',1495626159,0),
	(47,NULL,NULL,NULL,'你',0,'13333333333','吧','福建','厦门','思明区',3,'孕妇保健鞋3',37,'黑色','正常',879,NULL,1495681433,0),
	(48,NULL,NULL,NULL,'dfd',0,'18888888888','电风扇的大股东','福建','厦门','思明区',1,'孕妇保健鞋',39,'白色','正常',879,'但是发生大发的撒',1495688742,0),
	(49,NULL,NULL,NULL,'dfd',0,'18888888888','电风扇的大股东','福建','厦门','思明区',1,'孕妇保健鞋',39,'白色','正常',879,'但是发生大发的撒',1495688775,0),
	(50,NULL,NULL,NULL,'dfd',0,'18888888888','电风扇的大股东','福建','厦门','思明区',1,'孕妇保健鞋',39,'白色','正常',879,'但是发生大发的撒',1495688801,0);

/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table products
# ------------------------------------------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;

INSERT INTO `products` (`id`, `name`, `smallPic`, `swipePic`, `price`, `intro`, `size`, `color`, `type`)
VALUES
	(1,'孕妇保健鞋','https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png','static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg',879,'just do it','37,38,39,40','黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp','正常,加宽'),
	(2,'孕妇保健鞋2','https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png','static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg',879,'just do it','37,38,39,40,41','黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp','正常,加宽'),
	(3,'孕妇保健鞋3','https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png','static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg',879,'just do it','37,38,39,40,41','黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp','正常,加宽');

/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table swipe_configs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `swipe_configs`;

CREATE TABLE `swipe_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text NOT NULL COMMENT '地址',
  `showTime` int(11) NOT NULL COMMENT '显示时间',
  `place` tinyint(4) DEFAULT NULL COMMENT '显示位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `swipe_configs` WRITE;
/*!40000 ALTER TABLE `swipe_configs` DISABLE KEYS */;

INSERT INTO `swipe_configs` (`id`, `url`, `showTime`, `place`)
VALUES
	(1,'/static/assets/shoe/swipe/swipe.jpg',3,1),
	(2,'/static/assets/shoe/swipe/swipe1.jpg',3,2),
	(3,'/static/assets/shoe/swipe/swipe2.jpg',3,3);

/*!40000 ALTER TABLE `swipe_configs` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `wxid`, `userName`, `password`, `lastPeriod`, `name`, `height`, `weight`, `shape`, `isSingle`, `contact`, `gender`, `tel`, `address`, `city`, `headUrl`)
VALUES
	(1,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',NULL,NULL,'2017-01-01','陈钊文',160,48,2,0,'dfd',0,'18888888888','电风扇的大股东','厦门','http://wx.qlogo.cn/mmopen/ee0w56iagUJSSuicGhKBWWmBn0voibuwZ1cVwQpdriaejasF2KFC6VMuMicnoO8dXZiaQxibk1TjQBxlATv3VKKauKbh3GBqRPbicXAV/0'),
	(2,'onmcQ0309iaFOBhQAqA59K_2Ybgc',NULL,NULL,NULL,'音源',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,'http://wx.qlogo.cn/mmopen/PiajxSqBRaEKkkdY1iaSdxM83CMRFict3TO5d1vOwBoRs8fvr3bGvbn6AeBKoiadUMm8ic2AlanDSZx1LSCyMgHCRzUXZEX242hcKc5eiaEaVcxBg/0'),
	(3,'onmcQ07m8lSlejzIBofU85KQrXTY',NULL,NULL,'2017-02-19','吴朝曦',100,100,NULL,1,NULL,0,NULL,NULL,NULL,'http://wx.qlogo.cn/mmopen/ee0w56iagUJRn37xiaa1Rz76B5pxCbvaKWqzaA9IxXSKzCcmuDa5tkrahRicmJwknecfk0PdcyqUX6XsZTpK9XJxJSq4jw97SHz/0'),
	(4,'123',NULL,NULL,'2017-05-18','吴朝曦',120,52,NULL,1,NULL,0,NULL,NULL,NULL,'http://wx.qlogo.cn/mmopen/ee0w56iagUJRn37xiaa1Rz76B5pxCbvaKWqzaA9IxXSKzCcmuDa5tkrahRicmJwknecfk0PdcyqUX6XsZTpK9XJxJSq4jw97SHz/0'),
	(5,'321',NULL,NULL,NULL,'吴朝曦',NULL,NULL,NULL,1,NULL,0,NULL,NULL,NULL,'http://wx.qlogo.cn/mmopen/ee0w56iagUJRn37xiaa1Rz76B5pxCbvaKWqzaA9IxXSKzCcmuDa5tkrahRicmJwknecfk0PdcyqUX6XsZTpK9XJxJSq4jw97SHz/0');

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table weight_records
# ------------------------------------------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `weight_records` WRITE;
/*!40000 ALTER TABLE `weight_records` DISABLE KEYS */;

INSERT INTO `weight_records` (`id`, `userid`, `week`, `recordDate`, `hospital`, `weight`, `result`, `standard`, `tip`, `diet`)
VALUES
	(78,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',21,'2017-05-23',NULL,55,'正常','52.77kg-56.75kg','胎儿生长发育迅速，母体也发生了极大变化，要增加热量，给予足够的蛋白质，增加动物性食品、植物油、维生素及微量元素的摄入。膳食要荤素兼备、粗细搭配，同时摄取足够的粮谷类食物，每天膳食中粮谷类需有300克至450克，除大米、面粉外，还可选用B族维生素类和氨基酸丰富的杂粮，如小米、玉米、麦片等；每日肉、蛋、禽、鱼类动物性食物或豆类及其制品需有200克、动物内脏(肝)50克(每周一至两次)，蔬菜500克，水果200克、植物油30克至40克。孕中期每餐摄取量可因孕妇食欲增加而有所增加，但随着妊娠的进展，子宫不断增大，胃部会受到挤压，使得孕妇餐后出现饱胀感。为此，可增加每日的餐次，但每次的食量要适度，切忌盲目过量进食或大吃甜食，避免孕妇因肥胖或血糖过高导致妊娠期糖尿病的发生。',NULL),
	(92,'onmcQ08YlxYrpJYJgc7lJVIXAkt4',21,'2017-05-24',NULL,65,'偏重','52.77kg-56.75kg','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL);

/*!40000 ALTER TABLE `weight_records` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table weightAdvice_configs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `weightAdvice_configs`;

CREATE TABLE `weightAdvice_configs` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `weightAdvice_configs` WRITE;
/*!40000 ALTER TABLE `weightAdvice_configs` DISABLE KEYS */;

INSERT INTO `weightAdvice_configs` (`id`, `minWeek`, `maxWeek`, `normal`, `skinny`, `fat`, `tip_normal`, `tip_skinny`, `tip_fat`)
VALUES
	(1,1,12,'由于胎儿生长速度较慢，母体的相关组织增长变化也不明显，所需营养较为有限。因此，不必强求补充大量的营养，但这个阶段是胎儿生长发育最重要的时期，某种营养素的缺乏或过量，会引起胎儿早期发育障碍和畸形，此时需注意营养全面，烹调时应做到食物清淡爽口，避免刺激性强的食物。如有呕吐不可禁食，吐后仍要吃一些易消化的食物。','\"原因一：孕吐反应\r对策：为了缓解孕吐反应所带来的不适，建议孕妈咪在孕早期的饮食不要太油腻，多吃清淡的食物，像烤馍片、烤面包、苏打饼等食品有助缓解孕吐症状，所以孕吐比较厉害的孕妈咪可以买些来吃哦。饮食要营养均衡，像鸡蛋、绿叶蔬菜、豆类食品、动物肝脏、坚果、水果等食物都要各自摄取一些。有的孕妈咪孕吐实在严重就不强求说像常人那样有规律性的饮食，也不必为了宝宝而强制自己进食，都吐出来话也没什么营养可以吸收，进食的数量、种类、次数可以根据孕妈咪自身的食欲好坏来调整，少吃多餐，从整体上保证进食量。\r原因二：食欲不振\r对策：食欲不好的孕妈咪可以适当进行一些户外运动，这样可促进肠胃消化吸收，让食欲大增。然后再搭配清淡可口、富含营养、容易消化的饮食原则增加进食量。口味方面，孕妈咪也不必太忌讳哪些东西不能吃，以符合孕妈咪的饮食习惯和爱好为主，但不得接触酒类。\r原因三：胎儿的原因\r对策：想要增加体重，孕妈咪除了要保证正常的进食之外，还要记得补充动物肝脏、绿叶蔬菜等食物哦，这些食物都含有丰富的叶酸。\"','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL,NULL,NULL),
	(2,13,27,'胎儿生长发育迅速，母体也发生了极大变化，要增加热量，给予足够的蛋白质，增加动物性食品、植物油、维生素及微量元素的摄入。膳食要荤素兼备、粗细搭配，同时摄取足够的粮谷类食物，每天膳食中粮谷类需有300克至450克，除大米、面粉外，还可选用B族维生素类和氨基酸丰富的杂粮，如小米、玉米、麦片等；每日肉、蛋、禽、鱼类动物性食物或豆类及其制品需有200克、动物内脏(肝)50克(每周一至两次)，蔬菜500克，水果200克、植物油30克至40克。孕中期每餐摄取量可因孕妇食欲增加而有所增加，但随着妊娠的进展，子宫不断增大，胃部会受到挤压，使得孕妇餐后出现饱胀感。为此，可增加每日的餐次，但每次的食量要适度，切忌盲目过量进食或大吃甜食，避免孕妇因肥胖或血糖过高导致妊娠期糖尿病的发生。','\"一、孕吐反应\r措施：这时的饮食宜清淡、不要太油腻，烤面包片、烤馍片、苏打饼干等可以缓解孕吐症状，但同时也要平衡饮食，补充一定的叶酸，如动物的肝脏、鸡蛋、豆类、绿叶蔬菜、水果以及坚果。孕妇，孕吐反应比较严重的饮食不必像常人那样强调规律性，更不可强制进食，进食的餐次、数量、种类及时间应根据孕妇的食欲和反应轻重进行调整，采取少食多餐的办法，保证进食量。\r二、食欲不振\r措施：合理调配饮食，孕妇的饮食应以富含营养、清淡可口、容易消化为原则。在口味方面，不必太忌讳，可以尽可能照顾孕妇的饮食习惯和爱好。酒类应绝对禁止。\"','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL,NULL,NULL),
	(3,28,40,'\"1. 多吃鲫鱼、鲤鱼、萝卜和冬瓜等食物，有助于缓解水肿症状。\r2. 多吃含有丰富胶原蛋白的食品，如猪蹄等，有助于增加皮肤的弹性。\r3. 多吃鸡肉、鱼肉等易于消化吸收且含丰富蛋白质的食物。\r4. 吃一些动物的内脏，如心、肝、肾等，可满足多种无机盐和维生素的需要。\r5. 经常吃一些富含碘的食物，如海带、鱿鱼、紫菜等海洋植物。\r6. 多选用芹菜和莴苣等含有丰富的维生素和矿物质的食物。\r7. 多吃些花生、芝麻、豌豆、菠菜等含各种维生素和不饱和脂肪酸的食物，以避免胎儿发育异常和肌肉萎缩。\r不要过多摄入碳水化合物（主食）和饱和脂肪（例如奶油、油炸食品），避免胎儿过大。可以多吃一些优质蛋白，比如鱼、虾类的食物，另外要吃新鲜的蔬菜和水果，补充各种维生素和微量元素。\"','可以多吃些含有糖分的食物，如甘蔗汁、果汁等。不过，当发现宝宝体重不足时，最好先检查是不是胎 盘或其他功能有问题，如果是因为疾病因素造成胎儿体重不 足，只要控制病情，宝宝的体重自然就会跟上。其次饮食要均衡，宝宝体重才会标准，可着重增加蛋 白质、氣基酸的摄取，至于油脂类并不需要额外摄取，因为 日常饮食中的油脂已经很多了。','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL,NULL,NULL);

/*!40000 ALTER TABLE `weightAdvice_configs` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table weightRate_configs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `weightRate_configs`;

CREATE TABLE `weightRate_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `status` int(11) DEFAULT NULL COMMENT '1:偏轻 2:正常 3:偏重 4:肥胖',
  `rateMin` float DEFAULT NULL COMMENT '每周体重变化',
  `rateMax` float DEFAULT NULL,
  `dRateMin` float DEFAULT NULL COMMENT '多胎的数值',
  `dRateMax` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `weightRate_configs` WRITE;
/*!40000 ALTER TABLE `weightRate_configs` DISABLE KEYS */;

INSERT INTO `weightRate_configs` (`id`, `status`, `rateMin`, `rateMax`, `dRateMin`, `dRateMax`)
VALUES
	(1,1,0.44,0.58,0.66,0.87),
	(2,2,0.35,0.5,0.53,0.75),
	(3,3,0.23,0.33,0.46,0.66),
	(4,4,0.17,0.27,0.34,0.54);

/*!40000 ALTER TABLE `weightRate_configs` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
