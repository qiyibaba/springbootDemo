package com.qiyibaba.task.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.qiyibaba.task.config.CommonJobConifg;
import com.qiyibaba.task.job.dao.User;
import com.qiyibaba.task.job.step.UserItemProcessor;

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
@Configuration
@EnableBatchProcessing
public class TestUserMigrationJobConfiguration extends CommonJobConifg<User>{
	
	private static final Logger logger = LoggerFactory.getLogger(TestUserMigrationJobConfiguration.class);
	
	@Value("${read.fileName}")
	private String fileName;

	@Override
	@Bean
	public FlatFileItemReader<User> reader() {
		FlatFileItemReader<User> reader = new FlatFileItemReader<>();
		logger.info("begin to read file :{}", fileName);
		reader.setResource(new ClassPathResource(fileName));
		reader.setLineMapper(new DefaultLineMapper<User>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer("|") {
					{
						setNames(new String[] { "uid", "tag", "type" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {
					{
						setTargetType(User.class);
					}
				});
			}
		});
		return reader;
	}

	@Override
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}

	@Override
	@Bean
	public JdbcBatchItemWriter<User> Writer() {
		JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("INSERT INTO tb_user (uid,tag,type) VALUES (:uid, :tag,:type)");
		writer.setDataSource(dataSource);
		return writer;
	}
}