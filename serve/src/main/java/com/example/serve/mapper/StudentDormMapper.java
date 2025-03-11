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
        "<where>",
        "  <if test='keyword != null and keyword != \"\"'>",
        "    (s.studentNumber LIKE CONCAT('%',#{keyword},'%') OR s.studentName LIKE CONCAT('%',#{keyword},'%') OR s.majorname LIKE CONCAT('%',#{keyword},'%'))",
        "  </if>",
        "  <if test='department != null and department != \"\" and department != \"all\"'>",
        "    AND s.department = #{department}",
        "  </if>",
        "  <if test='majorname != null and majorname != \"\" and majorname != \"all\"'>",
        "    AND s.majorname = #{majorname}",
        "  </if>",
        "  <if test='classroomId != null and classroomId != 0'>",
        "    AND c.majorid = #{classroomId}",
        "  </if>",
        "</where>",
        "ORDER BY s.studentNumber",
        "</script>"
    })
    IPage<Student> selectAllStudents(Page<Student> page, 
                                   @Param("keyword") String keyword,
                                   @Param("department") String department, 
                                   @Param("majorname") String majorname, 
                                   @Param("classroomId") Integer classroomId);

    @Select({
        "<script>",
        "SELECT s.*, c.classroom FROM student s",
        "LEFT JOIN classroom c ON s.department = c.department",
        "WHERE (s.dormitory IS NULL OR s.dormitory = '' OR s.dorm_number IS NULL OR s.dorm_number = '')",
        "<if test='keyword != null and keyword != \"\"'>",
        "  AND (s.studentNumber LIKE CONCAT('%',#{keyword},'%') OR s.studentName LIKE CONCAT('%',#{keyword},'%') OR s.majorname LIKE CONCAT('%',#{keyword},'%'))",
        "</if>",
        "<if test='department != null and department != \"\"'>",
        "  AND s.department = #{department}",
        "</if>",
        "<if test='majorname != null and majorname != \"\"'>",
        "  AND s.majorname = #{majorname}",
        "</if>",
        "<if test='classroomId != null'>",
        "  AND c.majorid = #{classroomId}",
        "</if>",
        "<if test='selectDorm != null and selectDorm != \"\"'>",
        "  AND s.selectDorm = #{selectDorm}",
        "</if>",
        "ORDER BY s.studentNumber",
        "</script>"
    })
    IPage<Student> selectUnassignedStudents(Page<Student> page, 
                                          @Param("keyword") String keyword,
                                          @Param("department") String department, 
                                          @Param("majorname") String majorname, 
                                          @Param("classroomId") Integer classroomId,
                                          @Param("selectDorm") String selectDorm);

    @Select({
        "<script>",
        "SELECT s.*, c.classroom FROM student s",
        "LEFT JOIN classroom c ON s.department = c.department",
        "WHERE s.dormitory IS NOT NULL AND s.dormitory != '' AND s.dorm_number IS NOT NULL AND s.dorm_number != ''",
        "<if test='keyword != null and keyword != \"\"'>",
        "  AND (s.studentNumber LIKE CONCAT('%',#{keyword},'%') OR s.studentName LIKE CONCAT('%',#{keyword},'%') OR s.majorname LIKE CONCAT('%',#{keyword},'%'))",
        "</if>",
        "<if test='department != null and department != \"\"'>",
        "  AND s.department = #{department}",
        "</if>",
        "<if test='majorname != null and majorname != \"\"'>",
        "  AND s.majorname = #{majorname}",
        "</if>",
        "<if test='classroomId != null'>",
        "  AND c.majorid = #{classroomId}",
        "</if>",
        "<if test='dormitory != null and dormitory != \"\"'>",
        "  AND s.dormitory = #{dormitory}",
        "</if>",
        "<if test='dormCard != null and dormCard != \"\"'>",
        "  AND s.dorm_number = #{dormCard}",
        "</if>",
        "ORDER BY s.studentNumber",
        "</script>"
    })
    IPage<Student> selectAssignedStudents(Page<Student> page, 
                                        @Param("keyword") String keyword,
                                        @Param("department") String department, 
                                        @Param("majorname") String majorname, 
                                        @Param("classroomId") Integer classroomId,
                                        @Param("dormitory") String dormitory,
                                        @Param("dormCard") String dormCard);

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
    
    /**
     * 根据宿舍楼和宿舍号获取所有学生信息
     * 
     * @param dormitory 宿舍楼
     * @param dormCard 宿舍号
     * @return 学生列表
     */
    @Select("SELECT s.* FROM student s " +
            "JOIN student_dorm sd ON s.studentNumber = sd.studentNumber " +
            "WHERE sd.dormitory = #{dormitory} AND sd.dormCard = #{dormCard} " +
            "ORDER BY s.studentNumber")
    List<Student> getStudentsByDormInfo(@Param("dormitory") String dormitory, 
                                      @Param("dormCard") String dormCard);
}