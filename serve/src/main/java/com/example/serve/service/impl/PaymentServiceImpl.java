package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.mapper.PayItemMapper;
import com.example.serve.pojo.PayItem;
import com.example.serve.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 缴费项目服务实现类
 *
 * @author author
 * @since 2025-02-27
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PayItemMapper paymentItemMapper;

    @Override
    public List<PayItem> getFastTrackPayments(String studentNumber) {
        try {
            if (studentNumber == null || studentNumber.trim().isEmpty()) {
                return null;
            }

            // 测试代码：如果无法从数据库获取数据，则使用示例数据
            LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
            // 只获取与指定学号相关的缴费项目
            queryWrapper.eq(PayItem::getStudentNumber, studentNumber);
            // 根据缴费状态排序，未付款的在前面
            queryWrapper.orderByAsc(PayItem::getIndentStatue);

            List<PayItem> result = paymentItemMapper.selectList(queryWrapper);

            // 如果数据库没有数据，则创建示例数据
            if (result == null || result.isEmpty()) {
                result = createSamplePayItems(studentNumber);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建示例的缴费数据
     */
    private List<PayItem> createSamplePayItems(String studentNumber) {
        List<PayItem> sampleItems = new ArrayList<>();

        // 创建一条未支付的学费记录
        PayItem tuitionFee = new PayItem();
        tuitionFee.setStudentNumber(studentNumber);
        tuitionFee.setStudentName("测试学生");
        tuitionFee.setHallway("学费");
        tuitionFee.setAmountcard("TF" + studentNumber);
        tuitionFee.setAllmoney(5000);
        tuitionFee.setIndentStatue("未支付");
        tuitionFee.setRemark("2024年秋季学期学费");
        sampleItems.add(tuitionFee);

        // 创建一条已支付的住宿费记录
        PayItem accommodationFee = new PayItem();
        accommodationFee.setStudentNumber(studentNumber);
        accommodationFee.setStudentName("测试学生");
        accommodationFee.setHallway("住宿费");
        accommodationFee.setAmountcard("AF" + studentNumber);
        accommodationFee.setAllmoney(1500);
        accommodationFee.setIndentStatue("已支付");
        accommodationFee.setRemark("2024年秋季学期住宿费");
        sampleItems.add(accommodationFee);

        // 创建一条未支付的书本费记录
        PayItem bookFee = new PayItem();
        bookFee.setStudentNumber(studentNumber);
        bookFee.setStudentName("测试学生");
        bookFee.setHallway("书本费");
        bookFee.setAmountcard("BF" + studentNumber);
        bookFee.setAllmoney(800);
        bookFee.setIndentStatue("未支付");
        bookFee.setRemark("2024年秋季学期教材费");
        sampleItems.add(bookFee);

        return sampleItems;
    }

    @Override
    public PayItem getPaymentItemById(Integer paymentId) {
        try {
            return paymentItemMapper.selectById(paymentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IPage<PayItem> pageItems(Page<PayItem> page, LambdaQueryWrapper<PayItem> queryWrapper) {
        try {
            return paymentItemMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回空页
            return new Page<>();
        }
    }

    @Override
    public boolean addPaymentItem(PayItem paymentItem) {
        try {
            return paymentItemMapper.insert(paymentItem) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePaymentItem(PayItem paymentItem) {
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
    public boolean payForItem(Integer studentNumber, String amountcard, String method) {
        try {
            // 根据缴费编号查找缴费项目
            LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PayItem::getAmountcard, amountcard);
            PayItem payItem = paymentItemMapper.selectOne(queryWrapper);

            if (payItem == null) {
                return false;
            }

            // 更新缴费状态
            payItem.setIndentStatue("已支付");
            payItem.setRemark(payItem.getRemark() != null ? payItem.getRemark() + " (" + method + ")" : method);

            return paymentItemMapper.updateById(payItem) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据学号查询缴费记录
     *
     * @param studentNumber 学生学号
     * @return 缴费记录
     */
    @Override
    public PayItem getPayItemByStudentNumber(String studentNumber) {
        try {
            LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PayItem::getStudentNumber, studentNumber);
            // 使用缴费编号字段排序，获取最新的记录
            queryWrapper.orderByDesc(PayItem::getAmountcard);
            List<PayItem> records = paymentItemMapper.selectList(queryWrapper);
            return records != null && !records.isEmpty() ? records.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}