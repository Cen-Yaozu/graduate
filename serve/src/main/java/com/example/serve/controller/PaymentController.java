package com.example.serve.controller;

import com.example.serve.pojo.Student;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 缴费管理控制器
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取学生缴费状态
     */
    @GetMapping("/status/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getPaymentStatus(@PathVariable String studentNumber) {
        Integer status = studentService.getPaymentStatus(studentNumber);
        if (status != null) {
            return ResponseResult.okResult(status);
        }
        return ResponseResult.errorResult(404, "未找到学生缴费信息");
    }

    /**
     * 更新学生缴费状态
     */
    @PutMapping("/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult updatePaymentStatus(@RequestParam String studentNumber,
                                            @RequestParam Integer status) {
        Student student = studentService.getStudentByNumber(studentNumber);
        if (student != null) {
            student.setPaymentStatus(status);
            boolean result = studentService.updateStudentInfo(student);
            return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新缴费状态失败");
        }
        return ResponseResult.errorResult(404, "学生信息不存在");
    }
}