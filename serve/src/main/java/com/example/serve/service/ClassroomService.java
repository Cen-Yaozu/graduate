package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Classroom;

import java.util.List;

/**
 * 班级服务接口
 */
public interface ClassroomService extends IService<Classroom> {
    
    /**
     * 检查班级是否有学生
     * 
     * @param classroomId 班级ID
     * @return 是否有学生
     */
    boolean hasStudentsInClassroom(Integer classroomId);
    
    /**
     * 批量分配学生到班级
     * 
     * @param studentNumbers 学生学号列表
     * @param classroomId 班级ID
     * @return 是否成功
     */
    boolean assignStudentsToClassroom(List<String> studentNumbers, Integer classroomId);
} 