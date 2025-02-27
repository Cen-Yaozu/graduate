package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.mapper.DormMapper;
import com.example.serve.mapper.StudentDormMapper;
import com.example.serve.pojo.Dorm;
import com.example.serve.pojo.Student;
import com.example.serve.pojo.StudentDorm;
import com.example.serve.service.DormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import com.example.serve.mapper.StudentMapper;
import com.example.serve.mapper.ClassroomMapper;
import com.example.serve.pojo.Classroom;

@Service
public class DormServiceImpl extends ServiceImpl<DormMapper, Dorm> implements DormService {

    @Autowired
    private DormMapper dormMapper;

    @Autowired
    private StudentDormMapper studentDormMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClassroomMapper classroomMapper;
    

    @Override
    public IPage<Dorm> getDormPage(int page, int size, String keyword) {
        Page<Dorm> pageParam = new Page<>(page, size);
        IPage<Dorm> dormPage = dormMapper.selectDormPage(pageParam, keyword);
        
        // 获取每个宿舍的成员
        dormPage.getRecords().forEach(dorm -> {
            List<Student> members = dormMapper.selectDormMembers(dorm.getDormCard(), dorm.getDormitory());
            dorm.setMembers(members);
        });
        
        return dormPage;
    }

    @Override
    public List<Dorm> getAllDorms() {
        return dormMapper.selectAllDorms();
    }

    @Override
    public boolean addDorm(Dorm dorm) {
        return save(dorm);
    }

    @Override
    public boolean updateDorm(Dorm dorm) {
        // 使用dormitory和dormCard作为条件更新
        QueryWrapper<Dorm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dormitory", dorm.getDormitory())
                   .eq("dormCard", dorm.getDormCard());
        return update(dorm, queryWrapper);
    }

    @Override
    public boolean deleteDorm(String dormitory, String dormCard) {
        // 检查宿舍是否有学生
        QueryWrapper<Dorm> dormQuery = new QueryWrapper<>();
        dormQuery.eq("dormitory", dormitory)
                .eq("dormCard", dormCard);
        Dorm dorm = getOne(dormQuery);
        
        if (dorm == null) {
            return false;
        }
        
        // 检查是否有学生住在这个宿舍
        List<Student> members = dormMapper.selectDormMembers(dormCard, dormitory);
        if (members != null && !members.isEmpty()) {
            return false; // 宿舍有学生，不能删除
        }
        
        return remove(dormQuery);
    }

    @Override
    public IPage<Student> getAllStudents(int page, int size, String department, String majorname, Integer classroomId) {
        Page<Student> pageParam = new Page<>(page, size);
        return studentDormMapper.selectAllStudents(pageParam, department, majorname, classroomId);
    }

    @Override
    public IPage<Student> getUnassignedStudents(int page, int size, String department, String majorname, Integer classroomId) {
        Page<Student> pageParam = new Page<>(page, size);
        return studentDormMapper.selectUnassignedStudents(pageParam, department, majorname, classroomId);
    }

    @Override
    public IPage<Student> getAssignedStudents(int page, int size, String department, String majorname, Integer classroomId) {
        Page<Student> pageParam = new Page<>(page, size);
        return studentDormMapper.selectAssignedStudents(pageParam, department, majorname, classroomId);
    }

    @Override
    @Transactional
    public boolean assignStudentsToDorm(List<String> studentNumbers, String dormType, String dormitory, String dormCard) {
        if (studentNumbers == null || studentNumbers.isEmpty() || dormitory == null || dormCard == null) {
            return false;
        }
        
        // 检查宿舍是否存在
        QueryWrapper<Dorm> dormQuery = new QueryWrapper<>();
        dormQuery.eq("dormitory", dormitory)
                .eq("dormCard", dormCard);
        Dorm dorm = getOne(dormQuery);
        
        if (dorm == null) {
            return false;
        }
        
        // 检查宿舍已住人数
        List<Student> currentMembers = dormMapper.selectDormMembers(dormCard, dormitory);
        int currentOccupied = currentMembers != null ? currentMembers.size() : 0;
        
        // 检查宿舍容量
        if (dorm.getDormNum() != null && currentOccupied + studentNumbers.size() > dorm.getDormNum()) {
            return false; // 超出宿舍容量
        }
        
        // 删除学生现有宿舍分配（如果有）
        for (String studentNumber : studentNumbers) {
            studentDormMapper.deleteByStudentNumber(studentNumber);
        }
        
        // 批量分配学生到宿舍
        int count = studentDormMapper.batchInsert(studentNumbers, dormType, dormitory, dormCard);
        
        return count > 0;
    }
    @Override
    public List<String> getAllDormBuildings() {
        // 从宿舍表中获取所有不同的宿舍楼
        QueryWrapper<Dorm> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT dormitory");
        List<Dorm> dorms = dormMapper.selectList(wrapper);
        
        return dorms.stream()
                .map(Dorm::getDormitory)
                .collect(Collectors.toList());
    }

