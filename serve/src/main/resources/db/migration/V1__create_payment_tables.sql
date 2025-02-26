-- 创建缴费项目表
CREATE TABLE IF NOT EXISTS payment_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '项目名称',
    amount DECIMAL(10, 2) NOT NULL COMMENT '缴费金额',
    deadline VARCHAR(20) NOT NULL COMMENT '截止日期',
    description VARCHAR(500) COMMENT '项目描述',
    type INT DEFAULT 0 COMMENT '项目类型（0-学费，1-住宿费，2-其他）',
    grade VARCHAR(20) COMMENT '适用年级',
    major VARCHAR(50) COMMENT '适用专业，为空表示所有专业'
) COMMENT='缴费项目表';

-- 创建缴费记录表
CREATE TABLE IF NOT EXISTS payment_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) NOT NULL COMMENT '学号',
    payment_item_id INT NOT NULL COMMENT '缴费项目ID',
    amount DECIMAL(10, 2) NOT NULL COMMENT '缴费金额',
    payment_method VARCHAR(20) COMMENT '支付方式（微信支付、支付宝、银行卡）',
    payment_date DATETIME COMMENT '支付时间',
    status INT DEFAULT 0 COMMENT '支付状态（0-未支付，1-已支付）',
    transaction_number VARCHAR(50) COMMENT '交易流水号',
    remark VARCHAR(200) COMMENT '备注',
    FOREIGN KEY (payment_item_id) REFERENCES payment_item(id)
) COMMENT='缴费记录表';

-- 插入示例数据：缴费项目
INSERT INTO payment_item (name, amount, deadline, description, type, grade, major)
VALUES 
    ('2024年秋季学费', 4800.00, '2024-09-01', '2024年秋季学期学费', 0, '2024', NULL),
    ('2024年秋季住宿费', 1200.00, '2024-09-01', '2024年秋季学期住宿费', 1, '2024', NULL),
    ('计算机专业实验费', 500.00, '2024-09-15', '计算机专业实验室使用费', 2, '2024', '计算机科学与技术'),
    ('软件工程专业实验费', 600.00, '2024-09-15', '软件工程专业实验室使用费', 2, '2024', '软件工程');

-- 插入示例数据：缴费记录
INSERT INTO payment_record (student_number, payment_item_id, amount, payment_method, payment_date, status, transaction_number, remark)
VALUES 
    ('2024001', 1, 4800.00, '微信支付', '2024-02-15 10:30:00', 1, 'PAY2024021510300012345678', '学费已缴纳'),
    ('2024001', 2, 1200.00, '微信支付', '2024-02-15 10:35:00', 1, 'PAY2024021510350087654321', '住宿费已缴纳'),
    ('2024002', 1, 4800.00, '支付宝', '2024-02-20 14:20:00', 1, 'PAY2024022014200056781234', '学费已缴纳'); 