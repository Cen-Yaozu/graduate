package com.example.serve.controller.student;

import com.example.serve.mapper.FamilyMapper;
import com.example.serve.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private FamilyMapper familyMapper;

    @RequestMapping("/toUser")
    public String toUser(){
        System.out.println("123456");
        return null;
    }


}
