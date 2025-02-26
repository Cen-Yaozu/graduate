package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 缴费实体类
 *
 * @author author
 * @since 2025-02-23
 */
@Data
@TableName("pay")
public class PayItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableField("studentNumber")
    private Integer studentNumber;

    /**
     * 姓名
     */
    @TableField("studentName")
    private String studentName;

    /**
     * 缴费通道
     */
    @TableField("hallway")
    private String hallway;

    /**
     * 缴费编号
     */
    @TableField("amountcard")
    private String amountcard;

    /**
     * 总金额
     */
    @TableField("allmoney")
    private Integer allmoney;

    /**
     * 收费凭证图片
     */
    @TableField("img")
    private String img;

    /**
     * 订单状态
     */
    @TableField("indentStatue")
    private String indentStatue;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}