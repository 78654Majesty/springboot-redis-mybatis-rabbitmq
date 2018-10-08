package com.fang.rabbitmq.springboot.rabbitmq.demo.service;

import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONObject;
import com.fang.rabbitmq.springboot.rabbitmq.demo.RabbitMQConfig;
import com.fang.rabbitmq.springboot.rabbitmq.demo.dao.UserMapper;
import com.fang.rabbitmq.springboot.rabbitmq.demo.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class Consumer {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    public static final String USER_KEY="USER";

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(String message) {
        if (StringUtils.isNotBlank(message)){
            User user = new User();
            user.setUserName("fanglingxiao");
            user.setUserPassword("123");
            user.setUserRealName("fanglingxiao");
            user.setUserRole("管理员");
            userMapper.insert(user);
            String s = redisTemplate.opsForValue().get(USER_KEY);
            if (StringUtils.isNotBlank(s)){
                redisTemplate.delete(USER_KEY);
            }
        }
        Console.log("consume fang message {}", message);
    }

    public List<User> getList() {
        List<User> users = userMapper.selectAllUser();
        String jsonString = JSONObject.toJSONString(users);
        redisTemplate.opsForValue().set(USER_KEY,jsonString);
        return users;
    }
}