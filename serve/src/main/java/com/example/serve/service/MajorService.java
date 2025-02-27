package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Major;

import java.util.List;

/**
 * 专业服务接口
 */
public interface MajorService extends IService<Major> {
    
    /**
     * 获取所有系别信息
     * 
     * @return 系别列表
     */
    List<String> getAllDepartments();
    
    /**
     * 根据专业ID获取专业信息
     * 
     * @param majorid 专业ID
     * @return 专业信息
     */
    Major getByMajorid(String majorid);
} 