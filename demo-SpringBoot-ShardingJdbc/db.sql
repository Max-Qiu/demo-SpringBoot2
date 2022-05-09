
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sj_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sj_db`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_1` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
  `group_id` tinyint unsigned DEFAULT NULL COMMENT '组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user_1` VALUES (1522866999836454914,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_1` VALUES (1522866999903563778,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_1` VALUES (1522866999903563780,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_1` VALUES (1522866999970672642,'TOM',NULL,'xxx@xxx.com');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_2` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
  `group_id` tinyint unsigned DEFAULT NULL COMMENT '组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user_2` VALUES (1522866998133567489,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522866999836454913,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522866999903563779,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522866999903563781,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522866999970672641,'TOM',NULL,'xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522866999970672643,'TOM',NULL,'xxx@xxx.com');

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sj_db1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sj_db1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `city` VALUES (1522883012481159170,'江苏');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_1` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
  `group_id` tinyint unsigned NOT NULL COMMENT '组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user_1` VALUES (1522869888046731266,'TOM0',0,'0xxx@xxx.com');
INSERT INTO `user_1` VALUES (1522869889846087682,'TOM2',0,'2xxx@xxx.com');
INSERT INTO `user_1` VALUES (1522869889846087684,'TOM4',0,'4xxx@xxx.com');
INSERT INTO `user_1` VALUES (1522869889913196546,'TOM6',0,'6xxx@xxx.com');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_2` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
  `group_id` tinyint unsigned NOT NULL COMMENT '组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user_2` VALUES (1522869889976111105,'TOM8',0,'8xxx@xxx.com');

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sj_db2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sj_db2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `city` VALUES (1522883012481159170,'江苏');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_1` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
  `group_id` tinyint unsigned NOT NULL COMMENT '组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user_1` VALUES (1522869889976111106,'TOM9',1,'9xxx@xxx.com');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_2` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
  `group_id` tinyint unsigned NOT NULL COMMENT '组',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user_2` VALUES (1522869889846087681,'TOM1',1,'1xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522869889846087683,'TOM3',1,'3xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522869889913196545,'TOM5',1,'5xxx@xxx.com');
INSERT INTO `user_2` VALUES (1522869889913196547,'TOM7',1,'7xxx@xxx.com');

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sj_single` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sj_single`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `employee` VALUES (1522880054993555457,'TOM');
