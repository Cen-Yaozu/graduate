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
 * 学生缴费控制器
 * 处理学生缴费相关的功能
 */
@RestController
@RequestMapping("/api/student")
public class StudentPaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 简单获取学生缴费记录
     * 直接通过学号查询pay表中的数据
     * @param studentNumber 学生学号
     * @return 缴费记录列表
     */
    @GetMapping("/payment")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<List<PayItem>> getStudentPayments(@RequestParam String studentNumber) {
        List<PayItem> payments = paymentService.getPaymentListByStudentNumber(studentNumber);
        return ResponseResult.okResult(payments);
    }

    /**
     * 支付缴费项目
     * @param paymentInfo 支付信息
     * @return 支付结果
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