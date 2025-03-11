package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.serve.mapper.ClassMapper;
import com.example.serve.mapper.MajorMapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.mapper.TeacherMapper;
import com.example.serve.pojo.Classroom;
import com.example.serve.pojo.Major;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.Teacher;
import com.example.serve.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Major> getAllMajors() {
        return majorMapper.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getAllClasses() {
        // 使用自定义查询方法获取班级信息，包含老师姓名
        return classMapper.selectAllClassesWithTeacherName();
    }

    @Override
    public Map<String, Object> getStudentClassInfo(String studentNumber) {
        // 查询学生信息，获取班级ID
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStudentNumber, studentNumber);
        Student student = studentMapper.selectOne(queryWrapper);
        
        if (student == null) {
            throw new RuntimeException("未找到学生信息");
        }
        
        Integer classroomId = student.getClassroomId();
        if (classroomId == null) {
            throw new RuntimeException("学生未分配班级");
        }
        
        // 通过classroomId直接查询Classroom表
        Classroom classroom = classMapper.selectById(classroomId);
        if (classroom == null) {
            throw new RuntimeException("未找到班级信息");
        }
        
        // 使用专业ID查询专业信息，通过LambdaQueryWrapper而非selectById
        String majorId = classroom.getMajorid().toString();
        LambdaQueryWrapper<Major> majorQueryWrapper = new LambdaQueryWrapper<>();
        majorQueryWrapper.eq(Major::getMajorid, majorId);
        Major major = majorMapper.selectOne(majorQueryWrapper);
        if (major == null) {
            throw new RuntimeException("未找到专业信息");
        }
        
        // 组装班级信息
        Map<String, Object> classInfo = new HashMap<>();
        classInfo.put("classroom", classroom.getClassroom());
        classInfo.put("majorid", classroom.getMajorid());
        classInfo.put("majorname", major.getMajorname());
        classInfo.put("department", major.getDepartment());
        
        // 查询辅导员和学习导师姓名
        Integer fteacherId = classroom.getFteacherid();
        String steacherId = classroom.getSteacherid();
        
        // 查询辅导员信息，使用LambdaQueryWrapper
        if (fteacherId != null) {
            LambdaQueryWrapper<Teacher> fteacherQuery = new LambdaQueryWrapper<>();
            fteacherQuery.eq(Teacher::getTeacherNumber, fteacherId);
            Teacher fteacher = teacherMapper.selectOne(fteacherQuery);
            if (fteacher != null) {
                classInfo.put("fteacherName", fteacher.getTeacherName());
            }
        }
        
        // 查询学习导师信息，使用LambdaQueryWrapper
        if (steacherId != null && !steacherId.isEmpty()) {
            try {
                Integer steacherIdInt = Integer.parseInt(steacherId);
                LambdaQueryWrapper<Teacher> steacherQuery = new LambdaQueryWrapper<>();
                steacherQuery.eq(Teacher::getTeacherNumber, steacherIdInt);
                Teacher steacher = teacherMapper.selectOne(steacherQuery);
                if (steacher != null) {
                    classInfo.put("steacherName", steacher.getTeacherName());
                }
            } catch (NumberFormatException e) {
                // 如果转换失败，记录日志但不抛出异常
                System.err.println("无法将学习导师ID '" + steacherId + "' 转换为整数: " + e.getMessage());
            }
        }
        
        // 统计班级人数
        LambdaQueryWrapper<Student> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Student::getClassroomId, classroomId);
        long studentCount = studentMapper.selectCount(countWrapper);
        classInfo.put("studentCount", studentCount);
        
        return classInfo;
    }
}