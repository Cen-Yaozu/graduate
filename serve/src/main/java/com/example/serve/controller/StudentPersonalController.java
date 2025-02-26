package com.example.serve.controller;

import com.example.serve.pojo.Student;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 学生个人信息管理控制器
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api/personal")
public class StudentPersonalController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取学生个人信息
     */
    @GetMapping("/info/{studentNumber}")
    public ResponseResult getPersonalInfo(@PathVariable String studentNumber) {
        Student student = studentService.getStudentByNumber(studentNumber);
        return ResponseResult.okResult(student);
    }

    /**
     * 更新学生个人信息
     */
    @PutMapping("/update")
    public ResponseResult updatePersonalInfo(@RequestBody Student student) {
        boolean result = studentService.updateStudentInfo(student);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新失败");
    }
}