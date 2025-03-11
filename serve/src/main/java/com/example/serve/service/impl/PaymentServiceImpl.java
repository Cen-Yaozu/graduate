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
import java.util.Map;

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
            if (paymentItem == null) {
                return false;
            }

            // 设置查询条件
            LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
            
            // 如果有缴费编号，优先使用缴费编号查询
            if (paymentItem.getAmountcard() != null && !paymentItem.getAmountcard().isEmpty()) {
                queryWrapper.eq(PayItem::getAmountcard, paymentItem.getAmountcard());
                
                // 查询是否存在该编号的记录
                Long count = paymentItemMapper.selectCount(queryWrapper);
                
                // 如果不存在该编号的记录，尝试使用学号查询
                if (count == 0 && paymentItem.getStudentNumber() != null && !paymentItem.getStudentNumber().isEmpty()) {
                    // 使用学号查询是否存在记录
                    queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(PayItem::getStudentNumber, paymentItem.getStudentNumber());
                    
                    // 查询学号对应的记录数
                    Long studentCount = paymentItemMapper.selectCount(queryWrapper);
                    
                    if (studentCount > 0) {
                        // 存在学号对应的记录，直接使用这个条件更新
                        return paymentItemMapper.update(paymentItem, queryWrapper) > 0;
                    } else {
                        // 既没有找到该编号的记录，也没有找到该学号的记录
                        // 说明这是一个全新的记录，需要插入而不是更新
                        return paymentItemMapper.insert(paymentItem) > 0;
                    }
                }
            } else if (paymentItem.getStudentNumber() != null && !paymentItem.getStudentNumber().isEmpty()) {
                // 没有缴费编号但有学号，按学号查询
                queryWrapper.eq(PayItem::getStudentNumber, paymentItem.getStudentNumber());
            } else {
                // 既没有缴费编号也没有学号，无法更新
                return false;
            }
            
            // 执行更新
            int result = paymentItemMapper.update(paymentItem, queryWrapper);
            return result > 0;
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
    public boolean payForItem(String studentNumber, String amountcard, String method) {
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

            // 直接调用更新方法即可
            return updatePaymentItem(payItem);
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

    /**
     * 根据学生学号获取缴费记录列表
     *
     * @param studentNumber 学生学号
     * @return 该学生的所有缴费记录列表
     */
    @Override
    public List<PayItem> getPaymentListByStudentNumber(String studentNumber) {
        // 使用MyBatis-Plus的LambdaQueryWrapper查询指定学号的所有缴费记录
        LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayItem::getStudentNumber, studentNumber);
        // 按照缴费编号降序排序，最新的记录在前面
        queryWrapper.orderByDesc(PayItem::getAmountcard);
        
        // 执行查询并返回结果列表
        return paymentItemMapper.selectList(queryWrapper);
    }

    /**
     * 根据缴费编号查询缴费记录
     *
     * @param amountcard 缴费编号
     * @return 缴费记录
     */
    @Override
    public PayItem getPayItemByAmountcard(String amountcard) {
        try {
            if (amountcard == null || amountcard.isEmpty()) {
                return null;
            }
            LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PayItem::getAmountcard, amountcard);
            return paymentItemMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}