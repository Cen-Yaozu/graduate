package com.example.serve.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.payment.PaymentItem;
import com.example.serve.pojo.payment.PaymentRecord;

import java.util.List;
import java.util.Map;

/**
 * 缴费服务接口
 * 定义了学生缴费的相关功能
 *
 * @author author
 * @since 2025-02-27
 */
public interface PaymentService {

    /**
     * 获取学生未支付的缴费项目
     *
     * @param studentNumber 学号
     * @return 未支付的缴费项目列表
     */
    List<Map<String, Object>> getUnpaidItems(String studentNumber);

    /**
     * 获取学生缴费历史记录
     *
     * @param studentNumber 学号
     * @return 缴费历史记录列表
     */
    List<Map<String, Object>> getPaymentHistory(String studentNumber);

    /**
     * 学生支付缴费项目
     *
     * @param studentNumber 学号
     * @param paymentId     缴费项目ID
     * @param method        支付方式
     * @return 是否支付成功
     */
    boolean payForItem(String studentNumber, Integer paymentId, String method);

    /**
     * 根据ID获取缴费项目
     *
     * @param paymentId 缴费项目ID
     * @return 缴费项目
     */
    PaymentItem getPaymentItemById(Integer paymentId);

    /**
     * 添加缴费记录
     *
     * @param paymentRecord 缴费记录
     * @return 是否添加成功
     */
    boolean addPaymentRecord(PaymentRecord paymentRecord);

    /**
     * 分页查询缴费项目
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<PaymentItem> pageItems(Page<PaymentItem> page, LambdaQueryWrapper<PaymentItem> queryWrapper);

    /**
     * 添加缴费项目
     *
     * @param paymentItem 缴费项目
     * @return 是否添加成功
     */
    boolean addPaymentItem(PaymentItem paymentItem);

    /**
     * 更新缴费项目
     *
     * @param paymentItem 缴费项目
     * @return 是否更新成功
     */
    boolean updatePaymentItem(PaymentItem paymentItem);

    /**
     * 删除缴费项目
     *
     * @param id 缴费项目ID
     * @return 是否删除成功
     */
    boolean deletePaymentItem(Integer id);

    /**
     * 分页查询缴费记录
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<PaymentRecord> pageRecords(Page<PaymentRecord> page, LambdaQueryWrapper<PaymentRecord> queryWrapper);

    /**
     * 获取缴费统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getPaymentStats();

    /**
     * 更新缴费记录状态
     *
     * @param recordId 记录ID
     * @param status   状态
     * @return 是否更新成功
     */
    boolean updatePaymentRecordStatus(Integer recordId, Integer status);
}