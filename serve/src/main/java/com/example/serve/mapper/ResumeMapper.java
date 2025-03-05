package com.example.serve.mapper;

import com.example.serve.pojo.Resume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-12-29
 */
@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
    
}
