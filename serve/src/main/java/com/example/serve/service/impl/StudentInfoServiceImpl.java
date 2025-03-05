package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.mapper.FamilyMapper;
import com.example.serve.mapper.ResumeMapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.pojo.Family;
import com.example.serve.pojo.Resume;
import com.example.serve.pojo.Student;
import com.example.serve.service.StudentInfoService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生信息服务实现类 - 策略模式的具体策略
 */
@Service
public class StudentInfoServiceImpl implements StudentInfoService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private ResumeMapper resumeMapper;
    
    @Autowired
    private FamilyMapper familyMapper;
    
    @Override
    public ResponseResult<Student> getStudentInfo(String studentNumber) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentNumber", studentNumber);
        Student student = studentMapper.selectOne(queryWrapper);
        
        return new ResponseResult<>(200, "操作成功", student);
    }
    
    @Override
    public ResponseResult<Void> updateStudentInfo(Student student) {
        int rows = studentMapper.updateById(student);
        if (rows > 0) {
            return new ResponseResult<>(200, "更新成功");
        } else {
            return new ResponseResult<>(30000, "更新失败");
        }
    }
    
    @Override
    public ResponseResult<Resume> getResumeInfo(String studentNumber) {
        QueryWrapper<Resume> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentNumber", studentNumber);
        Resume resume = resumeMapper.selectOne(queryWrapper);
        
        return new ResponseResult<>(200, "操作成功", resume);
    }
    
    @Override
    public ResponseResult<Void> updateResumeInfo(Resume resume) {
        System.out.println("Received resume data: " + resume);
        
        int rows = resumeMapper.updateById(resume);
        if (rows > 0) {
            return new ResponseResult<>(200, "更新成功");
        } else {
            return new ResponseResult<>(30000, "更新失败");
        }
    }
    
    @Override
    public ResponseResult<List<Family>> getFamilyInfo(String studentNumber) {
        try {
            Page<Family> page = new Page<>(1, 10);
            Page<Family> familyPage = familyMapper.selectListByPageAndStudenNumber(page, studentNumber);
            
            return new ResponseResult<>(200, "操作成功", familyPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            
            QueryWrapper<Family> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("studentNumber", studentNumber);
            List<Family> familyList = familyMapper.selectList(queryWrapper);
            
            return new ResponseResult<>(200, "操作成功", familyList);
        }
    }
    
    @Override
    public ResponseResult<Void> addFamilyMember(Family family) {
        int rows = familyMapper.insert(family);
        if (rows > 0) {
            return new ResponseResult<>(200, "添加成功");
        } else {
            return new ResponseResult<>(30000, "添加失败");
        }
    }
    
    @Override
    public ResponseResult<Void> updateFamilyMember(Family family) {
        int rows = familyMapper.updateById(family);
        if (rows > 0) {
            return new ResponseResult<>(200, "更新成功");
        } else {
            return new ResponseResult<>(30000, "更新失败");
        }
    }
    
    @Override
    public ResponseResult<Void> deleteFamilyMember(Integer id) {
        int rows = familyMapper.deleteById(id);
        if (rows > 0) {
            return new ResponseResult<>(200, "删除成功");
        } else {
            return new ResponseResult<>(30000, "删除失败");
        }
    }
} 