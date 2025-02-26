package com.example.serve.controller;

import com.example.serve.pojo.StudentDorm;
import com.example.serve.service.StudentDormService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 宿舍管理控制器
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api/dormitory")
public class DormitoryController {

    @Autowired
    private StudentDormService studentDormService;

    /**
     * 获取学生宿舍信息
     */
    @GetMapping("/info/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getDormitoryInfo(@PathVariable String studentNumber) {
        String dormitoryInfo = studentDormService.getDormitoryInfo(studentNumber);
        return ResponseResult.okResult(dormitoryInfo);
    }

    /**
     * 更新学生宿舍信息
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult updateDormitoryInfo(@RequestParam String studentNumber,
            @RequestParam String dormitory) {
        StudentDorm studentDorm = studentDormService.getStudentDormInfo(studentNumber);
        if (studentDorm != null) {
            studentDorm.setDormitory(dormitory);
            boolean result = studentDormService.updateStudentDorm(studentDorm);
            return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新宿舍信息失败");
        }
        return ResponseResult.errorResult(404, "宿舍信息不存在");
    }
}