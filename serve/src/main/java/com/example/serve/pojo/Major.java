package com.example.serve.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("major")
public class Major {
    @TableField("department")
    private String department;    // 系别

    @TableField("majorid")
    private String majorid;      // 专业号

    @TableField("majorname")
    private String majorname;    // 专业名

    @TableField("departmentPrize")
    private Integer departmentPrize;  // 学费
}