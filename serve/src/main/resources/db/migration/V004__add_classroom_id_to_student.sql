-- 向student表添加classroom_id字段
ALTER TABLE student ADD COLUMN `classroom_id` INT DEFAULT NULL COMMENT '班级ID';

-- 添加外键约束（如果需要）
-- ALTER TABLE student ADD CONSTRAINT fk_student_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(majorid) ON DELETE SET NULL; 