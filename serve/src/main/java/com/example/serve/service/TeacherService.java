package com.example.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Teacher;

/**
 * 教师管理服务接口
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 分页查询教师列表
     * 
     * @param current 当前页码
     * @param size    每页大小
     * @param keyword 搜索关键词（工号或姓名）
     * @return 分页结果
     */
    Page<Teacher> getTeacherPage(Integer current, Integer size, String keyword);

    /**
     * 添加教师
     * 
     * @param teacher 教师信息
     * @return 是否成功
     */
    boolean addTeacher(Teacher teacher);

    /**
     * 更新教师信息
     * 
     * @param teacher 教师信息
     * @return 是否成功
     */
    boolean updateTeacher(Teacher teacher);

    /**
     * 删除教师
     * 
     * @param teacherId 教师工号
     * @return 是否成功
     */
    boolean deleteTeacher(Integer teacherId);
}