/*
 Navicat Premium Dump SQL

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : bs

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 09/03/2025 21:37:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for arrive
-- ----------------------------
DROP TABLE IF EXISTS `arrive`;
CREATE TABLE `arrive`  (
  `studentNumber` int NULL DEFAULT NULL,
  `studentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tool` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `familynum` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of arrive
-- ----------------------------
INSERT INTO `arrive` VALUES (214073002, '李明', '10:15', '2023-09-01', '飞机', 2);
INSERT INTO `arrive` VALUES (214073003, '张华', '16:45', '2023-09-01', '汽车', 0);
INSERT INTO `arrive` VALUES (214073004, '王芳', '09:20', '2023-09-02', '火车', 1);
INSERT INTO `arrive` VALUES (214073005, '刘洋', '11:30', '2023-09-02', '飞机', 2);
INSERT INTO `arrive` VALUES (214073006, '赵静', '15:00', '2023-09-02', '汽车', 1);
INSERT INTO `arrive` VALUES (214073007, '陈明', '08:45', '2023-09-03', '火车', 0);
INSERT INTO `arrive` VALUES (214073008, '周婷', '13:20', '2023-09-03', '飞机', 2);
INSERT INTO `arrive` VALUES (214073009, '杨帆', '17:30', '2023-09-03', '汽车', 1);
INSERT INTO `arrive` VALUES (214073010, '吴涛', '10:45', '2023-09-04', '火车', 0);
INSERT INTO `arrive` VALUES (214073001, '钱梦娇', '10:45', '2025-02-26', '高铁', 1);

-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS `classroom`;
CREATE TABLE `classroom`  (
  `majorid` int NOT NULL COMMENT '专业号',
  `classroom` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级',
  `fteacherid` int NULL DEFAULT NULL COMMENT '辅导员',
  `steacherid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学习导师',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系别',
  `classroomNum` int NULL DEFAULT NULL COMMENT '班级人数',
  PRIMARY KEY (`majorid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO `classroom` VALUES (101, '班7', 1107, '1115', '计算机系', 0);
INSERT INTO `classroom` VALUES (102, '班8', 1108, '1116', '计算机系', 0);
INSERT INTO `classroom` VALUES (201, '班3', 1103, '1111', '电子系', 0);
INSERT INTO `classroom` VALUES (202, '班4', 1104, '1112', '电子系', 0);
INSERT INTO `classroom` VALUES (301, '班5', 1105, '1113', '数码媒体系', 0);
INSERT INTO `classroom` VALUES (302, '班6', 1106, '1114', '数码媒体系', 0);
INSERT INTO `classroom` VALUES (401, '班1', 1101, '1109', '游戏系', 0);
INSERT INTO `classroom` VALUES (402, '班2', 1102, '1110', '游戏系', 0);

-- ----------------------------
-- Table structure for dorm
-- ----------------------------
DROP TABLE IF EXISTS `dorm`;
CREATE TABLE `dorm`  (
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系别',
  `dormType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍类型\r\n',
  `dormitory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍楼',
  `dormCard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `dormNum` int NULL DEFAULT NULL COMMENT '宿舍数量',
  `dormPrize` int NULL DEFAULT NULL COMMENT '宿舍价格',
  `dormsex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '住宿性别'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm
-- ----------------------------
INSERT INTO `dorm` VALUES ('计算机系', '四人间', '宿舍楼1', '101', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('计算机系', '四人间', '宿舍楼1', '102', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('计算机系', '四人间', '宿舍楼1', '103', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('计算机系', '四人间', '宿舍楼2', '201', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('计算机系', '四人间', '宿舍楼2', '202', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('计算机系', '六人间', '宿舍楼3', '301', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('计算机系', '六人间', '宿舍楼3', '302', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('计算机系', '六人间', '宿舍楼3', '303', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('计算机系', '六人间', '宿舍楼4', '401', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('计算机系', '六人间', '宿舍楼4', '402', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('电子系', '四人间', '宿舍楼1', '104', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('电子系', '四人间', '宿舍楼1', '105', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('电子系', '四人间', '宿舍楼1', '106', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('电子系', '四人间', '宿舍楼1', '107', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('电子系', '四人间', '宿舍楼2', '204', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('电子系', '六人间', '宿舍楼3', '304', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('电子系', '六人间', '宿舍楼3', '305', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('电子系', '六人间', '宿舍楼3', '306', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('电子系', '六人间', '宿舍楼3', '307', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('电子系', '六人间', '宿舍楼4', '403', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('游戏系', '四人间', '宿舍楼1', '108', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('游戏系', '四人间', '宿舍楼1', '109', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('游戏系', '四人间', '宿舍楼2', '205', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('游戏系', '四人间', '宿舍楼2', '206', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('游戏系', '四人间', '宿舍楼2', '207', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('游戏系', '六人间', '宿舍楼3', '308', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('游戏系', '六人间', '宿舍楼3', '309', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('游戏系', '六人间', '宿舍楼4', '404', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('游戏系', '六人间', '宿舍楼4', '405', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('游戏系', '六人间', '宿舍楼4', '406', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '四人间', '宿舍楼1', '110', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '四人间', '宿舍楼1', '111', 4, 3500, '男');
INSERT INTO `dorm` VALUES ('数码媒体系', '四人间', '宿舍楼2', '208', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '四人间', '宿舍楼2', '209', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '四人间', '宿舍楼2', '210', 4, 3500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '六人间', '宿舍楼3', '310', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('数码媒体系', '六人间', '宿舍楼3', '311', 6, 1500, '男');
INSERT INTO `dorm` VALUES ('数码媒体系', '六人间', '宿舍楼4', '112', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '六人间', '宿舍楼4', '113', 6, 1500, '女');
INSERT INTO `dorm` VALUES ('数码媒体系', '六人间', '宿舍楼4', '114', 6, 1500, '女');

-- ----------------------------
-- Table structure for family
-- ----------------------------
DROP TABLE IF EXISTS `family`;
CREATE TABLE `family`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '序号',
  `studentNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号\r\n',
  `familyPart` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '称谓\r\n',
  `familyName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名\r\n',
  `familyAge` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年龄\r\n',
  `familyPoliteAspect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政治面貌\r\n',
  `familyOrganization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位学习、工作\r\n',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of family
-- ----------------------------
INSERT INTO `family` VALUES (9, '214073001', '父亲', '张伟', '48', '中共党员', '广州市某企业');
INSERT INTO `family` VALUES (10, '214073001', '母亲', '李芳', '45', '群众', '广州市某学校');
INSERT INTO `family` VALUES (11, '214073002', '父亲', '王强', '50', '中共党员', '深圳市某政府部门');
INSERT INTO `family` VALUES (12, '214073002', '母亲', '赵敏', '48', '中共党员', '深圳市某医院');
INSERT INTO `family` VALUES (13, '214073002', '弟弟', '王小明', '16', '共青团员', '深圳市某中学');
INSERT INTO `family` VALUES (14, '214073003', '父亲', '刘军', '52', '中共党员', '上海市某企业');
INSERT INTO `family` VALUES (15, '214073003', '母亲', '张丽', '50', '群众', '上海市某事业单位');
INSERT INTO `family` VALUES (16, '214073004', '父亲', '陈明', '55', '民主人士', '北京市某大学');
INSERT INTO `family` VALUES (17, '214073004', '母亲', '王芳', '53', '中共党员', '北京市某研究所');
INSERT INTO `family` VALUES (18, '214073005', '父亲', '黄建国', '49', '中共党员', '武汉市某企业');
INSERT INTO `family` VALUES (19, '214073005', '母亲', '张燕', '47', '群众', '武汉市某学校');
INSERT INTO `family` VALUES (20, '214073005', '妹妹', '黄小红', '15', '共青团员', '武汉市某中学');

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系别',
  `majorid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业号',
  `majorname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业名',
  `departmentPrize` int NULL DEFAULT NULL COMMENT '学费'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES ('计算机系', '401', '计算机科学与技术', 500);
INSERT INTO `major` VALUES ('计算机系', '402', '物联网工程', 500);
INSERT INTO `major` VALUES ('电子系', '201', '电子信息工程', 300);
INSERT INTO `major` VALUES ('电子系', '202', '自动化', 600);
INSERT INTO `major` VALUES ('数码媒体系', '301', '风景园林', 500);
INSERT INTO `major` VALUES ('数码媒体系', '302', '数字媒体艺术', 580);
INSERT INTO `major` VALUES ('游戏系', '101', '网络与新媒体', 870);
INSERT INTO `major` VALUES ('游戏系', '102', '动画', 666);

-- ----------------------------
-- Table structure for pay
-- ----------------------------
DROP TABLE IF EXISTS `pay`;
CREATE TABLE `pay`  (
  `studentNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `studentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `hallway` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缴费通道',
  `amountcard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缴费编号',
  `allmoney` int NULL DEFAULT NULL COMMENT '总金额',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费凭证图片',
  `indentStatue` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay
-- ----------------------------
INSERT INTO `pay` VALUES ('214073001', '钱梦娇', '直行通道', 'PAY202503090357593001', 800, 'D:/Users/Merri/Desktop/graduate/uploads/student-photos/receipts/receipt_214073001_1741464025898.jpg', '已缴费', NULL);

-- ----------------------------
-- Table structure for resume
-- ----------------------------
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `studentNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号\r\n',
  `startTime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始时间\r\n',
  `endTime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结束时间\r\n',
  `organization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位学习、工作\r\n',
  `hats` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职务\r\n',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of resume
-- ----------------------------
INSERT INTO `resume` VALUES (1, '214073001', '2019年9月', '2022年6月', '广州市第一中学', '学生');
INSERT INTO `resume` VALUES (3, '214073002', '2018年9月', '2021年6月', '深圳市实验中学', '学生');
INSERT INTO `resume` VALUES (4, '214073002', '2021年9月', '2024年6月', '软件工程学院', '本科生');
INSERT INTO `resume` VALUES (5, '214073003', '2017年9月', '2020年6月', '上海市实验中学', '学生');
INSERT INTO `resume` VALUES (6, '214073003', '2020年9月', '2023年6月', '信息工程学院', '本科生');
INSERT INTO `resume` VALUES (7, '214073003', '2023年7月', '2023年8月', '字节跳动', '实习生');
INSERT INTO `resume` VALUES (8, '214073004', '2019年9月', '2022年6月', '北京市第四中学', '学生');
INSERT INTO `resume` VALUES (9, '214073004', '2022年9月', '2025年6月', '人工智能学院', '本科生');
INSERT INTO `resume` VALUES (10, '214073005', '2018年9月', '2021年6月', '武汉市第二中学', '学生');
INSERT INTO `resume` VALUES (11, '214073005', '2021年9月', '2024年6月', '数据科学与大数据技术专业', '本科生');
INSERT INTO `resume` VALUES (12, '214073005', '2023年12月', '2024年2月', '腾讯', '实习生');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int NOT NULL,
  `admissionTicket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '准考证\r\n',
  `studentNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号\r\n',
  `studentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名\r\n',
  `studentPicture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学生照片\r\n',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别\r\n',
  `birth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出生年月\r\n',
  `hometown` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '籍贯\r\n',
  `nation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '民族\r\n',
  `idType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '证件\r\n',
  `idCard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号码\r\n',
  `politeAspect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政治面貌\r\n',
  `zipCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码\r\n',
  `studentPhone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '\r\n电话\r\n',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细通讯地址\r\n',
  `majorname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业名\r\n',
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级\r\n',
  `level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '层次\r\n',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮件\r\n',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系别\r\n',
  `selectDorm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '心仪宿舍间',
  `arrive_status` int NULL DEFAULT NULL COMMENT '抵校状态',
  `payment_status` int NULL DEFAULT NULL COMMENT '缴费状态',
  `dormitory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍楼',
  `dorm_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `classroom_id` int NULL DEFAULT NULL COMMENT '班级ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '4567891001', '214073001', '钱梦娇', '/uploads/student-photos/669463fa-a0b8-40cf-9443-80230f6befc4.jpg', '男', '2006-09-10', '广东省中山市中山市', NULL, '身份证', '442000200609103879', NULL, '528400 ', '15255715527', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', '四人间', NULL, 1, NULL, NULL, 401);
INSERT INTO `student` VALUES (2, '4567891002', '214073002', '侯招弟', NULL, '女', '2006-04-05', '广东省中山市中山市', NULL, '身份证', '442000200604059581', NULL, '528400 ', '13707533319', NULL, '计算机科学与技术', '2021级', '本科', '', '计算机系', '四人间', NULL, 0, '宿舍楼1', '110', 101);
INSERT INTO `student` VALUES (3, '4567891003', '214073003', '西门龙婷', NULL, '女', '2006-04-14', '广东省中山市中山市', NULL, '身份证', '442000200604140443', NULL, '528400 ', '13354924193', NULL, '计算机科学与技术', '2021级', '本科', '', '计算机系', NULL, NULL, 1, '宿舍楼1', '110', NULL);
INSERT INTO `student` VALUES (4, '4567891004', '214073004', '曹黄萍', NULL, '女', '2006-11-10', '广东省中山市中山市', NULL, '身份证', '442000200611103800', NULL, '528400 ', '15065724113', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO `student` VALUES (5, '4567891005', '214073005', '尤岚', NULL, '女', '2006-11-18', '广东省中山市中山市', NULL, '身份证', '442000200611188445', NULL, '528400 ', '15589739287', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 0, '宿舍楼1', '110', NULL);
INSERT INTO `student` VALUES (6, '4567891006', '214073006', '郑子鑫', NULL, '女', '2006-09-17', '广东省中山市中山市', NULL, '身份证', '442000200609176306', NULL, '528400 ', '15207468250', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 0, '宿舍楼1', '110', NULL);
INSERT INTO `student` VALUES (7, '4567891007', '214073007', '尤有菊', NULL, '女', '2006-01-15', '广东省中山市中山市', NULL, '身份证', '442000200601158322', NULL, '528400 ', '13923404505', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 0, '宿舍楼2', '202', NULL);
INSERT INTO `student` VALUES (8, '4567891008', '214073008', '褚燕', NULL, '男', '2006-03-25', '广东省中山市中山市', NULL, '身份证', '442000200603252099', NULL, '528400 ', '13923263024', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO `student` VALUES (9, '4567891009', '214073009', '韩睿敏', NULL, '男', '2006-08-23', '广东省中山市中山市', NULL, '身份证', '44200020060823915X', NULL, '528400 ', '18868557032', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO `student` VALUES (10, '4567891010', '214073010', '郑如霜', NULL, '女', '2006-11-11', '广东省中山市中山市', NULL, '身份证', '442000200611117989', NULL, '528400 ', '13895038053', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `student` VALUES (11, '4567891011', '214073011', '施聪', NULL, '女', '2006-07-12', '广东省中山市中山市', NULL, '身份证', '442000200607120923', NULL, '528400 ', '13623337048', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (12, '4567891012', '214073012', '施思', NULL, '男', '2006-07-14', '广东省中山市中山市', NULL, '身份证', '44200020060714399X', NULL, '528400 ', '13991065942', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (13, '4567891013', '214073013', '孔传敏', NULL, '女', '2006-10-26', '广东省中山市中山市', NULL, '身份证', '442000200610267707', NULL, '528400 ', '13997521101', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (14, '4567891014', '214073014', '魏宁', NULL, '男', '2006-10-22', '广东省中山市中山市', NULL, '身份证', '442000200610229794', NULL, '528400 ', '13172119822', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (15, '4567891015', '214073015', '范惠', NULL, '女', '2006-11-21', '广东省中山市中山市', NULL, '身份证', '442000200611219686', NULL, '528400 ', '15313155406', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (16, '4567891016', '214073016', '陈海莲', NULL, '女', '2006-10-25', '广东省中山市中山市', NULL, '身份证', '44200020061025878X', NULL, '528400 ', '13325022110', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (17, '4567891017', '214073017', '韩琴', NULL, '女', '2006-05-13', '广东省中山市中山市', NULL, '身份证', '442000200605132920', NULL, '528400 ', '13287395212', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (18, '4567891018', '214073018', '蒋丽丽', NULL, '男', '2006-02-10', '广东省中山市中山市', NULL, '身份证', '442000200602106735', NULL, '528400 ', '13701701945', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (19, '4567891019', '214073019', '吴程燕', NULL, '女', '2006-08-08', '广东省中山市中山市', NULL, '身份证', '442000200608080222', NULL, '528400 ', '13861590979', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (20, '4567891020', '214073020', '冯太群', NULL, '女', '2006-02-02', '广东省中山市中山市', NULL, '身份证', '442000200602020181', NULL, '528400 ', '13794762634', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (21, '4567891021', '214073021', '杨红', NULL, '男', '2006-07-24', '广东省中山市中山市', NULL, '身份证', '442000200607245611', NULL, '528400 ', '15773742624', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (22, '4567891022', '214073022', '吕黛', NULL, '女', '2006-04-23', '广东省中山市中山市', NULL, '身份证', '442000200604238467', NULL, '528400 ', '13749284606', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (23, '4567891023', '214073023', '雍帮菊', NULL, '男', '2006-05-27', '广东省中山市中山市', NULL, '身份证', '44200020060527761X', NULL, '528400 ', '15703928217', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (24, '4567891024', '214073024', '范红', NULL, '男', '2006-06-25', '广东省中山市中山市', NULL, '身份证', '442000200606252879', NULL, '528400 ', '13798165044', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (25, '4567891025', '214073025', '金莉', NULL, '女', '2006-05-13', '广东省中山市中山市', NULL, '身份证', '442000200605138206', NULL, '528400 ', '15267553570', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (26, '4567891026', '214073026', '韩成倩', NULL, '女', '2006-09-09', '广东省中山市中山市', NULL, '身份证', '442000200609094802', NULL, '528400 ', '15629026018', NULL, '计算机科学与技术', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (27, '4567891027', '214073027', '褚舒', NULL, '男', '2006-09-02', '广东省中山市中山市', NULL, '身份证', '442000200609026690', NULL, '528400 ', '15862897283', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (28, '4567891028', '214073028', '彭彩艳', NULL, '女', '2006-01-13', '广东省中山市中山市', NULL, '身份证', '442000200601133926', NULL, '528400 ', '15864746296', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (29, '4567891029', '214073029', '冯翠安', NULL, '男', '2006-07-24', '广东省中山市中山市', NULL, '身份证', '442000200607241231', NULL, '528400 ', '18870616153', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (30, '4567891030', '214073030', '魏章洪', NULL, '女', '2006-08-15', '广东省中山市中山市', NULL, '身份证', '442000200608159627', NULL, '528400 ', '13402860652', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (31, '4567891031', '214073031', '魏姣', NULL, '女', '2006-02-13', '广东省中山市中山市', NULL, '身份证', '442000200602139748', NULL, '528400 ', '18657559378', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (32, '4567891032', '214073032', '吕亚梅', NULL, '女', '2006-07-08', '广东省中山市中山市', NULL, '身份证', '442000200607081223', NULL, '528400 ', '13052649951', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (33, '4567891033', '214073033', '朱秀娟', NULL, '男', '2006-02-10', '广东省中山市中山市', NULL, '身份证', '442000200602101774', NULL, '528400 ', '15830892826', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (34, '4567891034', '214073034', '何娣', NULL, '女', '2006-03-27', '广东省中山市中山市', NULL, '身份证', '442000200603274183', NULL, '528400 ', '13460392864', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (35, '4567891035', '214073035', '许梅梅', NULL, '男', '2006-02-16', '广东省中山市中山市', NULL, '身份证', '442000200602168338', NULL, '528400 ', '15797588937', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (36, '4567891036', '214073036', '金娜', NULL, '女', '2006-11-17', '广东省中山市中山市', NULL, '身份证', '442000200611174860', NULL, '528400 ', '13304991737', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (37, '4567891037', '214073037', '施韵', NULL, '男', '2006-11-02', '广东省中山市中山市', NULL, '身份证', '442000200611022576', NULL, '528400 ', '15854307929', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (38, '4567891038', '214073038', '严宗莉', NULL, '女', '2006-05-06', '广东省中山市中山市', NULL, '身份证', '442000200605068324', NULL, '528400 ', '15656971542', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (39, '4567891039', '214073039', '李娴', NULL, '男', '2006-09-25', '广东省中山市中山市', NULL, '身份证', '442000200609256998', NULL, '528400 ', '18961585000', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (40, '4567891040', '214073040', '周胜珍', NULL, '女', '2006-05-21', '广东省中山市中山市', NULL, '身份证', '442000200605218820', NULL, '528400 ', '18980193824', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (41, '4567891041', '214073041', '陈梓涵', NULL, '男', '2006-08-18', '广东省中山市中山市', NULL, '身份证', '442000200608189316', NULL, '528400 ', '13178040868', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (42, '4567891042', '214073042', '王婉', NULL, '男', '2006-09-26', '广东省中山市中山市', NULL, '身份证', '442000200609268390', NULL, '528400 ', '18526514455', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (43, '4567891043', '214073043', '戚瑶', NULL, '男', '2006-10-18', '广东省中山市中山市', NULL, '身份证', '44200020061018619X', NULL, '528400 ', '13706061311', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (44, '4567891044', '214073044', '李宛海', NULL, '男', '2006-02-03', '广东省中山市中山市', NULL, '身份证', '442000200602031294', NULL, '528400 ', '15957476578', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (45, '4567891045', '214073045', '何姣', NULL, '女', '2006-03-13', '广东省中山市中山市', NULL, '身份证', '442000200603139723', NULL, '528400 ', '13642572639', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (46, '4567891046', '214073046', '姜宜', NULL, '男', '2006-10-26', '广东省中山市中山市', NULL, '身份证', '442000200610266050', NULL, '528400 ', '18543917040', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (47, '4567891047', '214073047', '曹亚', NULL, '女', '2006-07-01', '广东省中山市中山市', NULL, '身份证', '44200020060701918X', NULL, '528400 ', '13011470852', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (48, '4567891048', '214073048', '祁昭娣', NULL, '女', '2006-06-22', '广东省中山市中山市', NULL, '身份证', '442000200606223867', NULL, '528400 ', '13588699793', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (49, '4567891049', '214073049', '魏莎', NULL, '女', '2006-01-04', '广东省中山市中山市', NULL, '身份证', '442000200601044122', NULL, '528400 ', '15526398117', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (50, '4567891050', '214073050', '施之瑶', NULL, '女', '2006-09-16', '广东省中山市中山市', NULL, '身份证', '442000200609166482', NULL, '528400 ', '18586613815', NULL, '物联网工程', '2021级', '本科', NULL, '计算机系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (51, '4567891051', '214073051', '沈凡', NULL, '男', '2006-02-13', '广东省中山市中山市', NULL, '身份证', '442000200602133813', NULL, '528400 ', '18703277390', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (52, '4567891052', '214073052', '王美丽', NULL, '男', '2006-10-04', '广东省中山市中山市', NULL, '身份证', '442000200610044474', NULL, '528400 ', '15690398542', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (53, '4567891053', '214073053', '冯子彤', NULL, '女', '2006-05-12', '广东省中山市中山市', NULL, '身份证', '442000200605125202', NULL, '528400 ', '13418651581', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (54, '4567891054', '214073054', '钱孝洁', NULL, '男', '2006-02-03', '广东省中山市中山市', NULL, '身份证', '44200020060203919X', NULL, '528400 ', '18531119675', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (55, '4567891055', '214073055', '曹琼琼', NULL, '男', '2006-07-16', '广东省中山市中山市', NULL, '身份证', '442000200607166171', NULL, '528400 ', '18875905694', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (56, '4567891056', '214073056', '钱笑白', NULL, '女', '2006-06-16', '广东省中山市中山市', NULL, '身份证', '442000200606161141', NULL, '528400 ', '13539835467', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (57, '4567891057', '214073057', '别贞', NULL, '女', '2006-02-14', '广东省中山市中山市', NULL, '身份证', '442000200602148142', NULL, '528400 ', '14760550702', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (58, '4567891058', '214073058', '秦柔', NULL, '男', '2006-07-16', '广东省中山市中山市', NULL, '身份证', '442000200607165099', NULL, '528400 ', '15672386257', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (59, '4567891059', '214073059', '施程燕', NULL, '男', '2006-05-19', '广东省中山市中山市', NULL, '身份证', '442000200605192659', NULL, '528400 ', '15546562071', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (60, '4567891060', '214073060', '曹书同', NULL, '男', '2006-10-24', '广东省中山市中山市', NULL, '身份证', '442000200610246834', NULL, '528400 ', '15095064443', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (61, '4567891061', '214073061', '许沛文', NULL, '男', '2006-08-11', '广东省中山市中山市', NULL, '身份证', '442000200608112933', NULL, '528400 ', '18044933701', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (62, '4567891062', '214073062', '朱傲文', NULL, '女', '2006-01-04', '广东省中山市中山市', NULL, '身份证', '442000200601045045', NULL, '528400 ', '13865547116', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (63, '4567891063', '214073063', '王美娟', NULL, '女', '2006-02-08', '广东省中山市中山市', NULL, '身份证', '442000200602085305', NULL, '528400 ', '13342394841', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (64, '4567891064', '214073064', '孙傲文', NULL, '女', '2006-01-12', '广东省中山市中山市', NULL, '身份证', '44200020060112750X', NULL, '528400 ', '15985486120', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (65, '4567891065', '214073065', '华翠花', NULL, '女', '2006-07-20', '广东省中山市中山市', NULL, '身份证', '442000200607207025', NULL, '528400 ', '18907995384', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (66, '4567891066', '214073066', '施翡翠', NULL, '女', '2006-02-25', '广东省中山市中山市', NULL, '身份证', '442000200602255546', NULL, '528400 ', '13251384533', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (67, '4567891067', '214073067', '华昌菊', NULL, '男', '2006-08-06', '广东省中山市中山市', NULL, '身份证', '442000200608061697', NULL, '528400 ', '18606255253', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (68, '4567891068', '214073068', '水梦娇', NULL, '男', '2006-08-21', '广东省中山市中山市', NULL, '身份证', '442000200608213152', NULL, '528400 ', '14777823910', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (69, '4567891069', '214073069', '秦程悦', NULL, '男', '2006-03-01', '广东省中山市中山市', NULL, '身份证', '442000200603018374', NULL, '528400 ', '15603195939', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (70, '4567891070', '214073070', '张玉婷', NULL, '女', '2006-04-12', '广东省中山市中山市', NULL, '身份证', '44200020060412294X', NULL, '528400 ', '13724933155', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (71, '4567891071', '214073071', '贡成倩', NULL, '女', '2006-05-09', '广东省中山市中山市', NULL, '身份证', '442000200605093888', NULL, '528400 ', '13399763952', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (72, '4567891072', '214073072', '蒋洋', NULL, '男', '2006-10-07', '广东省中山市中山市', NULL, '身份证', '442000200610074235', NULL, '528400 ', '15193098416', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (73, '4567891073', '214073073', '朱功碧', NULL, '女', '2006-07-03', '广东省中山市中山市', NULL, '身份证', '442000200607038305', NULL, '528400 ', '15876572613', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (74, '4567891074', '214073074', '严天春', NULL, '女', '2006-11-27', '广东省中山市中山市', NULL, '身份证', '442000200611278248', NULL, '528400 ', '18727604771', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (75, '4567891075', '214073075', '金欣琪', NULL, '女', '2006-05-18', '广东省中山市中山市', NULL, '身份证', '442000200605189046', NULL, '528400 ', '18013032388', NULL, '电子信息工程', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (76, '4567891076', '214073076', '吴德群', NULL, '女', '2006-06-03', '广东省中山市中山市', NULL, '身份证', '442000200606039082', NULL, '528400 ', '18953817191', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (77, '4567891077', '214073077', '孔天春', NULL, '男', '2006-07-22', '广东省中山市中山市', NULL, '身份证', '442000200607221679', NULL, '528400 ', '18572094039', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (78, '4567891078', '214073078', '陈火英', NULL, '女', '2006-04-02', '广东省中山市中山市', NULL, '身份证', '44200020060402520X', NULL, '528400 ', '18962222865', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (79, '4567891079', '214073079', '金琴', NULL, '女', '2006-10-05', '广东省中山市中山市', NULL, '身份证', '442000200610052925', NULL, '528400 ', '15814818701', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (80, '4567891080', '214073080', '朱耀文', NULL, '男', '2006-10-17', '广东省中山市中山市', NULL, '身份证', '442000200610170796', NULL, '528400 ', '15588271470', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (81, '4567891081', '214073081', '李高彭', NULL, '女', '2006-04-08', '广东省中山市中山市', NULL, '身份证', '442000200604086926', NULL, '528400 ', '18925679906', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (82, '4567891082', '214073082', '鄂倩倩', NULL, '男', '2006-02-09', '广东省中山市中山市', NULL, '身份证', '442000200602097656', NULL, '528400 ', '14538009521', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (83, '4567891083', '214073083', '施盼夏', NULL, '女', '2006-08-11', '广东省中山市中山市', NULL, '身份证', '442000200608113266', NULL, '528400 ', '14585058140', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (84, '4567891084', '214073084', '施纨', NULL, '女', '2006-03-08', '广东省中山市中山市', NULL, '身份证', '442000200603087863', NULL, '528400 ', '15189891549', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (85, '4567891085', '214073085', '姜玫', NULL, '女', '2006-09-02', '广东省中山市中山市', NULL, '身份证', '44200020060902752X', NULL, '528400 ', '15080680351', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (86, '4567891086', '214073086', '李夏梅', NULL, '男', '2006-02-25', '广东省中山市中山市', NULL, '身份证', '442000200602259176', NULL, '528400 ', '15914399453', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (87, '4567891087', '214073087', '孙安琪', NULL, '女', '2006-06-14', '广东省中山市中山市', NULL, '身份证', '442000200606144982', NULL, '528400 ', '15601304794', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (88, '4567891088', '214073088', '王丽', NULL, '男', '2006-11-01', '广东省中山市中山市', NULL, '身份证', '442000200611011172', NULL, '528400 ', '13330263089', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (89, '4567891089', '214073089', '吴娇娇', NULL, '男', '2006-07-22', '广东省中山市中山市', NULL, '身份证', '442000200607226533', NULL, '528400 ', '15075144821', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (90, '4567891090', '214073090', '东门之瑶', NULL, '女', '2006-01-06', '广东省中山市中山市', NULL, '身份证', '442000200601065986', NULL, '528400 ', '13554423959', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (91, '4567891091', '214073091', '钱荷', NULL, '女', '2006-07-20', '广东省中山市中山市', NULL, '身份证', '442000200607203227', NULL, '528400 ', '18565481629', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (92, '4567891092', '214073092', '沈丽', NULL, '男', '2006-06-23', '广东省中山市中山市', NULL, '身份证', '442000200606237871', NULL, '528400 ', '13482918053', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (93, '4567891093', '214073093', '孔金玫', NULL, '男', '2006-03-18', '广东省中山市中山市', NULL, '身份证', '442000200603185957', NULL, '528400 ', '13263788722', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (94, '4567891094', '214073094', '周心菲', NULL, '女', '2006-07-03', '广东省中山市中山市', NULL, '身份证', '442000200607034849', NULL, '528400 ', '15283372808', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (95, '4567891095', '214073095', '钱薇', NULL, '男', '2006-09-05', '广东省中山市中山市', NULL, '身份证', '442000200609051458', NULL, '528400 ', '15384919007', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (96, '4567891096', '214073096', '马开凤', NULL, '女', '2006-06-20', '广东省中山市中山市', NULL, '身份证', '442000200606209942', NULL, '528400 ', '13058691525', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (97, '4567891097', '214073097', '戚景玉', NULL, '男', '2006-01-21', '广东省中山市中山市', NULL, '身份证', '442000200601214419', NULL, '528400 ', '15931845902', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (98, '4567891098', '214073098', '顾枫', NULL, '女', '2006-09-07', '广东省中山市中山市', NULL, '身份证', '442000200609074545', NULL, '528400 ', '15719469174', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (99, '4567891099', '214073099', '严光桃', NULL, '男', '2006-11-11', '广东省中山市中山市', NULL, '身份证', '442000200611114315', NULL, '528400 ', '15859756991', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (100, '4567891100', '214073100', '任之念', NULL, '男', '2006-02-22', '广东省中山市中山市', NULL, '身份证', '442000200602220415', NULL, '528400 ', '13023034635', NULL, '自动化', '2021级', '本科', NULL, '电子系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (101, '4567891101', '214073101', '周明红', NULL, '男', '2006-08-18', '广东省中山市中山市', NULL, '身份证', '442000200608189615', NULL, '528400 ', '15801633669', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (102, '4567891102', '214073102', '张凤美', NULL, '女', '2006-09-12', '广东省中山市中山市', NULL, '身份证', '442000200609127942', NULL, '528400 ', '13513979619', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (103, '4567891103', '214073103', '沈寒雁', NULL, '女', '2006-06-03', '广东省中山市中山市', NULL, '身份证', '442000200606033561', NULL, '528400 ', '13182675674', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (104, '4567891104', '214073104', '许花长', NULL, '女', '2006-10-19', '广东省中山市中山市', NULL, '身份证', '442000200610196320', NULL, '528400 ', '18847845821', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (105, '4567891105', '214073105', '钱政群', NULL, '男', '2006-11-10', '广东省中山市中山市', NULL, '身份证', '442000200611109014', NULL, '528400 ', '13247904454', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (106, '4567891106', '214073106', '褚涵菡', NULL, '女', '2006-01-14', '广东省中山市中山市', NULL, '身份证', '442000200601140923', NULL, '528400 ', '18567865229', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (107, '4567891107', '214073107', '董黄萍', NULL, '女', '2006-05-09', '广东省中山市中山市', NULL, '身份证', '442000200605095429', NULL, '528400 ', '14707879649', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (108, '4567891108', '214073108', '朱宗莉', NULL, '女', '2006-01-24', '广东省中山市中山市', NULL, '身份证', '442000200601247907', NULL, '528400 ', '15129626464', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (109, '4567891109', '214073109', '杨美娟', NULL, '女', '2006-05-05', '广东省中山市中山市', NULL, '身份证', '442000200605059946', NULL, '528400 ', '18708364529', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (110, '4567891110', '214073110', '戚冰彤', NULL, '女', '2006-02-24', '广东省中山市中山市', NULL, '身份证', '442000200602242585', NULL, '528400 ', '15092903211', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (111, '4567891111', '214073111', '蒋园', NULL, '男', '2006-07-06', '广东省中山市中山市', NULL, '身份证', '442000200607064415', NULL, '528400 ', '13821884929', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (112, '4567891112', '214073112', '吕巧丽', NULL, '男', '2006-05-10', '广东省中山市中山市', NULL, '身份证', '442000200605102756', NULL, '528400 ', '13121385303', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (113, '4567891113', '214073113', '钱涵菡', NULL, '女', '2006-04-05', '广东省中山市中山市', NULL, '身份证', '442000200604056022', NULL, '528400 ', '14560695359', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (114, '4567891114', '214073114', '唐凌珍', NULL, '男', '2006-05-16', '广东省中山市中山市', NULL, '身份证', '442000200605161633', NULL, '528400 ', '18758408968', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (115, '4567891115', '214073115', '许开凤', NULL, '男', '2006-05-13', '广东省中山市中山市', NULL, '身份证', '442000200605137836', NULL, '528400 ', '15031592939', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (116, '4567891116', '214073116', '孔宛海', NULL, '男', '2006-08-27', '广东省中山市中山市', NULL, '身份证', '442000200608274078', NULL, '528400 ', '15942150222', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (117, '4567891117', '214073117', '姜向萍', NULL, '男', '2006-04-27', '广东省中山市中山市', NULL, '身份证', '442000200604277511', NULL, '528400 ', '13491695543', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (118, '4567891118', '214073118', '朱锦', NULL, '男', '2006-02-02', '广东省中山市中山市', NULL, '身份证', '442000200602022232', NULL, '528400 ', '18606349696', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (119, '4567891119', '214073119', '沈启芬', NULL, '女', '2006-05-11', '广东省中山市中山市', NULL, '身份证', '442000200605111169', NULL, '528400 ', '13610293380', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (120, '4567891120', '214073120', '阴火英', NULL, '女', '2006-08-20', '广东省中山市中山市', NULL, '身份证', '442000200608202365', NULL, '528400 ', '18922552275', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (121, '4567891121', '214073121', '张梅梅', NULL, '女', '2006-06-25', '广东省中山市中山市', NULL, '身份证', '442000200606250443', NULL, '528400 ', '15185246115', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (122, '4567891122', '214073122', '陶莎莎', NULL, '男', '2006-09-02', '广东省中山市中山市', NULL, '身份证', '442000200609020491', NULL, '528400 ', '13176909816', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (123, '4567891123', '214073123', '魏招弟', NULL, '女', '2006-11-05', '广东省中山市中山市', NULL, '身份证', '442000200611057381', NULL, '528400 ', '15888446947', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (124, '4567891124', '214073124', '朱念', NULL, '男', '2006-07-10', '广东省中山市中山市', NULL, '身份证', '44200020060710611X', NULL, '528400 ', '15977548010', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (125, '4567891125', '214073125', '李秋', NULL, '男', '2006-07-18', '广东省中山市中山市', NULL, '身份证', '442000200607185815', NULL, '528400 ', '13946566382', NULL, '风景园林', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (126, '4567891126', '214073126', '许平夏', NULL, '男', '2006-08-03', '广东省中山市中山市', NULL, '身份证', '442000200608036918', NULL, '528400 ', '15219347126', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (127, '4567891127', '214073127', '冯银姣', NULL, '女', '2006-05-16', '广东省中山市中山市', NULL, '身份证', '442000200605160585', NULL, '528400 ', '18613422706', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (128, '4567891128', '214073128', '戚易梦', NULL, '女', '2006-03-27', '广东省中山市中山市', NULL, '身份证', '442000200603279267', NULL, '528400 ', '15059625036', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (129, '4567891129', '214073129', '朱朱娇', NULL, '女', '2006-10-18', '广东省中山市中山市', NULL, '身份证', '44200020061018272X', NULL, '528400 ', '15392521880', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (130, '4567891130', '214073130', '卫娴', NULL, '男', '2006-07-07', '广东省中山市中山市', NULL, '身份证', '442000200607072458', NULL, '528400 ', '13381722766', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (131, '4567891131', '214073131', '吕娅', NULL, '男', '2006-03-01', '广东省中山市中山市', NULL, '身份证', '442000200603019115', NULL, '528400 ', '13294482058', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (132, '4567891132', '214073132', '潘杰', NULL, '女', '2006-01-24', '广东省中山市中山市', NULL, '身份证', '442000200601246728', NULL, '528400 ', '15695048675', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (133, '4567891133', '214073133', '郑青', NULL, '女', '2006-06-24', '广东省中山市中山市', NULL, '身份证', '442000200606243964', NULL, '528400 ', '18917203846', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (134, '4567891134', '214073134', '褚丽', NULL, '女', '2006-05-19', '广东省中山市中山市', NULL, '身份证', '44200020060519550X', NULL, '528400 ', '18635196691', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (135, '4567891135', '214073135', '魏红萍', NULL, '男', '2006-08-11', '广东省中山市中山市', NULL, '身份证', '442000200608117056', NULL, '528400 ', '13558268881', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (136, '4567891136', '214073136', '蒋晓磊', NULL, '女', '2006-05-26', '广东省中山市中山市', NULL, '身份证', '442000200605260228', NULL, '528400 ', '14586926641', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (137, '4567891137', '214073137', '孙夜白', NULL, '女', '2006-05-23', '广东省中山市中山市', NULL, '身份证', '442000200605232200', NULL, '528400 ', '15329904925', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (138, '4567891138', '214073138', '尤虹霖', NULL, '男', '2006-08-25', '广东省中山市中山市', NULL, '身份证', '442000200608250332', NULL, '528400 ', '14739791988', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (139, '4567891139', '214073139', '冯显', NULL, '男', '2006-07-05', '广东省中山市中山市', NULL, '身份证', '442000200607057055', NULL, '528400 ', '13867024392', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (140, '4567891140', '214073140', '陶亦瑶', NULL, '男', '2006-05-03', '广东省中山市中山市', NULL, '身份证', '442000200605038758', NULL, '528400 ', '13525140106', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (141, '4567891141', '214073141', '孙龙婷', NULL, '男', '2006-04-21', '广东省中山市中山市', NULL, '身份证', '442000200604219653', NULL, '528400 ', '18544786484', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (142, '4567891142', '214073142', '郑溶艳', NULL, '女', '2006-06-26', '广东省中山市中山市', NULL, '身份证', '442000200606261580', NULL, '528400 ', '15700672533', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (143, '4567891143', '214073143', '章涛', NULL, '男', '2006-10-07', '广东省中山市中山市', NULL, '身份证', '442000200610072352', NULL, '528400 ', '14572598956', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (144, '4567891144', '214073144', '戚滢', NULL, '女', '2006-08-01', '广东省中山市中山市', NULL, '身份证', '442000200608010945', NULL, '528400 ', '15004452590', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (145, '4567891145', '214073145', '杨可', NULL, '男', '2006-06-10', '广东省中山市中山市', NULL, '身份证', '442000200606109853', NULL, '528400 ', '13071581871', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (146, '4567891146', '214073146', '朱之念', NULL, '男', '2006-06-17', '广东省中山市中山市', NULL, '身份证', '442000200606174874', NULL, '528400 ', '15579587545', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (147, '4567891147', '214073147', '陈子彤', NULL, '女', '2006-11-15', '广东省中山市中山市', NULL, '身份证', '442000200611153066', NULL, '528400 ', '14779469766', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (148, '4567891148', '214073148', '赵向萍', NULL, '女', '2006-11-09', '广东省中山市中山市', NULL, '身份证', '442000200611095206', NULL, '528400 ', '18094571514', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (149, '4567891149', '214073149', '韩锦', NULL, '男', '2006-06-15', '广东省中山市中山市', NULL, '身份证', '442000200606159578', NULL, '528400 ', '15874949061', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (150, '4567891150', '214073150', '尤颖', NULL, '男', '2006-01-26', '广东省中山市中山市', NULL, '身份证', '44200020060126677X', NULL, '528400 ', '15078376415', NULL, '数字媒体艺术', '2021级', '本科', NULL, '数码媒体系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (151, '4567891151', '214073151', '施菁', NULL, '女', '2006-03-01', '广东省中山市中山市', NULL, '身份证', '442000200603010508', NULL, '528400 ', '13826457738', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (152, '4567891152', '214073152', '卫平夏', NULL, '女', '2006-06-01', '广东省中山市中山市', NULL, '身份证', '442000200606014985', NULL, '528400 ', '18862342566', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (153, '4567891153', '214073153', '陶咏', NULL, '男', '2006-02-23', '广东省中山市中山市', NULL, '身份证', '44200020060223207X', NULL, '528400 ', '15920239763', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (154, '4567891154', '214073154', '郑伊萍', NULL, '男', '2006-05-08', '广东省中山市中山市', NULL, '身份证', '442000200605089256', NULL, '528400 ', '13824787448', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (155, '4567891155', '214073155', '奚欣', NULL, '男', '2006-02-04', '广东省中山市中山市', NULL, '身份证', '442000200602043439', NULL, '528400 ', '13424744703', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (156, '4567891156', '214073156', '沈雪萍', NULL, '男', '2006-03-16', '广东省中山市中山市', NULL, '身份证', '44200020060316059X', NULL, '528400 ', '18743904569', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (157, '4567891157', '214073157', '赵海燕', NULL, '女', '2006-11-12', '广东省中山市中山市', NULL, '身份证', '442000200611125604', NULL, '528400 ', '18092701086', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (158, '4567891158', '214073158', '孙莉', NULL, '女', '2006-07-25', '广东省中山市中山市', NULL, '身份证', '442000200607252766', NULL, '528400 ', '13557062021', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (159, '4567891159', '214073159', '柯耀文', NULL, '男', '2006-03-23', '广东省中山市中山市', NULL, '身份证', '442000200603232493', NULL, '528400 ', '13343415844', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (160, '4567891160', '214073160', '李胜英', NULL, '女', '2006-02-26', '广东省中山市中山市', NULL, '身份证', '442000200602269526', NULL, '528400 ', '15215753783', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (161, '4567891161', '214073161', '严悦明', NULL, '男', '2006-10-05', '广东省中山市中山市', NULL, '身份证', '442000200610057830', NULL, '528400 ', '15361042158', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (162, '4567891162', '214073162', '王婷', NULL, '男', '2006-07-11', '广东省中山市中山市', NULL, '身份证', '442000200607110856', NULL, '528400 ', '13376105860', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (163, '4567891163', '214073163', '姜晓', NULL, '女', '2006-08-03', '广东省中山市中山市', NULL, '身份证', '442000200608031228', NULL, '528400 ', '18709877102', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (164, '4567891164', '214073164', '周金玫', NULL, '男', '2006-09-05', '广东省中山市中山市', NULL, '身份证', '44200020060905267X', NULL, '528400 ', '13949379010', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (165, '4567891165', '214073165', '韩彩艳', NULL, '男', '2006-09-15', '广东省中山市中山市', NULL, '身份证', '442000200609152590', NULL, '528400 ', '18940731552', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (166, '4567891166', '214073166', '孙秀芳', NULL, '男', '2006-08-27', '广东省中山市中山市', NULL, '身份证', '442000200608271459', NULL, '528400 ', '18657034408', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (167, '4567891167', '214073167', '韩雁', NULL, '男', '2006-07-05', '广东省中山市中山市', NULL, '身份证', '442000200607050451', NULL, '528400 ', '15908586201', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (168, '4567891168', '214073168', '冯远琴', NULL, '女', '2006-06-10', '广东省中山市中山市', NULL, '身份证', '442000200606107786', NULL, '528400 ', '13586380162', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (169, '4567891169', '214073169', '沙旭', NULL, '女', '2006-02-20', '广东省中山市中山市', NULL, '身份证', '442000200602203703', NULL, '528400 ', '13599957922', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (170, '4567891170', '214073170', '璩婷', NULL, '男', '2006-04-10', '广东省中山市中山市', NULL, '身份证', '442000200604105752', NULL, '528400 ', '15110479988', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (171, '4567891171', '214073171', '朱向萍', NULL, '男', '2006-06-21', '广东省中山市中山市', NULL, '身份证', '442000200606215699', NULL, '528400 ', '15118395992', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (172, '4567891172', '214073172', '王朱婷', NULL, '女', '2006-08-14', '广东省中山市中山市', NULL, '身份证', '44200020060814242X', NULL, '528400 ', '18752346844', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (173, '4567891173', '214073173', '曹程悦', NULL, '男', '2006-09-04', '广东省中山市中山市', NULL, '身份证', '442000200609046755', NULL, '528400 ', '18834665468', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (174, '4567891174', '214073174', '杨彤', NULL, '男', '2006-08-21', '广东省中山市中山市', NULL, '身份证', '442000200608212539', NULL, '528400 ', '15282107295', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (175, '4567891175', '214073175', '褚天春', NULL, '女', '2006-06-11', '广东省中山市中山市', NULL, '身份证', '442000200606114046', NULL, '528400 ', '18751368952', NULL, '网络与新媒体', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (176, '4567891176', '214073176', '蒋福兰', NULL, '女', '2006-03-01', '广东省中山市中山市', NULL, '身份证', '442000200603015122', NULL, '528400 ', '18697513473', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (177, '4567891177', '214073177', '曹春喜', NULL, '男', '2006-04-19', '广东省中山市中山市', NULL, '身份证', '442000200604196615', NULL, '528400 ', '15233382193', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (178, '4567891178', '214073178', '袁聪', NULL, '女', '2006-02-21', '广东省中山市中山市', NULL, '身份证', '442000200602215026', NULL, '528400 ', '18901661873', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (179, '4567891179', '214073179', '冯寒雁', NULL, '女', '2006-05-24', '广东省中山市中山市', NULL, '身份证', '442000200605240243', NULL, '528400 ', '18062253021', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (180, '4567891180', '214073180', '陶荷', NULL, '男', '2006-06-01', '广东省中山市中山市', NULL, '身份证', '442000200606019831', NULL, '528400 ', '13679391026', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (181, '4567891181', '214073181', '陶迎春', NULL, '男', '2006-04-18', '广东省中山市中山市', NULL, '身份证', '442000200604184737', NULL, '528400 ', '14507623078', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (182, '4567891182', '214073182', '陈谷山', NULL, '男', '2006-11-22', '广东省中山市中山市', NULL, '身份证', '442000200611228013', NULL, '528400 ', '18736035071', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (183, '4567891183', '214073183', '魏幻波', NULL, '女', '2006-09-15', '广东省中山市中山市', NULL, '身份证', '442000200609155708', NULL, '528400 ', '15833003136', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (184, '4567891184', '214073184', '郑世群', NULL, '女', '2006-03-23', '广东省中山市中山市', NULL, '身份证', '442000200603230623', NULL, '528400 ', '18751641550', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (185, '4567891185', '214073185', '孔姚', NULL, '男', '2006-10-08', '广东省中山市中山市', NULL, '身份证', '442000200610081339', NULL, '528400 ', '13384656481', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (186, '4567891186', '214073186', '褚海莲', NULL, '女', '2006-01-08', '广东省中山市中山市', NULL, '身份证', '442000200601081687', NULL, '528400 ', '13255330825', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (187, '4567891187', '214073187', '戚龙婷', NULL, '女', '2006-08-22', '广东省中山市中山市', NULL, '身份证', '442000200608224548', NULL, '528400 ', '15225697168', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (188, '4567891188', '214073188', '蒋尔珍', NULL, '女', '2006-02-23', '广东省中山市中山市', NULL, '身份证', '442000200602235940', NULL, '528400 ', '13861332023', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (189, '4567891189', '214073189', '钱黛', NULL, '女', '2006-11-05', '广东省中山市中山市', NULL, '身份证', '442000200611054826', NULL, '528400 ', '15156016694', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (190, '4567891190', '214073190', '陈睿婕', NULL, '男', '2006-11-27', '广东省中山市中山市', NULL, '身份证', '442000200611279371', NULL, '528400 ', '15110842341', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (191, '4567891191', '214073191', '许蓓', NULL, '男', '2006-10-25', '广东省中山市中山市', NULL, '身份证', '442000200610252177', NULL, '528400 ', '15953321019', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (192, '4567891192', '214073192', '韩千萍', NULL, '女', '2006-08-03', '广东省中山市中山市', NULL, '身份证', '442000200608034162', NULL, '528400 ', '13800474081', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (193, '4567891193', '214073193', '朱问筠', NULL, '女', '2006-07-26', '广东省中山市中山市', NULL, '身份证', '442000200607260141', NULL, '528400 ', '13049081745', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (194, '4567891194', '214073194', '茹灿灿', NULL, '男', '2006-10-09', '广东省中山市中山市', NULL, '身份证', '442000200610097592', NULL, '528400 ', '13695024513', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (195, '4567891195', '214073195', '王海莲', NULL, '男', '2006-03-04', '广东省中山市中山市', NULL, '身份证', '442000200603042470', NULL, '528400 ', '13634515052', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (196, '4567891196', '214073196', '郑翠翠', NULL, '女', '2006-06-14', '广东省中山市中山市', NULL, '身份证', '442000200606148043', NULL, '528400 ', '13242043256', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (197, '4567891197', '214073197', '窦睿敏', NULL, '女', '2006-06-13', '广东省中山市中山市', NULL, '身份证', '442000200606138603', NULL, '528400 ', '14532727588', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (198, '4567891198', '214073198', '张水云', NULL, '女', '2006-09-08', '广东省中山市中山市', NULL, '身份证', '442000200609088568', NULL, '528400 ', '15583545728', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (199, '4567891199', '214073199', '韩秀英', NULL, '女', '2006-11-22', '广东省中山市中山市', NULL, '身份证', '442000200611227889', NULL, '528400 ', '13434947378', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `student` VALUES (200, '4567891200', '214073200', '魏千萍', NULL, '女', '2006-07-24', '广东省中山市中山市', NULL, '身份证', '442000200607243667', NULL, '528400 ', '15327476691', NULL, '动画', '2021级', '本科', NULL, '游戏系', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for student_dorm
-- ----------------------------
DROP TABLE IF EXISTS `student_dorm`;
CREATE TABLE `student_dorm`  (
  `studentNumber` int(9) UNSIGNED ZEROFILL NULL DEFAULT NULL,
  `studentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dormType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dormitory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dormCard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student_dorm
-- ----------------------------
INSERT INTO `student_dorm` VALUES (214073001, '钱梦娇', '计算机系', '四人间', '', '');
INSERT INTO `student_dorm` VALUES (214073002, NULL, NULL, '四人间', NULL, NULL);

-- ----------------------------
-- Table structure for t_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_authority`;
CREATE TABLE `t_authority`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_authority
-- ----------------------------
INSERT INTO `t_authority` VALUES (1, 'ROLE_STUDENT');
INSERT INTO `t_authority` VALUES (2, 'ROLE_ADMIN');
INSERT INTO `t_authority` VALUES (3, 'ROLE_GUEST');

-- ----------------------------
-- Table structure for t_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_user_authority`;
CREATE TABLE `t_user_authority`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `authority_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_authority
-- ----------------------------
INSERT INTO `t_user_authority` VALUES (1, 1, 1);
INSERT INTO `t_user_authority` VALUES (2, 2, 0);
INSERT INTO `t_user_authority` VALUES (3, 3, 2);
INSERT INTO `t_user_authority` VALUES (4, 4, 1);
INSERT INTO `t_user_authority` VALUES (5, 5, 1);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `teacherid` int NOT NULL,
  `teacherName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teacherPhone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`teacherid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1101, '老师1', '18924651328');
INSERT INTO `teacher` VALUES (1102, '老师2', '18924651329');
INSERT INTO `teacher` VALUES (1103, '老师3', '18924651330');
INSERT INTO `teacher` VALUES (1104, '老师4', '18924651331');
INSERT INTO `teacher` VALUES (1105, '老师5', '18924651332');
INSERT INTO `teacher` VALUES (1106, '老师6', '18924651333');
INSERT INTO `teacher` VALUES (1107, '老师7', '18924651334');
INSERT INTO `teacher` VALUES (1108, '老师8', '18924651335');
INSERT INTO `teacher` VALUES (1109, '老师9', '18924651336');
INSERT INTO `teacher` VALUES (1110, '老师10', '18924651337');
INSERT INTO `teacher` VALUES (1111, '老师11', '18924651338');
INSERT INTO `teacher` VALUES (1112, '老师12', '18924651339');
INSERT INTO `teacher` VALUES (1113, '老师13', '18924651340');
INSERT INTO `teacher` VALUES (1114, '老师14', '18924651341');
INSERT INTO `teacher` VALUES (1115, '老师15', '18924651342');
INSERT INTO `teacher` VALUES (1116, '老师16', '18924651343');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `studentNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '214073001', '{noop}123456');
INSERT INTO `user` VALUES (2, '1234567', '{noop}1234567');
INSERT INTO `user` VALUES (3, 'admin', '{noop}admin');
INSERT INTO `user` VALUES (4, '214073002', '{noop}123456');
INSERT INTO `user` VALUES (5, '214073003', '{noop}123456');

SET FOREIGN_KEY_CHECKS = 1;
