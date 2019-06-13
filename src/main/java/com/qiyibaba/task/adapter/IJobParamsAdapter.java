package com.qiyibaba.task.adapter;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.boot.DefaultApplicationArguments;

public interface IJobParamsAdapter {

	void addParams(DefaultApplicationArguments aa,JobParametersBuilder jpb);
}
