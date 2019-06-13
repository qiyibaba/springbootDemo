package com.qiyibaba.task.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qiyibaba.task.command.TaskCommandRunner;
import com.qiyibaba.task.listener.JobMonitorListener;

@Configuration
// org.springframwork.cloud.task.configuration.EnableTask
// @EnableTask
@EnableBatchProcessing
// @EnableConfigurationProperties(DruidConfiguration.class)
public class TaskCommonConfig {
	
	@Bean
	public TaskCommandRunner taskCommandRunner(){
		return new TaskCommandRunner();
	}
	
	public JobMonitorListener jobMonitorListener(){
		return new JobMonitorListener();
	}
}