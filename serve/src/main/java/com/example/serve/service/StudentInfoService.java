package com.example.serve.service;

import com.example.serve.pojo.Family;
import com.example.serve.pojo.Resume;
import com.example.serve.pojo.Student;
import com.example.serve.tools.ResponseResult;

import java.util.List;

/**
 * 学生信息服务接口 - 策略模式的抽象策略
 */
public interface StudentInfoService {
    
    /**
     * 获取学生基本信息
     */
    ResponseResult<Student> getStudentInfo(String studentNumber);
    
    /**
     * 更新学生基本信息
     */
    ResponseResult<Void> updateStudentInfo(Student student);
    
    /**
     * 获取学生简历信息
     */
    ResponseResult<Resume> getResumeInfo(String studentNumber);
    
    /**
     * 更新学生简历信息
     */
    ResponseResult<Void> updateResumeInfo(Resume resume);
    
    /**
     * 获取学生家庭信息
     */
    ResponseResult<List<Family>> getFamilyInfo(String studentNumber);
    
    /**
     * 添加学生家庭成员信息
     */
    ResponseResult<Void> addFamilyMember(Family family);
    
    /**
     * 更新学生家庭成员信息
     */
    ResponseResult<Void> updateFamilyMember(Family family);
    
    /**
     * 删除学生家庭成员信息
     */
    ResponseResult<Void> deleteFamilyMember(Integer id);
} 