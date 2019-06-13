package com.qiyibaba.task.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

public class WriteMonitorListener implements ItemWriteListener<Object> {

	private static final Logger logger = LoggerFactory.getLogger(WriteMonitorListener.class);

	@Override
	public void beforeWrite(List<?> items) {
		logger.info("before to write list:{}", items.size());
	}

	@Override
	public void afterWrite(List<?> items) {
		logger.info("end to write list!");
	}

	@Override
	public void onWriteError(Exception exception, List<?> items) {
		logger.error("Failed writing ,error info : {}", exception.getMessage());
		logger.error("Failed writing item : {}", items);
	}
}
