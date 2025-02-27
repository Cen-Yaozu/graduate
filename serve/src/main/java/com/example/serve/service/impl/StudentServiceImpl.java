package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.pojo.Student;
import com.example.serve.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
    
    /**
     * 获取未分配班级的学生列表
     *
     * @param params 查询参数
     * @return 结果集
     */
    @Override
    public Map<String, Object> getUnassignedStudents(Map<String, Object> params) {
        // 设置assignType为unassigned
        params.put("assignType", "unassigned");
        return getStudentsByAssignStatus(params);
    }
    
    /**
     * 根据分配状态获取学生列表
     *
     * @param params 查询参数，包含assignType(all/assigned/unassigned)
     * @return 结果集
     */
    @Override
    public Map<String, Object> getStudentsByAssignStatus(Map<String, Object> params) {
        // 获取分页参数
        int page = params.get("page") != null ? (int) params.get("page") : 1;
        int size = params.get("size") != null ? (int) params.get("size") : 10;
        String department = (String) params.get("department");
        String majorname = (String) params.get("majorname");
        String assignType = (String) params.get("assignType");
        
        // 构建查询条件
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据分配状态筛选
        if ("unassigned".equals(assignType)) {
            // 未分配班级的学生
            queryWrapper.isNull(Student::getClassroomId);
        } else if ("assigned".equals(assignType)) {
            // 已分配班级的学生
            queryWrapper.isNotNull(Student::getClassroomId);
        }
        // all类型不添加条件，查询所有学生
        
        // 添加专业和系别过滤条件
        if (StringUtils.hasText(department)) {
            queryWrapper.eq(Student::getDepartment, department);
        }
        if (StringUtils.hasText(majorname)) {
            queryWrapper.eq(Student::getMajorname, majorname);
        }
        
        // 分页查询
        IPage<Student> pageData = this.page(new Page<>(page, size), queryWrapper);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("list", pageData.getRecords());
        
        return result;
    }
}