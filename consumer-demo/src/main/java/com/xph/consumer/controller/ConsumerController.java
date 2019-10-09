package com.xph.consumer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xph.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.hystrix.HystrixProperties;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// DefaultProperties注解：defaultFallback的值是配置这个类中异常处理的方法名，只要在使用方法上加上@HystrixCommand表示使用，并且使用的方法必须是空参
@DefaultProperties(defaultFallback = "defaultByIdFallback")
@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("{id}")
    //若使用全局超时配置只需使用@HystrixCommand注解即可
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="2500") //设置此方法超时时长为2秒
    })
    public String queryByid(@PathVariable("id")Long id){
        String url = "http://user-service/user/"+id;
        User user = restTemplate.getForObject(url,User.class);
        return user.toString();
    }

    public String queryByIdFallback(Long id){
        return "不好意思,服务器太拥挤了-局部";
    }

    public String defaultByIdFallback(){
        return "不好意思,服务器太拥挤了-全局";
    }


    /***************************方式一：-S****************************/
    /*
        这种方式是一种局部的异常处理方法，只作用于此方法
        @HystrixCommand中fallbackMethod的值为异常后要执行的方法名
        方法要求：返回值必须和原方法一样
     */
    /*
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("{id}")
    @HystrixCommand(fallbackMethod = "queryByIdFallback") //fallbackMethod的参数是
    public String queryByid(@PathVariable("id")Long id){
        String url = "http://user-service/user/"+id;
        User user = restTemplate.getForObject(url,User.class);
        return user.toString();
    }

    public String queryByIdFallback(Long id){
        return "不好意思,服务器太拥挤了-局部";
    }
    */
    /***************************方式一：-E****************************/


    /************************************ Ribbon负载均衡器-S **************************************/

        /***************************方式三:简化方式二-S****************************/
        /*
            需要在ConsumerController的restTemplate上添加LoadBalanced注解，
            加上注解后会有拦截器（LoadBalancerInterceptor）将restTemplate的请求拦截下来，
            然后根据restTemplate中的url的服务名称去通过负载均衡器获取真实url后替换原来的url

        @Autowired
        private RestTemplate restTemplate;

        @GetMapping("{id}")
        public User queryByid(@PathVariable("id")Long id){
            String url = "http://user-service/user/"+id;
            User user = restTemplate.getForObject(url,User.class);
            return user;
        }
        */
        /***************************方式三:简化方式二-E****************************/



        /***************************方式二:使用负载均衡器来获取实例-S*****************************/
        /*
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
        */
        /****************************方式二:使用负载均衡器来获取实例-E****************************/



        /***************************方式一:最初级的获取方式-S****************************/
        /*
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
        */
        /****************************方式一:最初级的获取方式-E***************************/

    /************************************ Ribbon负载均衡器-E **************************************/
}
