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
	 * 根据关键字及引擎标识进行搜索
	 * @param str 关键字
	 * @param start 结果起始位置
	 * @param cx 搜索引擎标识
	 * @return
	 */
	public String search(String str,String start,String cx);
}
