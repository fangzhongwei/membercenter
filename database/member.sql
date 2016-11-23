/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : member

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2016-11-23 14:21:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tm_member
-- ----------------------------
DROP TABLE IF EXISTS `tm_member`;
CREATE TABLE `tm_member` (
  `member_id` bigint(20) NOT NULL COMMENT 'member id',
  `username` varchar(64) NOT NULL COMMENT 'username',
  `status` tinyint(4) NOT NULL COMMENT 'member status, 1 normal, 2 disable',
  `password` varchar(256) NOT NULL COMMENT 'login password',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `gmt_update` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'last update time',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `uq_tm_u` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tm_member_identity
-- ----------------------------
DROP TABLE IF EXISTS `tm_member_identity`;
CREATE TABLE `tm_member_identity` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(11) NOT NULL,
  `identity` varchar(64) NOT NULL,
  `pid` tinyint(4) NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tmi_ip` (`identity`) USING BTREE,
  KEY `uq_tmi_m` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tm_member_reg
-- ----------------------------
DROP TABLE IF EXISTS `tm_member_reg`;
CREATE TABLE `tm_member_reg` (
  `member_id` bigint(20) NOT NULL,
  `reg_ip` bigint(11) NOT NULL,
  `reg_lat` varchar(32) NOT NULL,
  `reg_lng` varchar(32) NOT NULL,
  `reg_device_type` tinyint(4) NOT NULL,
  `reg_device_identity` varchar(128) NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sequence
-- ----------------------------
DROP TABLE IF EXISTS `t_sequence`;
CREATE TABLE `t_sequence` (
  `name` varchar(64) NOT NULL,
  `current_value` bigint(20) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  UNIQUE KEY `uq_ts_n` (`name`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='sequence table';

-- ----------------------------
-- Function structure for currval
-- ----------------------------
DROP FUNCTION IF EXISTS `currval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `currval`(seq_name VARCHAR(64)) RETURNS bigint(20)
    READS SQL DATA
    DETERMINISTIC
BEGIN

DECLARE VALUE BIGINT;

SET VALUE = 0;

SELECT current_value INTO VALUE FROM t_sequence WHERE NAME = seq_name;

RETURN VALUE;

END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `nextval`(seq_name VARCHAR(64)) RETURNS bigint(20)
    DETERMINISTIC
BEGIN

UPDATE t_sequence SET current_value = current_value + increment WHERE NAME = seq_name;

RETURN currval(seq_name);

END
;;
DELIMITER ;
