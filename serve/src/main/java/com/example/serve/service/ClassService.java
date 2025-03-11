package com.example.serve.service;

import com.example.serve.pojo.Classroom;
import com.example.serve.pojo.Major;

import java.util.List;
import java.util.Map;

public interface ClassService {

    /**
     * 获取所有专业信息
     * @return 专业信息列表
     */
    List<Major> getAllMajors();

    /**
     * 获取所有班级信息，包含辅导员和学习导师的姓名
     * @return 班级信息列表
     */
    List<Map<String, Object>> getAllClasses();
    
    /**
     * 根据学生学号获取学生所在班级的详细信息
     * @param studentNumber 学生学号
     * @return 包含班级、专业、系别、辅导员、学习导师信息的Map
     */
    Map<String, Object> getStudentClassInfo(String studentNumber);
}