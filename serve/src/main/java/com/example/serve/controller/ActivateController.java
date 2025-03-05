package com.example.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.mapper.UserMapper;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.User;
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

        // 在真实环境中，这里会有发送验证码到邮箱的逻辑
        // 此处仅作示例，不实际发送验证码

        // 模拟生成验证码并存储
        String verificationCode = UUID.randomUUID().toString().substring(0, 6);

        // 这里应该将验证码和学号关联存储，以便后续验证
        // 可以使用Redis等缓存服务存储验证码，并设置过期时间

        System.out.println("模拟向 " + email + " 发送验证码: " + verificationCode);

        return ResponseResult.okResult(200, "验证码已发送，请查收邮箱");
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

        // 在真实环境中，这里会验证验证码是否正确
        // 此处仅作示例，跳过验证码验证

        // 创建新用户
        User newUser = new User();
        newUser.setStudentNumber(studentNumber);

        // 密码简单处理：添加{noop}前缀表示不加密的密码
        newUser.setPassword("{noop}" + password);

        try {
            // 保存用户信息
            int result = userMapper.insert(newUser);

            if (result > 0) {
                // 成功创建用户后，可以更新学生表中的邮箱信息
                QueryWrapper<Student> studentQuery = new QueryWrapper<>();
                studentQuery.eq("studentNumber", studentNumber);
                Student student = studentMapper.selectOne(studentQuery);

                if (student != null && StringUtils.hasText(email)) {
                    student.setStudentPhone(email); // 暂时用phone字段存储邮箱
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
}