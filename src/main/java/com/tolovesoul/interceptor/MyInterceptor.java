package com.tolovesoul.interceptor;

import java.util.List;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.tolovesoul.util.PropertiesUtil;
@Component
public class MyInterceptor extends AbstractPhaseInterceptor<SoapMessage>{

	public MyInterceptor() {
		super(Phase.PRE_INVOKE); // 在调用方法之前调用自定义拦截器
	}

	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers=message.getHeaders();
		if(headers==null || headers.size()==0){
			throw new Fault(new IllegalArgumentException("没有Header,拦截器实施拦截"));
		}
		Header firstHeader=headers.get(0);
		Element ele=(Element) firstHeader.getObject();
		NodeList uList=ele.getElementsByTagName("userName");
		NodeList pList=ele.getElementsByTagName("password");
		if(uList.getLength()!=1){
			throw new Fault(new IllegalArgumentException("用户名格式不对"));
		}
		if(pList.getLength()!=1){
			throw new Fault(new IllegalArgumentException("密码格式不对"));
		}
		String userName=uList.item(0).getTextContent();
		String password=pList.item(0).getTextContent();
		if(!userName.equals(PropertiesUtil.getValue("userName")) || !password.equals(PropertiesUtil.getValue("password"))){
			throw new Fault(new IllegalArgumentException("用户名或者密码错误"));
		}
	}

}
