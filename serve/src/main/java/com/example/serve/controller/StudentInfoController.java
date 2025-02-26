package com.example.serve.controller;

import com.example.serve.command.StudentInfoCommand;
import com.example.serve.pojo.Family;
import com.example.serve.pojo.Resume;
import com.example.serve.pojo.Student;
import com.example.serve.service.StudentInfoService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生信息控制器 - 门面模式的门面
 */
@RestController
@RequestMapping("/api/student")
public class StudentInfoController {
    
    @Autowired
    private StudentInfoService studentInfoService;
    
    /**
     * 获取学生基本信息
     */
    @GetMapping("/info/{studentNumber}")
    public ResponseResult<Student> getStudentInfo(@PathVariable String studentNumber) {
        return executeCommand(() -> studentInfoService.getStudentInfo(studentNumber));
    }
    
    /**
     * 更新学生基本信息
     */
    @PutMapping("/info/update")
    public ResponseResult<Void> updateStudentInfo(@RequestBody Student student) {
        return executeCommand(() -> studentInfoService.updateStudentInfo(student));
    }
    
    /**
     * 获取学生简历信息
     */
    @GetMapping("/resume/{studentNumber}")
    public ResponseResult<Resume> getResumeInfo(@PathVariable String studentNumber) {
        return executeCommand(() -> studentInfoService.getResumeInfo(studentNumber));
    }
    
    /**
     * 更新学生简历信息
     */
    @PutMapping("/resume/update")
    public ResponseResult<Void> updateResumeInfo(@RequestBody Resume resume) {
        return executeCommand(() -> studentInfoService.updateResumeInfo(resume));
    }
    
    /**
     * 获取学生家庭信息
     */
    @GetMapping("/family/{studentNumber}")
    public ResponseResult<List<Family>> getFamilyInfo(@PathVariable String studentNumber) {
        return executeCommand(() -> studentInfoService.getFamilyInfo(studentNumber));
    }
    
    /**
     * 添加学生家庭成员信息
     */
    @PostMapping("/family/add")
    public ResponseResult<Void> addFamilyMember(@RequestBody Family family) {
        return executeCommand(() -> studentInfoService.addFamilyMember(family));
    }
    
    /**
     * 更新学生家庭成员信息
     */
    @PutMapping("/family/update")
    public ResponseResult<Void> updateFamilyMember(@RequestBody Family family) {
        return executeCommand(() -> studentInfoService.updateFamilyMember(family));
    }
    
    /**
     * 删除学生家庭成员信息
     */
    @DeleteMapping("/family/delete/{id}")
    public ResponseResult<Void> deleteFamilyMember(@PathVariable Integer id) {
        return executeCommand(() -> studentInfoService.deleteFamilyMember(id));
    }
    
    /**
     * 执行命令的通用方法 - 命令模式的调用者
     */
    private <T> ResponseResult<T> executeCommand(StudentInfoCommand<T> command) {
        try {
            return command.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(500, "操作失败：" + e.getMessage());
        }
    }
} 