package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.MajorMapper;
import com.example.serve.pojo.Major;
import com.example.serve.service.MajorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 专业服务实现类
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {

    @Override
    public List<String> getAllDepartments() {
        // 使用QueryWrapper查询所有不重复的department字段
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT department");
        
        // 从查询结果中提取department字段并收集到List
        return this.list(queryWrapper)
                .stream()
                .map(Major::getDepartment)
                .collect(Collectors.toList());
    }

    @Override
    public Major getByMajorid(String majorid) {
        // 将字符串转换为Integer
        Integer id;
        try {
            id = Integer.parseInt(majorid);
        } catch (NumberFormatException e) {
            return null;
        }
        
        // 查询
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("majorid", id);
        return this.getOne(queryWrapper);
    }
} 