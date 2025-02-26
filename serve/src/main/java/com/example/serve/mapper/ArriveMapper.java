package com.example.serve.mapper;

import com.example.serve.pojo.Arrive;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArriveMapper {
    @Select("SELECT * FROM arrive")
    List<Arrive> findAll();
    
    @Select("SELECT a.studentNumber, COALESCE(s.studentName, a.studentName) as name, a.time, a.tool, a.familyNum, a.date FROM arrive a LEFT JOIN student s ON a.studentNumber = s.studentNumber WHERE a.studentNumber = #{studentNumber}")
    Arrive findByStudentNumber(String studentNumber);
    
    @Update("UPDATE arrive SET name = #{name}, time = #{time}, tool = #{tool}, familynum = #{familyNum}, date = #{date} " +
           "WHERE studentNumber = #{studentNumber}")
    int update(Arrive arrive);
}