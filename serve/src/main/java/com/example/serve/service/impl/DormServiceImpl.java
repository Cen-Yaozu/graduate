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
    public IPage<Dorm> getDormPage(int page, int size, String keyword, String dormsex) {
        Page<Dorm> pageParam = new Page<>(page, size);

        // 将英文性别参数转换为中文
        String dormSexChinese = null;
        if (dormsex != null && !dormsex.isEmpty()) {
            if ("MALE".equalsIgnoreCase(dormsex)) {
                dormSexChinese = "男";
            } else if ("FEMALE".equalsIgnoreCase(dormsex)) {
                dormSexChinese = "女";
            }
        }

        IPage<Dorm> dormPage = dormMapper.selectDormPage(pageParam, keyword, dormSexChinese);

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
        // 调用带关键词参数的方法，传入null作为关键词
        return getAllStudents(page, size, department, majorname, classroomId, null);
    }

    @Override
    public IPage<Student> getAllStudents(int page, int size, String department, String majorname, Integer classroomId,
            String keyword) {
        // 创建分页对象
        Page<Student> pageResult = new Page<>(page, size);

        // 查询学生，注意参数顺序：page, keyword, department, majorname, classroomId
        IPage<Student> studentPage = studentDormMapper.selectAllStudents(pageResult, keyword, department, majorname,
                classroomId);

        return studentPage;
    }

    @Override
    public IPage<Student> getUnassignedStudents(int page, int size, String department, String majorname,
            Integer classroomId, String selectDorm) {
        // 调用带关键词参数的方法，传入null作为关键词
        return getUnassignedStudents(page, size, department, majorname, classroomId, selectDorm, null);
    }

    @Override
    public IPage<Student> getUnassignedStudents(int page, int size, String department, String majorname,
            Integer classroomId, String selectDorm, String keyword) {
        Page<Student> pageParam = new Page<>(page, size);
        return studentDormMapper.selectUnassignedStudents(pageParam, keyword, department, majorname, classroomId,
                selectDorm);
    }

    @Override
    public IPage<Student> getAssignedStudents(int page, int size, String department, String majorname,
            Integer classroomId, String dormitory, String dormCard) {
        // 调用带关键词参数的方法，传入null作为关键词
        return getAssignedStudents(page, size, department, majorname, classroomId, dormitory, dormCard, null);
    }

    @Override
    public IPage<Student> getAssignedStudents(int page, int size, String department, String majorname,
            Integer classroomId, String dormitory, String dormCard, String keyword) {
        Page<Student> pageParam = new Page<>(page, size);
        return studentDormMapper.selectAssignedStudents(pageParam, keyword, department, majorname, classroomId,
                dormitory, dormCard);
    }

    @Override
    @Transactional
    public boolean assignStudentsToDorm(List<String> studentNumbers, String dormType, String dormitory,
            String dormCard) {
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

        try {
            // 遍历所有需要分配的学生
            for (String studentNumber : studentNumbers) {
                // 获取学生信息
                Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                        .eq("studentNumber", studentNumber));

                if (student != null) {
                    // 检查学生性别是否与宿舍性别匹配
                    if (dorm.getDormsex() != null && !dorm.getDormsex().isEmpty()
                            && student.getSex() != null && !student.getSex().isEmpty()
                            && !dorm.getDormsex().equals(student.getSex())) {
                        // 性别不匹配
                        System.err.println("学生性别与宿舍不匹配: 学号=" + studentNumber
                                + ", 学生性别=" + student.getSex()
                                + ", 宿舍性别=" + dorm.getDormsex());
                        return false;
                    }

                    // 如果学生没有selectDorm且提供了宿舍类型，则设置
                    if ((student.getSelectDorm() == null || student.getSelectDorm().isEmpty())
                            && dormType != null && !dormType.isEmpty()) {
                        student.setSelectDorm(dormType);
                        studentMapper.updateById(student);
                    }
                }

                // 删除学生现有宿舍分配（如果有）
                studentDormMapper.deleteByStudentNumber(studentNumber);
            }

            // 批量分配学生到宿舍
            int count = studentDormMapper.batchInsert(studentNumbers, dormType, dormitory, dormCard);

            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            // 事务会自动回滚
            return false;
        }
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
            // 添加日志以便调试
            System.out.println("查询可用宿舍时的宿舍类型参数: " + dormType);

            // 处理不同格式的宿舍类型
            if ("FOUR".equalsIgnoreCase(dormType) || "四人间".equals(dormType)) {
                wrapper.eq("dormType", "四人间");
            } else if ("SIX".equalsIgnoreCase(dormType) || "六人间".equals(dormType)) {
                wrapper.eq("dormType", "六人间");
            } else if ("EIGHT".equalsIgnoreCase(dormType) || "八人间".equals(dormType)) {
                wrapper.eq("dormType", "八人间");
            } else {
                // 如果是其他值，则直接使用
                wrapper.eq("dormType", dormType);
            }
        }

        if (dormsex != null && !dormsex.isEmpty()) {
            // 添加日志以便调试
            System.out.println("查询可用宿舍时的性别参数: " + dormsex);
            wrapper.eq("dormsex", dormsex);
        }

        if (dormitory != null && !dormitory.isEmpty()) {
            wrapper.eq("dormitory", dormitory);
        }

        // 打印SQL语句以便调试
        System.out.println("查询可用宿舍的SQL: " + wrapper.getCustomSqlSegment());

        List<Dorm> dorms = dormMapper.selectList(wrapper);
        System.out.println("查询到的宿舍数量: " + dorms.size());

        // 获取所有宿舍的成员信息
        for (Dorm dorm : dorms) {
            // 1. 先查询student_dorm表，获取与该宿舍关联的所有学号
            List<String> studentNumbers = dormMapper.selectStudentNumbersByDorm(dorm.getDormCard(),
                    dorm.getDormitory());

            // 2. 再通过学号列表查询完整的学生信息
            List<Student> members = new ArrayList<>();
            if (studentNumbers != null && !studentNumbers.isEmpty()) {
                members = studentMapper.selectStudentsByStudentNumbers(studentNumbers);
            }

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
                String dormType = (String) assignment.get("dormType");
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

                // 获取学生信息
                Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                        .eq("studentNumber", studentNumber));

                if (student != null) {
                    // 检查学生性别是否与宿舍性别匹配
                    if (dorm.getDormsex() != null && !dorm.getDormsex().isEmpty()
                            && student.getSex() != null && !student.getSex().isEmpty()
                            && !dorm.getDormsex().equals(student.getSex())) {
                        // 性别不匹配
                        throw new RuntimeException("学生性别与宿舍不匹配: 学号=" + studentNumber
                                + ", 学生性别=" + student.getSex()
                                + ", 宿舍性别=" + dorm.getDormsex());
                    }

                    // 如果学生没有selectDorm且提供了宿舍类型，则设置
                    if ((student.getSelectDorm() == null || student.getSelectDorm().isEmpty())
                            && dormType != null && !dormType.isEmpty()) {
                        student.setSelectDorm(dormType);
                        studentMapper.updateById(student);
                    }
                }

                // 删除学生现有宿舍分配（如果有）
                studentDormMapper.deleteByStudentNumber(studentNumber);

                // 构建并插入新记录
                StudentDorm studentDorm = new StudentDorm();
                studentDorm.setStudentNumber(studentNumber);
                studentDorm.setStudentName(student.getStudentName());
                studentDorm.setDepartment(student.getDepartment());
                studentDorm.setDormType(dormType != null ? dormType : student.getSelectDorm());
                studentDorm.setDormitory(dormitory);
                studentDorm.setDormCard(dormCard);

                studentDormMapper.insert(studentDorm);
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

            // 不再需要修改学生表中的宿舍信息，因为字段已删除
            // 只需要从student_dorm表中删除记录
            studentDormMapper.deleteByStudentNumber(studentNumber);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStudentSelectDorm(String studentNumber, String selectDorm) {
        try {
            // 获取学生信息
            Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                    .eq("studentNumber", studentNumber));

            if (student == null) {
                return false;
            }

            // 更新学生的宿舍类型选择
            student.setSelectDorm(selectDorm);
            return studentMapper.updateById(student) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean batchUpdateStudentSelectDorm(List<String> studentNumbers, String selectDorm) {
        try {
            if (studentNumbers == null || studentNumbers.isEmpty() || selectDorm == null) {
                return false;
            }

            for (String studentNumber : studentNumbers) {
                updateStudentSelectDorm(studentNumber, selectDorm);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // 事务会自动回滚
            return false;
        }
    }
}