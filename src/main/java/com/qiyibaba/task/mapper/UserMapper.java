package com.qiyibaba.task.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.qiyibaba.task.vo.UserInfoRequest;
import com.qiyibaba.task.vo.UserInfoResponse;

@Mapper
public interface UserMapper {

    UserInfoResponse getUserInfo(UserInfoRequest request);
}
