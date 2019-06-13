package com.qiyibaba.task.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Service
public class JobMonitorListener implements JobExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(JobMonitorListener.class);

	@Override
	public void beforeJob(JobExecution je) {
		logger.info("before job:exeId:{},jobinsId:{},startTime:{}", je.getId(), je.getJobId(), je.getStartTime());
	}

	@Override
	public void afterJob(JobExecution je) {
		logger.info("after job:exeId:{},jobinsId:{},endTime{}", je.getId(), je.getJobId(), je.getEndTime());
	}
}
