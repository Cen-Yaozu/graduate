package com.example.serve.controller.student;

import com.example.serve.pojo.StudentDorm;
import com.example.serve.service.StudentDormService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/dorm")
public class StudentDormController {

    @Autowired
    private StudentDormService studentDormService;

    @GetMapping("/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getDormInfo(@PathVariable String studentNumber) {
        StudentDorm dormInfo = studentDormService.getStudentDormInfo(studentNumber);
        return ResponseResult.okResult(dormInfo);
    }

    @GetMapping("/roommates/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getRoommateInfo(@PathVariable String studentNumber) {
        List<Map<String, Object>> roommateList = studentDormService.getRoommateInfo(studentNumber);
        return ResponseResult.okResult(roommateList);
    }

    @PostMapping("/repair")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult submitRepair(@RequestBody Map<String, String> repairInfo) {
        String studentNumber = repairInfo.get("studentNumber");
        String dormitory = repairInfo.get("dormitory");

        // 处理报修请求的逻辑
        boolean success = studentDormService.submitRepairRequest(studentNumber, dormitory);
        if (success) {
            return ResponseResult.okResult("报修申请提交成功");
        } else {
            return ResponseResult.errorResult(500, "报修申请提交失败");
        }
    }
}