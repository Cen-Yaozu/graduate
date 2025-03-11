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

        // 使用selectList然后取第一条记录，避免TooManyResultsException
        List<StudentDorm> dormList = studentDormMapper.selectList(queryWrapper);
        return dormList.isEmpty() ? null : dormList.get(0);
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
     * @param dormitory     需要维修的宿舍号
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

        // 使用selectList然后取第一条记录，避免TooManyResultsException
        List<StudentDorm> dormList = studentDormMapper.selectList(queryWrapper);
        StudentDorm studentDorm = dormList.isEmpty() ? null : dormList.get(0);

        if (studentDorm != null) {
            return studentDorm.getDormitory();
        }
        return null;
    }

    @Override
    public boolean updateStudentDorm(StudentDorm studentDorm) {
        // 使用学号作为条件进行更新，而不是依赖ID
        if (studentDorm == null || studentDorm.getStudentNumber() == null) {
            return false;
        }
        
        LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentDorm::getStudentNumber, studentDorm.getStudentNumber());
        
        try {
            int result = studentDormMapper.update(studentDorm, queryWrapper);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存学生宿舍信息
     *
     * @param studentDorm 学生宿舍信息
     * @return 保存是否成功
     */
    @Override
    public boolean save(StudentDorm studentDorm) {
        try {
            if (studentDorm == null || studentDorm.getStudentNumber() == null) {
                return false;
            }
            
            // 检查是否已存在记录
            StudentDorm existingDorm = getStudentDormInfo(studentDorm.getStudentNumber());
            if (existingDorm != null) {
                // 已存在则更新 - 确保所有必要字段都被设置
                studentDorm.setStudentNumber(existingDorm.getStudentNumber());
                // 保留原有值，如果新对象中没有设置
                if (studentDorm.getStudentName() == null) {
                    studentDorm.setStudentName(existingDorm.getStudentName());
                }
                if (studentDorm.getDepartment() == null) {
                    studentDorm.setDepartment(existingDorm.getDepartment());
                }
                
                return updateStudentDorm(studentDorm);
            } else {
                // 不存在则插入
                try {
                    int result = studentDormMapper.insert(studentDorm);
                    return result > 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("插入学生宿舍记录失败: " + e.getMessage());
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("保存学生宿舍信息失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据宿舍楼和宿舍号获取该宿舍的所有学生
     *
     * @param dormitory 宿舍楼
     * @param dormCard 宿舍号
     * @return 学生列表
     */
    @Override
    public List<Student> getStudentsByDorm(String dormitory, String dormCard) {
        // 首先查询StudentDorm表获取该宿舍的所有学生记录
        LambdaQueryWrapper<StudentDorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentDorm::getDormitory, dormitory)
                   .eq(StudentDorm::getDormCard, dormCard);
        
        List<StudentDorm> dormList = studentDormMapper.selectList(queryWrapper);
        
        // 如果没有记录，返回空列表
        if (dormList.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查询每个宿舍记录对应的学生信息
        return studentDormMapper.getStudentsByDormInfo(dormitory, dormCard);
    }
}