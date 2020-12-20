-- MySQL dump 10.13  Distrib 5.7.32, for Win64 (x86_64)
--
-- Host: localhost    Database: smp
-- ------------------------------------------------------
-- Server version	5.7.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `smp_auto_fill`
--

DROP TABLE IF EXISTS `smp_auto_fill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_auto_fill` (
  `id` bigint(11) unsigned NOT NULL COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '测试字段',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自动插入';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_auto_fill`
--

LOCK TABLES `smp_auto_fill` WRITE;
/*!40000 ALTER TABLE `smp_auto_fill` DISABLE KEYS */;
/*!40000 ALTER TABLE `smp_auto_fill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_classes`
--

DROP TABLE IF EXISTS `smp_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_classes` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '班级名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='班级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_classes`
--

LOCK TABLES `smp_classes` WRITE;
/*!40000 ALTER TABLE `smp_classes` DISABLE KEYS */;
INSERT INTO `smp_classes` VALUES (1,'一班','2020-12-18 23:17:05'),(2,'二班','2020-12-18 23:17:09'),(3,'三班','2020-12-18 23:17:15');
/*!40000 ALTER TABLE `smp_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_logic_delete`
--

DROP TABLE IF EXISTS `smp_logic_delete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_logic_delete` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) DEFAULT NULL COMMENT '名称',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='逻辑删除';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_logic_delete`
--

LOCK TABLES `smp_logic_delete` WRITE;
/*!40000 ALTER TABLE `smp_logic_delete` DISABLE KEYS */;
INSERT INTO `smp_logic_delete` VALUES (1,'张三',_binary '\0'),(2,'李四',_binary ''),(3,'王五',_binary '\0'),(4,'赵六',_binary '\0');
/*!40000 ALTER TABLE `smp_logic_delete` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_optimistic_locker`
--

DROP TABLE IF EXISTS `smp_optimistic_locker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_optimistic_locker` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '测试字段',
  `version` bigint(255) unsigned NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='乐观锁';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_optimistic_locker`
--

LOCK TABLES `smp_optimistic_locker` WRITE;
/*!40000 ALTER TABLE `smp_optimistic_locker` DISABLE KEYS */;
INSERT INTO `smp_optimistic_locker` VALUES (1,'max',3);
/*!40000 ALTER TABLE `smp_optimistic_locker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_student`
--

DROP TABLE IF EXISTS `smp_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_student` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '学生姓名',
  `classes_id` bigint(20) unsigned NOT NULL COMMENT '班级id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='学生表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_student`
--

LOCK TABLES `smp_student` WRITE;
/*!40000 ALTER TABLE `smp_student` DISABLE KEYS */;
INSERT INTO `smp_student` VALUES (1,'张三',1,'2020-12-18 23:17:22'),(2,'李四',1,'2020-12-18 23:17:27'),(3,'王五',1,'2020-12-18 23:17:34'),(4,'赵六',1,'2020-12-18 23:17:44'),(5,'Tom',2,'2020-12-18 23:17:50'),(6,'Jerry',2,'2020-12-18 23:18:00'),(7,'Amy',2,'2020-12-18 23:18:06'),(8,'Max',2,'2020-12-18 23:18:17'),(9,'李白',3,'2020-12-18 23:18:33'),(10,'杜甫',3,'2020-12-18 23:18:37'),(11,'王维',3,'2020-12-18 23:18:44');
/*!40000 ALTER TABLE `smp_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_test_enum`
--

DROP TABLE IF EXISTS `smp_test_enum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_test_enum` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `gender` tinyint(4) NOT NULL COMMENT '性别',
  `state` tinyint(4) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='枚举';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_test_enum`
--

LOCK TABLES `smp_test_enum` WRITE;
/*!40000 ALTER TABLE `smp_test_enum` DISABLE KEYS */;
/*!40000 ALTER TABLE `smp_test_enum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_test_exclude`
--

DROP TABLE IF EXISTS `smp_test_exclude`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_test_exclude` (
  `test_id` varchar(255) NOT NULL COMMENT '测试字段'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='被排除的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_test_exclude`
--

LOCK TABLES `smp_test_exclude` WRITE;
/*!40000 ALTER TABLE `smp_test_exclude` DISABLE KEYS */;
INSERT INTO `smp_test_exclude` VALUES ('测试表，用于测试表排除');
/*!40000 ALTER TABLE `smp_test_exclude` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smp_user`
--

DROP TABLE IF EXISTS `smp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smp_user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `username` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` tinyint(4) unsigned DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smp_user`
--

LOCK TABLES `smp_user` WRITE;
/*!40000 ALTER TABLE `smp_user` DISABLE KEYS */;
INSERT INTO `smp_user` VALUES (1336695272901832706,'Jack',20,'test1@baomidou.com'),(1336695306078683137,'Tom',28,'test2@baomidou.com'),(1336697967133978625,'Sandy',24,'test3@baomidou.com'),(1336817864509751297,'Billie',30,'test4@baomidou.com');
/*!40000 ALTER TABLE `smp_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-20 15:11:41
