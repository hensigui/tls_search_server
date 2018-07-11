package com.tolovesoul.webservice.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tolovesoul.webservice.RelevantResults;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * webserivce�ӿ�ʵ����
 * @author user
 *
 */
@Component("relevantResults")
@WebService(targetNamespace="http://webservice.tolovesoul.com")
public class RelevantResultsImpl implements RelevantResults{
	@Autowired
	private WebClient webClient;
	
	@Value("${search.token}")
	private String token;//�ȸ���֤token
	
	int index=0;
	
	public static List<String> urlList=new ArrayList<String>();
	
	{
		BufferedReader urlReader=new BufferedReader(new InputStreamReader(RelevantResultsImpl.class.getResourceAsStream("/url.txt")));
		String url="";
		try {
			while((url=urlReader.readLine())!=null){
				urlList.add(url);
				System.out.println(url);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String search(String str, String start) {
		index=index%urlList.size(); // ��������
		JSONArray r=null;
		boolean flag=true;
		for(int i=1;i<=3;i++){
			try {
				r=trySearch(urlList.get(index),str,start,token);
				System.out.println("index="+index+"ִ�гɹ���");
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("index="+index+"ִ��ʧ�ܣ�");
				if(i==3){
					flag=false;
				}
				index++;
				continue;
			}
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
	 * @param token �ȸ�����������
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
		//Page page = webClient.get;
		
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
