package com.tolovesoul.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    
    /**
     * 用于存放cx-token映射
     * @return
     */
    
    @Bean(name="cxTokenMap")
    public Map<String,String> cxTokenMap(){
    	return new HashMap<String,String>();
    }
}
