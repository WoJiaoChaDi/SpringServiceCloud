package com.atguigu.springcloud.cfgbeans;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
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

	//如果没有配置 IRule 这个类型的bean，则采用默认的负载均衡  轮询
	@Bean
	public IRule myRule()
	{
		//return new RoundRobinRule();//轮询
		return new RandomRule();//随机
		//return new AvailabilityFilteringRule();//先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阀值的服务，然后对剩余的服务列表按照轮询策略进行轮询
		//return new WeightedResponseTimeRule();//根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大，被选中概率越大
		//return new RetryRule();//先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试
		//return new BestAvailableRule();//会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的
		//return new ZoneAvoidanceRule();//默认规则，复合判断server所在区域的性能和server的可用性选择服务器
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