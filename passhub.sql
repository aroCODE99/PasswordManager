-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: manage_pass
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `otp`
--

DROP TABLE IF EXISTS `otp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `otp` varchar(6) NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `otp_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otp`
--

LOCK TABLES `otp` WRITE;
/*!40000 ALTER TABLE `otp` DISABLE KEYS */;
INSERT INTO `otp` VALUES (55,7,'668184','2025-03-25 02:00:59','2025-03-25 01:55:59');
/*!40000 ALTER TABLE `otp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passwords`
--

DROP TABLE IF EXISTS `passwords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passwords` (
  `password_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `url` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `version` int DEFAULT NULL,
  `note` text,
  PRIMARY KEY (`password_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `passwords_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passwords`
--

LOCK TABLES `passwords` WRITE;
/*!40000 ALTER TABLE `passwords` DISABLE KEYS */;
INSERT INTO `passwords` VALUES (1,13,'https://developer.mozilla.org/','JhTp+Q9chmwycAzz4TZCj+YYs8k6jaz+Bnml1xVL/lI=','2025-03-24 17:44:20',9,'This is the YOutube Password and i am using this to test the password Hello'),(15,7,'https://www.youtube.com/','5Qt+pV8EfJRYIker+Qm9Ud8I1oVgsmee4DXauJcqiyM=','2025-03-20 18:05:46',4,'This is the Test youtube Note'),(16,7,'https://www.jbl.com/','AH1SKATRYfpc/QMXbk9ICA==','2025-03-19 20:26:48',1,'This is the test Note'),(24,15,'https://aro.com/','XFkhov146UbuHUwoVj/4S117xJ4EJumtmqGXwtiORF8=','2025-03-24 19:07:32',2,'Hello this is the main thing and i want to do this and gomti is so irritating and i don\'t like her at all.'),(25,7,'https://chatgpt.com/','66EC9aidw80GKR+H6wfGug==','2025-03-21 16:52:41',1,'Hello '),(33,7,'https://drive.uqu.edu.sa/','66EC9aidw80GKR+H6wfGug==','2025-03-21 16:55:35',0,'Hello'),(34,13,'https://www.digitalocean.com/','66EC9aidw80GKR+H6wfGug==','2025-03-22 17:21:30',0,'Hello'),(36,13,'https://www.google.com/','PxX2q51IbI6Bb62xRrZkBQ==','2025-03-24 18:04:36',1,'Hello'),(39,13,'https://www.amazon.es/','AKfHtmeJxTjdHC+iW3UjvQ==','2025-03-23 05:56:58',1,'Hello how are yall doing'),(40,13,'https://math.mit.edu/','66EC9aidw80GKR+H6wfGug==','2025-03-23 03:30:10',0,'Hello'),(41,13,'https://doc.rust-lang.org/','ebMN1Vn1dWC8cOf/J2Epjf82lJM7/vA8vvw8RolV+c0=','2025-03-23 17:09:40',2,'Hello this is the rust book'),(42,13,'https://wiki.libsdl.org/','0C0BfRFof381JtcopoSKsg==','2025-03-23 05:54:10',1,'This is the SDl'),(43,13,'https://os.phil-opp.com/','66EC9aidw80GKR+H6wfGug==','2025-03-23 16:42:31',0,'hello how are we doing this and i wanted do something with my life'),(44,13,'https://www.reddit.com/','6UQKUf4dwuIjQaE494+NzW76VokgPka4THUl/XwLMrI=','2025-03-24 18:32:48',4,'Hello this is the Reddit password manager'),(45,15,'https://www.reddit.com/','/KE0ZHbPytbru3t4NfFYog==','2025-03-24 20:05:22',1,'test this is teh reddit password'),(47,15,'https://www.instagram.com/','gNzeo60NySDZ3vEDov3pNQ==','2025-03-24 19:55:35',1,'This is  the instagram handler'),(48,15,'https://www.google.com/','66EC9aidw80GKR+H6wfGug==','2025-03-24 19:56:10',0,'This is the google password manager');
/*!40000 ALTER TABLE `passwords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,'USER',7),(2,'USER',8),(3,'USER',9),(4,'USER',13),(5,'USER',15),(6,'USER',16);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `version` int DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (7,'prathamesh.pagare789@gmail.com','$2a$12$Cm6bm/bn.KsbMEzm9w9ucO9KvfeUnBDN/esUD5tohFFNahxzCoVi.',0),(8,'shubham@gmail.com','$2a$12$CadNlgST3CnYmqbveVms3.UFCtpoHBw9NH8TebhkKrsviQGnrH24G',0),(9,'gomti@gmail.com','$2a$12$FBcs5E2hFrYcAofeFJpSIuVvj4AKGtieeM2s3kCVdjbEtf9NgGN.O',0),(13,'test@gmail.com','$2a$12$YrzyQdhCjny6Dq7z.EKsguZUIjlvN8yK9AwIo30rMN27i4iWL5seC',0),(15,'gautamip6@gmail.com','$2a$12$b/PqXgIykMyzx/JPpndmEOjdJ6eiMhsvQ5nWm934fSdcMZWtKzagG',0),(16,'gautami36@gmail.com','$2a$12$5vBbD2jlYZT6Cl6aano6iuQ1q178NNcF/V/sKgfLMuk4.R58KWtSC',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-25  2:59:47
