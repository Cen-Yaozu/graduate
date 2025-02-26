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
     * 获取所有班级信息
     * @return 班级信息列表
     */
    List<Classroom> getAllClasses();
}