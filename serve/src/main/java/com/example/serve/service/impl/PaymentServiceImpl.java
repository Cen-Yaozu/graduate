package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.mapper.PayItemMapper;
import com.example.serve.pojo.PayItem;
import com.example.serve.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PayItem::getHallway, "直通车")
                    .eq(PayItem::getStudentNumber, studentNumber);
            return paymentItemMapper.selectList(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            payItem.setStudentNumber(studentNumber);

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