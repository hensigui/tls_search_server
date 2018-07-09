package com.tolovesoul.config;

import java.util.Arrays;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.tolovesoul.interceptor.MyInterceptor;
import com.tolovesoul.webservice.RelevantResults;

@Configuration
public class CxfConfig {
    @Autowired
    private Bus bus;
    @Autowired
    private RelevantResults relevantResults;
    @Autowired
    private MyInterceptor myInterceptor;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus,relevantResults);
        endpoint.publish("/relevantResults");//接口发布在 /relevantResults 目录下
        endpoint.setInInterceptors(Arrays.asList(myInterceptor));//配置自定义拦截器列表
        return endpoint;
    }
    
    @Bean
    public WebClient webClient() {
    	WebClient client = new WebClient(BrowserVersion.CHROME);
		// 1 启动JS  
		client.getOptions().setJavaScriptEnabled(false);  
	    // 2 禁用Css，可避免自动二次请求CSS进行渲染  
		client.getOptions().setCssEnabled(false);  
	    // 3 启动客户端重定向  
		client.getOptions().setRedirectEnabled(true); 
		client.getOptions().setDownloadImages(false);
		return client;
    }
}
