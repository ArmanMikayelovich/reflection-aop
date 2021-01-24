package com.mikayelovich.reflecthack.service;

import com.mikayelovich.reflecthack.criteria.test.FirstCriteria;
import org.springframework.stereotype.Service;

@Service
public class UrishService {

    public void lrivUrishBan(FirstCriteria criteria) {

        System.out.println("UrishService.lrivUrishBan()" + criteria);
    }

    public void nulla() {

    }
}
