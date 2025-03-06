package com.example.serve.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.PayItem;

import java.util.List;
import java.util.Map;

/**
 * 缴费项目服务接口
 * 定义了缴费项目的基本CRUD操作
 *
 * @author author
 * @since 2025-02-27
 */
public interface PaymentService {

    /**
     * 根据ID获取缴费项目
     *
     * @param paymentId 缴费项目ID
     * @return 缴费项目
     */
    PayItem getPaymentItemById(Integer paymentId);

    /**
     * 分页查询缴费项目
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<PayItem> pageItems(Page<PayItem> page, LambdaQueryWrapper<PayItem> queryWrapper);

    /**
     * 添加缴费项目
     *
     * @param paymentItem 缴费项目
     * @return 是否添加成功
     */
    boolean addPaymentItem(PayItem paymentItem);

    /**
     * 更新缴费项目
     *
     * @param paymentItem 缴费项目
     * @return 是否更新成功
     */
    boolean updatePaymentItem(PayItem paymentItem);

    /**
     * 删除缴费项目
     *
     * @param id 缴费项目ID
     * @return 是否删除成功
     */
    boolean deletePaymentItem(Integer id);

    /**
     * 处理学生缴费请求
     *
     * @param studentNumber 学号
     * @param amountcard    缴费编号
     * @param method        支付方式
     * @return 支付是否成功
     */
    boolean payForItem(String studentNumber, String amountcard, String method);

    /**
     * 根据学号查询缴费记录
     *
     * @param studentNumber 学生学号
     * @return 缴费记录
     */
    PayItem getPayItemByStudentNumber(String studentNumber);

    /**
     * 根据学生学号获取缴费记录列表
     * 
     * @param studentNumber 学生学号
     * @return 该学生的所有缴费记录列表
     */
    List<PayItem> getPaymentListByStudentNumber(String studentNumber);
}