/*
Navicat MySQL Data Transfer

Source Server         : 39.107.109.210
Source Server Version : 50726
Source Host           : 39.107.109.210:3306
Source Database       : alarmclock

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-04-26 21:09:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
  `id` int(11) NOT NULL,
  `lat` varchar(255) DEFAULT NULL,
  `lng` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_event
-- ----------------------------
DROP TABLE IF EXISTS `user_event`;
CREATE TABLE `user_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventname` varchar(255) DEFAULT NULL,
  `picture` text COMMENT 'base64 picture',
  `typename` varchar(255) DEFAULT NULL COMMENT 'at home outhome',
  `description` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `colorkey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
