package com.example.serve.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.Student;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-学生管理控制器
 *
 * @author author
 * @since 2025-02-28
 */
@RestController
@RequestMapping("/api/admin/student")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页获取学生列表
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getStudentList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword) {

        // 构建查询条件
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Student::getStudentName, keyword)
                    .or().like(Student::getStudentNumber, keyword)
                    .or().like(Student::getMajorname, keyword);
        }

        // 分页查询
        IPage<Student> pageData = studentService.page(new Page<>(page, size), queryWrapper);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("list", pageData.getRecords());

        return ResponseResult.okResult(result);
    }

    /**
     * 获取学生详情
     */
    @GetMapping("/{studentNumber}")
    public ResponseResult<Student> getStudentDetail(@PathVariable String studentNumber) {
        Student student = studentService.getStudentByNumber(studentNumber);
        if (student != null) {
            return ResponseResult.okResult(student);
        }
        return ResponseResult.errorResult(404, "学生不存在");
    }

    /**
     * 添加学生
     */
    @PostMapping("/add")
    public ResponseResult<Void> addStudent(@RequestBody Student student) {
        boolean result = studentService.save(student);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "添加学生失败");
    }

    /**
     * 更新学生信息
     */
    @PutMapping("/update")
    public ResponseResult<Void> updateStudent(@RequestBody Student student) {
        boolean result = studentService.updateStudentInfo(student);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新学生信息失败");
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{studentNumber}")
    public ResponseResult<Void> deleteStudent(@PathVariable String studentNumber) {
        // 先查询学生信息
        Student student = studentService.getStudentByNumber(studentNumber);
        if (student == null) {
            return ResponseResult.errorResult(404, "学生不存在");
        }

        // 删除学生
        boolean result = studentService.removeById(student.getId());
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "删除学生失败");
    }

    /**
     * 批量导入学生
     */
    @PostMapping("/import")
    public ResponseResult<Map<String, Object>> importStudents(@RequestBody List<Student> students) {
        int success = 0;
        int fail = 0;

        for (Student student : students) {
            try {
                boolean result = studentService.save(student);
                if (result) {
                    success++;
                } else {
                    fail++;
                }
            } catch (Exception e) {
                fail++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("fail", fail);

        return ResponseResult.okResult(result);
    }
}