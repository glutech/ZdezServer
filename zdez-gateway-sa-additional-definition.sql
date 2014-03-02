/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50535
Source Host           : localhost:3306
Source Database       : zdez

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2014-03-01 21:25:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gateway_sa_auth_map
-- ----------------------------
DROP TABLE IF EXISTS `gateway_sa_auth_map`;
CREATE TABLE `gateway_sa_auth_map` (
  `auth_id` int(11) NOT NULL AUTO_INCREMENT,
  `school_admin_username` varchar(20) NOT NULL,
  `auth_token` varchar(32) NOT NULL,
  `create_ip` varchar(128) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`auth_id`),
  UNIQUE KEY `auth_token` (`auth_token`) USING BTREE,
  UNIQUE KEY `school_admin_username` (`school_admin_username`) USING BTREE,
  CONSTRAINT `gateway_sa_auth_map_ibfk_1` FOREIGN KEY (`school_admin_username`) REFERENCES `schoolAdmin` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gateway_school_msg_records
-- ----------------------------
DROP TABLE IF EXISTS `gateway_school_msg_records`;
CREATE TABLE `gateway_school_msg_records` (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_id_nullabled` int(11) DEFAULT NULL,
  `school_msg_id` int(11) NOT NULL,
  `post_ip` varchar(128) NOT NULL,
  `post_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  KEY `auth_id_nullabled` (`auth_id_nullabled`),
  KEY `school_msg_id` (`school_msg_id`),
  CONSTRAINT `gateway_school_msg_records_ibfk_1` FOREIGN KEY (`auth_id_nullabled`) REFERENCES `gateway_sa_auth_map` (`auth_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `gateway_school_msg_records_ibfk_2` FOREIGN KEY (`school_msg_id`) REFERENCES `schoolMsg` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;