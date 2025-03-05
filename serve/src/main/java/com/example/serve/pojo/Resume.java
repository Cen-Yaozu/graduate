package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-12-29
 */
@TableName("resume")
@Data
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号

     */
    @TableId(value = "studentNumber", type = IdType.NONE)
    private String studentNumber;

    /**
     * 开始时间

     */
    @TableField("startTime")
    private String startTime;

    /**
     * 结束时间

     */
    @TableField("endTime")
    private String endTime;

    /**
     * 单位学习、工作

     */
    @TableField("organization")
    private String organization;

    /**
     * 职务

     */
    @TableField("hats")
    private String hats;

}
