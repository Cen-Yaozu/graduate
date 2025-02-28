package com.example.serve.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.serve.common.Result;
import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import com.example.serve.service.DormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dorm")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDormController {

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
        try {
            IPage<Student> pageResult = dormService.getAllStudents(page, size, department, majorname, classroomId);

            // 使用Map存储已处理的学生，以学号为键，防止重复
            Map<String, Student> uniqueStudents = new HashMap<>();

            for (Student student : pageResult.getRecords()) {
                // 基于学号去重
                if (!uniqueStudents.containsKey(student.getStudentNumber())) {
                    uniqueStudents.put(student.getStudentNumber(), student);
                }
            }

            // 将去重后的学生数据转换为列表
            List<Student> enhancedStudents = new ArrayList<>(uniqueStudents.values());

            Map<String, Object> data = new HashMap<>();
            data.put("list", enhancedStudents);
            // 需要调整总数为去重后的数量
            data.put("total", uniqueStudents.size());

            System.out.println("查询到的学生总数(去重后): " + enhancedStudents.size());
            return Result.success(data);
        } catch (Exception e) {
            System.err.println("获取所有学生失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取所有学生失败: " + e.getMessage());
        }
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
        try {
            System.out.println("查询未分配学生参数: page=" + page + ", size=" + size +
                    ", department=" + department + ", majorname=" + majorname +
                    ", classroomId=" + classroomId);

            IPage<Student> pageResult = dormService.getUnassignedStudents(page, size, department, majorname,
                    classroomId);

            // 确保返回的学生数据包含完整字段
            List<Student> enhancedStudents = new ArrayList<>();
            // 使用Map存储已处理的学生，以学号为键，防止重复
            Map<String, Student> uniqueStudents = new HashMap<>();

            for (Student student : pageResult.getRecords()) {
                // 打印每个学生对象的姓名和性别字段，检查是否为空
                System.out.println("学生数据: id=" + student.getId() +
                        ", 学号=" + student.getStudentNumber() +
                        ", 姓名=" + (student.getStudentName() != null ? student.getStudentName() : "null") +
                        ", 性别=" + (student.getSex() != null ? student.getSex() : "null"));

                // 基于学号去重
                if (!uniqueStudents.containsKey(student.getStudentNumber())) {
                    uniqueStudents.put(student.getStudentNumber(), student);
                }
            }

            // 将去重后的学生数据转换为列表
            enhancedStudents = new ArrayList<>(uniqueStudents.values());

            Map<String, Object> data = new HashMap<>();
            data.put("list", enhancedStudents);
            // 需要调整总数为去重后的数量
            data.put("total", uniqueStudents.size());

            System.out.println("查询到的未分配学生数量(去重后): " + enhancedStudents.size());
            return Result.success(data);
        } catch (Exception e) {
            System.err.println("获取未分配学生失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取未分配学生失败: " + e.getMessage());
        }
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
        try {
            IPage<Student> pageResult = dormService.getAssignedStudents(page, size, department, majorname, classroomId);

            // 使用Map存储已处理的学生，以学号为键，防止重复
            Map<String, Student> uniqueStudents = new HashMap<>();

            for (Student student : pageResult.getRecords()) {
                // 基于学号去重
                if (!uniqueStudents.containsKey(student.getStudentNumber())) {
                    uniqueStudents.put(student.getStudentNumber(), student);
                }
            }

            // 将去重后的学生数据转换为列表
            List<Student> enhancedStudents = new ArrayList<>(uniqueStudents.values());

            Map<String, Object> data = new HashMap<>();
            data.put("list", enhancedStudents);
            // 需要调整总数为去重后的数量
            data.put("total", uniqueStudents.size());

            System.out.println("查询到的已分配学生数量(去重后): " + enhancedStudents.size());
            return Result.success(data);
        } catch (Exception e) {
            System.err.println("获取已分配学生失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取已分配学生失败: " + e.getMessage());
        }
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
        try {
            System.out.println("查询宿舍参数: 类型=" + dormType + ", 性别=" + dormsex + ", 宿舍楼=" + dormitory);
            List<Dorm> dorms = dormService.getAvailableDorms(dormType, dormsex, dormitory);
            System.out.println("查询到的宿舍数量: " + (dorms != null ? dorms.size() : "null"));
            if (dorms == null || dorms.isEmpty()) {
                return Result.success(new ArrayList<>());
            }
            return Result.success(dorms);
        } catch (Exception e) {
            System.err.println("获取可用宿舍失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取可用宿舍失败: " + e.getMessage());
        }
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
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("departments", dormService.getAllDepartments());
            data.put("majors", dormService.getAllMajors());
            data.put("classrooms", dormService.getAllClassrooms());
            System.out.println("获取筛选选项成功: " + data);
            return Result.success(data);
        } catch (Exception e) {
            System.err.println("获取筛选选项失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取筛选选项失败");
        }
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