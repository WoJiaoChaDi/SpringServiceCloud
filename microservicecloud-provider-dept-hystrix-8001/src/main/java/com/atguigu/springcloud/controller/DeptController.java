package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.Dept;
import com.atguigu.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptController
{
	@Autowired
	private DeptService service;

	@RequestMapping(value = "/dept/add", method = RequestMethod.POST)
	public boolean add(@RequestBody Dept dept)
	{
		return service.add(dept);
	}

	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
	//如果出现异常，像调用者返回一个 符合预期的、可处理的备选响应
	@HystrixCommand(fallbackMethod = "processHystrix_Get")
	public Dept get(@PathVariable("id") Long id)
	{
		Dept dept = this.service.get(id);

		//模仿出现异常，进行熔断
		if (null == dept) {
			throw new RuntimeException("该ID：" + id + "没有没有对应的信息");
		}

		return dept;
	}

	@RequestMapping(value = "/dept/list", method = RequestMethod.GET)
	public List<Dept> list()
	{
		return service.list();
	}

	//服务的发现
	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
	public Object discovery()
	{
		//查看Eureka的微服务列表
		List<String> list = client.getServices();
		System.out.println("**********" + list);

		//找一个叫 MICROSERVICECLOUD-DEPT 的微服务
		List<ServiceInstance> srvList = client.getInstances("MICROSERVICECLOUD-DEPT");
		for (ServiceInstance element : srvList) {
			System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
					+ element.getUri());
		}
		return this.client;
	}

	//hystrix 回调函数
	public Dept processHystrix_Get(@PathVariable("id") Long id)
	{
		//如果出现问题，则返回一个符合预期的内容（这里是Dept）
		return new Dept().setDeptno(id).setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand")
				.setDb_source("no this database in MySQL");
	}
}
