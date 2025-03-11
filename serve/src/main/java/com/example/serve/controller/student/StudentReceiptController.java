package com.example.serve.controller.student;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.serve.pojo.*;
import com.example.serve.service.*;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 学生缴费单控制器
 * 处理缴费单生成和下载功能
 */
@RestController
@RequestMapping("/api/student/receipt")
public class StudentReceiptController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;
    
    @Autowired
    private MajorService majorService;
    
    @Autowired
    private StudentDormService studentDormService;
    
    @Autowired
    private DormService dormService;
    
    @Value("${spring.upload.photo-path}")
    private String uploadPath;

    /**
     * 生成缴费单并更新支付状态
     * 
     * @param paymentInfo 包含学号、缴费编号和总金额的支付信息
     * @return 包含图片路径的响应结果
     */
    @PostMapping("/generate")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Map<String, Object>> generateReceipt(@RequestBody Map<String, Object> paymentInfo) {
        try {
            // 验证输入参数
            if (!paymentInfo.containsKey("studentNumber") || !paymentInfo.containsKey("allmoney")) {
                return ResponseResult.errorResult(400, "缺少必要参数");
            }
            
            String studentNumber = paymentInfo.get("studentNumber").toString();
            Integer allmoney;
            
            try {
                allmoney = Integer.parseInt(paymentInfo.get("allmoney").toString());
            } catch (NumberFormatException e) {
                return ResponseResult.errorResult(400, "金额格式不正确");
            }

            // 查询缴费记录
            PayItem payItem = paymentService.getPayItemByStudentNumber(studentNumber);
            
            // 如果缴费记录不存在，创建一个新的缴费记录
            if (payItem == null) {
                System.out.println("未找到学生" + studentNumber + "的缴费记录，创建新记录");
                payItem = new PayItem();
                payItem.setStudentNumber(studentNumber);
                
                // 查询学生名称
                Student student = studentService.getOne(
                    new LambdaQueryWrapper<Student>()
                        .eq(Student::getStudentNumber, studentNumber)
                );
                if (student != null) {
                    payItem.setStudentName(student.getStudentName());
                } else {
                    payItem.setStudentName("未知学生");
                }
                
                // 生成缴费编号
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String lastFourDigits = studentNumber.length() > 4 ? 
                    studentNumber.substring(studentNumber.length() - 4) : studentNumber;
                String amountCard = "PAY" + timestamp + lastFourDigits;
                payItem.setAmountcard(amountCard);
                
                payItem.setAllmoney(allmoney);
                payItem.setIndentStatue("未缴费");
                payItem.setHallway(paymentInfo.getOrDefault("hallway", "直行通道").toString());
                
                // 保存新的缴费记录
                boolean saved = paymentService.addPaymentItem(payItem);
                if (!saved) {
                    return ResponseResult.errorResult(500, "创建缴费记录失败");
                }
                System.out.println("成功创建新的缴费记录，缴费编号：" + amountCard);
            } else {
                // 如果缴费记录存在但缴费编号为空，生成一个新的缴费编号
                if (payItem.getAmountcard() == null || payItem.getAmountcard().isEmpty()) {
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String lastFourDigits = studentNumber.length() > 4 ? 
                        studentNumber.substring(studentNumber.length() - 4) : studentNumber;
                    String amountCard = "PAY" + timestamp + lastFourDigits;
                    payItem.setAmountcard(amountCard);
                    System.out.println("为已存在的缴费记录生成新的缴费编号：" + amountCard);
                }
                
                // 如果缴费记录存在但总金额为空，使用传入的金额
                if (payItem.getAllmoney() == null) {
                    payItem.setAllmoney(allmoney);
                    System.out.println("为已存在的缴费记录更新总金额：" + allmoney);
                }
                
                // 更新缴费记录
                boolean updated = paymentService.updatePaymentItem(payItem);
                if (!updated) {
                    System.out.println("更新缴费记录失败");
                }
            }

            // 如果已缴费但需要重新下载缴费单
            if ("已缴费".equals(payItem.getIndentStatue()) && payItem.getImg() != null && !payItem.getImg().isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("img", payItem.getImg());
                return ResponseResult.okResult(result);
            }

            // 最后确认检查：确保缴费编号和总金额不为空
            if (payItem.getAmountcard() == null || payItem.getAmountcard().isEmpty()) {
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String lastFourDigits = studentNumber.length() > 4 ? 
                    studentNumber.substring(studentNumber.length() - 4) : studentNumber;
                String amountCard = "PAY" + timestamp + lastFourDigits;
                payItem.setAmountcard(amountCard);
                System.out.println("最后检查：生成缴费编号：" + amountCard);
                paymentService.updatePaymentItem(payItem);
            }
            
            if (payItem.getAllmoney() == null) {
                payItem.setAllmoney(allmoney);
                System.out.println("最后检查：设置总金额：" + allmoney);
                paymentService.updatePaymentItem(payItem);
            }
            
            // 打印调试信息
            System.out.println("生成缴费单前的payItem信息：");
            System.out.println("- 学号: " + payItem.getStudentNumber());
            System.out.println("- 缴费编号: " + payItem.getAmountcard());
            System.out.println("- 总金额: " + payItem.getAllmoney());
            
            // 生成缴费单图片
            String imgPath = generateReceiptImage(payItem);
            if (imgPath == null) {
                return ResponseResult.errorResult(500, "生成缴费单失败");
            }

            // 更新缴费状态和图片路径
            boolean updateSuccess = updatePaymentStatus(payItem, imgPath, studentNumber);
            if (!updateSuccess) {
                return ResponseResult.errorResult(500, "更新缴费状态失败");
            }

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("img", imgPath);
            return ResponseResult.okResult(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "生成缴费单失败: " + e.getMessage());
        }
    }

    /**
     * 更新支付状态
     * 
     * @param payItem 支付项目
     * @param imgPath 图片路径
     * @param studentNumber 学生学号
     * @return 是否更新成功
     */
    private boolean updatePaymentStatus(PayItem payItem, String imgPath, String studentNumber) {
        try {
            // 调试信息：打印payItem对象
            System.out.println("正在更新支付状态，PayItem信息:");
            System.out.println("学号: " + payItem.getStudentNumber());
            System.out.println("缴费编号: " + payItem.getAmountcard());
            System.out.println("缴费金额: " + payItem.getAllmoney());
            System.out.println("图片路径: " + imgPath);
            
            // 更新缴费状态和图片路径
            payItem.setIndentStatue("已缴费");
            payItem.setImg(imgPath);
            boolean payUpdated = paymentService.updatePaymentItem(payItem);
            System.out.println("更新缴费状态结果: " + payUpdated);
            
            if (!payUpdated) {
                System.out.println("更新缴费状态失败");
                return false;
            }

            // 更新学生的缴费状态
            Student student = studentService.getOne(
                new LambdaQueryWrapper<Student>()
                    .eq(Student::getStudentNumber, studentNumber)
            );

            if (student != null) {
                student.setPaymentStatus(1);
                boolean studentUpdated = studentService.updateById(student);
                if (!studentUpdated) {
                    // 记录错误但不影响整体流程
                    System.err.println("更新学生缴费状态失败: " + studentNumber);
                } else {
                    System.out.println("成功更新学生的缴费状态: " + studentNumber);
                }
            } else {
                System.out.println("警告: 未找到学生记录: " + studentNumber);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("更新支付状态时发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载缴费单
     * 
     * @param filePath 文件路径
     * @return 文件资源
     */
    @GetMapping("/download")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Resource> downloadReceipt(@RequestParam String filePath) {
        try {
            // 验证文件路径
            if (filePath == null || filePath.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // 创建文件资源
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(filePath);
            String filename = file.getName();
            
            // 设置响应头
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 生成缴费单图片
     * 
     * @param payItem 缴费信息
     * @return 图片路径或null如果生成失败
     */
    private String generateReceiptImage(PayItem payItem) {
        try {
            // 获取学生信息
            Student student = studentService.getOne(
                new LambdaQueryWrapper<Student>()
                    .eq(Student::getStudentNumber, payItem.getStudentNumber())
            );
            
            if (student == null) {
                return null;
            }
            
            // 获取学费信息
            Integer tuitionFee = getTuitionFee(student);
            
            // 获取宿舍费信息
            Integer accommodationFee = getAccommodationFee(payItem.getStudentNumber());
            
            // 创建并配置图片
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            
            // 设置背景和字体
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 800, 600);
            g2d.setColor(Color.BLACK);
            
            // 绘制标题
            g2d.setFont(new Font("宋体", Font.BOLD, 20));
            g2d.drawString("广州软件学院综合收费缴费单", 250, 50);
            
            // 绘制内容
            g2d.setFont(new Font("宋体", Font.PLAIN, 16));
            g2d.drawString("缴纳项目: 广州软件学院综合收费", 100, 100);
            g2d.drawString("缴纳状态: 已缴费", 100, 130);
            g2d.drawString("缴费通道: " + payItem.getHallway(), 100, 160);
            g2d.drawString("收费机构: 广州软件学院", 100, 190);
            g2d.drawString("联系方式: 87818009", 100, 220);
            g2d.drawString("支付方式: 微信支付", 100, 250);
            g2d.drawString("缴费编号: " + payItem.getAmountcard(), 100, 280);
            g2d.drawString("用户名称: " + payItem.getStudentName(), 100, 310);
            g2d.drawString("学费: " + tuitionFee, 100, 340);
            g2d.drawString("宿舍费: " + accommodationFee, 100, 370);
            g2d.drawString("医保: 300", 100, 400); // 医保费用固定为300
            g2d.drawString("缴费总金额: " + payItem.getAllmoney(), 100, 430);
            g2d.drawString("缴费日期: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 100, 460);
            
            // 释放资源
            g2d.dispose();
            
            // 保存图片
            return saveReceiptImage(image, payItem.getStudentNumber());
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 保存缴费单图片
     * 
     * @param image 图片对象
     * @param studentNumber 学生学号
     * @return 图片保存路径
     */
    private String saveReceiptImage(BufferedImage image, String studentNumber) throws IOException {
        // 确保目录存在
        File dir = new File(uploadPath + "/receipts");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 保存图片
        String fileName = "receipt_" + studentNumber + "_" + System.currentTimeMillis() + ".jpg";
        String filePath = uploadPath + "/receipts/" + fileName;
        ImageIO.write(image, "jpg", new File(filePath));
        
        return filePath;
    }
    
    /**
     * 获取学费信息
     * 
     * @param student 学生信息
     * @return 学费金额，如果获取失败则返回0
     */
    private Integer getTuitionFee(Student student) {
        try {
            // 获取班级ID
            Integer classroomId = student.getClassroomId();
            if (classroomId == null) {
                return 0;
            }
            
            // 获取班级信息，使用LambdaQueryWrapper而不是getById
            // 注意：在Classroom实体中，majorid实际上是主键，而不是专业ID
            // 这里可能有命名混淆，我们使用majorid作为主键查询
            Classroom classroom = classroomService.getOne(
                new LambdaQueryWrapper<Classroom>()
                    .eq(Classroom::getMajorid, classroomId)
            );
            
            if (classroom == null) {
                return 0;
            }
            
            // 在这个数据模型中，班级的majorid字段既是主键又是专业ID
            Integer majorId = classroom.getMajorid();
            if (majorId == null) {
                return 0;
            }
            
            // 使用Lambda条件构造器查询专业，而不是直接使用getById
            Major major = majorService.getOne(
                new LambdaQueryWrapper<Major>()
                    .eq(Major::getMajorid, majorId)
            );
            
            if (major == null) {
                return 0;
            }
            
            return major.getDepartmentPrize();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 获取宿舍费用
     * 
     * @param studentNumber 学生学号
     * @return 宿舍费用，如果获取失败则返回0
     */
    private Integer getAccommodationFee(String studentNumber) {
        try {
            // 获取学生宿舍信息
            StudentDorm studentDorm = studentDormService.getOne(
                new LambdaQueryWrapper<StudentDorm>()
                    .eq(StudentDorm::getStudentNumber, studentNumber)
            );
            
            if (studentDorm == null) {
                System.out.println("未找到学生宿舍信息：" + studentNumber);
                return 0;
            }
            
            // 获取宿舍信息 - 使用last("limit 1")限制只返回一条记录
            Dorm dorm = dormService.getOne(
                new LambdaQueryWrapper<Dorm>()
                    .eq(Dorm::getDormType, studentDorm.getDormType())
                    .last("limit 1")
            );
            
            if (dorm == null) {
                System.out.println("未找到宿舍类型信息：" + studentDorm.getDormType());
                return 0;
            }
            
            return dorm.getDormPrize();
        } catch (Exception e) {
            System.out.println("获取宿舍费用时发生异常：" + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取学生综合缴费信息
     * 从多个表联合查询数据，包括:
     * 1. pay表 - 缴费编号(amountcard)和支付状态(indentStatue)
     * 2. major表 - 学费(departmentPrize)
     * 3. dorm表 - 宿舍费(dormPrize)
     * 
     * @param studentNumber 学生学号
     * @return 综合缴费信息
     */
    @GetMapping("/payment-info")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult<Map<String, Object>> getPaymentInfo(@RequestParam String studentNumber) {
        try {
            // 验证学号参数
            if (studentNumber == null || studentNumber.isEmpty()) {
                return ResponseResult.errorResult(400, "学号不能为空");
            }
            
            // 查询学生信息
            Student student = studentService.getOne(
                new LambdaQueryWrapper<Student>()
                    .eq(Student::getStudentNumber, studentNumber)
            );
            
            if (student == null) {
                return ResponseResult.errorResult(404, "学生信息不存在");
            }
            
            // 获取缴费记录
            PayItem payItem = paymentService.getPayItemByStudentNumber(studentNumber);
            
            // 如果缴费记录不存在，创建一个新的缴费记录
            if (payItem == null) {
                System.out.println("未找到学生" + studentNumber + "的缴费记录，创建新记录");
                payItem = new PayItem();
                payItem.setStudentNumber(studentNumber);
                payItem.setStudentName(student.getStudentName());
                
                // 生成缴费编号
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String lastFourDigits = studentNumber.length() > 4 ? 
                    studentNumber.substring(studentNumber.length() - 4) : studentNumber;
                String amountCard = "PAY" + timestamp + lastFourDigits;
                payItem.setAmountcard(amountCard);
                
                // 计算各项费用
                Integer tuitionFee = getTuitionFee(student);
                Integer accommodationFee = getAccommodationFee(studentNumber);
                Integer insuranceFee = 300; // 医保费固定为300
                
                // 计算总金额
                Integer totalAmount = tuitionFee + accommodationFee + insuranceFee;
                payItem.setAllmoney(totalAmount);
                
                // 设置初始状态为未缴费
                payItem.setIndentStatue("未缴费");
                payItem.setHallway("直行通道");
                
                // 保存新的缴费记录
                boolean saved = paymentService.addPaymentItem(payItem);
                if (!saved) {
                    System.out.println("创建缴费记录失败，但继续返回计算的费用信息");
                } else {
                    System.out.println("成功创建新的缴费记录，缴费编号：" + amountCard);
                }
            }
            
            // 获取最新的学费和宿舍费信息（无论是否为新创建的记录）
            Integer tuitionFee = getTuitionFee(student);
            Integer accommodationFee = getAccommodationFee(studentNumber);
            Integer insuranceFee = 300; // 医保费固定为300
            
            // 组装返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("studentNumber", studentNumber);
            result.put("studentName", student.getStudentName());
            result.put("amountcard", payItem.getAmountcard());
            result.put("indentStatue", payItem.getIndentStatue());
            result.put("hallway", payItem.getHallway() != null ? payItem.getHallway() : "直行通道");
            result.put("tuitionFee", tuitionFee);
            result.put("accommodationFee", accommodationFee);
            result.put("insuranceFee", insuranceFee);
            
            // 确保总金额不为空，如果为空则计算
            if (payItem.getAllmoney() == null) {
                Integer totalAmount = tuitionFee + accommodationFee + insuranceFee;
                result.put("allmoney", totalAmount);
            } else {
                result.put("allmoney", payItem.getAllmoney());
            }
            
            result.put("img", payItem.getImg());
            
            return ResponseResult.okResult(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "获取缴费信息失败: " + e.getMessage());
        }
    }
} 