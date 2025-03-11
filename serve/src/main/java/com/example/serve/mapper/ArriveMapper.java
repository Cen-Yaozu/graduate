package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.Arrive;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 抵校信息数据访问接口
 */
@Mapper
public interface ArriveMapper extends BaseMapper<Arrive> {
    
    /**
     * 查询所有抵校信息
     * 
     * @return 抵校信息列表
     */
    @Select("SELECT * FROM arrive")
    List<Arrive> findAll();
    
    /**
     * 根据学号查询抵校信息，关联学生表获取姓名
     * 
     * @param studentNumber 学号
     * @return 抵校信息
     */
    @Select("SELECT a.studentNumber, COALESCE(s.studentName, a.studentName) as studentName, a.time, a.tool, a.familyNum, a.date FROM arrive a LEFT JOIN student s ON a.studentNumber = s.studentNumber WHERE a.studentNumber = #{studentNumber}")
    Arrive findByStudentNumber(String studentNumber);
    
    /**
     * 更新抵校信息
     * 
     * @param arrive 抵校信息
     * @return 影响行数
     */
    @Update("UPDATE arrive SET name = #{name}, time = #{time}, tool = #{tool}, familynum = #{familyNum}, date = #{date} " +
           "WHERE studentNumber = #{studentNumber}")
    int update(Arrive arrive);
}