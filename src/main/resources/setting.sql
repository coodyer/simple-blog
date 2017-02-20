/*
Navicat MySQL Data Transfer

Source Server         : blog
Source Server Version : 50173
Source Host           : 127.0.0.1:3366
Source Database       : simple-blog

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-02-20 17:22:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for setting
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `keywords` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `copyright` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of setting
-- ----------------------------
INSERT INTO `setting` VALUES ('1', '代码描绘人生的小狗窝', 'JAVA技术,永无止尽,QQ644556636', '编程一辈子，代码描绘人生', 'Copyright © 2014-2019 代码人生');
