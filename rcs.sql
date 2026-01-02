/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `rcs` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rcs`;

CREATE TABLE IF NOT EXISTS `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `form_submissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `data_json` text NOT NULL,
  `remarks` text,
  `status` enum('APPROVED','DRAFT','IN_REVIEW','REJECTED','SUBMITTED') NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  `template_id` bigint NOT NULL,
  `updated_by_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mc7ytaekyicivs9a7r7452xn` (`created_by_id`),
  KEY `FKvrm14d2r3vhp6gmipqst982k` (`customer_id`),
  KEY `FKgj3pemiqhgpaqb2jr47bjn0d3` (`template_id`),
  KEY `FK6c1sn5hkcjoifw867sr76p6w6` (`updated_by_id`),
  CONSTRAINT `FK1mc7ytaekyicivs9a7r7452xn` FOREIGN KEY (`created_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK6c1sn5hkcjoifw867sr76p6w6` FOREIGN KEY (`updated_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgj3pemiqhgpaqb2jr47bjn0d3` FOREIGN KEY (`template_id`) REFERENCES `form_templates` (`id`),
  CONSTRAINT `FKvrm14d2r3vhp6gmipqst982k` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `form_templates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `schema_json` text NOT NULL,
  `type` enum('ORDER','QUALIFICATION') DEFAULT NULL,
  `version` int NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8mu66o7cts0xu2nwh6xj2dava` (`type`,`version`),
  KEY `FK9n5d8w85jh4127bfdhl5a9xwy` (`created_by_id`),
  CONSTRAINT `FK9n5d8w85jh4127bfdhl5a9xwy` FOREIGN KEY (`created_by_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `submission_audits` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `new_status` enum('APPROVED','DRAFT','IN_REVIEW','REJECTED','SUBMITTED') DEFAULT NULL,
  `old_status` enum('APPROVED','DRAFT','IN_REVIEW','REJECTED','SUBMITTED') DEFAULT NULL,
  `remarks` text,
  `timestamp` datetime(6) NOT NULL,
  `actor_id` bigint NOT NULL,
  `submission_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6j49e23o2mcrukvsrrni48qkt` (`actor_id`),
  KEY `FKd2cf788i3ehnejj3w5ahtf1ge` (`submission_id`),
  CONSTRAINT `FK6j49e23o2mcrukvsrrni48qkt` FOREIGN KEY (`actor_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKd2cf788i3ehnejj3w5ahtf1ge` FOREIGN KEY (`submission_id`) REFERENCES `form_submissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `updated_by_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk8d0f2n7n88w1a16yhua64onx` (`user_name`),
  KEY `FK8nakkftyppd62ke6tv7oo5a92` (`created_by_id`),
  KEY `FKchxdoybbydcaj5smgxe0qq5mk` (`customer_id`),
  KEY `FK6nm9u1qpw9xxphk70xr614m7n` (`updated_by_id`),
  CONSTRAINT `FK6nm9u1qpw9xxphk70xr614m7n` FOREIGN KEY (`updated_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK8nakkftyppd62ke6tv7oo5a92` FOREIGN KEY (`created_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKchxdoybbydcaj5smgxe0qq5mk` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO roles(name) VALUES ('ROLE_CUSTOMER'), ('ROLE_TPM'), ('ROLE_SALES'), ('ROLE_ADMIN');