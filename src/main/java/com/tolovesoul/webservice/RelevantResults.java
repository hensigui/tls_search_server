package com.tolovesoul.webservice;

import javax.jws.WebService;
/**
 * 获取相关资源接口
 * @author yuanyue
 * @Description: 
 * @date 2018年7月4日
 */
@WebService(targetNamespace="http://webservice.tolovesoul.com")
public interface RelevantResults {

	/**
	 * 根据指定查询关键字搜索
	 * @param str
	 * @param start
	 * @return
	 */
	public String search(String str,String start);
}
