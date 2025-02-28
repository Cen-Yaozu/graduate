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
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private FamilyMapper familyMapper;

    @RequestMapping("/toUser")
    public String toUser(){
        System.out.println("123456");
        return null;
    }

    @RequestMapping(value = "/addStudentpicture",method = RequestMethod.POST)
    public String addStudentpicture(Student student, MultipartFile img1) {//单上传
        String imgFilename="";
        //设置上传文件的保存地址目录
        String path="D:\\毕业设计\\picture\\studentpicture";
        File filePath = new File(path);
        // 如果保存文件的地址不存在，就先创建目录
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        System.out.println(img1);
        if (!img1.isEmpty()){
            imgFilename = img1.getOriginalFilename();
            try {
                // 使用MultipartFile接口的方法完成文件上传到指定位置
                img1.transferTo(new File(path +File.separator + imgFilename));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
