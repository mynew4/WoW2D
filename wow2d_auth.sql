/*
Navicat MySQL Data Transfer

Source Server         : Localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : wow2d_auth

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-10-26 13:35:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_banned_names
-- ----------------------------
DROP TABLE IF EXISTS `auth_banned_names`;
CREATE TABLE `auth_banned_names` (
  `user_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_characters
-- ----------------------------
DROP TABLE IF EXISTS `auth_characters`;
CREATE TABLE `auth_characters` (
  `user_id` int(11) NOT NULL,
  `realm_id` int(11) DEFAULT NULL,
  `user_name` varchar(16) DEFAULT NULL,
  `x_position` float(255,0) DEFAULT NULL,
  `y_position` float(255,0) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_realms
-- ----------------------------
DROP TABLE IF EXISTS `auth_realms`;
CREATE TABLE `auth_realms` (
  `realm_id` int(11) NOT NULL,
  `realm_name` varchar(32) DEFAULT NULL,
  `realm_port` int(5) DEFAULT NULL,
  PRIMARY KEY (`realm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_users
-- ----------------------------
DROP TABLE IF EXISTS `auth_users`;
CREATE TABLE `auth_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `user_level` int(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
