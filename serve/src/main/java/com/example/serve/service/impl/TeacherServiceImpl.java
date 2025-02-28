package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.TeacherMapper;
import com.example.serve.pojo.Teacher;
import com.example.serve.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 教师管理服务实现类
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    /**
     * 分页查询教师列表
     * 
     * @param current 当前页码
     * @param size    每页大小
     * @param keyword 搜索关键词（工号或姓名）
     * @return 分页结果
     */
    @Override
    public Page<Teacher> getTeacherPage(Integer current, Integer size, String keyword) {
        // 创建分页对象
        Page<Teacher> page = new Page<>(current, size);
        // 创建条件构造器
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();

        // 如果关键词不为空，则按工号或姓名模糊查询
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Teacher::getTeacherNumber, keyword)
                    .or()
                    .like(Teacher::getTeacherName, keyword);
        }

        // 执行分页查询
        return this.page(page, queryWrapper);
    }

    /**
     * 添加教师
     * 
     * @param teacher 教师信息
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean addTeacher(Teacher teacher) {
        return this.save(teacher);
    }

    /**
     * 更新教师信息
     * 
     * @param teacher 教师信息
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean updateTeacher(Teacher teacher) {
        return this.updateById(teacher);
    }

    /**
     * 删除教师
     * 
     * @param teacherId 教师工号
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteTeacher(Integer teacherId) {
        return this.removeById(teacherId);
    }
}