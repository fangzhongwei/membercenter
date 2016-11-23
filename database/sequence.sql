CREATE TABLE IF NOT EXISTS `t_sequence` (
  `name` varchar(64) NOT NULL,
  `current_value` BIGINT(20) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  UNIQUE KEY `uq_ts_n` (`name`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='sequence table';

INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('session_id_0', 0, 5);
INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('session_id_1', 1, 5);
INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('session_id_2', 2, 5);
INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('session_id_3', 3, 5);
INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('session_id_4', 4, 5);

DROP FUNCTION IF EXISTS `currval`;

DELIMITER //

CREATE  FUNCTION `currval`(seq_name VARCHAR(64)) RETURNS BIGINT(20)

    READS SQL DATA

    DETERMINISTIC

BEGIN

DECLARE VALUE BIGINT;

SET VALUE = 0;

SELECT current_value INTO VALUE FROM t_sequence WHERE NAME = seq_name;

RETURN VALUE;

END//

DELIMITER ;


DROP FUNCTION IF EXISTS `nextval`;

DELIMITER //

CREATE  FUNCTION `nextval`(seq_name VARCHAR(64)) RETURNS BIGINT(20)

    DETERMINISTIC

BEGIN

UPDATE t_sequence SET current_value = current_value + increment WHERE NAME = seq_name;

RETURN currval(seq_name);

END//

DELIMITER ;