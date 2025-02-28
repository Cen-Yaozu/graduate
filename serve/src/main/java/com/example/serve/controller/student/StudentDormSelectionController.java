package com.example.serve.controller.student;

import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.StudentDorm;
import com.example.serve.service.DormService;
import com.example.serve.service.StudentDormService;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生宿舍选择控制器
 * 处理学生宿舍类型选择和提交的相关请求
 *
 * @author author
 * @since 2025-02-23
 */
@RestController
@RequestMapping("/api/student/dorm-selection")
public class StudentDormSelectionController {

    @Autowired
    private DormService dormService;

    @Autowired
    private StudentDormService studentDormService;

    @Autowired
    private StudentService studentService;

    /**
     * 获取可用的宿舍类型列表
     * 返回所有可供选择的宿舍类型及其基本信息
     */
    @GetMapping("/types")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getDormTypes() {
        try {
            // 获取所有可用的宿舍类型
            List<Dorm> allDorms = dormService.getAvailableDorms(null, null, null);

            // 按宿舍类型分组，计算每种类型的剩余数量
            Map<String, Map<String, Object>> dormTypeInfoMap = new HashMap<>();

            for (Dorm dorm : allDorms) {
                String type = dorm.getDormType();

                if (!dormTypeInfoMap.containsKey(type)) {
                    Map<String, Object> typeInfo = new HashMap<>();
                    typeInfo.put("type", type);
                    typeInfo.put("price", dorm.getDormPrize());
                    typeInfo.put("availableCount", 0);
                    typeInfo.put("description", getDormTypeDescription(type));
                    dormTypeInfoMap.put(type, typeInfo);
                }

                // 增加可用数量
                Map<String, Object> typeInfo = dormTypeInfoMap.get(type);
                int currentCount = (int) typeInfo.get("availableCount");
                typeInfo.put("availableCount", currentCount + dorm.getDormNum());
            }

            return ResponseResult.okResult(new ArrayList<>(dormTypeInfoMap.values()));
        } catch (Exception e) {
            return ResponseResult.errorResult(500, "获取宿舍类型失败: " + e.getMessage());
        }
    }

    /**
     * 提交学生宿舍类型选择
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult submitDormSelection(@RequestBody Map<String, String> selectionData) {
        try {
            String studentNumber = selectionData.get("studentNumber");
            String dormType = selectionData.get("dormType");

            if (studentNumber == null || dormType == null) {
                return ResponseResult.errorResult(400, "学号和宿舍类型不能为空");
            }

            // 查询学生是否已经有宿舍分配
            StudentDorm existingDorm = studentDormService.getStudentDormInfo(studentNumber);
            if (existingDorm != null && existingDorm.getDormitory() != null) {
                return ResponseResult.errorResult(400, "您已有宿舍分配，无法再次选择");
            }

            // 查询该类型宿舍是否还有空余
            List<Dorm> availableDorms = dormService.getAvailableDorms(dormType, null, null);
            if (availableDorms == null || availableDorms.isEmpty()) {
                return ResponseResult.errorResult(400, "所选宿舍类型已无空余床位");
            }

            // 获取学生信息并更新学生表中的字段
            Student student = studentService.getStudentByNumber(studentNumber);
            if (student == null) {
                return ResponseResult.errorResult(400, "未找到学生信息");
            }

            // 设置学生选择的宿舍类型
            student.setSelectDorm(dormType);

            // 更新student_dorm表
            boolean studentDormResult;
            if (existingDorm == null) {
                existingDorm = new StudentDorm();
                existingDorm.setStudentNumber(studentNumber);
                existingDorm.setDormType(dormType);
                studentDormResult = studentDormService.save(existingDorm);
            } else {
                existingDorm.setDormType(dormType);
                studentDormResult = studentDormService.updateStudentDorm(existingDorm);
            }

            // 更新student表
            boolean studentResult = studentService.updateStudentInfo(student);

            if (studentDormResult && studentResult) {
                return ResponseResult.okResult("宿舍类型选择成功，等待管理员分配宿舍");
            } else {
                return ResponseResult.errorResult(500, "保存宿舍类型选择失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "提交宿舍选择失败: " + e.getMessage());
        }
    }

    /**
     * 获取宿舍类型信息
     */
    @GetMapping("/info/{studentNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getDormSelectionInfo(@PathVariable String studentNumber) {
        try {
            // 查询学生是否已经有宿舍类型选择
            StudentDorm studentDorm = studentDormService.getStudentDormInfo(studentNumber);

            Map<String, Object> result = new HashMap<>();

            if (studentDorm != null) {
                result.put("selected", true);
                result.put("dormType", studentDorm.getDormType());
                result.put("status", studentDorm.getDormitory() != null ? "已分配" : "待分配");
                result.put("dormitory", studentDorm.getDormitory());
                result.put("dormCard", studentDorm.getDormCard());
            } else {
                result.put("selected", false);
            }

            return ResponseResult.okResult(result);
        } catch (Exception e) {
            return ResponseResult.errorResult(500, "获取宿舍选择信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取宿舍类型描述
     * 根据宿舍类型返回对应的描述信息
     */
    private String getDormTypeDescription(String dormType) {
        switch (dormType) {
            case "四人间":
                return "标准四人间，配备独立卫生间、空调、书桌、衣柜等基本设施";
            case "六人间":
                return "经济六人间，配备基本设施，性价比高";
            default:
                return "标准宿舍，配备基本生活设施";
        }
    }
}