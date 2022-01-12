
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `demo_transactional` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `demo_transactional`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_wallet` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_id` bigint unsigned NOT NULL COMMENT '流水ID',
  `wallet_id` bigint unsigned NOT NULL COMMENT '钱包ID',
  `type` tinyint NOT NULL COMMENT '交易类型 1收入 2支出',
  `money` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='钱包日志';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_flow` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `from_wallet_id` bigint unsigned NOT NULL COMMENT '支出账户ID',
  `to_wallet_id` bigint unsigned NOT NULL COMMENT '收入账户ID',
  `money` decimal(10,2) NOT NULL COMMENT '交易金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交易流水';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `money` decimal(10,2) NOT NULL COMMENT '金额',
  `lock` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否锁定 0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE COMMENT '用户ID唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='钱包账户';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `wallet` VALUES (1,1,100.00,0,'2022-01-12 21:23:35','2022-01-12 22:32:37');
INSERT INTO `wallet` VALUES (2,2,100.00,0,'2022-01-12 21:23:40','2022-01-12 22:32:41');
