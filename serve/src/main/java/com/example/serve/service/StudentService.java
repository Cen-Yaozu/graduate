package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Student;
import java.util.List;
import java.util.Map;

/**
 * 学生服务接口
 * 定义了学生基本信息管理、缴费状态查询等功能
 *
 * @author author
 * @since 2025-02-23
 */
public interface StudentService extends IService<Student> {
    /**
     * 根据学号获取学生信息
     *
     * @param studentNumber 学号
     * @return 学生信息
     */
    Student getStudentByNumber(String studentNumber);

    /**
     * 更新学生信息
     *
     * @param student 学生信息
     * @return 是否更新成功
     */
    boolean updateStudentInfo(Student student);

    /**
     * 获取学生缴费状态
     *
     * @param studentNumber 学号
     * @return 缴费状态
     */
    Integer getPaymentStatus(String studentNumber);

    /**
     * 获取学生抵校状态
     *
     * @param studentNumber 学号
     * @return 抵校状态
     */
    Integer getArriveStatus(String studentNumber);

    /**
     * 获取未分配班级的学生列表
     * 
     * @param params 查询参数
     * @return 结果集
     */
    Map<String, Object> getUnassignedStudents(Map<String, Object> params);

    /**
     * 根据分配状态获取学生列表
     * 
     * @param params 查询参数，包含assignType(all/assigned/unassigned)
     * @return 结果集
     */
    Map<String, Object> getStudentsByAssignStatus(Map<String, Object> params);
}