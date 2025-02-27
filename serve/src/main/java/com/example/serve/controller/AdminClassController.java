package com.example.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.Classroom;
import com.example.serve.pojo.Major;
import com.example.serve.pojo.Student;
import com.example.serve.service.ClassroomService;
import com.example.serve.service.MajorService;
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
 * 管理员-班级管理控制器
 *
 * @author author
 * @since 2025-03-01
 */
@RestController
@RequestMapping("/api/admin/class")
@PreAuthorize("hasRole('ADMIN')")
public class AdminClassController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private StudentService studentService;

    /**
     * 获取所有系别信息
     */
    @GetMapping("/departments")
    public ResponseResult<List<String>> getDepartments() {
        List<String> departments = majorService.getAllDepartments();
        return ResponseResult.okResult(departments);
    }

    /**
     * 获取专业列表
     */
    @GetMapping("/majors")
    public ResponseResult<List<Major>> getMajors(
            @RequestParam(value = "department", required = false) String department) {
        LambdaQueryWrapper<Major> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(department)) {
            queryWrapper.eq(Major::getDepartment, department);
        }
        List<Major> majors = majorService.list(queryWrapper);
        return ResponseResult.okResult(majors);
    }

    /**
     * 获取班级列表（分页）
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getClassList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "majorid", required = false) String majorid) {
        
        // 构建查询条件
        LambdaQueryWrapper<Classroom> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(department)) {
            queryWrapper.eq(Classroom::getDepartment, department);
        }
        if (StringUtils.hasText(majorid)) {
            queryWrapper.eq(Classroom::getMajorid, majorid);
        }
        
        // 分页查询
        IPage<Classroom> pageData = classroomService.page(new Page<>(page, size), queryWrapper);
        
        // 查询班级对应的专业名称
        List<Classroom> classrooms = pageData.getRecords();
        for (Classroom classroom : classrooms) {
            Major major = majorService.getByMajorid(classroom.getMajorid().toString());
            if (major != null) {
                Map<String, Object> extraData = new HashMap<>();
                extraData.put("majorName", major.getMajorname());
                classroom.setExtraData(extraData);
            }
        }
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("list", classrooms);
        
        return ResponseResult.okResult(result);
    }

    /**
     * 获取未分配班级的学生列表
     */
    @GetMapping("/unassigned-students")
    public ResponseResult<Map<String, Object>> getUnassignedStudents(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "majorname", required = false) String majorname) {
        
        // 构建查询条件
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("department", department);
        params.put("majorname", majorname);
        params.put("assignType", "unassigned"); // 指定类型为未分配
        
        // 调用Service获取未分配班级的学生
        Map<String, Object> result = studentService.getStudentsByAssignStatus(params);
        
        return ResponseResult.okResult(result);
    }
    
    /**
     * 获取所有学生列表（含分配和未分配）
     */
    @GetMapping("/students")
    public ResponseResult<Map<String, Object>> getAllStudents(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "majorname", required = false) String majorname) {
        
        // 构建查询条件
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("department", department);
        params.put("majorname", majorname);
        params.put("assignType", "all"); // 指定类型为所有
        
        // 调用Service获取所有学生
        Map<String, Object> result = studentService.getStudentsByAssignStatus(params);
        
        return ResponseResult.okResult(result);
    }
    
    /**
     * 获取已分配班级的学生列表
     */
    @GetMapping("/assigned-students")
    public ResponseResult<Map<String, Object>> getAssignedStudents(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "majorname", required = false) String majorname) {
        
        // 构建查询条件
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("department", department);
        params.put("majorname", majorname);
        params.put("assignType", "assigned"); // 指定类型为已分配
        
        // 调用Service获取已分配班级的学生
        Map<String, Object> result = studentService.getStudentsByAssignStatus(params);
        
        return ResponseResult.okResult(result);
    }

    /**
     * 批量分配班级
     */
    @PostMapping("/assign")
    public ResponseResult<Void> assignClassroom(
            @RequestBody Map<String, Object> params) {
        List<String> studentNumbers = (List<String>) params.get("studentNumbers");
        Integer classroomId = (Integer) params.get("classroomId");
        
        boolean result = classroomService.assignStudentsToClassroom(studentNumbers, classroomId);
        
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "分配班级失败");
    }

    /**
     * 添加班级
     */
    @PostMapping("/add")
    public ResponseResult<Void> addClass(@RequestBody Classroom classroom) {
        // 获取系别名称
        Major major = majorService.getByMajorid(classroom.getMajorid().toString());
        if (major != null) {
            classroom.setDepartment(major.getDepartment());
        }
        
        boolean result = classroomService.save(classroom);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "添加班级失败");
    }

    /**
     * 更新班级信息
     */
    @PutMapping("/update")
    public ResponseResult<Void> updateClass(@RequestBody Classroom classroom) {
        boolean result = classroomService.updateById(classroom);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "更新班级信息失败");
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/{majorid}")
    public ResponseResult<Void> deleteClass(@PathVariable Integer majorid) {
        // 检查班级是否有学生
        boolean hasStudents = classroomService.hasStudentsInClassroom(majorid);
        if (hasStudents) {
            return ResponseResult.errorResult(400, "该班级存在学生，无法删除");
        }
        
        boolean result = classroomService.removeById(majorid);
        return result ? ResponseResult.okResult() : ResponseResult.errorResult(500, "删除班级失败");
    }
} 