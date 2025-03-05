-- 清空现有数据
DELETE FROM resume;
DELETE FROM family;

-- 为student表中的学生创建简历数据（每个学生只有一条记录）
INSERT INTO resume (studentNumber, startTime, endTime, organization, hats) VALUES
('214073001', '2019年9月-2022年6月(广州市第一中学,学生); 2022年9月-2025年6月(计算机科学与技术学院,本科生)', '', '', '');

INSERT INTO resume (studentNumber, startTime, endTime, organization, hats) VALUES
('214073002', '2018年9月-2021年6月(深圳市实验中学,学生); 2021年9月-2024年6月(软件工程学院,本科生)', '', '', '');

INSERT INTO resume (studentNumber, startTime, endTime, organization, hats) VALUES
('214073003', '2017年9月-2020年6月(上海市实验中学,学生); 2020年9月-2023年6月(信息工程学院,本科生); 2023年7月-2023年8月(字节跳动,实习生)', '', '', '');

INSERT INTO resume (studentNumber, startTime, endTime, organization, hats) VALUES
('214073004', '2019年9月-2022年6月(北京市第四中学,学生); 2022年9月-2025年6月(人工智能学院,本科生)', '', '', '');

INSERT INTO resume (studentNumber, startTime, endTime, organization, hats) VALUES
('214073005', '2018年9月-2021年6月(武汉市第二中学,学生); 2021年9月-2024年6月(数据科学与大数据技术专业,本科生); 2023年12月-2024年2月(腾讯,实习生)', '', '', '');

-- 为student表中的学生创建家庭成员数据
-- 学生214073001的家庭成员
INSERT INTO family (studentNumber, familyPart, familyName, familyAge, familyPoliteAspect, familyOrganization) VALUES
('214073001', '父亲', '张伟', '48', '中共党员', '广州市某企业'),
('214073001', '母亲', '李芳', '45', '群众', '广州市某学校');

-- 学生214073002的家庭成员
INSERT INTO family (studentNumber, familyPart, familyName, familyAge, familyPoliteAspect, familyOrganization) VALUES
('214073002', '父亲', '王强', '50', '中共党员', '深圳市某政府部门'),
('214073002', '母亲', '赵敏', '48', '中共党员', '深圳市某医院'),
('214073002', '弟弟', '王小明', '16', '共青团员', '深圳市某中学');

-- 学生214073003的家庭成员
INSERT INTO family (studentNumber, familyPart, familyName, familyAge, familyPoliteAspect, familyOrganization) VALUES
('214073003', '父亲', '刘军', '52', '中共党员', '上海市某企业'),
('214073003', '母亲', '张丽', '50', '群众', '上海市某事业单位');

-- 学生214073004的家庭成员
INSERT INTO family (studentNumber, familyPart, familyName, familyAge, familyPoliteAspect, familyOrganization) VALUES
('214073004', '父亲', '陈明', '55', '民主人士', '北京市某大学'),
('214073004', '母亲', '王芳', '53', '中共党员', '北京市某研究所');

-- 学生214073005的家庭成员
INSERT INTO family (studentNumber, familyPart, familyName, familyAge, familyPoliteAspect, familyOrganization) VALUES
('214073005', '父亲', '黄建国', '49', '中共党员', '武汉市某企业'),
('214073005', '母亲', '张燕', '47', '群众', '武汉市某学校'),
('214073005', '妹妹', '黄小红', '15', '共青团员', '武汉市某中学'); 