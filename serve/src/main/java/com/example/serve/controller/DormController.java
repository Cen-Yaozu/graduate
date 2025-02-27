package com.example.serve.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.serve.common.Result;
import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import com.example.serve.service.DormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dorm")
public class DormController {

    @Autowired
    private DormService dormService;

    /**
     * 分页获取宿舍列表
     */
    @GetMapping("/list")
    public Result getDormList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        IPage<Dorm> pageResult = dormService.getDormPage(page, size, keyword);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        return Result.success(data);
    }

    /**
     * 获取所有宿舍（用于分配）
     */
    @GetMapping("/all")
    public Result getAllDorms() {
        List<Dorm> dorms = dormService.getAllDorms();
        return Result.success(dorms);
    }

    /**
     * 添加宿舍
     */
    @PostMapping("/add")
    public Result addDorm(@RequestBody Dorm dorm) {
        boolean success = dormService.addDorm(dorm);
        return success ? Result.success() : Result.error("添加宿舍失败");
    }

    /**
     * 更新宿舍
     */
    @PutMapping("/update")
    public Result updateDorm(@RequestBody Dorm dorm) {
        boolean success = dormService.updateDorm(dorm);
        return success ? Result.success() : Result.error("更新宿舍失败");
    }

    /**
     * 删除宿舍
     */
    @DeleteMapping("/delete")
    public Result deleteDorm(
            @RequestParam String dormitory,
            @RequestParam String dormCard) {
        boolean success = dormService.deleteDorm(dormitory, dormCard);
        return success ? Result.success() : Result.error("删除宿舍失败，请确保宿舍中没有学生");
    }

    /**
     * 获取所有学生
     */
    @GetMapping("/students")
    public Result getAllStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String majorname,
            @RequestParam(required = false) Integer classroomId) {
        IPage<Student> pageResult = dormService.getAllStudents(page, size, department, majorname, classroomId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        return Result.success(data);
    }

    /**
     * 获取未分配宿舍的学生
     */
    @GetMapping("/unassigned-students")
    public Result getUnassignedStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String majorname,
            @RequestParam(required = false) Integer classroomId) {
        IPage<Student> pageResult = dormService.getUnassignedStudents(page, size, department, majorname, classroomId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        return Result.success(data);
    }

    /**
     * 获取已分配宿舍的学生
     */
    @GetMapping("/assigned-students")
    public Result getAssignedStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String majorname,
            @RequestParam(required = false) Integer classroomId) {
        IPage<Student> pageResult = dormService.getAssignedStudents(page, size, department, majorname, classroomId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        return Result.success(data);
    }

    /**
     * 批量分配学生到宿舍
     */
    @PostMapping("/assign")
    public Result assignStudentsToDorm(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> studentNumbers = (List<String>) params.get("studentNumbers");
        String dormType = (String) params.get("dormType");
        String dormitory = (String) params.get("dormitory");
        String dormCard = (String) params.get("dormCard");

        if (studentNumbers == null || studentNumbers.isEmpty() || dormitory == null || dormCard == null) {
            return Result.error("参数错误");
        }

        boolean success = dormService.assignStudentsToDorm(studentNumbers, dormType, dormitory, dormCard);
        return success ? Result.success() : Result.error("分配失败，请检查宿舍容量");
    }

    /**
     * 获取所有宿舍楼
     */
    @GetMapping("/buildings")
    public Result getDormBuildings() {
        List<String> buildings = dormService.getAllDormBuildings();
        return Result.success(buildings);
    }

    /**
     * 获取可用宿舍
     */
    @GetMapping("/available")
    public Result getAvailableDorms(
            @RequestParam(required = false) String dormType,
            @RequestParam(required = false) String dormsex,
            @RequestParam(required = false) String dormitory) {
        List<Dorm> dorms = dormService.getAvailableDorms(dormType, dormsex, dormitory);
        return Result.success(dorms);
    }

    /**
     * 批量分配学生到宿舍
     */
    @PostMapping("/batch-assign")
    public Result batchAssignStudentsToDorm(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> assignments = (List<Map<String, Object>>) params.get("assignments");
        
        if (assignments == null || assignments.isEmpty()) {
            return Result.error("参数错误");
        }
        
        boolean success = dormService.batchAssignStudentsToDorm(assignments);
        return success ? Result.success() : Result.error("批量分配失败，请检查宿舍容量");
    }

    /**
     * 获取筛选选项
     */
    @GetMapping("/filter-options")
    public Result getFilterOptions() {
        Map<String, Object> data = new HashMap<>();
        data.put("departments", dormService.getAllDepartments());
        data.put("majors", dormService.getAllMajors());
        data.put("classrooms", dormService.getAllClassrooms());
        return Result.success(data);
    }

    /**
     * 从宿舍移除学生（重新分配）
     */
    @PostMapping("/remove-student")
    public Result removeStudentFromDorm(@RequestBody Map<String, Object> params) {
        String studentNumber = (String) params.get("studentNumber");
        
        if (studentNumber == null) {
            return Result.error("参数错误");
        }
        
        boolean success = dormService.removeStudentFromDorm(studentNumber);
        return success ? Result.success() : Result.error("移除学生失败");
    }
} 