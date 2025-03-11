package com.example.serve.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Arrive;

import java.time.LocalDate;
import java.util.List;

/**
 * 抵校信息服务接口
 */
public interface ArriveService extends IService<Arrive> {

    /**
     * 查询所有抵校信息
     * 
     * @return 抵校信息列表
     */
    List<Arrive> findAll();
    
    /**
     * 根据学号查询抵校信息（向后兼容旧API）
     * 
     * @param studentNumber 学号
     * @return 抵校信息
     */
    Arrive findByStudentNumber(String studentNumber);
    
    /**
     * 根据学号查询抵校信息
     *
     * @param studentNumber 学号
     * @return 抵校信息
     */
    Arrive getArriveByStudentNumber(String studentNumber);

    /**
     * 分页查询抵校信息
     *
     * @param page 页码
     * @param size 每页大小
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<Arrive> getArrivePage(Integer page, Integer size, LambdaQueryWrapper<Arrive> queryWrapper);

    /**
     * 获取学生总数
     *
     * @return 学生总数
     */
    long getTotalStudentCount();

    /**
     * 获取已抵校学生数量
     *
     * @return 已抵校学生数量
     */
    long getArrivedStudentCount();

    /**
     * 获取指定日期抵校人数
     *
     * @param date 日期
     * @return 指定日期抵校人数
     */
    long getTodayArrivalCount(LocalDate date);

    /**
     * 更新抵校信息
     *
     * @param arrive 抵校信息
     * @return 是否成功
     */
    boolean updateArrive(Arrive arrive);
}