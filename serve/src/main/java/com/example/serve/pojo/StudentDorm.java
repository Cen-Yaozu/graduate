package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生宿舍信息实体类
 *
 * @author author
 * @since 2025-02-23
 */
@TableName("student_dorm")
@Data
public class StudentDorm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableField("studentNumber")
    private String studentNumber;

    /**
     * 学生姓名
     */
    @TableField("studentName")
    private String studentName;

    /**
     * 院系
     */
    @TableField("department")
    private String department;

    /**
     * 宿舍类型
     */
    @TableField("dormType")
    private String dormType;

    /**
     * 宿舍号
     */
    @TableField("dormitory")
    private String dormitory;

    /**
     * 宿舍门卡号
     */
    @TableField("dormCard")
    private String dormCard;
}