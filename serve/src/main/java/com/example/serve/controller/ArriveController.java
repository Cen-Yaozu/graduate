package com.example.serve.controller;

import com.example.serve.pojo.Arrive;
import com.example.serve.service.ArriveService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 到站信息控制器
 */
@RestController
@RequestMapping("/arrive")
public class ArriveController {

    @Autowired
    private ArriveService arriveService;

    /**
     * 查询学生到站信息
     * 用于前端检查是否已经提交过信息
     */
    @GetMapping("/info")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult getArriveInfo(@RequestParam String studentNumber) {
        try {
            if (studentNumber == null || studentNumber.isEmpty()) {
                return ResponseResult.errorResult(400, "学号不能为空");
            }
            
            Arrive arrive = arriveService.findByStudentNumber(studentNumber);
            
            if (arrive != null) {
                // 判断是否已完成信息填写
                boolean isCompleted = arrive.getTime() != null && !arrive.getTime().isEmpty() &&
                        arrive.getDate() != null &&
                        arrive.getTool() != null && !arrive.getTool().isEmpty() &&
                        arrive.getFamilyNum() != null;
                
                if (isCompleted) {
                    // 已完成信息采集，返回信息和状态
                    return ResponseResult.okResult(arrive);
                }
            }
            
            // 未采集完整信息或没有记录
            return ResponseResult.errorResult(404, "未找到完整的到站信息记录");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "查询到站信息失败: " + e.getMessage());
        }
    }

    /**
     * 更新学生到站信息
     * 对接前端ArriveView.vue页面
     */
    @PostMapping("/update")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseResult updateArrive(@RequestBody Arrive arrive) {
        try {
            // 检查学号是否存在于请求体中
            String studentNumber = arrive.getStudentNumber();
            
            // 这里我们假设前端会在登录后的每个请求中都传递studentNumber
            // 如果需要，您可以在前端的ArriveView.vue中添加获取和设置学号的逻辑
            
            // 检查必要参数
            if (arrive.getTime() == null || arrive.getTime().isEmpty()) {
                return ResponseResult.errorResult(400, "到站时间段不能为空");
            }
            
            if (arrive.getDate() == null) {
                return ResponseResult.errorResult(400, "到站日期不能为空");
            }
            
            if (arrive.getTool() == null || arrive.getTool().isEmpty()) {
                return ResponseResult.errorResult(400, "交通方式不能为空");
            }
            
            if (arrive.getFamilyNum() == null) {
                return ResponseResult.errorResult(400, "随行人数不能为空");
            }
            
            // 先检查是否已经提交过完整的到站信息
            Arrive existingArrive = arriveService.findByStudentNumber(studentNumber);
            if (existingArrive != null) {
                boolean isCompleted = existingArrive.getTime() != null && !existingArrive.getTime().isEmpty() &&
                        existingArrive.getDate() != null &&
                        existingArrive.getTool() != null && !existingArrive.getTool().isEmpty() &&
                        existingArrive.getFamilyNum() != null;
                
                if (isCompleted) {
                    return ResponseResult.errorResult(403, "您已经提交过到站信息，不允许修改");
                }
            }
            
            // 处理日期格式
            if (arrive.getDate() != null && arrive.getDate().toString().contains("T")) {
                // 如果日期是ISO格式（带T的），需要转换
                String dateStr = arrive.getDate().toString();
                LocalDate parsedDate = LocalDate.parse(dateStr.split("T")[0], DateTimeFormatter.ISO_DATE);
                arrive.setDate(parsedDate);
            }
            
            // 调用服务更新到站信息
            boolean result = arriveService.updateArrive(arrive);
            
            if (result) {
                return ResponseResult.okResult("到站信息提交成功");
            } else {
                return ResponseResult.errorResult(500, "更新到站信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(500, "服务器错误: " + e.getMessage());
        }
    }
} 