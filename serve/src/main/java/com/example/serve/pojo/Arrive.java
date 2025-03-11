package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@TableName("arrive")
public class Arrive {
    @TableField("studentNumber")
    private String studentNumber;  // 学号
    @TableField("studentName")
    private String studentName;          // 姓名
    private String time;        // 抵达时间
    private String tool;          // 交通工具
    @TableField("familyNum")
    private Integer familyNum;    // 家人陪同人数
    private LocalDate date;       // 抵达日期
}