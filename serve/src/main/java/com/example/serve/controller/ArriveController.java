package com.example.serve.controller;

import com.example.serve.pojo.Student;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 抵校管理控制器
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api/arrive")
public class ArriveController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取学生抵校状态
     */
    @GetMapping("/status/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getArriveStatus(@PathVariable String studentNumber) {
        Integer status = studentService.getArriveStatus(studentNumber);
        if (status != null) {
            return ResponseResult.okResult(status);
        }
        return ResponseResult.errorResult(404, "未找到学生抵校信息");
    }

    /**
     * 更新学生抵校状态
     */
    @PutMapping("/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult updateArriveStatus(@RequestParam String studentNumber,
            @RequestParam Integer status) {
        Student student = studentService.getStudentByNumber(studentNumber);
        if (student != null) {
            // 假设Student类中的抵校状态字段名为arriveStatus
            student.setArriveStatus(status); // 请确保Student类中有此方法
            boolean result = studentService.updateStudentInfo(student);
            return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新抵校状态失败");
        }
        return ResponseResult.errorResult(404, "学生信息不存在");
    }
}