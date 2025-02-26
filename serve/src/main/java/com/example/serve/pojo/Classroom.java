package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("classroom")
public class Classroom {
    @TableId(type = IdType.AUTO)
    @TableField("majorid")
    private Integer majorid;      // 专业号

    @TableField("classroom")
    private String classroom;     // 班级

    @TableField("fteacherid")
    private Integer fteacherid;   // 辅导员

    @TableField("steacherid")
    private String steacherid;    // 学习导师

    @TableField("department")
    private String department;    // 系别

    @TableField("classroomNum")
    private Integer classroomNum; // 班级人数
}