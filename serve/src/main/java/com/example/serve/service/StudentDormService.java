package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.StudentDorm;
import java.util.List;
import java.util.Map;

/**
 * 学生宿舍服务接口
 * 定义了学生宿舍信息管理、室友查询、报修请求等功能
 *
 * @author author
 * @since 2025-02-23
 */
public interface StudentDormService extends IService<StudentDorm> {
    /**
     * 获取学生宿舍信息
     *
     * @param studentNumber 学号
     * @return 宿舍信息
     */
    StudentDorm getStudentDormInfo(String studentNumber);

    /**
     * 获取室友信息
     *
     * @param studentNumber 学号
     * @return 室友信息列表
     */
    List<Map<String, Object>> getRoommateInfo(String studentNumber);

    /**
     * 更新学生宿舍信息
     *
     * @param studentDorm 学生宿舍信息
     * @return 更新是否成功
     */
    boolean updateStudentDorm(StudentDorm studentDorm);

    /**
     * 提交报修请求
     *
     * @param studentNumber 学号
     * @param dormitory     宿舍号
     * @return 是否提交成功
     */
    boolean submitRepairRequest(String studentNumber, String dormitory);

    /**
     * 获取学生宿舍信息
     *
     * @param studentNumber 学号
     * @return 宿舍信息
     */
    String getDormitoryInfo(String studentNumber);

    /**
     * 保存学生宿舍信息
     *
     * @param studentDorm 学生宿舍信息
     * @return 保存是否成功
     */
    boolean save(StudentDorm studentDorm);
}