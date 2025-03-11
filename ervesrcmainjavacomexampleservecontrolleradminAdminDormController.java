warning: in the working copy of 'serve/src/main/java/com/example/serve/mapper/ArriveMapper.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/java/com/example/serve/mapper/ClassMapper.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/java/com/example/serve/mapper/StudentDormMapper.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/java/com/example/serve/pojo/Arrive.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/java/com/example/serve/service/ArriveService.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/java/com/example/serve/service/ClassService.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/java/com/example/serve/service/impl/ClassServiceImpl.java', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'serve/src/main/resources/application.yml', LF will be replaced by CRLF the next time Git touches it
warning: in the working copy of 'web/yarn.lock', LF will be replaced by CRLF the next time Git touches it
[1mdiff --git a/bs.sql b/bs.sql[m
[1mindex 3d23219..80f9314 100644[m
[1m--- a/bs.sql[m
[1m+++ b/bs.sql[m
[36m@@ -11,7 +11,7 @@[m
  Target Server Version : 80032 (8.0.32)[m
  File Encoding         : 65001[m
 [m
[31m- Date: 06/03/2025 21:14:01[m
[32m+[m[32m Date: 08/03/2025 08:39:16[m
 */[m
 [m
 SET NAMES utf8mb4;[m
[36m@@ -187,7 +187,7 @@[m [mINSERT INTO `major` VALUES ('游戏系', '102', '动画', 666);[m
 -- ----------------------------[m
 DROP TABLE IF EXISTS `pay`;[m
 CREATE TABLE `pay`  ([m
[31m-  `studentNumber` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '学号',[m
[32m+[m[32m  `studentNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',[m
   `studentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',[m
   `hallway` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缴费通道',[m
   `amountcard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缴费编号',[m
[36m@@ -200,57 +200,7 @@[m [mCREATE TABLE `pay`  ([m
 -- ----------------------------[m
 -- Records of pay[m
 -- ----------------------------[m
[31m-INSERT INTO `pay` VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);[m
[31m-[m
[31m--- ----------------------------[m
[31m--- Table structure for payment_item[m
[31m--- ----------------------------[m
[31m-DROP TABLE IF EXISTS `payment_item`;[m
[31m-CREATE TABLE `payment_item`  ([m
[31m-  `id` int NOT NULL AUTO_INCREMENT,[m
[31m-  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',[m
[31m-  `amount` decimal(10, 2) NOT NULL COMMENT '缴费金额',[m
[31m-  `deadline` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '截止日期',[m
[31m-  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目描述',[m
[31m-  `type` int NULL DEFAULT 0 COMMENT '项目类型（0-学费，1-住宿费，2-其他）',[m
[31m-  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '适用年级',[m
[31m-  `major` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '适用专业，为空表示所有专业',[m
[31m-  PRIMARY KEY (`id`) USING BTREE[m
[31m-) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '缴费项目表' ROW_FORMAT = DYNAMIC;[m
[31m-[m
[31m--- ----------------------------[m
[31m--- Records of payment_item[m
[31m--- ----------------------------[m
[31m-INSERT INTO `payment_item` VALUES (1, '2024年秋季学费', 4800.00, '2024-09-01', '2024年秋季学期学费', 0, '2024', NULL);[m
[31m-INSERT INTO `payment_item` VALUES (2, '2024年秋季住宿费', 1200.00, '2024-09-01', '2024年秋季学期住宿费', 1, '2024', NULL);[m
[31m-INSERT INTO `payment_item` VALUES (3, '计算机专业实验费', 500.00, '2024-09-15', '计算机专业实验室使用费', 2, '2024', '计算机科学与技术');[m
[31m-INSERT INTO `payment_item` VALUES (4, '软件工程专业实验费', 600.00, '2024-09-15', '软件工程专业实验室使用费', 2, '2024', '软件工程');[m
[31m-[m
[31m--- ----------------------------[m
[31m--- Table structure for payment_record[m
[31m--- ----------------------------[m
[31m-DROP TABLE IF EXISTS `payment_record`;[m
[31m-CREATE TABLE `payment_record`  ([m
[31m-  `id` int NOT NULL AUTO_INCREMENT,[m
[31m-  `student_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',[m
[31m-  `payment_item_id` int NOT NULL COMMENT '缴费项目ID',[m
[31m-  `amount` decimal(10, 2) NOT NULL COMMENT '缴费金额',[m
[31m-  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式（微信支付、支付宝、银行卡）',[m
[31m-  `payment_date` datetime NULL DEFAULT NULL COMMENT '支付时间',[m
[31m-  `status` int NULL DEFAULT 0 COMMENT '支付状态（0-未支付，1-已支付）',[m
[31m-  `transaction_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易流水号',[m
[31m-  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',[m
[31m-  PRIMARY KEY (`id`) USING BTREE,[m
[31m-  INDEX `payment_item_id`(`payment_item_id` ASC) USING BTREE,[m
[31m-  CONSTRAINT `payment_record_ibfk_1` FOREIGN KEY (`payment_item_id`) REFERENCES `payment_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT[m
[31m-) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '缴费记录表' ROW_FORMAT = DYNAMIC;[m
[31m-[m
[31m--- ----------------------------[m
[31m--- Records of payment_record[m
[31m--- ----------------------------[m
[31m-INSERT INTO `payment_record` VALUES (1, '2024001', 1, 4800.00, '微信支付', '2024-02-15 10:30:00', 1, 'PAY2024021510300012345678', '学费已缴纳');[m
[31m-INSERT INTO `payment_record` VALUES (2, '2024001', 2, 1200.00, '微信支付', '2024-02-15 10:35:00', 1, 'PAY2024021510350087654321', '住宿费已缴纳');[m
[31m-INSERT INTO `payment_record` VALUES (3, '2024002', 1, 4800.00, '支付宝', '2024-02-20 14:20:00', 1, 'PAY2024022014200056781234', '学费已缴纳');[m
[32m+[m[32mINSERT INTO `pay` VALUES ('214073001', '钱梦娇', '直通车', NULL, NULL, NULL, NULL, NULL);[m
 [m
 -- ----------------------------[m
 -- Table structure for resume[m
[36m@@ -270,7 +220,6 @@[m [mCREATE TABLE `resume`  ([m
 -- Records of resume[m
 -- ----------------------------[m
 INSERT INTO `resume` VALUES (1, '214073001', '2019年9月', '2022年6月', '广州市第一中学', '学生');[m
[31m-INSERT INTO `resume` VALUES (2, '214073001', '2022年9月', '2025年6月', '计算机科学与技术学院', '本科生');[m
 INSERT INTO `resume` VALUES (3, '214073002', '2018年9月', '2021年6月', '深圳市实验中学', '学生');[m
 INSERT INTO `resume` VALUES (4, '214073002', '2021年9月', '2024年6月', '软件工程学院', '本科生');[m
 INSERT INTO `resume` VALUES (5, '214073003', '2017年9月', '2020年6月', '上海市实验中学', '学生');[m
[36m@@ -319,16 +268,16 @@[m [mCREATE TABLE `student`  ([m
 -- ----------------------------[m
 -- Records of student[m
 -- ----------------------------[m
[31m-INSERT INTO `student` VALUES (1, '4567891001', '214073001', '钱梦娇', '/uploads/student-photos/669463fa-a0b8-40cf-9443-80230f6befc4.jpg', '男', '2006-09-10', '广东省中山市中山市', NULL, '身份证', '442000200609103879', NULL, '528400 ', '15255715527', NULL, '计