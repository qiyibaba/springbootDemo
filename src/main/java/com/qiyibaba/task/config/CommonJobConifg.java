package com.qiyibaba.task.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.qiyibaba.task.listener.JobMonitorListener;

/**
 * 1. "READ-PROCESS-WRITE" 处理，根据字面意思理解就可以:
 * READ就是从资源文件里面读取数据，比如从xml文件,csv文件,数据库中读取数据. PROCESS就是处理读取的数据
 * WRITE就是将处理过的数据写入到其他资源文件中去，可以是XML,CSV,或者数据库. 比如：从CSV文件中 读取数据，经过处理之后，保存到数据库.
 * spring batch 提供了很多类去处理这方面的东西。
 * 
 * 2.单个task, 也就是处理单个任务。比如在一个step 开始之前或者完成之后清除资源文件等. 3.许多个step 组成在一起，就组成了一个job.
 * 所以他们之间的关系，就如同下面的描述: 一个 job = 很多steps 一个step = 一个READ-PROCESS-WRITE 或者 一个task.
 * 
 * @author 10210074
 *
 */
public abstract class CommonJobConifg<T> {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	public abstract ItemReader<T> reader();

	public abstract ItemWriter<T> Writer();

	public abstract ItemProcessor<T, T> processor();

	@Bean
	public Job execJob(JobMonitorListener listener) {
		return jobBuilderFactory.get("execJob").incrementer(new RunIdIncrementer()).listener(listener).flow(execStep())
				.end().build();
	}

	@Bean
	public Step execStep() {
		return stepBuilderFactory.get("execStep").<T, T>chunk(100).reader(reader()).processor(processor())
				.writer(Writer()).build();
	}
}