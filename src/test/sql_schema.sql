create database wenda;
use wenda;
-- 另外创建user用户用于登录；use/pass
GRANT ALL ON *.* TO user@'%' IDENTIFIED BY 'pass' WITH GRANT OPTION;

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT NULL,
  `user_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `comment_count` INT NOT NULL,
  `status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `date_index` (`created_date` ASC)
  )ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `user`;
  CREATE TABLE `user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL DEFAULT '',
    `password` varchar(128) NOT NULL DEFAULT '',
    `salt` varchar(32) NOT NULL DEFAULT '',
    `head_url` varchar(256) NOT NULL DEFAULT '',
    `status` int NULL DEFAULT 0,
    `pri_lv` int NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


  DROP TABLE IF EXISTS `login_ticket`;
  CREATE TABLE `login_ticket` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `ticket` VARCHAR(45) NOT NULL,
    `expired` DATETIME NOT NULL,
    `status` INT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ticket_UNIQUE` (`ticket` ASC)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


  DROP TABLE IF EXISTS `comment`;
  CREATE TABLE `comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT NOT NULL,
  `user_id` INT NOT NULL,
  `entity_id` INT NOT NULL,
  `entity_type` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  DROP TABLE IF EXISTS `message`;
  CREATE TABLE `message` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `from_id` INT NULL,
    `to_id` INT NULL,
    `content` TEXT NULL,
    `created_date` DATETIME NULL,
    `has_read` INT NULL,
    `conversation_id` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `conversation_index` (`conversation_id` ASC),
    INDEX `created_date` (`created_date` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

  DROP TABLE IF EXISTS `report`;
  CREATE TABLE `report` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
	`reason` TEXT NOT NULL,
  `entity_id` INT NOT NULL,
  `entity_type` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
	`deal_date` DATETIME ,
  `deal_status` INT NOT NULL DEFAULT 0,
	`dealer_id` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*修改数据库设置 root root账号操作*/
set GLOBAL sql_mode ='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION ';