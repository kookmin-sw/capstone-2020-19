DROP DATABASE IF EXISTS silver_watch;
CREATE DATABASE silver_watch;

use silver_watch;
DROP TABLE IF EXISTS watch_battery;
DROP TABLE IF EXISTS watch_gps;
DROP TABLE IF EXISTS watch_wear;
DROP TABLE IF EXISTS watch_user;

CREATE TABLE `watch_user` (
  `watch_id` varchar(100) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`watch_id`),
  UNIQUE KEY `watch_id_UNIQUE` (`watch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `watch_gps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `watch_id` varchar(100) NOT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `watch_id_idx` (`watch_id`),
  CONSTRAINT `watch_id_gps` FOREIGN KEY (`watch_id`) REFERENCES `watch_user` (`watch_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `watch_battery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `watch_id` varchar(100) NOT NULL,
  `watch_battery` varchar(45) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `watch_id_UNIQUE` (`watch_id`),
  KEY `watch_id_battery_idx` (`watch_id`),
  CONSTRAINT `watch_id_battery` FOREIGN KEY (`watch_id`) REFERENCES `watch_user` (`watch_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `watch_wear` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `watch_id` varchar(100) NOT NULL,
  `wear` tinyint(1) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `watch_id_UNIQUE` (`watch_id`),
  KEY `watch_id_idx` (`watch_id`),
  CONSTRAINT `watch_id_wear` FOREIGN KEY (`watch_id`) REFERENCES `watch_user` (`watch_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

