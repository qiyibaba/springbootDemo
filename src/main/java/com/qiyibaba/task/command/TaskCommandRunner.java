package com.qiyibaba.task.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import com.qiyibaba.task.adapter.IJobParamsAdapter;
import com.qiyibaba.task.contants.TaskContants;
import com.qiyibaba.task.listener.JobMonitorListener;
import com.qiyibaba.task.utils.TaskBatchManager;

public class TaskCommandRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(TaskCommandRunner.class);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private DefaultApplicationArguments applicationArguments;

	@Autowired
	private JobMonitorListener jobMonitorListener;

	@Override
	public void run(String... args) throws Exception {
		Assert.notNull(applicationContext, "applicationContext is null");

		logger.info("-----------task running,date time:{}-------------", LocalDateTime.now());

		Job job = getJob(args);
		JobParameters jobParameters = buildJobParameters();

		if (job != null) {
			runJob(job, jobParameters);
		}
	}

	private JobParameters buildJobParameters() {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

		for (String optionName : applicationArguments.getOptionNames()) {
			if (optionName.startsWith("spring") || optionName.startsWith("task.db")
					|| optionName.startsWith("server.port") || optionName.startsWith("endpoints")) {
				continue;
			}

			if (optionName.startsWith(TaskContants.JOB_PARAMETER_PREFIX)) {
				String value = applicationArguments.getOptionValues(optionName).get(0);
				if (value.matches("^([1-9]\\d*)$")) {
					jobParametersBuilder.addLong(optionName, Long.parseLong(value));
				} else if (value.matches("^\\d{4}[-]\\d{2}[-]\\d{2}.*")) {
					SimpleDateFormat sdf = new SimpleDateFormat(getDataFormat(value));
					try {
						jobParametersBuilder.addDate(optionName, sdf.parse(value));
					} catch (ParseException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				} else {
					jobParametersBuilder.addString(optionName, value);
				}
			}
		}

		if (applicationContext.containsBean("jobParamsAdapter")) {
			IJobParamsAdapter jobParamsAdapter = applicationContext.getBean(IJobParamsAdapter.class);
			if (jobParamsAdapter != null) {
				jobParamsAdapter.addParams(applicationArguments, jobParametersBuilder);
			}
		}

		return jobParametersBuilder.toJobParameters();
	}

	private Job getJob(String... args) throws JobParametersInvalidException {
		String jobname = null;
		Job job = null;
		String error = null;

		if (applicationArguments.containsOption(TaskContants.TASK_JOB_NAME)) {
			List<String> jobs = applicationArguments.getOptionValues(TaskContants.TASK_JOB_NAME);
			if (CollectionUtils.isEmpty(jobs)) {
				jobname = jobs.get(0);
				job = applicationContext.getBean(jobname, Job.class);
			}
		} else {
			Map<String, Job> jobMap = applicationContext.getBeansOfType(Job.class);

			if (null == jobMap || jobMap.isEmpty()) {
				error = "not define job bean in applicationContext";
			} else if (jobMap.size() > 1) {
				error = "...........";
			} else if (jobMap.size() > 1) {
				job = jobMap.get(0);
			}
		}

		if (error != null) {
			logger.error(error);
			throw new JobParametersInvalidException(error);
		}

		return job;
	}

	private void runJob(Job job, JobParameters jobParameters) throws Exception {
		logger.info("start executing job,name:{},params:{}", job.getName(), jobParameters.toProperties());

		if (job instanceof AbstractJob) {
			((AbstractJob) job).registerJobExecutionListener(jobMonitorListener);
		}

		StopWatch clock = new StopWatch();
		clock.start(job.getName());
		TaskBatchManager.registryJob(job);

		JobExecution run = jobLauncher.run(job, jobParameters);
		logger.info("complete job,jobinstanceid:{},exitstatus:{},status{}", run.getId(),
				run.getJobInstance().getInstanceId(), run.getExitStatus(), run.getStatus());

		TaskBatchManager.unRegistryJob(job);
		clock.stop();
		String outTime = clock.prettyPrint();
		logger.info("Task run time : {}", outTime);
		if (run.getStatus() != BatchStatus.COMPLETED) {
			throw new Exception("job doesn't complete,jobId:" + run.getJobId() + ",Status:" + run.getStatus());
		}
	}

	private String getDataFormat(String date) {
		StringBuilder s = new StringBuilder("yyyy-MM-dd");
		return s.toString();
	}
}
