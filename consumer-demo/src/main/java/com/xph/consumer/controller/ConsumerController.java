package com.xph.consumer.controller;

import com.xph.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    /***************************方式三:简化方式二-S****************************/
    /*
        需要在ConsumerController的restTemplate上添加LoadBalanced注解，
        加上注解后会有拦截器（LoadBalancerInterceptor）将restTemplate的请求拦截下来，
        然后根据restTemplate中的url的服务名称去通过负载均衡器获取真实url后替换原来的url
     */
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("{id}")
    public User queryByid(@PathVariable("id")Long id){
        String url = "http://user-service/user/"+id;
        User user = restTemplate.getForObject(url,User.class);
        return user;
    }


    /***************************方式二:使用负载均衡器来获取实例-S****************************
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RibbonLoadBalancerClient client;

    @GetMapping("{id}")
    public User queryByid(@PathVariable("id")Long id){
        // 进过负载均衡器后，直接获取具体的某个实例
        ServiceInstance instance = client.choose("user-service");
        // 拼接url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/user/"+id;
        User user = restTemplate.getForObject(url,User.class);
        return user;
    }
    ***************************方式二:使用负载均衡器来获取实例-E****************************/



    /***************************方式一:最初级的获取方式-S***************************
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
        User user = restTemplate.getForObject(url,User.class);
        return user;
    }
     ***************************方式一:最初级的获取方式-E***************************/
}
