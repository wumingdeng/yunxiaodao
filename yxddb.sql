/*
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

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;

INSERT INTO `products` (`id`, `name`, `smallPic`, `swipePic`, `price`, `intro`, `size`, `color`, `type`, `pid`, `introNum`)
VALUES
  (1,'孕妇保健鞋','https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png','static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg',879,'just do it','37,38,39,40','黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp','正常,加宽',1,18),
  (2,'孕妇保健鞋2','https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png','static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg',879,'just do it','37,38,39,40,41','黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp','正常,加宽',2,1),
  (3,'孕妇保健鞋3','https://v3.modao.cc/uploads3/images/924/9242419/raw_1493883466.png','static/assets/shoe/product/p1/ps1.jpg,static/assets/shoe/product/p1/ps2.jpg,static/assets/shoe/product/p1/ps3.jpg,static/assets/shoe/product/p1/ps4.jpg,static/assets/shoe/product/p1/ps5.jpg',879,'just do it','37,38,39,40,41','黑色:static/assets/shoe/product/color_p1/color1.webp,白色:static/assets/shoe/product/color_p1/color2.webp,红色:static/assets/shoe/product/color_p1/color3.webp,灰色:static/assets/shoe/product/color_p1/color4.webp','正常,加宽',3,1);

/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;


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


LOCK TABLES `swipe_configs` WRITE;
/*!40000 ALTER TABLE `swipe_configs` DISABLE KEYS */;

INSERT INTO `swipe_configs` (`id`, `url`, `showTime`, `place`)
VALUES
  (1,'/static/assets/shoe/swipe/swipe.jpg',3,1),
  (2,'/static/assets/shoe/swipe/swipe1.jpg',3,2),
  (3,'/static/assets/shoe/swipe/swipe2.jpg',3,3);

/*!40000 ALTER TABLE `swipe_configs` ENABLE KEYS */;
UNLOCK TABLES;

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
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(128) DEFAULT NULL COMMENT '用户id',
  `week` int(11) DEFAULT NULL COMMENT '怀孕周数',
  `recordDate` datetime DEFAULT NULL COMMENT '测重时间',
  `hospital` varchar(20) DEFAULT NULL COMMENT '医院id',
  `weight` float DEFAULT NULL COMMENT '体重(kg)',
  `result` varchar(6) DEFAULT NULL COMMENT '评估结果',
  `standard` varchar(32) DEFAULT NULL COMMENT '标准体重范围',
  `tip` text COMMENT '注意点',
  `diet` text COMMENT '日常饮食贴士',
  PRIMARY KEY (`id`),
  UNIQUE KEY `as` (`userid`,`recordDate`) USING BTREE
) 
ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

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

LOCK TABLES `weightadvice_configs` WRITE;
/*!40000 ALTER TABLE `weightadvice_configs` DISABLE KEYS */;

