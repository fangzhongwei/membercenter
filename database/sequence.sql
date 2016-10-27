CREATE TABLE IF NOT EXISTS `t_sequence` (
  `name` varchar(64) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  UNIQUE KEY `uq_ts_n` (`name`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='sequence table';

INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('member_id_0', 0, 3);
INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('member_id_1', 1, 3);
INSERT INTO `t_sequence` (`name`, `current_value`, `increment`) VALUES  ('member_id_2', 2, 3);

DROP FUNCTION IF EXISTS `currval`;

DELIMITER //

CREATE  FUNCTION `currval`(seq_name VARCHAR(64)) RETURNS int(11)

    READS SQL DATA

    DETERMINISTIC

BEGIN

DECLARE VALUE INTEGER;

SET VALUE = 0;

SELECT current_value INTO VALUE FROM t_sequence WHERE NAME = seq_name;

RETURN VALUE;

END//

DELIMITER ;


DROP FUNCTION IF EXISTS `nextval`;

DELIMITER //

CREATE  FUNCTION `nextval`(seq_name VARCHAR(64)) RETURNS int(11)

    DETERMINISTIC

BEGIN

UPDATE t_sequence SET current_value = current_value + increment WHERE NAME = seq_name;

RETURN currval(seq_name);

END//

DELIMITER ;