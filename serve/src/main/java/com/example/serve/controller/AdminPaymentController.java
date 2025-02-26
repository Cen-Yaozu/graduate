package com.example.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.PayItem;
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
        LambdaQueryWrapper<PayItem> queryWrapper = new LambdaQueryWrapper<>();
                                                                                                     if (StringUtils.hasText(keyword)) {
            queryWrapper.like(PayItem::getStudentName, keyword)
                    .or().like(PayItem::getHallway, keyword)
                    .or().like(PayItem::getAmountcard, keyword);
        }

        // 分页查询
        IPage<PayItem> pageData = paymentService.pageItems(new Page<>(page, size), queryWrapper);

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
    public ResponseResult<Void> addPaymentItem(@RequestBody PayItem paymentItem) {
        boolean result = paymentService.addPaymentItem(paymentItem);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "添加缴费项目失败");
    }

    /**
     * 更新缴费项目
     */
    @PutMapping("/item/update")
    public ResponseResult<Void> updatePaymentItem(@RequestBody PayItem paymentItem) {
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

}