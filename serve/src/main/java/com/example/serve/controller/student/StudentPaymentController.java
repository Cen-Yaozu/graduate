package com.example.serve.controller.student;

import com.example.serve.pojo.PayItem;
import com.example.serve.service.PaymentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 缴费管理控制器
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api")
public class StudentPaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 获取直通车缴费数据
     */
    @GetMapping("/student/payment/fasttrack")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<List<PayItem>> getFastTrackPayments() {
        try {
            List<PayItem> payments = paymentService.getFastTrackPayments();
            return ResponseResult.okResult(payments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "获取直通车缴费数据失败");
        }
    }

    /**
     * 处理学生缴费请求
     */
    @PostMapping("/student/payment/pay")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Void> payForItem(@RequestBody Map<String, Object> paymentInfo) {
        try {
            Integer studentNumber = (Integer) paymentInfo.get("studentNumber");
            String amountcard = (String) paymentInfo.get("amountcard");
            String method = (String) paymentInfo.get("method");

            boolean success = paymentService.payForItem(studentNumber, amountcard, method);

            return success ? ResponseResult.okResult() : ResponseResult.okResult(400, "支付处理失败");
        } catch (Exception e) {
            e.printStackTrace();
            // 即使出错，也返回一个有效的响应结构，避免前端出错
            return ResponseResult.okResult(500, "支付处理失败: " + e.getMessage());
        }
    }
}