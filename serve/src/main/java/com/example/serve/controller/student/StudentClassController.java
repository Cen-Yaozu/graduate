package com.example.serve.controller.student;

import com.example.serve.controller.BaseController;
import com.example.serve.service.ClassService;
import com.example.serve.tools.ResponseResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/class")
public class StudentClassController extends BaseController {

    @Autowired
    private ClassService classService;

    @GetMapping("/majors")
    public ResponseResult<Object> getAllMajors() {
        try {
            return new ResponseResult<>(200, "获取专业信息成功", classService.getAllMajors());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(500, "获取专业信息失败");
        }
    }

    @GetMapping("/classes")
    public ResponseResult<Object> getAllClasses() {
        try {
            return new ResponseResult<>(200, "获取班级信息成功", classService.getAllClasses());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(500, "获取班级信息失败");
        }
    }
}