package com.example.serve.dto;

import com.example.serve.pojo.Student;
import lombok.Data;

import java.util.List;

@Data
public class DormDTO {
    private Integer id;
    private String buildingNo;
    private String roomNo;
    private Integer type;
    private Integer capacity;
    private Integer occupied;
    private List<Student> members;
} 