/*
Navicat MySQL Data Transfer

Source Server         : Localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : wow2d_world

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-10-29 15:26:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for world_entities
-- ----------------------------
DROP TABLE IF EXISTS `world_entities`;
CREATE TABLE `world_entities` (
  `entity_id` int(11) NOT NULL,
  `entity_name` varchar(255) NOT NULL,
  `scriptFile` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
