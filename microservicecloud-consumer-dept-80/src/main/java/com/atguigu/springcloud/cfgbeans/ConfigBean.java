package com.atguigu.springcloud.cfgbeans;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//配置类
@Configuration
public class ConfigBean //boot -->spring   applicationContext.xml --- @Configuration配置   ConfigBean = applicationContext.xml
{
	@Bean
	@LoadBalanced	//开启负载均衡，SpringCloud Ribbon 是基于 Netflix Ribbon 实现的一套客户端 负载均衡的工具。
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}

//配置项相当于配置了bean对象
//@Bean
//public UserServcie getUserServcie()
//{
//	return new UserServcieImpl();
//}
// applicationContext.xml == ConfigBean(@Configuration)
//<bean id="userServcie" class="com.atguigu.tmall.UserServiceImpl">