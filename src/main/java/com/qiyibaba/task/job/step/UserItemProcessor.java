package com.qiyibaba.task.job.step;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.qiyibaba.task.job.dao.User;

public class UserItemProcessor implements ItemProcessor<User, User> {

	@Override
	public User process(User userMap) throws Exception {
		Long uid = userMap.getUid();
		String tag = "Format:" + userMap.getTag();
		int label = userMap.getType() == null ? Integer.valueOf(0) : userMap.getType();
		if (StringUtils.isNotBlank(tag)) {
			Map<String, Object> params = new HashMap<>();
			params.put("uid", uid);
			return new User(uid, tag, label);
		}
		return null;
	}
}
