package com.xph.consumer.controller;

import com.xph.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("{id}")
    public User queryByid(@PathVariable("id")Long id){
        // 根据服务id获取实例
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        // 从所有实例中取出一个具体的实例
        ServiceInstance instance = instances.get(0);
        // 根据具体的一个实例中获取id与端口，拼接url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/user/"+id;
        System.out.println(url);
        User user = restTemplate.getForObject(url,User.class);
        return user;
    }

}
