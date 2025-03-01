package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.serve.mapper.ClassMapper;
import com.example.serve.mapper.MajorMapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.pojo.Classroom;
import com.example.serve.pojo.Major;
import com.example.serve.pojo.Student;
import com.example.serve.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Override
    public List<Major> getAllMajors() {
        return majorMapper.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getAllClasses() {
        // 使用自定义查询方法获取班级信息，包含老师姓名
        return classMapper.selectAllClassesWithTeacherName();
    }
}