package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-01-06
 */
@TableName("family")
@Data
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号

     */
    @TableField("studentNumber")
    private String studentNumber;

    /**
     * 称谓

     */
    @TableField("familyPart")
    private String familyPart;

    /**
     * 姓名

     */
    @TableField("familyName")
    private String familyName;

    /**
     * 年龄

     */
    @TableField("familyAge")
    private String familyAge;

    /**
     * 政治面貌

     */
    @TableField("familyPoliteAspect")
    private String familyPoliteAspect;

    /**
     * 单位学习、工作

     */
    @TableField("familyOrganization")
    private String familyOrganization;
}
