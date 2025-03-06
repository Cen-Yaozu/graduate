package com.example.serve.controller.student;

import com.example.serve.pojo.Arrive;
import com.example.serve.service.ArriveService;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 抵校管理控制器
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api/student/arrive")
public class StudentArriveController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ArriveService arriveService;

    /**
     * 获取所有学生抵校信息
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult getAllArriveInfo() {
        List<Arrive> arrives = arriveService.findAll();
        return ResponseResult.okResult(arrives);
    }

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
     * 获取学生抵校详细信息
     */
    @GetMapping("/info/{studentNumber}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public ResponseResult getArriveInfo(@PathVariable String studentNumber) {
        Arrive arrive = arriveService.findByStudentNumber(studentNumber);
        if (arrive != null) {
            return ResponseResult.okResult(arrive);
        }
        return ResponseResult.errorResult(404, "未找到学生抵校信息");
    }

    /**
     * 更新学生抵校信息
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult updateArriveInfo(@RequestBody Arrive arrive) {
        if (arrive.getStudentNumber() == null) {
            return ResponseResult.errorResult(400, "学号不能为空");
        }
        boolean result = arriveService.updateArrive(arrive);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新抵校信息失败");
    }
}