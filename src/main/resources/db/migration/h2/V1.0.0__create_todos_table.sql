DROP TABLE IF EXISTS `todos`;
CREATE TABLE `todos` (
  `id`        INT AUTO_INCREMENT,
  `title`     VARCHAR(255) DEFAULT NULL,
  `completed` BIT DEFAULT FALSE,
  CONSTRAINT pk_todos PRIMARY KEY (`id`)
);

CREATE INDEX idx_todos_title ON `todos` (`title`);