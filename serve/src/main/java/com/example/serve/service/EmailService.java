package com.example.serve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.mail.username}")
    private String sender;

    // 验证码前缀
    private static final String CODE_PREFIX = "activate:code:";

    // 验证码有效期（分钟）
    private static final long CODE_EXPIRATION_MINUTES = 5;

    /**
     * 生成6位随机验证码
     */
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位数字验证码
        return String.valueOf(code);
    }

    /**
     * 发送验证码邮件
     *
     * @param to            收件人邮箱
     * @param studentNumber 学生学号（用于关联验证码）
     * @return 生成的验证码
     */
    public String sendVerificationEmail(String to, String studentNumber) throws MessagingException {
        // 生成验证码
        String verificationCode = generateVerificationCode();

        // 构建邮件内容
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject("广州软件学院账号激活验证码");

        String content = "<div style='padding:20px;border:1px solid #ddd;border-radius:5px;background-color:#f8f8f8;'>"
                +
                "<h2 style='color:#244cb2;'>广州软件学院账号激活</h2>" +
                "<p>您好，感谢您激活广州软件学院账号，您的验证码为：</p>" +
                "<div style='font-size:24px;font-weight:bold;color:#244cb2;padding:10px;background-color:#e8e8e8;border-radius:5px;letter-spacing:5px;text-align:center;'>"
                +
                verificationCode +
                "</div>" +
                "<p style='margin-top:20px;font-size:12px;color:#999;'>该验证码5分钟内有效，请勿泄露给他人</p>" +
                "<p style='font-size:12px;color:#999;'>如非本人操作，请忽略此邮件</p>" +
                "</div>";

        helper.setText(content, true);

        // 发送邮件
        mailSender.send(message);

        // 将验证码存储到Redis中，设置过期时间
        String key = CODE_PREFIX + studentNumber;
        redisTemplate.opsForValue().set(key, verificationCode, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        return verificationCode;
    }

    /**
     * 验证验证码是否正确
     *
     * @param studentNumber 学生学号
     * @param code          用户输入的验证码
     * @return 验证结果
     */
    public boolean verifyCode(String studentNumber, String code) {
        if (studentNumber == null || code == null) {
            return false;
        }

        // 从Redis中获取验证码
        String key = CODE_PREFIX + studentNumber;
        String savedCode = redisTemplate.opsForValue().get(key);

        // 验证码不存在或已过期
        if (savedCode == null) {
            return false;
        }

        // 验证码匹配
        if (savedCode.equals(code)) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
            return true;
        }

        return false;
    }
}