package com.example.serve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.Classroom;
import java.util.Map;

import java.util.List;

public interface DormService extends IService<Dorm> {

    /**
     * 分页查询宿舍列表
     */
    IPage<Dorm> getDormPage(int page, int size, String keyword);

    /**
     * 获取所有宿舍
     */
    List<Dorm> getAllDorms();

    /**
     * 添加宿舍
     */
    boolean addDorm(Dorm dorm);

    /**
     * 更新宿舍信息
     */
    boolean updateDorm(Dorm dorm);

    /**
     * 删除宿舍
     */
    boolean deleteDorm(String dormitory, String dormCard);

    /**
     * 获取所有学生
     */
    IPage<Student> getAllStudents(int page, int size, String department, String majorname, Integer classroomId);

    /**
     * 获取所有学生（带关键词搜索）
     */
    IPage<Student> getAllStudents(int page, int size, String department, String majorname, Integer classroomId, String keyword);

    /**
     * 获取未分配宿舍的学生
     */
    IPage<Student> getUnassignedStudents(int page, int size, String department, String majorname, Integer classroomId, String selectDorm);

    /**
     * 获取未分配宿舍的学生（带关键词搜索）
     */
    IPage<Student> getUnassignedStudents(int page, int size, String department, String majorname, Integer classroomId, String selectDorm, String keyword);

    /**
     * 获取已分配宿舍的学生
     */
    IPage<Student> getAssignedStudents(int page, int size, String department, String majorname, Integer classroomId, String dormitory, String dormCard);

    /**
     * 获取已分配宿舍的学生（带关键词搜索）
     */
    IPage<Student> getAssignedStudents(int page, int size, String department, String majorname, Integer classroomId, String dormitory, String dormCard, String keyword);

    /**
     * 分配学生到宿舍
     */
    boolean assignStudentsToDorm(List<String> studentNumbers, String dormType, String dormitory, String dormCard);

    /**
     * 获取所有宿舍楼
     */
    List<String> getAllDormBuildings();

    /**
     * 获取可用宿舍
     */
    List<Dorm> getAvailableDorms(String dormType, String dormsex, String dormitory);

    /**
     * 获取筛选选项的系别列表
     */
    List<String> getAllDepartments();

    /**
     * 获取筛选选项的专业列表
     */
    List<String> getAllMajors();

    /**
     * 获取筛选选项的班级列表
     */
    List<Classroom> getAllClassrooms();

    /**
     * 从宿舍移除学生
     */
    boolean removeStudentFromDorm(String studentNumber);

    /**
     * 更新学生的宿舍类型选择
     */
    boolean updateStudentSelectDorm(String studentNumber, String selectDorm);

    /**
     * 批量更新学生的宿舍类型选择
     */
    boolean batchUpdateStudentSelectDorm(List<String> studentNumbers, String selectDorm);

    /**
     * 批量分配学生到宿舍
     */
    boolean batchAssignStudentsToDorm(List<Map<String, Object>> assignments);
} 