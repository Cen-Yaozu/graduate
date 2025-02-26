package com.example.serve;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.serve.mapper.FamilyMapper;
import com.example.serve.mapper.UserMapper;
import com.example.serve.pojo.Family;
import com.example.serve.pojo.User;
import com.example.serve.tools.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class ServeApplicationTests {
    @Autowired
    private FamilyMapper familyMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testselectmap(){//条件查询
        HashMap<String,Object> map=new HashMap<>();
        //自定义查询
        map.put("familyPart","123");
        List<Family> list=familyMapper.selectByMap(map);
        list.forEach(System.out::println);
    }

    @Test
    public void testPage(){//分页查询
        Page<Family> page=new Page<>(1,3);
        QueryWrapper<Family> wrapper=new QueryWrapper<>();
        wrapper.eq("studentNumber","123");
        familyMapper.selectList(page,wrapper);
        page.getRecords().forEach(System.out::println);
    }

    @Test
    public void testinsert(){//添加
        Family family=new Family();
        family.setStudentNumber("456");
        family.setFamilyAge("45");
        family.setFamilyPart("4567");
        family.setFamilyName("789");
        int r=familyMapper.insert(family);
        System.out.println(r);
        System.out.println(family);
    }

    @Test
    public void testupdate(){//更新
        Family family=new Family();
        family.setId(1);
        family.setFamilyAge("4567");
        int i=familyMapper.updateById(family);
        System.out.println(i);
    }

    @Test
    public void testdeleteById(){
        familyMapper.deleteById(7);
    }

    @Test
    public void testdelete(){//批量删除
        familyMapper.deleteBatchIds(Arrays.asList(5,6));
    }

    @Test
    public void test(){
//        String jwt= JwtUtil.createJWT("10086","ljq",null);
//        System.out.println(jwt);
        try {
            Claims claims=JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDA4NiIsInN1YiI6ImxqcSIsImlzcyI6Ind1IiwiaWF0IjoxNzM5NTUxODc5LCJleHAiOjE3Mzk2MzgyNzl9.vIBkTSFBZ3GcRxE4ttPkHz2iEjM7Z_d5t62HvTGJqHE");
            String id=claims.getId();
            String subject=claims.getSubject();
            System.out.println(id+"=="+subject);

        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Test
    public void test1() {
        List<User> users=userMapper.selectList(null);
        users.forEach(System.out::println);
    }
}
