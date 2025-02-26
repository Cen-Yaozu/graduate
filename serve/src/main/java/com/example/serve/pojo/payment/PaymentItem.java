package com.example.serve.pojo.payment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 缴费项目实体类
 *
 * @author author
 * @since 2025-02-27
 */
@TableName("payment_item")
@Data
public class PaymentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目名称
     */
    @TableField("name")
    private String name;

    /**
     * 缴费金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 截止日期
     */
    @TableField("deadline")
    private String deadline;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    /**
     * 项目类型（0-学费，1-住宿费，2-其他）
     */
    @TableField("type")
    private Integer type;

    /**
     * 适用年级
     */
    @TableField("grade")
    private String grade;

    /**
     * 适用专业，为空表示所有专业
     */
    @TableField("major")
    private String major;
}