INSERT INTO `weightadvice_configs` (`id`, `minWeek`, `maxWeek`, `normal`, `skinny`, `fat`, `tip_normal`, `tip_skinny`, `tip_fat`)
VALUES
  (1,1,12,'由于胎儿生长速度较慢，母体的相关组织增长变化也不明显，所需营养较为有限。因此，不必强求补充大量的营养，但这个阶段是胎儿生长发育最重要的时期，某种营养素的缺乏或过量，会引起胎儿早期发育障碍和畸形，此时需注意营养全面，烹调时应做到食物清淡爽口，避免刺激性强的食物。如有呕吐不可禁食，吐后仍要吃一些易消化的食物。','\"原因一：孕吐反应\r对策：为了缓解孕吐反应所带来的不适，建议孕妈咪在孕早期的饮食不要太油腻，多吃清淡的食物，像烤馍片、烤面包、苏打饼等食品有助缓解孕吐症状，所以孕吐比较厉害的孕妈咪可以买些来吃哦。饮食要营养均衡，像鸡蛋、绿叶蔬菜、豆类食品、动物肝脏、坚果、水果等食物都要各自摄取一些。有的孕妈咪孕吐实在严重就不强求说像常人那样有规律性的饮食，也不必为了宝宝而强制自己进食，都吐出来话也没什么营养可以吸收，进食的数量、种类、次数可以根据孕妈咪自身的食欲好坏来调整，少吃多餐，从整体上保证进食量。\r原因二：食欲不振\r对策：食欲不好的孕妈咪可以适当进行一些户外运动，这样可促进肠胃消化吸收，让食欲大增。然后再搭配清淡可口、富含营养、容易消化的饮食原则增加进食量。口味方面，孕妈咪也不必太忌讳哪些东西不能吃，以符合孕妈咪的饮食习惯和爱好为主，但不得接触酒类。\r原因三：胎儿的原因\r对策：想要增加体重，孕妈咪除了要保证正常的进食之外，还要记得补充动物肝脏、绿叶蔬菜等食物哦，这些食物都含有丰富的叶酸。\"','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL,NULL,NULL),
  (2,13,27,'胎儿生长发育迅速，母体也发生了极大变化，要增加热量，给予足够的蛋白质，增加动物性食品、植物油、维生素及微量元素的摄入。膳食要荤素兼备、粗细搭配，同时摄取足够的粮谷类食物，每天膳食中粮谷类需有300克至450克，除大米、面粉外，还可选用B族维生素类和氨基酸丰富的杂粮，如小米、玉米、麦片等；每日肉、蛋、禽、鱼类动物性食物或豆类及其制品需有200克、动物内脏(肝)50克(每周一至两次)，蔬菜500克，水果200克、植物油30克至40克。孕中期每餐摄取量可因孕妇食欲增加而有所增加，但随着妊娠的进展，子宫不断增大，胃部会受到挤压，使得孕妇餐后出现饱胀感。为此，可增加每日的餐次，但每次的食量要适度，切忌盲目过量进食或大吃甜食，避免孕妇因肥胖或血糖过高导致妊娠期糖尿病的发生。','\"一、孕吐反应\r措施：这时的饮食宜清淡、不要太油腻，烤面包片、烤馍片、苏打饼干等可以缓解孕吐症状，但同时也要平衡饮食，补充一定的叶酸，如动物的肝脏、鸡蛋、豆类、绿叶蔬菜、水果以及坚果。孕妇，孕吐反应比较严重的饮食不必像常人那样强调规律性，更不可强制进食，进食的餐次、数量、种类及时间应根据孕妇的食欲和反应轻重进行调整，采取少食多餐的办法，保证进食量。\r二、食欲不振\r措施：合理调配饮食，孕妇的饮食应以富含营养、清淡可口、容易消化为原则。在口味方面，不必太忌讳，可以尽可能照顾孕妇的饮食习惯和爱好。酒类应绝对禁止。\"','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL,NULL,NULL),
  (3,28,40,'\"1. 多吃鲫鱼、鲤鱼、萝卜和冬瓜等食物，有助于缓解水肿症状。\r2. 多吃含有丰富胶原蛋白的食品，如猪蹄等，有助于增加皮肤的弹性。\r3. 多吃鸡肉、鱼肉等易于消化吸收且含丰富蛋白质的食物。\r4. 吃一些动物的内脏，如心、肝、肾等，可满足多种无机盐和维生素的需要。\r5. 经常吃一些富含碘的食物，如海带、鱿鱼、紫菜等海洋植物。\r6. 多选用芹菜和莴苣等含有丰富的维生素和矿物质的食物。\r7. 多吃些花生、芝麻、豌豆、菠菜等含各种维生素和不饱和脂肪酸的食物，以避免胎儿发育异常和肌肉萎缩。\r不要过多摄入碳水化合物（主食）和饱和脂肪（例如奶油、油炸食品），避免胎儿过大。可以多吃一些优质蛋白，比如鱼、虾类的食物，另外要吃新鲜的蔬菜和水果，补充各种维生素和微量元素。\"','可以多吃些含有糖分的食物，如甘蔗汁、果汁等。不过，当发现宝宝体重不足时，最好先检查是不是胎 盘或其他功能有问题，如果是因为疾病因素造成胎儿体重不 足，只要控制病情，宝宝的体重自然就会跟上。其次饮食要均衡，宝宝体重才会标准，可着重增加蛋 白质、氣基酸的摄取，至于油脂类并不需要额外摄取，因为 日常饮食中的油脂已经很多了。','\"尽量少吃零食和夜宵。大家都知道，吃零食是导致肥胖的重要因素之一。其实夜宵也是保持体重的大敌，特别是就寝前两个小时左右吃夜宵，缺乏消耗，脂肪很容易在体内囤积，使人发胖。\r多吃一些绿色蔬菜。蔬菜本身不但含有丰富的维生素，而且还有助于体内钙、铁、纤维素的吸收，以防止便秘。少吃油腻食物，多吃富含蛋白、维生素的食物。\r饮食过量时隔天节食：有时不小心贪吃，也不必过于自责，建议不妨减少第二天的饮食量，而且以吃清淡食物为宜。\"',NULL,NULL,NULL);

/*!40000 ALTER TABLE `weightadvice_configs` ENABLE KEYS */;
UNLOCK TABLES;

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

LOCK TABLES `weightrate_configs` WRITE;
/*!40000 ALTER TABLE `weightrate_configs` DISABLE KEYS */;

INSERT INTO `weightrate_configs` (`id`, `status`, `rateMin`, `rateMax`, `dRateMin`, `dRateMax`)
VALUES
  (1,1,0.44,0.58,0.66,0.87),
  (2,2,0.35,0.5,0.53,0.75),
  (3,3,0.23,0.33,0.46,0.66),
  (4,4,0.17,0.27,0.34,0.54);

/*!40000 ALTER TABLE `weightrate_configs` ENABLE KEYS */;
UNLOCK TABLES;


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

DROP TABLE IF EXISTS `weight_diet_configs`;
CREATE TABLE `weight_diet_configs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `week` int(11) DEFAULT NULL COMMENT '周数',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
