package com.example.serve.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.pojo.Arrive;
import com.example.serve.service.ArriveService;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员-抵校管理控制器
 *
 * @author author
 * @since 2025-03-01
 */
@RestController
@RequestMapping("/api/admin/arrive")
@PreAuthorize("hasRole('ADMIN')")
public class AdminArriveController {

    @Autowired
    private ArriveService arriveService;

    /**
     * 分页获取抵校学生列表
     *
     * @param page    当前页码
     * @param size    每页大小
     * @param keyword 搜索关键词（学号、姓名）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 抵校信息分页数据
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getArriveList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        // 构建查询条件
        LambdaQueryWrapper<Arrive> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索（学号或姓名）
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Arrive::getStudentNumber, keyword)
                    .or()
                    .like(Arrive::getStudentName, keyword);
        }
        
        // 日期范围筛选
        if (StringUtils.hasText(startDate)) {
            LocalDate start = LocalDate.parse(startDate);
            queryWrapper.ge(Arrive::getDate, start);
        }
        
        if (StringUtils.hasText(endDate)) {
            LocalDate end = LocalDate.parse(endDate);
            queryWrapper.le(Arrive::getDate, end);
        }
        
        // 默认按抵校日期降序排序
        queryWrapper.orderByDesc(Arrive::getDate);

        // 分页查询
        IPage<Arrive> pageData = arriveService.getArrivePage(page, size, queryWrapper);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotal());
        result.put("list", pageData.getRecords());

        return ResponseResult.okResult(result);
    }

    /**
     * 获取抵校详情
     *
     * @param studentNumber 学号
     * @return 抵校信息详情
     */
    @GetMapping("/{studentNumber}")
    public ResponseResult<Arrive> getArriveDetail(@PathVariable String studentNumber) {
        Arrive arrive = arriveService.getArriveByStudentNumber(studentNumber);
        
        if (arrive != null) {
            return ResponseResult.okResult(arrive);
        }
        return ResponseResult.errorResult(404, "抵校信息不存在");
    }

    /**
     * 获取抵校统计信息
     *
     * @return 抵校统计数据
     */
    @GetMapping("/statistics")
    public ResponseResult<Map<String, Object>> getArriveStatistics() {
        // 获取总学生数
        long totalStudents = arriveService.getTotalStudentCount();
        
        // 获取已抵校学生数
        long arrivedStudents = arriveService.getArrivedStudentCount();
        
        // 计算抵校率
        double arrivalRate = totalStudents > 0 ? (double) arrivedStudents / totalStudents * 100 : 0;
        
        // 获取今日抵校人数
        LocalDate today = LocalDate.now();
        long todayArrivals = arriveService.getTodayArrivalCount(today);
        
        // 构建统计结果
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalStudents", totalStudents);
        statistics.put("arrivedStudents", arrivedStudents);
        statistics.put("arrivalRate", Math.round(arrivalRate * 100) / 100.0); // 保留两位小数
        statistics.put("todayArrivals", todayArrivals);
        
        return ResponseResult.okResult(statistics);
    }
} 