package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DormMapper extends BaseMapper<Dorm> {

        @Select({
                        "<script>",
                        "SELECT d.* FROM dorm d",
                        "<where>",
                        "  <if test='keyword != null and keyword != \"\"'>",
                        "    (d.department LIKE CONCAT('%',#{keyword},'%') OR d.dormitory LIKE CONCAT('%',#{keyword},'%') OR d.dormCard LIKE CONCAT('%',#{keyword},'%'))",
                        "  </if>",
                        "  <if test='dormsex != null and dormsex != \"\"'>",
                        "    AND d.dormsex = #{dormsex}",
                        "  </if>",
                        "  <if test='dormType != null and dormType != \"\"'>",
                        "    AND d.dormType = #{dormType}",
                        "  </if>",
                        "</where>",
                        "ORDER BY d.department, d.dormitory, d.dormCard",
                        "</script>"
        })
        IPage<Dorm> selectDormPage(Page<Dorm> page, @Param("keyword") String keyword, @Param("dormsex") String dormsex, @Param("dormType") String dormType);

        @Select("SELECT * FROM dorm ORDER BY department, dormitory, dormCard")
        List<Dorm> selectAllDorms();

        @Select({
                        "SELECT s.* FROM student s",
                        "JOIN student_dorm sd ON s.studentNumber = sd.studentNumber",
                        "WHERE sd.dormCard = #{dormCard} AND sd.dormitory = #{dormitory}"
        })
        List<Student> selectDormMembers(@Param("dormCard") String dormCard, @Param("dormitory") String dormitory);

        @Select({
                        "SELECT studentNumber FROM student_dorm",
                        "WHERE dormCard = #{dormCard} AND dormitory = #{dormitory}"
        })
        List<String> selectStudentNumbersByDorm(@Param("dormCard") String dormCard,
                        @Param("dormitory") String dormitory);
}