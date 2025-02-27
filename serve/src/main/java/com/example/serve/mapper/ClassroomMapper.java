package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.Classroom;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 班级数据访问接口
 */
@Mapper
public interface ClassroomMapper extends BaseMapper<Classroom> {
    // 继承了BaseMapper，拥有基本的CRUD方法
} 