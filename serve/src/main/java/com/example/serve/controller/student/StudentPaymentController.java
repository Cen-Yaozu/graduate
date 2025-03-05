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
@RequestMapping("/api/student")
public class StudentPaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 获取直通车缴费数据
     */
    @GetMapping("/payment/fasttrack")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<List<PayItem>> getFastTrackPayments(@RequestParam String studentNumber) {
        try {
            if (studentNumber == null || studentNumber.trim().isEmpty()) {
                return ResponseResult.errorResult(400, "学号不能为空");
            }
            List<PayItem> payments = paymentService.getFastTrackPayments(studentNumber);
            return ResponseResult.okResult(payments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "获取缴费数据失败: " + e.getMessage());
        }
    }

    /**
     * 处理学生缴费请求
     */
    @PostMapping("/payment/pay")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Void> payForItem(@RequestBody Map<String, Object> paymentInfo) {
        try {
            String studentNumber = paymentInfo.get("studentNumber").toString();
            String amountcard = (String) paymentInfo.get("amountcard");
            String method = (String) paymentInfo.get("method");

            boolean success = paymentService.payForItem(studentNumber, amountcard, method);

            return success ? ResponseResult.okResult() : ResponseResult.errorResult(400, "支付处理失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "支付处理失败: " + e.getMessage());
        }
    }
}