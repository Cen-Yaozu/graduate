package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 宿舍实体类
 */
@Data
@TableName("dorm")
public class Dorm {
    /**
     * 系别
     */
    private String department;

    /**
     * 宿舍类型
     */
    @TableField("dormType")
    private String dormType;

    /**
     * 宿舍楼
     */
    @TableField("dormitory")
    private String dormitory;

    /**
     * 宿舍号
     */
    @TableField("dormCard")
    private String dormCard;

    /**
     * 宿舍数量
     */
    @TableField("dormNum")
    private Integer dormNum;

    /**
     * 宿舍价格
     */
    @TableField("dormPrize")
    private Integer dormPrize;

    /**
     * 住宿性别
     */
    @TableField("dormsex")
    private String dormsex;

    /**
     * 宿舍成员，非数据库字段
     */
    @TableField(exist = false)
    private List<Student> members;
} 