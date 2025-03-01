package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.Classroom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClassMapper extends BaseMapper<Classroom> {
    
    /**
     * 获取所有班级信息，并关联查询辅导员和学习导师的姓名
     * @return 班级信息列表，包含辅导员和学习导师姓名
     */
    @Select("SELECT c.*, " +
            "f.teacherName as fteacherName, " +
            "s.teacherName as steacherName " +
            "FROM classroom c " +
            "LEFT JOIN teacher f ON c.fteacherid = f.teacherid " +
            "LEFT JOIN teacher s ON c.steacherid = s.teacherid")
    List<Map<String, Object>> selectAllClassesWithTeacherName();
}