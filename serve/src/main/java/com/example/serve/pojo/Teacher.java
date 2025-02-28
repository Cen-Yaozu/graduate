package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 教师实体类
 */
@Data
@TableName("teacher")
public class Teacher {

    /**
     * 教师工号，主键
     */
    @TableId(value = "teacherid", type = IdType.INPUT)
    private Integer teacherNumber;

    /**
     * 教师姓名
     */
    @TableField("teacherName")
    private String teacherName;

    /**
     * 联系电话
     */
    @TableField("teacherPhone")
    private String teacherPhone;

    /**
     * 无参构造函数
     */
    public Teacher() {
    }

    /**
     * 全参构造函数
     */
    public Teacher(Integer teacherNumber, String teacherName, String teacherPhone) {
        this.teacherNumber = teacherNumber;
        this.teacherName = teacherName;
        this.teacherPhone = teacherPhone;
    }
}