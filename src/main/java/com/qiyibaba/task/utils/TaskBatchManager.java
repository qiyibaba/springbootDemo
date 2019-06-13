package com.qiyibaba.task.utils;

import org.springframework.batch.core.Job;

public class TaskBatchManager {

	private TaskBatchManager() {

	}

	private static Job runJob;

	public static Job getRunJob() {
		return runJob;
	}

	public static void setRunJob(Job runJob) {
		TaskBatchManager.runJob = runJob;
	}

	public static void registryJob(Job job) {
		runJob = job;
	}

	public static void unRegistryJob(Job job) {
		runJob = null;
	}
}
