package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.ClassroomMapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.pojo.Classroom;
import com.example.serve.pojo.Student;
import com.example.serve.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级服务实现类
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements ClassroomService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public boolean hasStudentsInClassroom(Integer classroomId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classroom_id", classroomId);
        
        // 查询数量大于0即表示有学生
        return studentMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    @Transactional
    public boolean assignStudentsToClassroom(List<String> studentNumbers, Integer classroomId) {
        if (studentNumbers == null || studentNumbers.isEmpty() || classroomId == null) {
            return false;
        }
        
        try {
            // 查询要分配的班级
            Classroom classroom = this.getById(classroomId);
            if (classroom == null) {
                return false;
            }
            
            // 使用UpdateWrapper进行批量更新
            UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("studentNumber", studentNumbers)
                        .set("classroom_id", classroomId);
            
            // 执行批量更新
            int affectedRows = studentMapper.update(null, updateWrapper);
            
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 