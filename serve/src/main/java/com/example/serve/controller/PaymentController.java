package com.example.serve.controller;

import com.example.serve.pojo.Student;
import com.example.serve.service.PaymentService;
import com.example.serve.service.StudentService;
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
public class PaymentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PaymentService paymentService;

    /**
     * 获取学生缴费状态
     */
    @GetMapping("/payment/status/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Integer> getPaymentStatus(@PathVariable String studentNumber) {
        Integer status = studentService.getPaymentStatus(studentNumber);
        if (status != null) {
            return ResponseResult.okResult(status);
        }
        return ResponseResult.errorResult(404, "未找到学生缴费信息");
    }

    /**
     * 更新学生缴费状态
     */
    @PutMapping("/payment/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Void> updatePaymentStatus(@RequestParam String studentNumber,
            @RequestParam Integer status) {
        Student student = studentService.getStudentByNumber(studentNumber);
        if (student != null) {
            student.setPaymentStatus(status);
            boolean result = studentService.updateStudentInfo(student);
            return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新缴费状态失败");
        }
        return ResponseResult.errorResult(404, "学生信息不存在");
    }

    /**
     * 获取学生未支付的缴费项目
     */
    @GetMapping("/student/payment/unpaid/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<List<Map<String, Object>>> getUnpaidItems(@PathVariable String studentNumber) {
        try {
            List<Map<String, Object>> unpaidItems = paymentService.getUnpaidItems(studentNumber);
            return ResponseResult.okResult(unpaidItems);
        } catch (Exception e) {
            return ResponseResult.okResult(new java.util.ArrayList<>());
        }
    }

    /**
     * 获取学生缴费历史记录
     */
    @GetMapping("/student/payment/history/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<List<Map<String, Object>>> getPaymentHistory(@PathVariable String studentNumber) {
        try {
            List<Map<String, Object>> paymentHistory = paymentService.getPaymentHistory(studentNumber);
            return ResponseResult.okResult(paymentHistory);
        } catch (Exception e) {
            return ResponseResult.okResult(new java.util.ArrayList<>());
        }
    }

    /**
     * 处理学生缴费请求
     */
    @PostMapping("/student/payment/pay")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Void> payForItem(@RequestBody Map<String, Object> paymentInfo) {
        try {
            String studentNumber = (String) paymentInfo.get("studentNumber");
            Integer paymentId = (Integer) paymentInfo.get("paymentId");
            String method = (String) paymentInfo.get("method");

            boolean success = paymentService.payForItem(studentNumber, paymentId, method);

            return success ? ResponseResult.okResult() : ResponseResult.okResult(400, "支付处理失败");
        } catch (Exception e) {
            e.printStackTrace();
            // 即使出错，也返回一个有效的响应结构，避免前端出错
            return ResponseResult.okResult(500, "支付处理失败: " + e.getMessage());
        }
    }
}