package com.qiyibaba.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.qiyibaba.task.config.EnableBatchTask;

@EnableTransactionManagement
@SpringBootApplication
@EnableBatchTask
@EnableCaching
@MapperScan("com.lt.task")
@ComponentScan(basePackages = {"com.qiyibaba.task"})
public class MainTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainTaskApplication.class, args);
	}
}