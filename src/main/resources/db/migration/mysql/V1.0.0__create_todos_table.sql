DROP TABLE IF EXISTS `todos`;
CREATE TABLE `todos` (
  `id`        INT AUTO_INCREMENT,
  `title`     VARCHAR(255) DEFAULT NULL,
  `completed` BIT DEFAULT FALSE,
  PRIMARY KEY (`id`),
  INDEX (`title`)
);