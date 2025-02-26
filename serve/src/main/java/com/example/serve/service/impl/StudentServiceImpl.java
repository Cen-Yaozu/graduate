package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.pojo.Student;
import com.example.serve.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 学生服务实现类
 * 提供学生基本信息管理、缴费状态查询等功能的具体实现
 *
 * @author author
 * @since 2025-02-23
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 根据学号查询学生信息
     *
     * @param studentNumber 学生学号
     * @return 返回学生信息对象，如果不存在则返回null
     */
    @Override
    public Student getStudentByNumber(String studentNumber) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStudentNumber, studentNumber);
        return studentMapper.selectOne(queryWrapper);
    }

    /**
     * 更新学生信息
     *
     * @param student 需要更新的学生信息对象
     * @return 返回更新是否成功
     */
    @Override
    public boolean updateStudentInfo(Student student) {
        return updateById(student);
    }

    /**
     * 查询学生的缴费状态
     *
     * @param studentNumber 学生学号
     * @return 返回缴费状态，如果学生不存在则返回null
     */
    @Override
    public Integer getPaymentStatus(String studentNumber) {
        Student student = getStudentByNumber(studentNumber);
        return student != null ? student.getPaymentStatus() : null;
    }

    /**
     * 查询学生的抵校状态
     *
     * @param studentNumber 学生学号
     * @return 返回抵校状态，如果学生不存在则返回null
     */
    @Override
    public Integer getArriveStatus(String studentNumber) {
        Student student = getStudentByNumber(studentNumber);
        return student != null ? student.getArriveStatus() : null;
    }
}