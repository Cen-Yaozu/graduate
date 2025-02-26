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
 * @since 2025-01-15
 */
@TableName("student")
@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private Integer id;

    /**
     * 准考证
     */
    @TableField("admissionTicket")
    private String admissionTicket;
    /**
     * 学号
     */
    @TableField("studentNumber")
    private String studentNumber;
    /**
     * 姓名
     */
    @TableField("studentName")
    private String studentName;
    /**
     * 学生照片
     */
    @TableField("studentPicture")
    private String studentPicture;
    /**
     * 性别
     */
    @TableField("sex")
    private String sex;
    /**
     * 出生年月
     */
    @TableField("birth")
    private String birth;
    /**
     * 籍贯
     */
    @TableField("hometown")
    private String hometown;
    /**
     * 民族
     */
    @TableField("nation")
    private String nation;
    /**
     * 证件
     */
    @TableField("idType")
    private String idType;
    /**
     * 身份证号码
     */
    @TableField("idCard")
    private String idCard;
    /**
     * 政治面貌
     */
    @TableField("politeAspect")
    private String politeAspect;
    /**
     * 邮政编码
     */
    @TableField("zipCode")
    private String zipCode;
    /**
     * 
电话
     */
    @TableField("studentPhone")
    private String studentPhone;
    /**
     * 详细通讯地址
     */
    @TableField("address")
    private String address;
    /**
     * 专业名
     */
    @TableField("majorname")
    private String majorname;
    /**
     * 年级
     */
    @TableField("grade")
    private String grade;
    /**
     * 层次
     */
    @TableField("level")
    private String level;
    /**
     * 邮件
     */
    @TableField("email")
    private String email;
    /**
     * 系别
     */
    @TableField("department")
    private String department;
    /**
     * 抵校状态
     */
    @TableField("arrive_status")
    private Integer arriveStatus;
    /**
     * 缴费状态
     */
    @TableField("payment_status")
    private Integer paymentStatus;

    /**
     * 宿舍楼
     */
    @TableField("dormitory")
    private String dormitory;

    /**
     * 宿舍号
     */
    @TableField("dorm_number")
    private String dormNumber;

    public Integer getArriveStatus() {
        return arriveStatus;
    }
    public void setArriveStatus(Integer arriveStatus) {
        this.arriveStatus = arriveStatus;
    }
}
