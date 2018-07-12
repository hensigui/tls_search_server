package com.tolovesoul.webservice.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.tolovesoul.webservice.RelevantResults;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * webserviceʵ����
 * @author yuanyue
 * @Description: 
 * @date 2018��7��12��
 */
@Component("relevantResults")
@WebService(targetNamespace="http://webservice.tolovesoul.com")
public class RelevantResultsImpl implements RelevantResults{
	/**
	 * cx-tokenӳ���
	 */
	@Resource(name="cxTokenMap")
	private Map<String,String> cxTokenMap;
	
	public String search(String str, String start ,String cx) {
		JSONArray r=null;
		boolean flag=true;
		try {
			r=trySearch(cx,str,start,cxTokenMap.get(cx));
			System.out.println("ִ�гɹ���");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ִ��ʧ�ܣ�");
			flag=false; 
		}
		
		JSONObject result=new JSONObject();
		if(!flag){
			result.put("success", false);
			result.put("errorInfo", "������æ������������ԣ�");
		}else if(r.size()==0){
			result.put("success", false);
			result.put("errorInfo", "Ϊ��ѯ��������뻻���ؼ��֣�");
		}else{
			result.put("success", true);
			result.put("rows", r);
		}
		return result.toString();
	}

	/**
	 * ���Բ�ѯ
	 * @param cx �ȸ��Զ�����������id
	 * @param query
	 * @param start
	 * @param token �ȸ����������ƣ�һ���������Ķ�Ӧһ��token
	 * @return
	 * @throws Exception
	 */
	private JSONArray trySearch(String cx,String query,String start,String token)throws Exception{
		String urlStr=
				"https://www.googleapis.com/customsearch/v1element?key=AIzaSyCVAXiUzRYsML1Pv6RwSG1gunmMikTzQqY&rsz=filtered_cse&num=10&hl=en&prettyPrint=false&source=gcsc&gss=.com&sig=0c3990ce7a056ed50667fe0c3873c9b6&start="+start+"&cx="+cx+"&q="+query+"&cse_tok="+token+"&sort=&googlehost=www.google.com";
		URL url=new URL(urlStr);
		BufferedReader bufr=new BufferedReader(new InputStreamReader(new BufferedInputStream(url.openStream()),"utf-8"));
		String line;
		StringBuffer sb=new StringBuffer();
		while((line=bufr.readLine())!=null){
			sb.append(line);
		}
		bufr.close();
		System.out.println(sb.toString());
		
		JSONArray r=new JSONArray();
		JSONObject jsonObject=JSONObject.fromObject(sb.toString());
		JSONArray results=jsonObject.getJSONArray("results");
		for(int i=0;i<results.size();i++){
			JSONObject j=(JSONObject) results.get(i);
			JSONObject o=new JSONObject();
			o.put("title", j.get("title"));
			o.put("content", j.get("content"));
			o.put("unescapedUrl", j.get("unescapedUrl"));
			r.add(o);
		}
		return r;
	}
	
}
