package com.example.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.payment.PaymentItem;
import com.example.serve.pojo.payment.PaymentRecord;
import com.example.serve.service.PaymentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-缴费管理控制器
 *
 * @author author
 * @since 2025-02-28
 */
@RestController
@RequestMapping("/api/admin/payment")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 获取缴费项目列表
     */
    @GetMapping("/items")
    public ResponseResult<Map<String, Object>> getPaymentItems(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword) {

        // 构建查询条件
        LambdaQueryWrapper<PaymentItem> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(PaymentItem::getName, keyword)
                    .or().like(PaymentItem::getGrade, keyword)
                    .or().like(PaymentItem::getMajor, keyword);
        }

        // 分页查询
        IPage<PaymentItem> pageData = paymentService.pageItems(new Page<>(page, size), queryWrapper);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("list", pageData.getRecords());

        return ResponseResult.okResult(result);
    }

    /**
     * 添加缴费项目
     */
    @PostMapping("/item/add")
    public ResponseResult<Void> addPaymentItem(@RequestBody PaymentItem paymentItem) {
        boolean result = paymentService.addPaymentItem(paymentItem);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "添加缴费项目失败");
    }

    /**
     * 更新缴费项目
     */
    @PutMapping("/item/update")
    public ResponseResult<Void> updatePaymentItem(@RequestBody PaymentItem paymentItem) {
        boolean result = paymentService.updatePaymentItem(paymentItem);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新缴费项目失败");
    }

    /**
     * 删除缴费项目
     */
    @DeleteMapping("/item/{id}")
    public ResponseResult<Void> deletePaymentItem(@PathVariable Integer id) {
        boolean result = paymentService.deletePaymentItem(id);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "删除缴费项目失败");
    }

    /**
     * 获取缴费记录列表
     */
    @GetMapping("/records")
    public ResponseResult<Map<String, Object>> getPaymentRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "studentNumber", required = false) String studentNumber,
            @RequestParam(value = "status", required = false) Integer status) {

        // 构建查询条件
        LambdaQueryWrapper<PaymentRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(studentNumber)) {
            queryWrapper.eq(PaymentRecord::getStudentNumber, studentNumber);
        }
        if (status != null) {
            queryWrapper.eq(PaymentRecord::getStatus, status);
        }
        queryWrapper.orderByDesc(PaymentRecord::getPaymentDate);

        // 分页查询
        IPage<PaymentRecord> pageData = paymentService.pageRecords(new Page<>(page, size), queryWrapper);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("list", pageData.getRecords());

        return ResponseResult.okResult(result);
    }

    /**
     * 获取缴费统计信息
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getPaymentStats() {
        Map<String, Object> stats = paymentService.getPaymentStats();
        return ResponseResult.okResult(stats);
    }

    /**
     * 更新缴费记录状态
     */
    @PutMapping("/record/status")
    public ResponseResult<Void> updatePaymentRecordStatus(
            @RequestParam Integer recordId,
            @RequestParam Integer status) {
        boolean result = paymentService.updatePaymentRecordStatus(recordId, status);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新缴费记录状态失败");
    }

    /**
     * 导出缴费记录
     */
    @GetMapping("/export")
    public ResponseResult<String> exportPaymentRecords(
            @RequestParam(value = "studentNumber", required = false) String studentNumber,
            @RequestParam(value = "status", required = false) Integer status) {

        // 实际项目中应该返回文件下载URL
        String downloadUrl = "http://example.com/download/payment-records.xlsx";
        return ResponseResult.okResult(downloadUrl);
    }
}