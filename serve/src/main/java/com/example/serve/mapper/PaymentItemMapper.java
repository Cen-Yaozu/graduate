package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.payment.PaymentItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 缴费项目Mapper接口
 *
 * @author author
 * @since 2025-02-27
 */
@Mapper
public interface PaymentItemMapper extends BaseMapper<PaymentItem> {

    /**
     * 获取学生未支付的缴费项目
     *
     * @param studentNumber 学号
     * @return 未支付的缴费项目列表
     */
    @Select("SELECT pi.* FROM payment_item pi " +
            "LEFT JOIN student s ON s.grade = pi.grade AND (pi.major IS NULL OR pi.major = s.majorname) " +
            "LEFT JOIN payment_record pr ON pr.payment_item_id = pi.id AND pr.student_number = #{studentNumber} " +
            "WHERE s.studentNumber = #{studentNumber} AND pr.id IS NULL")
    List<Map<String, Object>> getUnpaidItems(@Param("studentNumber") String studentNumber);
}