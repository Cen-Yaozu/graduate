package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Student;

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
}