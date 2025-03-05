 -- pay表模拟数据
-- 执行此脚本前，建议先清空原有数据
DELETE FROM `pay` WHERE 1=1;

-- 添加模拟的学生缴费数据
INSERT INTO `pay` VALUES (0214073001, '钱梦娇', '线上支付', 'PAY202502280001', 5500, 'payment_receipt_001.jpg', '已支付', '学费和住宿费');
INSERT INTO `pay` VALUES (0214073002, '李明', '线上支付', 'PAY202502280002', 5500, 'payment_receipt_002.jpg', '已支付', '学费和住宿费');
INSERT INTO `pay` VALUES (0214073003, '张华', '银行转账', 'PAY202502280003', 4800, 'payment_receipt_003.jpg', '已支付', '仅学费');
INSERT INTO `pay` VALUES (0214073004, '王芳', '现场缴费', 'PAY202502280004', 5300, NULL, '未支付', '学费和部分实验费');
INSERT INTO `pay` VALUES (0214073005, '刘洋', '线上支付', 'PAY202502280005', 6000, 'payment_receipt_005.jpg', '已支付', '学费、住宿费和实验费');
INSERT INTO `pay` VALUES (0214073006, '赵静', '银行转账', 'PAY202502280006', 5500, 'payment_receipt_006.jpg', '已支付', '学费和住宿费');
INSERT INTO `pay` VALUES (0214073007, '陈明', '线上支付', 'PAY202502280007', 4800, NULL, '未支付', '仅学费');
INSERT INTO `pay` VALUES (0214073008, '周婷', '现场缴费', 'PAY202502280008', 5800, 'payment_receipt_008.jpg', '已支付', '学费、住宿费和书本费');
INSERT INTO `pay` VALUES (0214073009, '杨帆', '线上支付', 'PAY202502280009', 5500, 'payment_receipt_009.jpg', '已支付', '学费和住宿费');
INSERT INTO `pay` VALUES (0214073010, '吴涛', '银行转账', 'PAY202502280010', 5500, NULL, '未支付', '学费和住宿费');

-- 执行说明：
-- 1. 将此SQL文件导入到您的数据库管理工具中执行
-- 2. 或者复制SQL语句到管理工具的查询窗口执行
-- 3. 确保在执行前数据库连接正确，并且pay表已经存在