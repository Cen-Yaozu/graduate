package com.example.serve.service;

import com.example.serve.mapper.ArriveMapper;
import com.example.serve.pojo.Arrive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArriveService {
    @Autowired
    private ArriveMapper arriveMapper;

    public List<Arrive> findAll() {
        return arriveMapper.findAll();
    }

    public Arrive findByStudentNumber(String studentNumber) {
        return arriveMapper.findByStudentNumber(studentNumber);
    }

    public boolean updateArrive(Arrive arrive) {
        return arriveMapper.update(arrive) > 0;
    }
}