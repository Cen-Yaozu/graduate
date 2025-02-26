package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.payment.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 缴费记录Mapper接口
 *
 * @author author
 * @since 2025-02-27
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 获取学生缴费历史记录
     *
     * @param studentNumber 学号
     * @return 缴费历史记录列表
     */
    @Select("SELECT pr.id, pi.name, pr.amount, pr.payment_date as paymentDate, " +
            "CASE pr.status WHEN 1 THEN '已支付' ELSE '未支付' END as status, " +
            "pr.payment_method as paymentMethod " +
            "FROM payment_record pr " +
            "JOIN payment_item pi ON pr.payment_item_id = pi.id " +
            "WHERE pr.student_number = #{studentNumber} " +
            "ORDER BY pr.payment_date DESC")
    List<Map<String, Object>> getPaymentHistory(@Param("studentNumber") String studentNumber);
}