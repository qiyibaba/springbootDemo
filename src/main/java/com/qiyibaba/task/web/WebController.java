package com.qiyibaba.task.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qiyibaba.task.mapper.UserMapper;
import com.qiyibaba.task.vo.UserInfoRequest;
import com.qiyibaba.task.vo.UserInfoResponse;

@RestController
public class WebController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/test", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json;charset=UTF-8")
    public UserInfoResponse login(@RequestBody UserInfoRequest request){
          UserInfoResponse userInfoResponse = new UserInfoResponse();
          userInfoResponse = userMapper.getUserInfo(request);
          return userInfoResponse;
    }

}
