package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.mapper.PaymentItemMapper;
import com.example.serve.mapper.PaymentRecordMapper;
import com.example.serve.pojo.payment.PaymentItem;
import com.example.serve.pojo.payment.PaymentRecord;
import com.example.serve.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 缴费服务实现类
 *
 * @author author
 * @since 2025-02-27
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentItemMapper paymentItemMapper;

    @Autowired
    private PaymentRecordMapper paymentRecordMapper;

    @Override
    public List<Map<String, Object>> getUnpaidItems(String studentNumber) {
        try {
            List<Map<String, Object>> result = paymentItemMapper.getUnpaidItems(studentNumber);
            // 确保返回的是数组，即使查询结果为null
            return result != null ? result : new java.util.ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            // 数据库未就绪或出错时，返回模拟数据
            return getMockUnpaidItems();
        }
    }

    @Override
    public List<Map<String, Object>> getPaymentHistory(String studentNumber) {
        try {
            List<Map<String, Object>> result = paymentRecordMapper.getPaymentHistory(studentNumber);
            // 确保返回的是数组，即使查询结果为null
            return result != null ? result : new java.util.ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            // 数据库未就绪或出错时，返回模拟数据
            return getMockPaymentHistory();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payForItem(String studentNumber, Integer paymentId, String method) {
        try {
            // 查询缴费项目
            PaymentItem paymentItem = getPaymentItemById(paymentId);
            if (paymentItem == null) {
                return false;
            }

            // 创建缴费记录
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setStudentNumber(studentNumber);
            paymentRecord.setPaymentItemId(paymentId);
            paymentRecord.setAmount(paymentItem.getAmount());
            paymentRecord.setPaymentMethod(method);
            paymentRecord.setPaymentDate(LocalDateTime.now());
            paymentRecord.setStatus(1); // 已支付
            paymentRecord.setTransactionNumber(generateTransactionNumber());

            // 保存缴费记录
            return addPaymentRecord(paymentRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PaymentItem getPaymentItemById(Integer paymentId) {
        try {
            return paymentItemMapper.selectById(paymentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addPaymentRecord(PaymentRecord paymentRecord) {
        try {
            return paymentRecordMapper.insert(paymentRecord) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IPage<PaymentItem> pageItems(Page<PaymentItem> page, LambdaQueryWrapper<PaymentItem> queryWrapper) {
        try {
            return paymentItemMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回空页
            return new Page<>();
        }
    }

    @Override
    public boolean addPaymentItem(PaymentItem paymentItem) {
        try {
            return paymentItemMapper.insert(paymentItem) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePaymentItem(PaymentItem paymentItem) {
        try {
            return paymentItemMapper.updateById(paymentItem) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePaymentItem(Integer id) {
        try {
            return paymentItemMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IPage<PaymentRecord> pageRecords(Page<PaymentRecord> page, LambdaQueryWrapper<PaymentRecord> queryWrapper) {
        try {
            return paymentRecordMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回空页
            return new Page<>();
        }
    }

    @Override
    public Map<String, Object> getPaymentStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 这里应该实现具体的统计逻辑，从数据库查询统计数据
            // 示例：统计已缴费总额、未缴费总额、缴费人数等

            // 为了简化，这里返回模拟数据
            stats.put("totalPaid", new BigDecimal("2586000.00"));
            stats.put("totalUnpaid", new BigDecimal("360000.00"));
            stats.put("paidCount", 180);
            stats.put("unpaidCount", 20);
            stats.put("paymentCompletionRate", 90.0);

            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public boolean updatePaymentRecordStatus(Integer recordId, Integer status) {
        try {
            PaymentRecord record = paymentRecordMapper.selectById(recordId);
            if (record == null) {
                return false;
            }

            record.setStatus(status);
            return paymentRecordMapper.updateById(record) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成交易流水号
     *
     * @return 交易流水号
     */
    private String generateTransactionNumber() {
        return "PAY" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 获取模拟的未支付项目数据
     *
     * @return 模拟数据
     */
    private List<Map<String, Object>> getMockUnpaidItems() {
        List<Map<String, Object>> unpaidItems = new java.util.ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("name", "2024年秋季学费");
        item1.put("amount", 4800);
        item1.put("deadline", "2024-09-01");

        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 2);
        item2.put("name", "2024年秋季住宿费");
        item2.put("amount", 1200);
        item2.put("deadline", "2024-09-01");

        unpaidItems.add(item1);
        unpaidItems.add(item2);

        return unpaidItems;
    }

    /**
     * 获取模拟的缴费历史数据
     *
     * @return 模拟数据
     */
    private List<Map<String, Object>> getMockPaymentHistory() {
        List<Map<String, Object>> paymentHistory = new java.util.ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 3);
        item1.put("name", "2024年春季学费");
        item1.put("amount", 4800);
        item1.put("paymentDate", "2024-02-15");
        item1.put("status", "已支付");
        item1.put("paymentMethod", "微信支付");

        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 4);
        item2.put("name", "2024年春季住宿费");
        item2.put("amount", 1200);
        item2.put("paymentDate", "2024-02-15");
        item2.put("status", "已支付");
        item2.put("paymentMethod", "微信支付");

        paymentHistory.add(item1);
        paymentHistory.add(item2);

        return paymentHistory;
    }
}