    @Override
    public List<Dorm> getAvailableDorms(String dormType, String dormsex, String dormitory) {
        QueryWrapper<Dorm> wrapper = new QueryWrapper<>();
        
        // 添加过滤条件
        if (dormType != null && !dormType.isEmpty()) {
            wrapper.eq("dormType", dormType);
        }
        
        if (dormsex != null && !dormsex.isEmpty()) {
            wrapper.eq("dormsex", dormsex);
        }
        
        if (dormitory != null && !dormitory.isEmpty()) {
            wrapper.eq("dormitory", dormitory);
        }
        
        List<Dorm> dorms = dormMapper.selectList(wrapper);
        
        // 获取所有宿舍的成员信息
        for (Dorm dorm : dorms) {
            List<Student> members = dormMapper.selectDormMembers(dorm.getDormCard(), dorm.getDormitory());
            dorm.setMembers(members);
        }
        
        return dorms;
    }

    @Override
    @Transactional
    public boolean batchAssignStudentsToDorm(List<Map<String, Object>> assignments) {
        try {
            for (Map<String, Object> assignment : assignments) {
                String studentNumber = (String) assignment.get("studentNumber");
                // String dormType = (String) assignment.get("dormType"); // 学生表没有这个字段，只记录在student_dorm表中
                String dormitory = (String) assignment.get("dormitory");
                String dormCard = (String) assignment.get("dormCard");
                
                // 检查宿舍是否已满
                Dorm dorm = dormMapper.selectOne(new QueryWrapper<Dorm>()
                        .eq("dormitory", dormitory)
                        .eq("dormCard", dormCard));
                
                List<Student> members = dormMapper.selectDormMembers(dormCard, dormitory);
                
                if (dorm == null || members.size() >= dorm.getDormNum()) {
                    // 宿舍已满，回滚事务
                    throw new RuntimeException("宿舍已满: " + dormitory + " " + dormCard);
                }
                
                // 更新学生宿舍信息
                Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                        .eq("studentNumber", studentNumber));
                
                if (student != null) {
                    // 只设置学生表中实际存在的字段
                    student.setDormitory(dormitory);
                    student.setDormNumber(dormCard);
                    
                    studentMapper.updateById(student);
                    
                    // 如果需要记录宿舍类型，应该在student_dorm表中添加记录
                    // String dormType = dorm.getDormType(); // 从宿舍表获取宿舍类型
                    // studentDormMapper.insert(new StudentDorm(studentNumber, student.getStudentName(), student.getDepartment(), dormType, dormitory, dormCard));
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // 事务会自动回滚
            return false;
        }
    }

    @Override
    public List<String> getAllDepartments() {
        // 从学生表或系部表中获取所有不同的系别
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT department");
        List<Student> students = studentMapper.selectList(wrapper);
        
        return students.stream()
                .map(Student::getDepartment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMajors() {
        // 从学生表或专业表中获取所有不同的专业
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT majorname");
        List<Student> students = studentMapper.selectList(wrapper);
        
        return students.stream()
                .map(Student::getMajorname)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        // 从班级表中获取所有班级
        List<Classroom> classrooms = classroomMapper.selectList(null);
        
        if (classrooms == null) {
            classrooms = new ArrayList<>();
        }
        
        return classrooms;
    }

    @Override
    public boolean removeStudentFromDorm(String studentNumber) {
        try {
            // 获取学生
            Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                    .eq("studentNumber", studentNumber));
            
            if (student != null) {
                // 重置学生宿舍信息
                student.setDormitory(null);
                student.setDormNumber(null);
                
                studentMapper.updateById(student);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 