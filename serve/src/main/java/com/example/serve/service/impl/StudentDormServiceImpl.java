package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.StudentDormMapper;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.StudentDorm;
import com.example.serve.service.StudentDormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生宿舍服务实现类
 * 提供学生宿舍信息管理、室友查询、报修请求等功能的具体实现
 *
 * @author author
 * @since 2025-02-23
 */
@Service
public class StudentDormServiceImpl extends ServiceImpl<StudentDormMapper, StudentDorm> implements StudentDormService {

    @Autowired
    private StudentDormMapper studentDormMapper;

    /**
     * 获取学生的宿舍信息
     *
     * @param studentNumber 学生学号
     * @return 返回学生宿舍信息对象，如果不存在则返回null
     */
    @Override
    public StudentDorm getStudentDormInfo(String studentNumber) {
        LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentDorm::getStudentNumber, studentNumber);
        return studentDormMapper.selectOne(queryWrapper);
    }

    /**
     * 获取指定学生的室友信息列表
     *
     * @param studentNumber 学生学号
     * @return 返回室友信息列表，包含室友姓名和学号，如果学生没有宿舍信息则返回空列表
     */
    @Override
    public List<Map<String, Object>> getRoommateInfo(String studentNumber) {
        // 先获取当前学生的宿舍信息
        StudentDorm currentStudent = getStudentDormInfo(studentNumber);
        if (currentStudent == null) {
            return new ArrayList<>();
        }

        // 查找同一宿舍的其他学生
        LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentDorm::getDormitory, currentStudent.getDormitory())
                .eq(StudentDorm::getDormCard, currentStudent.getDormCard())
                .ne(StudentDorm::getStudentNumber, studentNumber);

        List<StudentDorm> roommates = studentDormMapper.selectList(queryWrapper);
        List<Map<String, Object>> roommateList = new ArrayList<>();

        for (StudentDorm roommate : roommates) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", roommate.getStudentName());
            map.put("studentNumber", roommate.getStudentNumber());
            roommateList.add(map);
        }

        return roommateList;
    }

    /**
     * 提交宿舍维修申请
     *
     * @param studentNumber 申请学生的学号
     * @param dormitory 需要维修的宿舍号
     * @return 返回提交是否成功
     */
    @Override
    public boolean submitRepairRequest(String studentNumber, String dormitory) {
        // 这里可以添加报修记录到数据库
        // 为简化示例，这里直接返回true
        return true;
    }

    /**
     * 获取学生的宿舍号
     *
     * @param studentNumber 学生学号
     * @return 返回宿舍号，如果不存在则返回null
     */
    @Override
    public String getDormitoryInfo(String studentNumber) {
        LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentDorm::getStudentNumber, studentNumber);
        StudentDorm studentDorm = studentDormMapper.selectOne(queryWrapper);
        if (studentDorm != null) {
            return studentDorm.getDormitory();
        }
        return null;
    }
    @Override
    public boolean updateStudentDorm(StudentDorm studentDorm) {
        return updateById(studentDorm);
    }
}