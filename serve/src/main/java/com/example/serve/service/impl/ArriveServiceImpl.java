package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.ArriveMapper;
import com.example.serve.pojo.Arrive;
import com.example.serve.pojo.Student;
import com.example.serve.service.ArriveService;
import com.example.serve.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 抵校信息服务实现类
 */
@Slf4j
@Service
public class ArriveServiceImpl extends ServiceImpl<ArriveMapper, Arrive> implements ArriveService {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private ArriveMapper arriveMapper;
    
    @Override
    public List<Arrive> findAll() {
        // 使用自定义方法获取所有抵校信息
        return arriveMapper.findAll();
    }
    
    @Override
    public Arrive findByStudentNumber(String studentNumber) {
        // 调用自定义方法通过学号查询抵校信息（包含关联查询）
        return arriveMapper.findByStudentNumber(studentNumber);
    }

    @Override
    public Arrive getArriveByStudentNumber(String studentNumber) {
        // 委托给findByStudentNumber方法，保持行为一致
        return findByStudentNumber(studentNumber);
    }

    @Override
    public IPage<Arrive> getArrivePage(Integer page, Integer size, LambdaQueryWrapper<Arrive> queryWrapper) {
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public long getTotalStudentCount() {
        // 获取所有学生数量
        return studentService.count();
    }

    @Override
    public long getArrivedStudentCount() {
        // 获取已抵校学生数量，即当前表中的记录数
        return count();
    }

    @Override
    public long getTodayArrivalCount(LocalDate date) {
        // 获取指定日期的抵校人数
        LambdaQueryWrapper<Arrive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Arrive::getDate, date);
        return count(queryWrapper);
    }

    @Override
    public boolean updateArrive(Arrive arrive) {
        // 首先尝试使用自定义方法更新
        try {
            int rows = arriveMapper.update(arrive);
            if (rows > 0) {
                return true;
            }
        } catch (Exception e) {
            // 如果自定义方法失败，回退到MyBatis-Plus通用更新
            log.error("更新抵校信息失败，尝试使用MyBatis-Plus通用方法", e);
        }
        
        // 使用MyBatis-Plus提供的更新方法
        LambdaQueryWrapper<Arrive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Arrive::getStudentNumber, arrive.getStudentNumber());
        return update(arrive, queryWrapper);
    }
} 