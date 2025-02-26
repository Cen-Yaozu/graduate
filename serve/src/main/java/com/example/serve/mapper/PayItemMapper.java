package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.PayItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 缴费项目Mapper接口
 *
 * @author author
 * @since 2025-02-27
 */
@Mapper
public interface PayItemMapper extends BaseMapper<PayItem> {
    // 继承BaseMapper后默认已经有基础的CRUD方法
    // 如需要自定义方法可以在这里添加
}