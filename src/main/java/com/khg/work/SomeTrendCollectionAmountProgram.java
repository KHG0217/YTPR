package com.khg.work;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tapacross.sns.util.ThreadUtil;

public class SomeTrendCollectionAmountProgram {
	int SLEEP_SEC = 5;
	
	private Document parse(String url, String cookies, String fromDate, String toDate, String type, String keyword, String host, int port) throws IOException {
        ThreadUtil.sleepSec(SLEEP_SEC);
		String requestBody = 
        		"{\"startDate\": \""
        		+ fromDate
        		+ "\",\"endDate\": \""
        		+ toDate
        		+ "\",\"topN\": 500,\"period\": \"0\",\"analysisMonths\": 0,\"categorySetName\": \"TSN\",\"excludeRT\": false,"
        		+ "\"sources\": \""
        		+ type
        		+ "\",\"keyword\": \""
        		+ keyword
        		+ "\",\"synonym\": null,\"keywordFilterIncludes\": null,\"keywordFilterExcludes\": null,\"includeWordOperators\": \"||\",\"excludeWordOperators\": \"||\",\"scoringKeyword\": \"null\",\"ex\": null}";
	
        Document doc = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Connection", "keep-alive")
                .header("Content-Length", "354")
                .header("Content-Type", "application/json")
                .header("Cookie", cookies)
                .header("Host", "some.co.kr")
                .header("Origin", "https://some.co.kr")
                .header("Referer", "https://some.co.kr/analysis/social/mention?keyword=%EC%94%A8%EC%9C%A0&startDate=20230101&endDate=20230131&sources=blog&excludeRT=false")
                .header("sec-ch-ua", "\"Not_A Brand\";v=\"99\", \"Google Chrome\";v=\"109\", \"Chromium\";v=\"109\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-origin")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                .header("X-Requested-With", "XMLHttpRequest")
                .proxy(host, port)
                .requestBody(requestBody)
                .ignoreContentType(true)
                .post();
		return doc;
	}
	
	private String extractKeywordAmount(String JsonData) {
		JSONObject obj = new JSONObject(JsonData);
		String keyWordAmount = String.valueOf( obj.getJSONObject("item").getJSONArray("dataList").getJSONObject(0).getJSONObject("data")
				.getInt("keywordDocumentCount"));
		return keyWordAmount;	
	}
	
	private String extractTotalAmount(String JsonData) {
		JSONObject obj = new JSONObject(JsonData);
		String totalAmount = String.valueOf( obj.getJSONObject("item").getJSONArray("dataList").getJSONObject(0).getJSONObject("data")
				.getInt("totalDocumentCount"));
		return totalAmount;	
	}
	
	private List<String> readKeywordList(){
		List<String> keywordList = new ArrayList<String>();
		String path = "../work/src/main/resources/data/keywordList.txt";
		BufferedReader reader = null;
		try {
			String line = null;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			while((line = reader.readLine()) != null) {
				keywordList.add(line);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(reader != null) {
				try {
					reader.close();
				}catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return keywordList;
		
	}
	
	public static void main(String[] args) {
		SomeTrendCollectionAmountProgram pr = new SomeTrendCollectionAmountProgram();
		String url ="https://some.co.kr/sometrend/analysis/composite/v2/keyword-transition";
		String cookies = "_fwb=24A8gpRmyJullEPEgzxiz.1717554454724; _gcl_au=1.1.795220251.1717554455; _ga=GA1.1.2124567963.1717554455; _fbp=fb.2.1717554455467.247077424297125882; _hjSessionUser_3745614=eyJpZCI6ImQzYmQ0MThmLTlmODctNTRjYi05NGIwLWRjMzYyODIzMGYwNSIsImNyZWF0ZWQiOjE3MTc1NTQ0NTU1MTIsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_3745614=eyJpZCI6IjEwYjM3YTQ3LWU4MTgtNDRlMy05ZmE3LTcwNjJkMWMwZTczZCIsImMiOjE3MTc1NjI2NTgxMjEsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; OAuth_Token_Request_State=06769bc4-61c0-49e1-9579-880a3c32765c; SESSION=NTYyZDQyYzQtNWUxOS00OWQ2LTkwNjItZjcyZTVkOGRhZjg3; wcs_bt=s_29fb873cc748:1717562665; JSESSIONID=3C11E33FC2799D6D1E49997AEE60FB0C;";
		String fromDate = "20240501";
		String toDate = "20240531";
		String type = "blog"; //insta, twitter, news, blog
		String host = "115.144.22.39";
		int port = 5287;
		List<String> keywordList = pr.readKeywordList();
		try {
			
			for(int i = 0; i< keywordList.size(); i ++) {
				if(i == 0) {
					Document doc = pr.parse(url, cookies, fromDate, toDate, type, keywordList.get(i), host, port);
					String JsonData = doc.text();
					String totalAmount = pr.extractTotalAmount(JsonData);
					System.out.println("전체:"+totalAmount);
					String keywordTotal = pr.extractKeywordAmount(JsonData);
					System.out.println(keywordList.get(i)+":"+keywordTotal);
				}else {
					Document doc = pr.parse(url, cookies, fromDate, toDate, type, keywordList.get(i), host, port);
					String JsonData = doc.text();
					String keywordTotal = pr.extractKeywordAmount(JsonData);
					System.out.println(keywordList.get(i)+":"+keywordTotal);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
