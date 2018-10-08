package com.fang.rabbitmq.springboot.rabbitmq.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;
import com.fang.rabbitmq.springboot.rabbitmq.demo.RabbitMQConfig;
import com.fang.rabbitmq.springboot.rabbitmq.demo.entity.User;
import com.fang.rabbitmq.springboot.rabbitmq.demo.service.Consumer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Consumer consumer;

    @GetMapping("/sendMessage")
    public Object sendMessage() {

        List<Thread> list = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Thread t = new Thread(()->{
                String value = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
                Console.log("send message {}", value);
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, value);
            });
            list.add(t);
        }
        list.forEach(Thread::start);

        return "ok";
    }

    @GetMapping("/getList")
    @ResponseBody
    public List<User> getList(){
        List<User> list = consumer.getList();
        return list;
    }
}