package com.example.serve.pojo.payment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费记录实体类
 *
 * @author author
 * @since 2025-02-27
 */
@TableName("payment_record")
@Data
public class PaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    @TableField("student_number")
    private String studentNumber;

    /**
     * 缴费项目ID
     */
    @TableField("payment_item_id")
    private Integer paymentItemId;

    /**
     * 缴费金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 支付方式（微信支付、支付宝、银行卡）
     */
    @TableField("payment_method")
    private String paymentMethod;

    /**
     * 支付时间
     */
    @TableField("payment_date")
    private LocalDateTime paymentDate;

    /**
     * 支付状态（0-未支付，1-已支付）
     */
    @TableField("status")
    private Integer status;

    /**
     * 交易流水号
     */
    @TableField("transaction_number")
    private String transactionNumber;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}