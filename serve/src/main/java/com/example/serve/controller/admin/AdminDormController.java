package com.example.serve.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.serve.common.Result;
import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.StudentDorm;
import com.example.serve.pojo.Classroom;
import com.example.serve.service.DormService;
import com.example.serve.service.StudentDormService;
import com.example.serve.service.StudentService;
import com.example.serve.service.ClassroomService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/dorm")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDormController {

    @Autowired
    private DormService dormService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentDormService studentDormService;

    @Autowired
    private ClassroomService classroomService;

    /**
     * 分页获取宿舍列表
     * 
     * @param page    当前页码
     * @param size    每页大小
     * @param keyword 搜索关键词（宿舍楼、房间号等）
     * @param dormsex 宿舍性别（MALE/FEMALE）
     * @param dormType 宿舍类型（四人间/六人间）
     * @return 宿舍分页数据
     */
    @GetMapping("/list")
    public Result getDormList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dormsex,
            @RequestParam(required = false) String dormType) {
        try {
            IPage<Dorm> pageResult = dormService.getDormPage(page, size, keyword, dormsex, dormType);
            Map<String, Object> data = new HashMap<>();
            data.put("list", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取宿舍列表失败: " + e.getMessage());
        }
    }

    /**
     * 添加新宿舍
     * 
     * @param dorm 宿舍信息
     * @return 操作结果
     */
    @PostMapping("/dorm/add")
    public Result addDorm(@RequestBody Dorm dorm) {
        try {
            // 参数验证
            if (dorm.getDormitory() == null || dorm.getDormCard() == null) {
                return Result.error("宿舍楼和房间号不能为空");
            }

            boolean success = dormService.addDorm(dorm);
            return success ? Result.success() : Result.error("添加宿舍失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加宿舍失败: " + e.getMessage());
        }
    }

    /**
     * 更新宿舍信息
     * 
     * @param dorm 宿舍信息
     * @return 操作结果
     */
    @PutMapping("/dorm/update")
    public Result updateDorm(@RequestBody Dorm dorm) {
        try {
            // 参数验证
            if (dorm.getDormitory() == null || dorm.getDormCard() == null) {
                return Result.error("宿舍楼和房间号不能为空");
            }

            boolean success = dormService.updateDorm(dorm);
            return success ? Result.success() : Result.error("更新宿舍失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新宿舍失败: " + e.getMessage());
        }
    }

    /**
     * 删除宿舍
     * 
     * @param dormitory 宿舍楼
     * @param dormCard  房间号
     * @return 操作结果
     */
    @DeleteMapping("/dorm/delete")
    public Result deleteDorm(
            @RequestParam String dormitory,
            @RequestParam String dormCard) {
        try {
            boolean success = dormService.deleteDorm(dormitory, dormCard);
            return success ? Result.success() : Result.error("删除宿舍失败，请确保宿舍中没有学生");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除宿舍失败: " + e.getMessage());
        }
    }

    /**
     * 获取宿舍成员列表
     * @param dormitory 宿舍楼名称
     * @param dormCard 宿舍号
     * @return 该宿舍的所有学生信息
     */
    @GetMapping("/members")
    public ResponseResult getDormMembers(
            @RequestParam String dormitory,
            @RequestParam String dormCard) {
        
        try {
            // 通过宿舍楼和宿舍号查询该宿舍下的所有学生
            List<Student> members = studentDormService.getStudentsByDorm(dormitory, dormCard);
            
            // 为每个学生添加班级详细信息
            List<Map<String, Object>> studentList = new ArrayList<>();
            for (Student student : members) {
                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("studentNumber", student.getStudentNumber());
                studentInfo.put("studentName", student.getStudentName());
                studentInfo.put("sex", student.getSex());
                studentInfo.put("department", student.getDepartment());
                studentInfo.put("majorname", student.getMajorname());
                
                // 班级ID
                Integer classroomId = student.getClassroomId();
                studentInfo.put("className", student.getClassroomId());
                
                // 添加班级详细信息
                if (classroomId != null) {
                    Classroom classroom = classroomService.getById(classroomId);
                    if (classroom != null) {
                        studentInfo.put("classroom_name", classroom.getClassroom());
                        studentInfo.put("classroom_department", classroom.getDepartment());
                        studentInfo.put("classroom_num", classroom.getClassroomNum());
                    }
                }
                
                studentList.add(studentInfo);
            }
            
            return ResponseResult.okResult(studentList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "获取宿舍成员失败: " + e.getMessage());
        }
    }

    /**
     * 根据宿舍分配状态获取学生列表
     * 
     * @param status 分配状态：assigned(已分配), pending(待分配), unassigned(未分配)，如果不传则返回所有学生
     * @return 符合条件的学生列表
     */
    @GetMapping("/students")
    public Result getStudentsByAssignmentStatus(@RequestParam(required = false) String status) {
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();

            if (status == null || status.isEmpty()) {
                // 如果不传状态，返回所有学生
                List<Student> allStudents = studentService.list();
                for (Student student : allStudents) {
                    Map<String, Object> studentInfo = new HashMap<>();
                    studentInfo.put("studentNumber", student.getStudentNumber());
                    studentInfo.put("studentName", student.getStudentName());
                    studentInfo.put("sex", student.getSex());
                    studentInfo.put("department", student.getDepartment());
                    studentInfo.put("majorname", student.getMajorname());
                    
                    // 查询班级信息
                    Integer classroomId = student.getClassroomId();
                    studentInfo.put("classroom_id", classroomId);
                    
                    // 添加班级详细信息
                    if (classroomId != null) {
                        Classroom classroom = classroomService.getById(classroomId);
                        if (classroom != null) {
                            studentInfo.put("classroom_name", classroom.getClassroom());
                            studentInfo.put("classroom_department", classroom.getDepartment());
                            studentInfo.put("classroom_num", classroom.getClassroomNum());
                        }
                    }

                    // 查询宿舍信息
                    StudentDorm studentDorm = studentDormService.getStudentDormInfo(student.getStudentNumber());
                    if (studentDorm != null && studentDorm.getDormitory() != null
                            && !studentDorm.getDormitory().isEmpty()
                            && studentDorm.getDormCard() != null
                            && !studentDorm.getDormCard().isEmpty()) {
                        studentInfo.put("dormitory", studentDorm.getDormitory());
                        studentInfo.put("dormCard", studentDorm.getDormCard());
                        studentInfo.put("dormType", studentDorm.getDormType());
                        studentInfo.put("assignmentStatus", "assigned");
                    } else if (studentDorm != null) {
                        studentInfo.put("assignmentStatus", "pending");
                    } else {
                        studentInfo.put("assignmentStatus", "unassigned");
                    }

                    resultList.add(studentInfo);
                }
            } else if ("assigned".equals(status)) {
                // 已分配：查询student_dorm表中的数据，其中的studentnumber字段有值，dormitory和dormCard字段都有值的
                LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.isNotNull(StudentDorm::getStudentNumber)
                        .isNotNull(StudentDorm::getDormitory)
                        .isNotNull(StudentDorm::getDormCard)
                        .ne(StudentDorm::getDormitory, "")
                        .ne(StudentDorm::getDormCard, "");
                List<StudentDorm> assignedDorms = studentDormService.list(queryWrapper);

                for (StudentDorm dorm : assignedDorms) {
                    // 根据学号查询学生信息
                    Student student = studentService.getStudentByNumber(dorm.getStudentNumber().toString());
                    if (student != null) {
                        Map<String, Object> studentInfo = new HashMap<>();
                        studentInfo.put("studentNumber", student.getStudentNumber());
                        studentInfo.put("studentName", student.getStudentName());
                        studentInfo.put("sex", student.getSex());
                        studentInfo.put("department", student.getDepartment());
                        studentInfo.put("majorname", student.getMajorname());
                        
                        // 查询班级信息
                        Integer classroomId = student.getClassroomId();
                        studentInfo.put("classroom_id", classroomId);
                        
                        // 添加班级详细信息
                        if (classroomId != null) {
                            Classroom classroom = classroomService.getById(classroomId);
                            if (classroom != null) {
                                studentInfo.put("classroom_name", classroom.getClassroom());
                                studentInfo.put("classroom_department", classroom.getDepartment());
                                studentInfo.put("classroom_num", classroom.getClassroomNum());
                            }
                        }
                        
                        studentInfo.put("dormitory", dorm.getDormitory());
                        studentInfo.put("dormCard", dorm.getDormCard());
                        studentInfo.put("dormType", dorm.getDormType());
                        studentInfo.put("assignmentStatus", "assigned");

                        resultList.add(studentInfo);
                    }
                }
            } else if ("pending".equals(status)) {
                // 待分配：查询student_dorm表中的数据，其中的studentnumber字段有值，但是dormitory或dormCard字段为空的
                LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.isNotNull(StudentDorm::getStudentNumber)
                        .and(q -> q.isNull(StudentDorm::getDormitory)
                                .or().eq(StudentDorm::getDormitory, "")
                                .or().isNull(StudentDorm::getDormCard)
                                .or().eq(StudentDorm::getDormCard, ""));
                List<StudentDorm> pendingDorms = studentDormService.list(queryWrapper);

                for (StudentDorm dorm : pendingDorms) {
                    // 根据学号查询学生信息
                    Student student = studentService.getStudentByNumber(dorm.getStudentNumber().toString());
                    if (student != null) {
                        Map<String, Object> studentInfo = new HashMap<>();
                        studentInfo.put("studentNumber", student.getStudentNumber());
                        studentInfo.put("studentName", student.getStudentName());
                        studentInfo.put("sex", student.getSex());
                        studentInfo.put("department", student.getDepartment());
                        studentInfo.put("majorname", student.getMajorname());
                        
                        // 查询班级信息
                        Integer classroomId = student.getClassroomId();
                        studentInfo.put("classroom_id", classroomId);
                        
                        // 添加班级详细信息
                        if (classroomId != null) {
                            Classroom classroom = classroomService.getById(classroomId);
                            if (classroom != null) {
                                studentInfo.put("classroom_name", classroom.getClassroom());
                                studentInfo.put("classroom_department", classroom.getDepartment());
                                studentInfo.put("classroom_num", classroom.getClassroomNum());
                            }
                        }
                        
                        studentInfo.put("dormType", dorm.getDormType());
                        studentInfo.put("assignmentStatus", "pending");

                        resultList.add(studentInfo);
                    }
                }
            } else if ("unassigned".equals(status)) {
                // 未分配：查询student表中的数据，然后用studentnumber字段去和student_dorm表中的studentnumber匹配
                // 如果没有匹配到，就是未分配的学生
                List<Student> allStudents = studentService.list();
                List<StudentDorm> allDorms = studentDormService.list();

                // 获取所有已在student_dorm表中的学生学号
                Set<String> assignedStudentNumbers = allDorms.stream()
                        .map(dorm -> dorm.getStudentNumber().toString())
                        .collect(Collectors.toSet());

                for (Student student : allStudents) {
                    // 如果学生不在已分配或待分配的列表中，则为未分配
                    if (!assignedStudentNumbers.contains(student.getStudentNumber())) {
                        Map<String, Object> studentInfo = new HashMap<>();
                        studentInfo.put("studentNumber", student.getStudentNumber());
                        studentInfo.put("studentName", student.getStudentName());
                        studentInfo.put("sex", student.getSex());
                        studentInfo.put("department", student.getDepartment());
                        studentInfo.put("majorname", student.getMajorname());
                        
                        // 查询班级信息
                        Integer classroomId = student.getClassroomId();
                        studentInfo.put("classroom_id", classroomId);
                        
                        // 添加班级详细信息
                        if (classroomId != null) {
                            Classroom classroom = classroomService.getById(classroomId);
                            if (classroom != null) {
                                studentInfo.put("classroom_name", classroom.getClassroom());
                                studentInfo.put("classroom_department", classroom.getDepartment());
                                studentInfo.put("classroom_num", classroom.getClassroomNum());
                            }
                        }
                        
                        studentInfo.put("assignmentStatus", "unassigned");

                        resultList.add(studentInfo);
                    }
                }
            } else {
                return Result.error("无效的分配状态参数");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("list", resultList);
            data.put("total", resultList.size());

            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取学生列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取可用宿舍列表
     * 
     * @param dormType 宿舍类型（FOUR/SIX/EIGHT）
     * @param dormsex  宿舍性别（MALE/FEMALE）
     * @return 可用宿舍列表
     */
    @GetMapping("/available")
    public Result getAvailableDorms(
            @RequestParam(required = false) String dormType,
            @RequestParam(required = false) String dormsex) {
        try {
            // 将英文性别参数转换为中文
            String dormSexChinese = null;
            if (dormsex != null && !dormsex.isEmpty()) {
                if ("MALE".equalsIgnoreCase(dormsex)) {
                    dormSexChinese = "男";
                } else if ("FEMALE".equalsIgnoreCase(dormsex)) {
                    dormSexChinese = "女";
                }
            }

            // 使用原始的dormType参数，不进行转换
            // dormType参数为FOUR, SIX, EIGHT等英文值
            // 日志输出便于调试
            System.out.println("查询可用宿舍：宿舍类型 = " + dormType + ", 性别 = " + dormSexChinese);

            List<Dorm> dorms = dormService.getAvailableDorms(dormType, dormSexChinese, null);
            return Result.success(dorms);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取可用宿舍列表失败: " + e.getMessage());
        }
    }

    /**
     * 为学生分配宿舍
     * 
     * @param requestMap 包含学生学号、宿舍类型、宿舍楼和宿舍号的请求体
     * @return 分配结果
     */
    @PostMapping("/assign")
    public Result assignDorm(@RequestBody Map<String, Object> requestMap) {
        try {
            String studentNumber = (String) requestMap.get("studentNumber");
            String studentName = (String) requestMap.get("studentName");  // 获取学生姓名
            String department = (String) requestMap.get("department");    // 获取系别
            String dormType = (String) requestMap.get("dormType");
            String dormitory = (String) requestMap.get("dormitory");
            String dormCard = (String) requestMap.get("dormCard");

            if(dormType.equals("FOUR")){
                dormType = "四人间";
            }else if (dormType.equals("SIX")){
                dormType = "六人间";
            }

            if (studentNumber == null || dormType == null || dormitory == null || dormCard == null) {
                return Result.error("缺少必要的参数");
            }

            // 检查学生是否存在
            Student student = studentService.getStudentByNumber(studentNumber);
            if (student == null) {
                return Result.error("学生不存在");
            }

            // 检查该学生是否已有宿舍
            StudentDorm existingDorm = studentDormService.getStudentDormInfo(studentNumber);
            if (existingDorm != null) {
                // 更新宿舍信息
                existingDorm.setDormitory(dormitory);
                existingDorm.setDormCard(dormCard);
                existingDorm.setDormType(dormType);
                
                // 更新学生姓名和系别（如果有提供）
                if (studentName != null && !studentName.isEmpty()) {
                    existingDorm.setStudentName(studentName);
                }
                if (department != null && !department.isEmpty()) {
                    existingDorm.setDepartment(department);
                }
                
                boolean updated = studentDormService.updateStudentDorm(existingDorm);
                if (!updated) {
                    return Result.error("更新宿舍分配失败");
                }
            } else {
                // 创建新的宿舍分配
                StudentDorm newDorm = new StudentDorm();
                newDorm.setStudentNumber(studentNumber);
                newDorm.setDormitory(dormitory);
                newDorm.setDormCard(dormCard);
                newDorm.setDormType(dormType);
                
                // 设置学生姓名和系别（如果有提供）
                if (studentName != null && !studentName.isEmpty()) {
                    newDorm.setStudentName(studentName);
                }
                if (department != null && !department.isEmpty()) {
                    newDorm.setDepartment(department);
                }
                
                boolean saved = studentDormService.save(newDorm);
                if (!saved) {
                    return Result.error("保存宿舍分配失败");
                }
            }

            return Result.success("宿舍分配成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("宿舍分配失败: " + e.getMessage());
        }
    }

    /**
     * 批量为学生分配宿舍
     * 
     * @param requestMap 包含学生学号列表、宿舍类型、宿舍楼和宿舍号的请求体
     * @return 分配结果
     */
    @PostMapping("/assign/batch")
    public Result assignDormBatch(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> studentNumbers = (List<String>) requestMap.get("studentNumbers");
            List<Map<String, Object>> studentInfoList = (List<Map<String, Object>>) requestMap.get("studentInfoList");
            String dormType = (String) requestMap.get("dormType");
            String dormitory = (String) requestMap.get("dormitory");
            String dormCard = (String) requestMap.get("dormCard");

            if(dormType.equals("FOUR")){
                dormType = "四人间";
            }else if (dormType.equals("SIX")){
                dormType = "六人间";
            }

            if (studentNumbers == null || studentNumbers.isEmpty() || dormType == null || dormitory == null
                    || dormCard == null) {
                return Result.error("缺少必要的参数");
            }

            int successCount = 0;
            List<String> failedStudents = new ArrayList<>();

            for (String studentNumber : studentNumbers) {
                try {
                    // 检查学生是否存在
                    Student student = studentService.getStudentByNumber(studentNumber);
                    if (student == null) {
                        failedStudents.add(studentNumber);
                        continue;
                    }
                    
                    // 从学生信息列表中查找学生的姓名和系别
                    String studentName = null;
                    String department = null;
                    if (studentInfoList != null) {
                        for (Map<String, Object> studentInfo : studentInfoList) {
                            if (studentNumber.equals(studentInfo.get("studentNumber"))) {
                                studentName = (String) studentInfo.get("studentName");
                                department = (String) studentInfo.get("department");
                                break;
                            }
                        }
                    }

                    // 检查该学生是否已有宿舍
                    StudentDorm existingDorm = studentDormService.getStudentDormInfo(studentNumber);
                    if (existingDorm != null) {
                        // 更新宿舍信息
                        existingDorm.setDormitory(dormitory);
                        existingDorm.setDormCard(dormCard);
                        existingDorm.setDormType(dormType);
                        
                        // 设置学生姓名和系别（如果有）
                        if (studentName != null && !studentName.isEmpty()) {
                            existingDorm.setStudentName(studentName);
                        }
                        if (department != null && !department.isEmpty()) {
                            existingDorm.setDepartment(department);
                        }
                        
                        // 使用基于条件的更新而不是基于ID的更新
                        boolean updated = studentDormService.updateStudentDorm(existingDorm);
                        if (!updated) {
                            failedStudents.add(studentNumber);
                            continue;
                        }
                    } else {
                        // 创建新的宿舍分配
                        StudentDorm newDorm = new StudentDorm();
                        newDorm.setStudentNumber(studentNumber);
                        newDorm.setDormitory(dormitory);
                        newDorm.setDormCard(dormCard);
                        newDorm.setDormType(dormType);
                        
                        // 设置学生姓名和系别（如果有）
                        if (studentName != null && !studentName.isEmpty()) {
                            newDorm.setStudentName(studentName);
                        }
                        if (department != null && !department.isEmpty()) {
                            newDorm.setDepartment(department);
                        }
                        
                        boolean saved = studentDormService.save(newDorm);
                        if (!saved) {
                            failedStudents.add(studentNumber);
                            continue;
                        }
                    }

                    successCount++;
                } catch (Exception e) {
                    failedStudents.add(studentNumber);
                }
            }

            if (failedStudents.isEmpty()) {
                return Result.success("成功为" + successCount + "名学生分配宿舍");
            } else {
                return Result.success(Map.of(
                        "message", "部分学生宿舍分配成功",
                        "successCount", successCount,
                        "failedStudents", failedStudents));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量宿舍分配失败: " + e.getMessage());
        }
    }

    /**
     * 移除学生的宿舍分配
     * 
     * @param studentNumber 学生学号
     * @return 移除结果
     */
    @DeleteMapping("/assign/{studentNumber}")
    public Result removeDormAssignment(@PathVariable String studentNumber) {
        try {
            // 检查学生是否存在
            Student student = studentService.getStudentByNumber(studentNumber);
            if (student == null) {
                return Result.error("学生不存在");
            }

            // 检查该学生是否已有宿舍
            StudentDorm existingDorm = studentDormService.getStudentDormInfo(studentNumber);
            if (existingDorm == null) {
                return Result.error("该学生没有分配宿舍");
            }

            // 移除宿舍分配
            LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StudentDorm::getStudentNumber, studentNumber);
            studentDormService.remove(queryWrapper);

            return Result.success("成功移除宿舍分配");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("移除宿舍分配失败: " + e.getMessage());
        }
    }

    /**
     * 获取宿舍选择统计数据
     * 统计已选择宿舍的学生数量
     * 
     * @return 统计结果
     */
    @GetMapping("/student-dorm/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Result getDormStats() {
        try {
            // 查询已分配宿舍的学生数量（dormitory和dormCard都不为空的记录）
            LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.isNotNull(StudentDorm::getStudentNumber)
                    .isNotNull(StudentDorm::getDormitory)
                    .isNotNull(StudentDorm::getDormCard)
                    .ne(StudentDorm::getDormitory, "")
                    .ne(StudentDorm::getDormCard, "");
            
            // 获取已选择宿舍的学生数量
            long selectedCount = studentDormService.count(queryWrapper);
            
            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("selectedCount", selectedCount);
            
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取宿舍统计数据失败: " + e.getMessage());
        }
    }
}