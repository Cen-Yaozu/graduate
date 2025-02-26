package com.example.serve.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Arrive {
    private String studentNumber;  // 学号
    private String name;          // 姓名
    private String time;        // 抵达时间
    private String tool;          // 交通工具
    private Integer familyNum;    // 家人陪同人数
    private LocalDate date;       // 抵达日期
}