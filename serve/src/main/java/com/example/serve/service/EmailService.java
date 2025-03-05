package com.example.serve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;
    
    // 使用内存Map存储验证码，key为学生学号，value为验证码及过期时间
    private final Map<String, VerificationCodeInfo> verificationCodes = new ConcurrentHashMap<>();
    
    // 定时任务执行器，用于清理过期的验证码
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // 验证码有效期（毫秒）
    private static final long CODE_EXPIRATION_MS = 5 * 60 * 1000; // 5分钟
    
    /**
     * 构造函数，启动定时清理任务
     */
    public EmailService() {
        // 每分钟检查一次过期的验证码
        scheduler.scheduleAtFixedRate(this::cleanExpiredCodes, 1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * 清理过期的验证码
     */
    private void cleanExpiredCodes() {
        long now = System.currentTimeMillis();
        verificationCodes.entrySet().removeIf(entry -> entry.getValue().expirationTime < now);
    }

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
     * @param to 收件人邮箱
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
        
        String content = 
            "<div style='padding:20px;border:1px solid #ddd;border-radius:5px;background-color:#f8f8f8;'>" +
            "<h2 style='color:#244cb2;'>广州软件学院账号激活</h2>" +
            "<p>您好，感谢您激活广州软件学院账号，您的验证码为：</p>" +
            "<div style='font-size:24px;font-weight:bold;color:#244cb2;padding:10px;background-color:#e8e8e8;border-radius:5px;letter-spacing:5px;text-align:center;'>" +
            verificationCode +
            "</div>" +
            "<p style='margin-top:20px;font-size:12px;color:#999;'>该验证码5分钟内有效，请勿泄露给他人</p>" +
            "<p style='font-size:12px;color:#999;'>如非本人操作，请忽略此邮件</p>" +
            "</div>";
        
        helper.setText(content, true);
        
        // 发送邮件
        mailSender.send(message);
        
        // 将验证码存储到内存Map中，设置过期时间
        long expirationTime = System.currentTimeMillis() + CODE_EXPIRATION_MS;
        verificationCodes.put(studentNumber, new VerificationCodeInfo(verificationCode, expirationTime));
        
        return verificationCode;
    }
    
    /**
     * 验证验证码是否正确
     *
     * @param studentNumber 学生学号
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    public boolean verifyCode(String studentNumber, String code) {
        if (studentNumber == null || code == null) {
            return false;
        }
        
        // 从内存Map中获取验证码信息
        VerificationCodeInfo codeInfo = verificationCodes.get(studentNumber);
        
        // 验证码不存在或已过期
        if (codeInfo == null || codeInfo.expirationTime < System.currentTimeMillis()) {
            verificationCodes.remove(studentNumber); // 清理过期验证码
            return false;
        }
        
        // 验证码匹配
        if (codeInfo.code.equals(code)) {
            // 验证成功后删除验证码
            verificationCodes.remove(studentNumber);
            return true;
        }
        
        return false;
    }
    
    /**
     * 内部类，用于存储验证码信息
     */
    private static class VerificationCodeInfo {
        final String code;
        final long expirationTime;
        
        VerificationCodeInfo(String code, long expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }
    }
} 