package com.mikayelovich.reflecthack;

import com.mikayelovich.reflecthack.criteria.test.FirstCriteria;
import com.mikayelovich.reflecthack.service.UrishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ReflectApplication implements CommandLineRunner {


    @Autowired
    private UrishService urishService;

    public static void main(String[] args) {
        SpringApplication.run(ReflectApplication.class, args);
    }


    @Override
    public void run(String... args) {
        FirstCriteria firstCriteria = new FirstCriteria();
        firstCriteria.setDocumentName("'\\");
        firstCriteria.setDocumentId(123);
        firstCriteria.getSecondStringList().add("new");
        firstCriteria.getSecondStringList().add("new2");
        firstCriteria.getSecondStringList().add("new3");
        urishService.lrivUrishBan(firstCriteria);
    }
}
