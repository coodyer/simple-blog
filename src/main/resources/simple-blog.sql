/*
Navicat MySQL Data Transfer

Source Server         : blog
Source Server Version : 50173
Source Host           : 127.0.0.1:3366
Source Database       : simple-blog

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-02-21 14:31:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for member_info
-- ----------------------------
DROP TABLE IF EXISTS `member_info`;
CREATE TABLE `member_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `user_pwd` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `mobile` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  `contactor` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `unit` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `unit_address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `balance` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of member_info
-- ----------------------------
INSERT INTO `member_info` VALUES ('1', 'admin', 'fff74ef28fcdc7053ffc969ddb0a9a6b', '13226635321', '系统管理员', null, null, '系统管理员', '1', '1', '2017-01-07 01:01:45', '2017-01-07 01:01:47', null);
INSERT INTO `member_info` VALUES ('2', 'member', '594a9d81c3495b323d47ffd74b5a5e06', '13226635321', '张先生', '广州易醉网络科技有限公司', '广东省广州市天河区', '商户', '2', '2', '2017-01-07 01:01:45', '2017-01-07 01:01:47', '10000.00');

-- ----------------------------
-- Table structure for member_role
-- ----------------------------
DROP TABLE IF EXISTS `member_role`;
CREATE TABLE `member_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `menus` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of member_role
-- ----------------------------
INSERT INTO `member_role` VALUES ('1', '系统管理员', '1,2,4,8,10,12,13,14,15,16,17,18,19,29,30,31,32,33,34,35');
INSERT INTO `member_role` VALUES ('2', '商户', '5,6,7,36,37,38,39,40,41,42,43,44,45,46,47');

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

-- ----------------------------
-- Table structure for suffix
-- ----------------------------
DROP TABLE IF EXISTS `suffix`;
CREATE TABLE `suffix` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `suffix` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of suffix
-- ----------------------------
INSERT INTO `suffix` VALUES ('1', 'do', '0');
INSERT INTO `suffix` VALUES ('2', 'asp', '0');
INSERT INTO `suffix` VALUES ('3', 'php', '0');
INSERT INTO `suffix` VALUES ('4', 'aspx', '0');
INSERT INTO `suffix` VALUES ('5', 'jspx', '0');
INSERT INTO `suffix` VALUES ('6', 'phpx', '0');
INSERT INTO `suffix` VALUES ('7', 'cer', '0');
INSERT INTO `suffix` VALUES ('8', 'txt', '0');
INSERT INTO `suffix` VALUES ('9', 'ashx', '0');
INSERT INTO `suffix` VALUES ('10', 'ascx', '0');
INSERT INTO `suffix` VALUES ('11', 'ser', '0');
INSERT INTO `suffix` VALUES ('12', 'cgi', '0');
INSERT INTO `suffix` VALUES ('13', 'xml', '0');
INSERT INTO `suffix` VALUES ('14', 'html', '0');
INSERT INTO `suffix` VALUES ('15', 'htm', '0');
INSERT INTO `suffix` VALUES ('16', 'sb', '0');
INSERT INTO `suffix` VALUES ('17', 'jshx', '0');
INSERT INTO `suffix` VALUES ('18', 'gov', '0');
INSERT INTO `suffix` VALUES ('19', 'edu', '0');
INSERT INTO `suffix` VALUES ('20', 'sos', '0');
INSERT INTO `suffix` VALUES ('21', 'asa', '0');
INSERT INTO `suffix` VALUES ('22', 'asax', '0');
INSERT INTO `suffix` VALUES ('23', 'shtml', '0');
INSERT INTO `suffix` VALUES ('24', 'iis', '0');
INSERT INTO `suffix` VALUES ('25', 'swf', '0');
INSERT INTO `suffix` VALUES ('26', 'exp', '0');
INSERT INTO `suffix` VALUES ('27', 'esp', '2');
INSERT INTO `suffix` VALUES ('28', 'csp', '0');
INSERT INTO `suffix` VALUES ('29', 'psp', '0');
INSERT INTO `suffix` VALUES ('30', 'fsp', '0');
INSERT INTO `suffix` VALUES ('31', 'xsp', '0');
INSERT INTO `suffix` VALUES ('32', 'action', '0');
INSERT INTO `suffix` VALUES ('33', 'ftl', '0');
INSERT INTO `suffix` VALUES ('34', 'cmd', '0');
INSERT INTO `suffix` VALUES ('35', 'bat', '0');
INSERT INTO `suffix` VALUES ('36', 'vbs', '0');
INSERT INTO `suffix` VALUES ('37', 'vbe', '0');
INSERT INTO `suffix` VALUES ('38', 'com', '0');
INSERT INTO `suffix` VALUES ('39', 'xhtml', '0');
INSERT INTO `suffix` VALUES ('40', 'jhtml', '0');
INSERT INTO `suffix` VALUES ('41', 'tmp', '0');
INSERT INTO `suffix` VALUES ('42', 'cssx', '0');
INSERT INTO `suffix` VALUES ('43', 'jsx', '0');
INSERT INTO `suffix` VALUES ('44', 'exe', '0');
INSERT INTO `suffix` VALUES ('45', 'mspx', '0');
INSERT INTO `suffix` VALUES ('46', 'exec', '0');
INSERT INTO `suffix` VALUES ('47', 'org', '0');
INSERT INTO `suffix` VALUES ('48', 'jpgx', '0');
INSERT INTO `suffix` VALUES ('49', 'gifx', '0');
INSERT INTO `suffix` VALUES ('50', 'pngx', '0');
INSERT INTO `suffix` VALUES ('51', 'py', '0');
INSERT INTO `suffix` VALUES ('52', 'cgix', '0');
INSERT INTO `suffix` VALUES ('53', 'shell', '0');
INSERT INTO `suffix` VALUES ('54', 'csrf', '0');
INSERT INTO `suffix` VALUES ('55', 'xss', '0');

-- ----------------------------
-- Table structure for suffix_static
-- ----------------------------
DROP TABLE IF EXISTS `suffix_static`;
CREATE TABLE `suffix_static` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `suffix` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of suffix_static
-- ----------------------------
INSERT INTO `suffix_static` VALUES ('1', 'css');
INSERT INTO `suffix_static` VALUES ('2', 'js');
INSERT INTO `suffix_static` VALUES ('3', 'jpg');
INSERT INTO `suffix_static` VALUES ('4', 'gif');
INSERT INTO `suffix_static` VALUES ('5', 'bmp');
INSERT INTO `suffix_static` VALUES ('6', 'ico');
INSERT INTO `suffix_static` VALUES ('7', 'txt');
INSERT INTO `suffix_static` VALUES ('8', 'exe');
INSERT INTO `suffix_static` VALUES ('9', 'zip');
INSERT INTO `suffix_static` VALUES ('10', 'rar');
INSERT INTO `suffix_static` VALUES ('11', '7z');
INSERT INTO `suffix_static` VALUES ('12', 'jpeg');
INSERT INTO `suffix_static` VALUES ('13', 'png');
INSERT INTO `suffix_static` VALUES ('14', 'doc');
INSERT INTO `suffix_static` VALUES ('15', 'ppt');
INSERT INTO `suffix_static` VALUES ('16', 'avi');
INSERT INTO `suffix_static` VALUES ('17', 'mp4');
INSERT INTO `suffix_static` VALUES ('18', 'rmvb');
INSERT INTO `suffix_static` VALUES ('19', 'flv');
INSERT INTO `suffix_static` VALUES ('20', 'woff');
INSERT INTO `suffix_static` VALUES ('21', 'woff2');
INSERT INTO `suffix_static` VALUES ('22', 'eot');
INSERT INTO `suffix_static` VALUES ('23', 'svg');
INSERT INTO `suffix_static` VALUES ('24', 'ttf');
INSERT INTO `suffix_static` VALUES ('25', 'otf');

-- ----------------------------
-- Table structure for sys_menus
-- ----------------------------
DROP TABLE IF EXISTS `sys_menus`;
CREATE TABLE `sys_menus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `up_id` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  `code` varchar(128) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `menu_up_id_fk` (`up_id`),
  CONSTRAINT `menu_up_id_fk` FOREIGN KEY (`up_id`) REFERENCES `sys_menus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menus
-- ----------------------------
INSERT INTO `sys_menus` VALUES ('1', '基本设置', '#', '0', null, '1', '', null);
INSERT INTO `sys_menus` VALUES ('2', '内容管理', '#', '0', null, '2', null, null);
INSERT INTO `sys_menus` VALUES ('4', '调试中心', '#', '0', null, '3', null, null);
INSERT INTO `sys_menus` VALUES ('5', '商户中心', '#', '0', null, '4', null, null);
INSERT INTO `sys_menus` VALUES ('6', '应用中心', '#', '0', null, '5', null, null);
INSERT INTO `sys_menus` VALUES ('7', '财务中心', '#', '0', null, '6', null, null);
INSERT INTO `sys_menus` VALUES ('8', '后台首页', '/admin/debug/superIndex.do', '1', '1', '0', 'superIndex', '统计商户/订单/流水类信息');
INSERT INTO `sys_menus` VALUES ('10', '支付管理', '/admin/pay/payList.do', '1', '1', '2', 'paySetting', '配置支付宝/微信商户号以及秘钥');
INSERT INTO `sys_menus` VALUES ('16', '商户管理', '/admin/member/memberList.do', '1', '2', '0', 'memberSetting', '对商户信息进行管理和审核');
INSERT INTO `sys_menus` VALUES ('17', '应用管理', '/admin/app/appList.do', '1', '2', '1', 'appSetting', '对商户应用进行审核');
INSERT INTO `sys_menus` VALUES ('18', '订单管理', '/admin/order/orderList.do', '1', '2', '3', 'orderSetting', '交易订单管理');
INSERT INTO `sys_menus` VALUES ('19', '提款管理', '/admin/draw/drawList.do', '1', '2', '4', 'drawSetting', '对商户提款进行管理和审核');
INSERT INTO `sys_menus` VALUES ('29', '资源管理', '/admin/debug/fileList.do', '1', '4', '1', 'fileSetting', '对项目文件进行管理');
INSERT INTO `sys_menus` VALUES ('30', '监听管理', '/admin/debug/monitorList.do', '1', '4', '2', 'monitorSetting', '对项目方法进行监听');
INSERT INTO `sys_menus` VALUES ('31', '任务管理', '/admin/taskSetting.do', '1', '4', '3', 'taskSetting', '对定时任务进行管理');
INSERT INTO `sys_menus` VALUES ('32', '缓存管理', '/admin/debug/cacheSetting.do', '1', '4', '4', 'cacheSetting', '对系统缓存进行清理');
INSERT INTO `sys_menus` VALUES ('33', '调试日志', '/admin/debug/logSetting.do', '1', '4', '5', 'logSetting', '对debug阶段的日志进行浏览');
INSERT INTO `sys_menus` VALUES ('36', '数据统计', '/admin', '1', '5', '0', 'currMemberRunInfoSetting', '商户运营统计');
INSERT INTO `sys_menus` VALUES ('37', '信息修改', '/admin/member/memberEdit.do', '1', '5', '1', 'memberSetting', '修改商户信息');
INSERT INTO `sys_menus` VALUES ('38', '账户审核', '/admin/currMemberAuth.do', '1', '5', '2', 'currMemberAuthSetting', '审核信息提交');
INSERT INTO `sys_menus` VALUES ('42', '应用管理', '/admin/app/appList.do', '1', '6', '0', 'appSetting', '商户应用列表');
INSERT INTO `sys_menus` VALUES ('43', '交易管理', '/admin/order/orderList.do', '1', '6', '1', 'orderSetting', '商户交易订单管理');
INSERT INTO `sys_menus` VALUES ('46', '提款管理', '/admin/draw/drawList.do', '1', '7', '0', 'drawSetting', '用户提款管理');
INSERT INTO `sys_menus` VALUES ('47', '提款设置', '/admin/currMemberDrawInfo.do', '1', '7', '1', 'currMemberDrawInfoSetting', '用户提款账号管理');
