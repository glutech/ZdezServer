/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.108
Source Server Version : 50616
Source Host           : 192.168.1.108:3306
Source Database       : zdez

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2014-03-10 16:56:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for a_activities
-- ----------------------------
DROP TABLE IF EXISTS `a_activities`;
CREATE TABLE `a_activities` (
  `act_id` int(11) NOT NULL AUTO_INCREMENT,
  `act_name` varchar(255) NOT NULL,
  `act_addr` varchar(255) NOT NULL,
  `act_state_enum` enum('ok','cancel','finished') NOT NULL,
  `act_info` varchar(255) NOT NULL,
  `actg_id` int(11) NOT NULL,
  `tm_id` int(11) NOT NULL,
  `act_type_enum` enum('school_level','department_level','major_level') NOT NULL,
  PRIMARY KEY (`act_id`),
  KEY `act_act_catg` (`actg_id`),
  KEY `act_act_time` (`tm_id`),
  KEY `act_state_enum` (`act_state_enum`),
  KEY `act_type_enum` (`act_type_enum`),
  CONSTRAINT `act_act_time` FOREIGN KEY (`tm_id`) REFERENCES `a_activities_time` (`act_time_id`) ON UPDATE CASCADE,
  CONSTRAINT `act_act_catg` FOREIGN KEY (`actg_id`) REFERENCES `a_act_category_terms` (`catg_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_activities_time
-- ----------------------------
DROP TABLE IF EXISTS `a_activities_time`;
CREATE TABLE `a_activities_time` (
  `act_time_id` int(11) NOT NULL AUTO_INCREMENT,
  `act_time_type_enum` enum('shorttime','longtime') NOT NULL,
  `act_time_begin` date NOT NULL,
  `act_time_end_nullabled` date DEFAULT NULL,
  PRIMARY KEY (`act_time_id`),
  KEY `act_time_type_enum` (`act_time_type_enum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_act_category_terms
-- ----------------------------
DROP TABLE IF EXISTS `a_act_category_terms`;
CREATE TABLE `a_act_category_terms` (
  `catg_id` int(11) NOT NULL AUTO_INCREMENT,
  `catg_name` varchar(255) NOT NULL,
  PRIMARY KEY (`catg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_act_usr_map
-- ----------------------------
DROP TABLE IF EXISTS `a_act_usr_map`;
CREATE TABLE `a_act_usr_map` (
  `map_id` int(11) NOT NULL AUTO_INCREMENT,
  `map_type_enum` enum('creator','joinner') NOT NULL,
  `act_id` int(11) NOT NULL,
  `usr_id` int(11) NOT NULL,
  PRIMARY KEY (`map_id`),
  KEY `map_act` (`act_id`),
  KEY `map_usr` (`usr_id`),
  KEY `map_type_enum` (`map_type_enum`),
  CONSTRAINT `map_usr` FOREIGN KEY (`usr_id`) REFERENCES `a_users` (`usr_id`) ON UPDATE CASCADE,
  CONSTRAINT `map_act` FOREIGN KEY (`act_id`) REFERENCES `a_activities` (`act_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_audit
-- ----------------------------
DROP TABLE IF EXISTS `a_audit`;
CREATE TABLE `a_audit` (
  `adt_id` int(11) NOT NULL AUTO_INCREMENT,
  `adt_time` datetime NOT NULL,
  `adt_state_enum` enum('pass','nopass') NOT NULL,
  `adt_content` varchar(255) NOT NULL,
  `adto_id` int(11) NOT NULL,
  `note_id` int(11) NOT NULL,
  `cmt_id` int(11) NOT NULL,
  `usr_id` int(11) NOT NULL,
  PRIMARY KEY (`adt_id`),
  KEY `audit_adto` (`adto_id`),
  KEY `audit_cmt` (`cmt_id`),
  KEY `audit_note` (`note_id`),
  KEY `usr_id` (`usr_id`),
  KEY `adt_state_enum` (`adt_state_enum`),
  CONSTRAINT `audit_adto` FOREIGN KEY (`adto_id`) REFERENCES `a_auditor` (`adto_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `audit_cmt` FOREIGN KEY (`cmt_id`) REFERENCES `a_comments` (`cmt_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `audit_note` FOREIGN KEY (`note_id`) REFERENCES `a_notices` (`note_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `a_audit_ibfk_1` FOREIGN KEY (`usr_id`) REFERENCES `a_users` (`usr_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_auditor
-- ----------------------------
DROP TABLE IF EXISTS `a_auditor`;
CREATE TABLE `a_auditor` (
  `adto_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(255) NOT NULL,
  PRIMARY KEY (`adto_id`),
  KEY `a_auditor_ibfk_1` (`admin_name`),
  CONSTRAINT `a_auditor_ibfk_1` FOREIGN KEY (`admin_name`) REFERENCES `schooladmin` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_comments
-- ----------------------------
DROP TABLE IF EXISTS `a_comments`;
CREATE TABLE `a_comments` (
  `cmt_id` int(11) NOT NULL AUTO_INCREMENT,
  `cmt_time` datetime NOT NULL,
  `cmt_content` varchar(255) NOT NULL,
  `cmt_state_enum` enum('ok','delete','cancel') NOT NULL,
  `cmt_parent_cmt_id_nullabled` int(11) DEFAULT NULL,
  `act_id` int(11) NOT NULL,
  `usr_id` int(11) NOT NULL,
  PRIMARY KEY (`cmt_id`),
  KEY `cmt_act` (`act_id`),
  KEY `cmt_usr` (`usr_id`),
  KEY `cmt_parent_cmt_id_nullabled` (`cmt_parent_cmt_id_nullabled`),
  KEY `cmt_state_enum` (`cmt_state_enum`),
  CONSTRAINT `a_comments_ibfk_1` FOREIGN KEY (`cmt_parent_cmt_id_nullabled`) REFERENCES `a_comments` (`cmt_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cmt_act` FOREIGN KEY (`act_id`) REFERENCES `a_activities` (`act_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cmt_usr` FOREIGN KEY (`usr_id`) REFERENCES `a_users` (`usr_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_notices
-- ----------------------------
DROP TABLE IF EXISTS `a_notices`;
CREATE TABLE `a_notices` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `note_time` date NOT NULL,
  `note_content` varchar(255) NOT NULL,
  `note_state_enum` enum('ok','delete','cancel') NOT NULL,
  `act_id` int(11) NOT NULL,
  PRIMARY KEY (`note_id`),
  KEY `note_act` (`act_id`),
  KEY `note_state_enum` (`note_state_enum`),
  CONSTRAINT `note_act` FOREIGN KEY (`act_id`) REFERENCES `a_activities` (`act_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for a_users
-- ----------------------------
DROP TABLE IF EXISTS `a_users`;
CREATE TABLE `a_users` (
  `usr_id` int(11) NOT NULL AUTO_INCREMENT,
  `usr_type_enum` enum('teacher','student','auditor','enterprise') NOT NULL,
  `usr_able_state_enum` enum('yes','no') NOT NULL,
  `usr_signature` varchar(255) NOT NULL,
  `usr_avatar` varchar(255) NOT NULL,
  `usr_phone` varchar(20) NOT NULL,
  `usr_nickname` varchar(255) NOT NULL,
  `stu_id` int(11) NOT NULL,
  PRIMARY KEY (`usr_id`),
  UNIQUE KEY `usr_nickname` (`usr_nickname`),
  KEY `stu_id` (`stu_id`),
  KEY `usr_type_enum` (`usr_type_enum`),
  KEY `usr_able_state_enum` (`usr_able_state_enum`),
  CONSTRAINT `a_users_ibfk_1` FOREIGN KEY (`stu_id`) REFERENCES `student` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

