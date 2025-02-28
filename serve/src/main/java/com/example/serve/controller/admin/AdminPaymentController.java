package com.example.serve.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.PayItem;
import com.example.serve.pojo.Student;
import com.example.serve.service.PaymentService;
import com.example.serve.service.StudentService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-缴费管理控制器
 *
 * @author author
 * @since 2025-02-28
 */
@RestController
@RequestMapping("/api/admin/payment")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StudentService studentService;

    /**
     * 获取所有学生的缴费记录（根据缴费状态）
     * 如果学生未缴费，则返回基本信息
     * 如果学生已缴费，则关联查询pay表获取详细缴费记录
     */
    @GetMapping("/all-payment-records")
    public ResponseResult<Map<String, Object>> getAllPaymentRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status) {

        try {
            Map<String, Object> result = new HashMap<>();

            // 如果是"缴费记录"选项卡 (status=null 且非明确筛选已缴费或未缴费状态，也就是全部)
            if (status == null) {
                // 缴费记录只返回pay表中的记录
                LambdaQueryWrapper<PayItem> payItemQueryWrapper = new LambdaQueryWrapper<>();

                // 关键词搜索条件
                if (StringUtils.hasText(keyword)) {
                    payItemQueryWrapper.like(PayItem::getStudentName, keyword)
                            .or().like(PayItem::getStudentNumber, keyword)
                            .or().like(PayItem::getHallway, keyword)
                            .or().like(PayItem::getAmountcard, keyword);
                }

                IPage<PayItem> payItemPage = paymentService.pageItems(new Page<>(page, size), payItemQueryWrapper);

                result.put("total", payItemPage.getTotal());
                result.put("list", payItemPage.getRecords());

                return ResponseResult.okResult(result);
            }

            // 处理已缴费/未缴费筛选 - 从学生表查询
            Page<Student> studentPage = new Page<>(page, size);
            LambdaQueryWrapper<Student> studentQueryWrapper = new LambdaQueryWrapper<>();

            // 添加关键词搜索条件
            if (StringUtils.hasText(keyword)) {
                studentQueryWrapper.like(Student::getStudentName, keyword)
                        .or().like(Student::getStudentNumber, keyword);
            }

            // 添加缴费状态筛选条件
            if (status == 1) {
                // 已缴费
                studentQueryWrapper.eq(Student::getPaymentStatus, 1);
            } else {
                // 未缴费 (status=0)
                studentQueryWrapper.and(wrapper -> wrapper.eq(Student::getPaymentStatus, 0)
                        .or()
                        .isNull(Student::getPaymentStatus));
            }

            // 执行学生查询
            IPage<Student> studentIPage = studentService.page(studentPage, studentQueryWrapper);
            List<Student> students = studentIPage.getRecords();

            // 存储最终的支付记录列表
            List<Map<String, Object>> paymentRecords = new ArrayList<>();

            // 处理每个学生的缴费记录
            for (Student student : students) {
                Map<String, Object> record = new HashMap<>();

                // 添加基本学生信息
                record.put("studentNumber", student.getStudentNumber());
                record.put("studentName", student.getStudentName());

                // 根据缴费状态处理
                if (student.getPaymentStatus() != null && student.getPaymentStatus() == 1) {
                    // 已缴费：查询pay表获取详细信息
                    PayItem payItem = paymentService.getPayItemByStudentNumber(student.getStudentNumber());

                    if (payItem != null) {
                        // 如果找到缴费记录，添加详细信息
                        record.put("hallway", payItem.getHallway());
                        record.put("amountcard", payItem.getAmountcard());
                        record.put("allmoney", payItem.getAllmoney());
                        record.put("remark", payItem.getRemark());
                    } else {
                        // 未找到缴费记录但学生状态为已缴费
                        record.put("hallway", "系统记录");
                        record.put("amountcard", "");
                        record.put("allmoney", 0);
                        record.put("remark", "已缴费但无详细记录");
                    }

                    record.put("indentStatue", "已缴费");
                } else {
                    // 未缴费：只返回基本信息
                    record.put("hallway", "");
                    record.put("amountcard", "");
                    record.put("allmoney", null);
                    record.put("remark", "");
                    record.put("indentStatue", "未缴费");
                }

                paymentRecords.add(record);
            }

            // 构建返回结果
            result.put("total", studentIPage.getTotal());
            result.put("list", paymentRecords);

            return ResponseResult.okResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "获取缴费记录失败: " + e.getMessage());
        }
    }
}