package com.example.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.serve.mapper.FamilyMapper;
import com.example.serve.mapper.ResumeMapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.pojo.Family;
import com.example.serve.pojo.Resume;
import com.example.serve.pojo.Student;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 新生报道控制器
 * 处理新生报道过程中的各种功能
 */
@RestController
@RequestMapping("/api/freshman-report")
public class FreshmanReportController {
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private ResumeMapper resumeMapper;
    
    @Autowired
    private FamilyMapper familyMapper;
    
    @Value("${spring.upload.photo-path}")
    private String uploadPhotoPath;
    
    @Value("${spring.upload.photo-url-prefix}")
    private String photoUrlPrefix;
    
    /**
     * 上传学生照片
     * 将照片保存在服务器，并更新学生表中的照片路径
     */
    @PostMapping("/upload-photo")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Void> uploadPhoto(@RequestParam("file") MultipartFile file, 
                                           @RequestParam("studentNumber") String studentNumber) {
        try {
            // 调试日志
            System.out.println("收到照片上传请求 - 学号: " + studentNumber + ", 文件名: " + (file != null ? file.getOriginalFilename() : "文件为空"));
            
            // 验证参数
            if (file == null || file.isEmpty()) {
                System.out.println("上传失败: 文件为空");
                return ResponseResult.errorResult(400, "请选择要上传的照片");
            }
            
            if (studentNumber == null || studentNumber.trim().isEmpty()) {
                System.out.println("上传失败: 学号为空");
                return ResponseResult.errorResult(400, "学号不能为空");
            }
            
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 生成新的文件名
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            
            // 创建保存目录
            File dir = new File(uploadPhotoPath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    System.out.println("上传失败: 无法创建目录 " + uploadPhotoPath);
                    return ResponseResult.errorResult(500, "无法创建文件上传目录，请检查路径权限");
                }
            }
            
            // 打印完整路径，用于调试
            System.out.println("保存照片到: " + dir.getAbsolutePath() + File.separator + newFilename);
            
            // 保存文件到服务器
            File destFile = new File(dir.getAbsolutePath() + File.separator + newFilename);
            file.transferTo(destFile);
            
            // 将照片的URL路径保存到数据库
            String photoUrl = photoUrlPrefix + newFilename;
            System.out.println("照片URL: " + photoUrl);
            
            // 先通过学号查询学生信息
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("studentNumber", studentNumber);
            Student existingStudent = studentMapper.selectOne(queryWrapper);
            
            if (existingStudent == null) {
                System.out.println("上传失败: 未找到学号为 " + studentNumber + " 的学生");
                return ResponseResult.errorResult(404, "未找到对应学生信息");
            }
            
            // 更新学生信息
            existingStudent.setStudentPicture(photoUrl);
            
            // 使用MyBatis-Plus的updateById方法更新数据库
            int result = studentMapper.updateById(existingStudent);
            
            if (result > 0) {
                return ResponseResult.okResult(200, "照片上传成功");
            } else {
                return ResponseResult.errorResult(500, "照片上传失败，无法更新学生信息");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "照片上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 一次性保存所有学生信息（基本信息、简历、家庭成员）
     */
    @PostMapping("/save-all-info")
    @Transactional
    public ResponseResult<Void> saveAllInfo(@RequestBody StudentInfoDTO studentInfoDTO) {
        try {
            // 提取数据
            Student student = studentInfoDTO.getStudent();
            List<Resume> resumes = studentInfoDTO.getResumes();
            List<Family> familyMembers = studentInfoDTO.getFamilyMembers();
            
            // 验证数据
            if (student == null || student.getStudentNumber() == null || student.getStudentNumber().trim().isEmpty()) {
                return ResponseResult.errorResult(400, "学生信息或学号不能为空");
            }
            
            // 获取学生ID
            String studentNumber = student.getStudentNumber();
            
            // 检查学生信息是否已经完成
            Student existingStudent = studentMapper.selectById(studentNumber);
            if (existingStudent != null && existingStudent.getHometown() != null && existingStudent.getNation() != null
                && existingStudent.getPoliteAspect() != null && existingStudent.getStudentPhone() != null
                && existingStudent.getAddress() != null && !existingStudent.getHometown().isEmpty() 
                && !existingStudent.getNation().isEmpty() && !existingStudent.getPoliteAspect().isEmpty()
                && !existingStudent.getStudentPhone().isEmpty() && !existingStudent.getAddress().isEmpty()) {
                return ResponseResult.errorResult(403, "信息已经提交，不允许修改");
            }
            
            // 更新学生基本信息
            studentMapper.updateById(student);
            
            // 保存简历信息
            if (resumes != null && !resumes.isEmpty()) {
                // 删除已有的简历记录
                QueryWrapper<Resume> resumeWrapper = new QueryWrapper<>();
                resumeWrapper.eq("studentNumber", studentNumber);
                resumeMapper.delete(resumeWrapper);
                
                // 添加新的简历记录
                for (Resume resume : resumes) {
                    resume.setStudentNumber(studentNumber);
                    resumeMapper.insert(resume);
                }
            }
            
            // 保存家庭成员信息
            if (familyMembers != null && !familyMembers.isEmpty()) {
                // 删除已有的家庭成员记录
                QueryWrapper<Family> familyWrapper = new QueryWrapper<>();
                familyWrapper.eq("studentNumber", studentNumber);
                familyMapper.delete(familyWrapper);
                
                // 添加新的家庭成员记录
                for (Family family : familyMembers) {
                    family.setStudentNumber(studentNumber);
                    familyMapper.insert(family);
                }
            }
            
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseResult.errorResult(500, "保存所有信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据学号获取学生的完整信息（基本信息、简历信息和家庭成员信息）
     */
    @GetMapping("/get-student-info")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<StudentInfoDTO> getStudentInfo(@RequestParam("studentNumber") String studentNumber) {
        try {
            // 验证参数
            if (studentNumber == null || studentNumber.trim().isEmpty()) {
                return ResponseResult.errorResult(400, "学号不能为空");
            }
            
            // 查询学生基本信息
            Student student = null;
            try {
                // 使用条件构造器
                LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
                studentWrapper.eq(Student::getStudentNumber, studentNumber);
                student = studentMapper.selectOne(studentWrapper);
                
                if (student == null) {
                    return ResponseResult.errorResult(404, "未找到该学生信息");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return ResponseResult.errorResult(500, "查询学生信息失败: " + ex.getMessage());
            }
            
            // 查询学生简历信息
            List<Resume> resumes = new ArrayList<>();
            try {
                // 使用条件构造器
                LambdaQueryWrapper<Resume> resumeWrapper = new LambdaQueryWrapper<>();
                resumeWrapper.eq(Resume::getStudentNumber, studentNumber);
                resumes = resumeMapper.selectList(resumeWrapper);
            } catch (Exception ex) {
                // 如果查询失败，记录错误但继续程序执行，返回空列表
                ex.printStackTrace();
                System.err.println("查询简历信息失败: " + ex.getMessage());
            }
            
            // 查询学生家庭成员信息
            List<Family> familyMembers = new ArrayList<>();
            try {
                // 使用条件构造器
                LambdaQueryWrapper<Family> familyWrapper = new LambdaQueryWrapper<>();
                familyWrapper.eq(Family::getStudentNumber, studentNumber);
                familyMembers = familyMapper.selectList(familyWrapper);
            } catch (Exception ex) {
                // 如果查询失败，记录错误但继续程序执行，返回空列表
                ex.printStackTrace();
                System.err.println("查询家庭成员失败: " + ex.getMessage());
            }
            
            // 组装返回数据
            StudentInfoDTO studentInfoDTO = new StudentInfoDTO();
            studentInfoDTO.setStudent(student);
            studentInfoDTO.setResumes(resumes);
            studentInfoDTO.setFamilyMembers(familyMembers);
            
            return ResponseResult.okResult(studentInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "获取学生信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 添加获取照片的API
     */
    @GetMapping("/get-photo")
    public void getPhoto(@RequestParam("path") String photoPath, HttpServletResponse response) throws IOException {
        try {
            // 从路径中提取文件名
            String fileName = photoPath;
            if (photoPath.startsWith("/")) {
                fileName = photoPath.substring(1);
            }
            if (photoPath.contains("/")) {
                fileName = photoPath.substring(photoPath.lastIndexOf("/") + 1);
            }
            
            // 构建完整物理路径
            String fullPath = uploadPhotoPath + File.separator + fileName;
            File file = new File(fullPath);
            
            // 安全检查：确保文件存在且在允许的目录内
            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
                return;
            }
            
            // 设置响应头
            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control", "max-age=604800"); // 缓存一周
            
            // 输出文件内容
            try (FileInputStream in = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading file");
        }
    }
    
    /**
     * 用于传输学生所有信息的DTO类
     */
    public static class StudentInfoDTO {
        private Student student;
        private List<Resume> resumes;
        private List<Family> familyMembers;
        
        public Student getStudent() {
            return student;
        }
        
        public void setStudent(Student student) {
            this.student = student;
        }
        
        public List<Resume> getResumes() {
            return resumes;
        }
        
        public void setResumes(List<Resume> resumes) {
            this.resumes = resumes;
        }
        
        public List<Family> getFamilyMembers() {
            return familyMembers;
        }
        
        public void setFamilyMembers(List<Family> familyMembers) {
            this.familyMembers = familyMembers;
        }
    }
} 