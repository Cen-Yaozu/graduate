-- 备份现有数据
CREATE TABLE resume_backup AS SELECT * FROM resume;

-- 删除现有表
DROP TABLE resume;

-- 创建新的resume表，添加id作为主键
CREATE TABLE resume (
  id INT NOT NULL AUTO_INCREMENT,
  studentNumber VARCHAR(20) NOT NULL,
  startTime VARCHAR(50),
  endTime VARCHAR(50),
  organization VARCHAR(100),
  hats VARCHAR(50),
  PRIMARY KEY (id),
  INDEX idx_studentNumber (studentNumber)
);

-- 导入测试数据
INSERT INTO resume (studentNumber, startTime, endTime, organization, hats) VALUES
('214073001', '2019年9月', '2022年6月', '广州市第一中学', '学生'),
('214073001', '2022年9月', '2025年6月', '计算机科学与技术学院', '本科生'),
('214073002', '2018年9月', '2021年6月', '深圳市实验中学', '学生'),
('214073002', '2021年9月', '2024年6月', '软件工程学院', '本科生'),
('214073003', '2017年9月', '2020年6月', '上海市实验中学', '学生'),
('214073003', '2020年9月', '2023年6月', '信息工程学院', '本科生'),
('214073003', '2023年7月', '2023年8月', '字节跳动', '实习生'),
('214073004', '2019年9月', '2022年6月', '北京市第四中学', '学生'),
('214073004', '2022年9月', '2025年6月', '人工智能学院', '本科生'),
('214073005', '2018年9月', '2021年6月', '武汉市第二中学', '学生'),
('214073005', '2021年9月', '2024年6月', '数据科学与大数据技术专业', '本科生'),
('214073005', '2023年12月', '2024年2月', '腾讯', '实习生');

-- 尝试从备份中恢复原有数据
-- 注意：如果原始表中确实将studentNumber作为主键，那么恢复数据时需要去重
INSERT IGNORE INTO resume (studentNumber, startTime, endTime, organization, hats)
SELECT studentNumber, startTime, endTime, organization, hats FROM resume_backup;

-- 删除备份表
DROP TABLE resume_backup; 