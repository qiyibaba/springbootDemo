package com.qiyibaba.task.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDataSourceBuilder {
	private static final Logger logger = LoggerFactory.getLogger(DruidDataSourceBuilder.class);

	private Map<String,String> props = new HashMap<String,String>();
	
	private Map<String,DruidDataSource> cache = new ConcurrentHashMap<String,DruidDataSource>();	
}
