package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.StudentDorm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学生宿舍信息数据访问接口
 *
 * @author author
 * @since 2025-02-23
 */
public interface StudentDormMapper extends BaseMapper<StudentDorm> {

    @Select({
        "<script>",
        "SELECT s.*, c.classroom FROM student s",
        "LEFT JOIN classroom c ON s.department = c.department",
        "LEFT JOIN student_dorm sd ON s.studentNumber = sd.studentNumber",
        "LEFT JOIN dorm d ON sd.dormitory = d.dormitory AND sd.dormCard = d.dormCard",
        "<where>",
        "  <if test='department != null and department != \"\"'>",
        "    s.department = #{department}",
        "  </if>",
        "  <if test='majorname != null and majorname != \"\"'>",
        "    AND s.majorname = #{majorname}",
        "  </if>",
        "  <if test='classroomId != null'>",
        "    AND c.majorid = #{classroomId}",
        "  </if>",
        "</where>",
        "ORDER BY s.studentNumber",
        "</script>"
    })
    IPage<Student> selectAllStudents(Page<Student> page, 
                                   @Param("department") String department, 
                                   @Param("majorname") String majorname, 
                                   @Param("classroomId") Integer classroomId);

    @Select({
        "<script>",
        "SELECT s.*, c.classroom FROM student s",
        "LEFT JOIN classroom c ON s.department = c.department",
        "LEFT JOIN student_dorm sd ON s.studentNumber = sd.studentNumber",
        "WHERE sd.studentNumber IS NULL",
        "<if test='department != null and department != \"\"'>",
        "  AND s.department = #{department}",
        "</if>",
        "<if test='majorname != null and majorname != \"\"'>",
        "  AND s.majorname = #{majorname}",
        "</if>",
        "<if test='classroomId != null'>",
        "  AND c.majorid = #{classroomId}",
        "</if>",
        "ORDER BY s.studentNumber",
        "</script>"
    })
    IPage<Student> selectUnassignedStudents(Page<Student> page, 
                                          @Param("department") String department, 
                                          @Param("majorname") String majorname, 
                                          @Param("classroomId") Integer classroomId);

    @Select({
        "<script>",
        "SELECT s.*, c.classroom, d.dormitory as 'dormInfo.dormitory', d.dormCard as 'dormInfo.dormCard' FROM student s",
        "LEFT JOIN classroom c ON s.department = c.department",
        "JOIN student_dorm sd ON s.studentNumber = sd.studentNumber",
        "JOIN dorm d ON sd.dormitory = d.dormitory AND sd.dormCard = d.dormCard",
        "<where>",
        "  <if test='department != null and department != \"\"'>",
        "    s.department = #{department}",
        "  </if>",
        "  <if test='majorname != null and majorname != \"\"'>",
        "    AND s.majorname = #{majorname}",
        "  </if>",
        "  <if test='classroomId != null'>",
        "    AND c.majorid = #{classroomId}",
        "  </if>",
        "</where>",
        "ORDER BY s.studentNumber",
        "</script>"
    })
    IPage<Student> selectAssignedStudents(Page<Student> page, 
                                        @Param("department") String department, 
                                        @Param("majorname") String majorname, 
                                        @Param("classroomId") Integer classroomId);

    @Insert({
        "<script>",
        "INSERT INTO student_dorm(studentNumber, studentName, department, dormType, dormitory, dormCard) VALUES",
        "<foreach collection='studentNumbers' item='studentNumber' separator=','>",
        "(",
        "  #{studentNumber},",
        "  (SELECT studentName FROM student WHERE studentNumber = #{studentNumber}),",
        "  (SELECT department FROM student WHERE studentNumber = #{studentNumber}),",
        "  #{dormType},",
        "  #{dormitory},",
        "  #{dormCard}",
        ")",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("studentNumbers") List<String> studentNumbers, 
                    @Param("dormType") String dormType,
                    @Param("dormitory") String dormitory, 
                    @Param("dormCard") String dormCard);

    @Delete("DELETE FROM student_dorm WHERE studentNumber = #{studentNumber}")
    int deleteByStudentNumber(@Param("studentNumber") String studentNumber);
}