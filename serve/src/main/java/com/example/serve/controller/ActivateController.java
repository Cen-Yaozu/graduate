package com.example.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.mapper.UserMapper;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.User;
import com.example.serve.service.EmailService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 账号激活控制器
 * 提供账号激活相关功能
 */
@RestController
@RequestMapping("/api")
public class ActivateController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    /**
     * 验证学生信息
     * 根据准考证号、姓名、证件类型和证件号验证学生信息
     */
    @PostMapping("/verify-student")
    public ResponseResult<Map<String, String>> verifyStudent(@RequestBody Map<String, Object> requestBody) {
        String admissionTicket = (String) requestBody.get("admissionTicket");
        String studentName = (String) requestBody.get("studentName");
        String idType = (String) requestBody.get("idType");
        String idCard = (String) requestBody.get("idCard");

        // 验证参数是否为空
        if (!StringUtils.hasText(admissionTicket) ||
                !StringUtils.hasText(studentName) ||
                !StringUtils.hasText(idType) ||
                !StringUtils.hasText(idCard)) {
            return ResponseResult.errorResult(400, "请填写完整信息");
        }

        // 使用QueryWrapper查询学生信息
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admissionTicket", admissionTicket)
                .eq("studentName", studentName)
                .eq("idType", idType)
                .eq("idCard", idCard);

        Student student = studentMapper.selectOne(queryWrapper);

        if (student != null) {
            // 学生信息存在，返回学号
            Map<String, String> data = new HashMap<>();

            // 如果学号为空，模拟生成学号 (实际项目中可能已有学号或通过其他方式分配)
            if (!StringUtils.hasText(student.getStudentNumber())) {
                // 简单示例：使用年份(2024) + 准考证号后6位作为学号
                String generatedStudentNumber = "2024"
                        + admissionTicket.substring(Math.max(0, admissionTicket.length() - 6));

                // 更新学生记录的学号
                student.setStudentNumber(generatedStudentNumber);
                studentMapper.updateById(student);

                data.put("studentNumber", generatedStudentNumber);
            } else {
                data.put("studentNumber", student.getStudentNumber());
            }

            // 检查学生是否已经拥有系统账号（查找user表中是否有对应的studentNumber）
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("studentNumber", data.get("studentNumber"));
            User user = userMapper.selectOne(userQueryWrapper);

            // 如果用户账号存在，表示已激活
            if (user != null) {
                data.put("isActivated", "true");
                System.out.println("学生" + data.get("studentNumber") + "已有账号，直接跳到最后一步");
            } else {
                data.put("isActivated", "false");
                System.out.println("学生" + data.get("studentNumber") + "未激活账号，需完成激活流程");
            }

            return new ResponseResult<>(200, "考生信息验证成功", data);
        } else {
            return ResponseResult.errorResult(404, "信息不正确，请重新输入");
        }
    }

    /**
     * 发送验证码
     * 发送验证码到邮箱
     */
    @PostMapping("/send-verification")
    public ResponseResult<Void> sendVerification(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String studentNumber = (String) requestBody.get("studentNumber");

        // 验证参数是否为空
        if (!StringUtils.hasText(email) || !StringUtils.hasText(studentNumber)) {
            return ResponseResult.errorResult(400, "邮箱和学号不能为空");
        }

        // 验证邮箱格式
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            return ResponseResult.errorResult(400, "邮箱格式不正确");
        }

        try {
            // 发送验证码邮件
            String verificationCode = emailService.sendVerificationEmail(email, studentNumber);

            // 记录日志
            System.out.println("向 " + email + " 发送验证码: " + verificationCode);

            return ResponseResult.okResult(200, "验证码已发送，请查收邮箱");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "验证码发送失败: " + e.getMessage());
        }
    }

    /**
     * 验证验证码
     * 检查用户输入的验证码是否正确
     */
    @PostMapping("/verify-code")
    public ResponseResult<Void> verifyCode(@RequestBody Map<String, Object> requestBody) {
        String studentNumber = (String) requestBody.get("studentNumber");
        String verificationCode = (String) requestBody.get("verificationCode");

        // 验证参数是否为空
        if (!StringUtils.hasText(studentNumber) || !StringUtils.hasText(verificationCode)) {
            return ResponseResult.errorResult(400, "学号和验证码不能为空");
        }

        // 调用EmailService的verifyCode方法验证验证码
        boolean isValid = emailService.verifyCode(studentNumber, verificationCode);

        if (isValid) {
            return ResponseResult.okResult(200, "验证成功");
        } else {
            return ResponseResult.errorResult(400, "验证码输入错误");
        }
    }

    /**
     * 激活账号
     * 验证验证码并创建用户账号
     */
    @PostMapping("/activate-account")
    public ResponseResult<Void> activateAccount(@RequestBody Map<String, Object> requestBody) {
        String studentNumber = (String) requestBody.get("studentNumber");
        String password = (String) requestBody.get("password");
        String email = (String) requestBody.get("email");
        String verificationCode = (String) requestBody.get("verificationCode");
        // 获取可选的验证标记
        Boolean codeVerified = (Boolean) requestBody.get("codeVerified");

        // 验证参数是否为空
        if (!StringUtils.hasText(studentNumber) || !StringUtils.hasText(password)) {
            return ResponseResult.errorResult(400, "学号和密码不能为空");
        }

        // 检查用户是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentNumber", studentNumber);
        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            return ResponseResult.errorResult(409, "该账号已激活，请直接登录");
        }

        // 验证密码长度
        if (password.length() < 6) {
            return ResponseResult.errorResult(400, "密码长度不能少于6位");
        }

        // 只有当未验证过且提供了验证码时才进行验证
        if (codeVerified == null || !codeVerified) {
            if (StringUtils.hasText(verificationCode)) {
                boolean codeValid = emailService.verifyCode(studentNumber, verificationCode);
    
                if (!codeValid) {
                    return ResponseResult.errorResult(400, "验证码错误或已过期");
                }
            }
        } else {
            System.out.println("验证码已在之前验证通过，跳过验证步骤");
        }

        // 创建新用户
        User newUser = new User();
        newUser.setStudentNumber(studentNumber);

        // 密码简单处理：添加{noop}前缀表示不加密的密码
        newUser.setPassword("{noop}" + password);

        try {
            // 保存用户信息
            int result = userMapper.insert(newUser);

            if (result > 0) {
                // 为用户分配STUDENT角色
                Integer roleId = userMapper.getAuthorityIdByName("ROLE_STUDENT");
                
                if (roleId == null) {
                    System.out.println("警告: 未找到STUDENT角色，需要检查数据库配置");
                } else {
                    // 新增用户-角色关联
                    userMapper.insertUserAuthority(newUser.getId(), roleId);
                    System.out.println("成功为用户 " + studentNumber + " 分配STUDENT角色");
                }
                
                // 成功创建用户后，可以更新学生表中的邮箱信息
                QueryWrapper<Student> studentQuery = new QueryWrapper<>();
                studentQuery.eq("studentNumber", studentNumber);
                Student student = studentMapper.selectOne(studentQuery);

                if (student != null && StringUtils.hasText(email)) {
                    student.setEmail(email); // 使用email字段存储邮箱
                    studentMapper.updateById(student);
                }

                return ResponseResult.okResult(200, "账号激活成功");
            } else {
                return ResponseResult.errorResult(500, "账号激活失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "账号激活失败: " + e.getMessage());
        }
    }

    /**
     * 忘记密码 - 发送验证码
     * 根据学号查询学生邮箱并发送验证码
     */
    @PostMapping("/send-verify-code")
    public ResponseResult<Void> sendVerifyCodeForPasswordReset(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中获取学号
        String studentNumber = (String) requestBody.get("studentNumber");
        
        // 验证参数是否为空
        if (!StringUtils.hasText(studentNumber)) {
            return ResponseResult.errorResult(400, "学号不能为空");
        }
        
        try {
            // 根据学号查询学生信息
            LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Student::getStudentNumber, studentNumber);
            Student student = studentMapper.selectOne(queryWrapper);
            
            // 检查学生是否存在
            if (student == null) {
                return ResponseResult.errorResult(404, "未找到该学号对应的学生信息");
            }
            
            // 获取学生邮箱
            String email = student.getEmail();
            
            // 检查邮箱是否为空
            if (email == null || email.trim().isEmpty()) {
                return ResponseResult.errorResult(400, "请先激活账号");
            }
            
            // 生成并发送验证码
            emailService.sendVerificationEmail(email, studentNumber);
            
            return ResponseResult.okResult(200, "验证码已发送到邮箱");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "验证码发送失败: " + e.getMessage());
        }
    }

    /**
     * 重置密码
     * 验证验证码并更新用户密码
     */
    @PostMapping("/reset-password")
    public ResponseResult<Void> resetPassword(@RequestBody Map<String, Object> requestBody) {
        String studentNumber = (String) requestBody.get("studentNumber");
        String verifyCode = (String) requestBody.get("verifyCode");
        String newPassword = (String) requestBody.get("newPassword");
        
        // 验证参数是否为空
        if (!StringUtils.hasText(studentNumber) || !StringUtils.hasText(verifyCode) || !StringUtils.hasText(newPassword)) {
            return ResponseResult.errorResult(400, "学号、验证码和新密码不能为空");
        }
        
        try {
            // 验证验证码
            boolean isValid = emailService.verifyCode(studentNumber, verifyCode);
            
            if (!isValid) {
                return ResponseResult.errorResult(400, "验证码无效或已过期");
            }
            
            // 查询用户信息
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getStudentNumber, studentNumber);
            User user = userMapper.selectOne(queryWrapper);
            
            if (user == null) {
                return ResponseResult.errorResult(404, "未找到该学号对应的用户账号");
            }
            
            // 更新密码 (这里假设密码已经在前端进行了加密或者在Service层会处理密码加密)
            user.setPassword("{noop}"+newPassword);
            userMapper.updateById(user);
            
            return ResponseResult.okResult(200, "密码重置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "密码重置失败: " + e.getMessage());
        }
    }
}