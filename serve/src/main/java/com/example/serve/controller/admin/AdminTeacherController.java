package com.example.serve.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.tools.ResponseResult;
import com.example.serve.pojo.Teacher;
import com.example.serve.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 教师管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/teacher")
public class AdminTeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 分页查询教师列表
     * 
     * @param current 当前页码
     * @param size    每页大小
     * @param keyword 搜索关键词（工号或姓名）
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseResult<Page<Teacher>> getTeacherPage(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("分页查询教师列表，当前页：{}，每页大小：{}，关键词：{}", current, size, keyword);
        Page<Teacher> page = teacherService.getTeacherPage(current, size, keyword);
        return ResponseResult.okResult(page);
    }

    /**
     * 根据工号查询教师
     * 
     * @param id 教师工号
     * @return 教师信息
     */
    @GetMapping("/{id}")
    public ResponseResult<Teacher> getTeacherById(@PathVariable("id") Integer id) {
        log.info("根据工号查询教师，工号：{}", id);
        Teacher teacher = teacherService.getById(id);
        if (teacher != null) {
            return ResponseResult.okResult(teacher);
        }
        return ResponseResult.errorResult(404, "未找到该教师");
    }

    /**
     * 添加教师
     * 
     * @param teacher 教师信息
     * @return 操作结果
     */
    @PostMapping
    public ResponseResult<String> addTeacher(@RequestBody Teacher teacher) {
        log.info("添加教师，教师信息：{}", teacher);
        boolean result = teacherService.addTeacher(teacher);
        if (result) {
            return ResponseResult.okResult("添加教师成功");
        }
        return ResponseResult.errorResult(500, "添加教师失败");
    }

    /**
     * 更新教师信息
     * 
     * @param teacher 教师信息
     * @return 操作结果
     */
    @PutMapping
    public ResponseResult<String> updateTeacher(@RequestBody Teacher teacher) {
        log.info("更新教师，教师信息：{}", teacher);
        boolean result = teacherService.updateTeacher(teacher);
        if (result) {
            return ResponseResult.okResult("更新教师成功");
        }
        return ResponseResult.errorResult(500, "更新教师失败");
    }

    /**
     * 删除教师
     * 
     * @param id 教师工号
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteTeacher(@PathVariable("id") Integer id) {
        log.info("删除教师，工号：{}", id);
        boolean result = teacherService.deleteTeacher(id);
        if (result) {
            return ResponseResult.okResult("删除教师成功");
        }
        return ResponseResult.errorResult(500, "删除教师失败");
    }
}