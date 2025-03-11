package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.Classroom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    
    /**
     * 根据班级ID获取班级信息，包括关联的专业信息和教师信息
     * @param classroomId 班级ID
     * @return 班级详细信息
     */
    @Select("SELECT c.*, m.majorname, m.department, " +
            "f.teacherName as fteacherName, " +
            "s.teacherName as steacherName " +
            "FROM classroom c " +
            "LEFT JOIN major m ON c.majorid = m.majorid " +
            "LEFT JOIN teacher f ON c.fteacherid = f.teacherid " +
            "LEFT JOIN teacher s ON c.steacherid = s.teacherid " +
            "WHERE c.majorid = #{classroomId}")
    Map<String, Object> selectClassInfoWithTeacherName(@Param("classroomId") String classroomId);
    
    /**
     * 根据班级名称和专业ID查询特定班级信息
     * @param majorId 专业ID
     * @param classroom 班级名称
     * @return 班级详细信息
     */
    @Select("SELECT c.*, m.majorname, m.department, " +
            "f.teacherName as fteacherName, " +
            "s.teacherName as steacherName " +
            "FROM classroom c " +
            "LEFT JOIN major m ON c.majorid = m.majorid " +
            "LEFT JOIN teacher f ON c.fteacherid = f.teacherid " +
            "LEFT JOIN teacher s ON c.steacherid = s.teacherid " +
            "WHERE c.majorid = #{majorId} AND c.classroom = #{classroom}")
    Map<String, Object> selectClassInfoByMajorAndName(@Param("majorId") String majorId, @Param("classroom") String classroom);
    
    /**
     * 统计指定班级的学生人数
     * @param classroomId 班级ID
     * @return 学生人数
     */
    @Select("SELECT COUNT(*) FROM student WHERE classroom_id = #{classroomId}")
    Integer countStudentsByClassroomId(@Param("classroomId") String classroomId);
}