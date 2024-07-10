package com.khg.work;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tapacross.sns.util.ThreadUtil;
/**
 * @author: hgkim
 * @date : 2024.06.25 
 * @see 
 * 	- 경쟁사와 수집량을 비교할 수 있는 프로그램
 *  - isMonthlySurvey true,false에 따라 콘솔출력값이 바뀐다.
 *  - 프로그램을 시작하기 위해선 트렌드업, 썸트렌드에 로그인하여 쿠키값을 하드코딩 해야한다,
 *  	- 트렌드업: 브랜드분석 -> 키워드 검색 api w202의 헤더에서 값을 추출
 *  	- 썸트렌드: 아무키워드 검색 -> keyword-transition의 헤더에서 값을 추출 
 * 
 */
public class SomeTrendCollectionAmountProgram {
	int SLEEP_SEC = 5;
	private final String TREAND_UP_API_URL = "http://tu4.trendup.co.kr/wdata/w202";
	private final String SOMETREAND_UP_API_URL = "https://some.co.kr/sometrend/analysis/composite/v2/keyword-transition";
	private final String TARET_TRENDUP = "trendUp"; 
	private final String TARET_SOMETREND = "sometrend";
	
	/**
	 * 조사할 키워드리스트를 읽어 List에담아 반환한다.
	 * 
	 */
	private List<String> readKeywordList(){
		List<String> keyWordList = new ArrayList<String>();
		String path = "../work/src/main/resources/data/keywordList.txt";
		BufferedReader reader = null;
		try {
			String line = null;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			while((line = reader.readLine()) != null) {
				keyWordList.add(line);
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
		return keyWordList;
		
	}
	
	/**
	 * 경쟁사의 jsonData를 parse한다.
	 */
	private Document parseSomeTrend(String someTrendUrl, String someTrendcookies, String fromDate, String toDate, String someTreandType, String keyWord) {
        ThreadUtil.sleepSec(SLEEP_SEC);
		String requestBody = 
        		"{\"startDate\": \""
        		+ fromDate
        		+ "\",\"endDate\": \""
        		+ toDate
        		+ "\",\"topN\": 500,\"period\": \"0\",\"analysisMonths\": 0,\"categorySetName\": \"TSN\",\"excludeRT\": false,"
        		+ "\"sources\": \""
        		+ someTreandType
        		+ "\",\"keyword\": \""
        		+ keyWord
        		+ "\",\"synonym\": null,\"keywordFilterIncludes\": null,\"keywordFilterExcludes\": null,\"includeWordOperators\": \"||\",\"excludeWordOperators\": \"||\",\"scoringKeyword\": \"null\",\"ex\": null}";
		Document doc = null;
		try {
			doc = Jsoup.connect(someTrendUrl)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Connection", "keep-alive")
                .header("Content-Length", "354")
                .header("Content-Type", "application/json")
                .header("Cookie", someTrendcookies)
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
//                .proxy(host, port)
                .requestBody(requestBody)
                .ignoreContentType(true)
                .post();
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		return doc;
	}

	/**
	 * 자사의 jsonData를 parse한다.
	 */
	private Document parseTrendUp(String trendUpUrl, String trendUpCookies, String fromDate, String toDate, String trendUpSiteType, String keyword) {
		Document doc = null;
		try {
			String encodeKeyword = URLEncoder.encode(keyword,"utf-8"); 
			String requestBody = "dashId=4765"
	    			+ "&media%5B%5D="
	    			+ trendUpSiteType
	    			+ "&from="
	    			+ fromDate
	    			+ "&to="
	    			+ toDate
	    			+ "&dateType=date&size=100&offset=0&queryType=&publisher=A&groupCode="
	    			+ "&search="
	    			+ encodeKeyword
	    			+ "&exclude=&filter=&hash=&prox=-1";
	    	doc = Jsoup.connect(trendUpUrl)
	        .header("Accept", "application/json, text/javascript, */*; q=0.01")
	        .header("Accept-Encoding", "gzip, deflate")
	        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
	        .header("Connection", "keep-alive")
	        .header("Content-Length", "174")
	        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
	        .header("Cookie", trendUpCookies)
	        .header("Host", "tu4.trendup.co.kr")
	        .header("Origin", "http://tu4.trendup.co.kr")
	        .header("Referer", "http://tu4.trendup.co.kr/trend/brand")
	        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
	        .header("X-Requested-With", "XMLHttpRequest")
	        .requestBody(requestBody)
	        .ignoreContentType(true)
	        .post();  
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * 특정기간의 수집량을 추출하여 반환한다.
	 */
	private String extractKeywordAmount(String JsonData, String target) {
		JSONObject obj = new JSONObject(JsonData);
		String keyWordAmount = null;
		
		if(target.equals(TARET_TRENDUP)) {
			keyWordAmount = String.valueOf( obj.getInt("total"));
		}else if(target.equals(TARET_SOMETREND)){
			keyWordAmount = String.valueOf( obj.getJSONObject("item").getJSONArray("dataList").getJSONObject(0).getJSONObject("data")
					.getInt("keywordDocumentCount"));
		}
		return keyWordAmount;	
	}
	
	/**
	 * 경쟁사 특정기간 매채별 총 수집량 반환한다.
	 */
	private String extractTotalAmount(String JsonData) {
		JSONObject obj = new JSONObject(JsonData);
		String totalAmount = String.valueOf( obj.getJSONObject("item").getJSONArray("dataList").getJSONObject(0).getJSONObject("data")
					.getInt("totalDocumentCount"));
		return totalAmount;	
	}
	/**
	 * 자사의 매채별 타입을 경쟁사의 맞게 변환하여 반환한다.
	 */
	private String transformTUSiteTypeToSTSiteType(String trendUpSiteType) {
		String someTreandSiteType = null;
		switch (trendUpSiteType) {
		case "T":
			someTreandSiteType = "twitter";
			break;
		case "I":
			someTreandSiteType = "insta";
			break;
		case "R":
			someTreandSiteType = "blog";
			break;
		case "M":
			someTreandSiteType = "news";
			break;	
		default:
			break;
		}	
		return someTreandSiteType;
	}
	
	/**
	 * 자사의 특정키워드 수집량을 반환한다. 
	 */
	private String extractTrendUpCollectionAmounToKeyword(String keyWord, String trendCookies, String fromDate, String toDate, String trendUpSiteType) {
		Document treandUpDoc = parseTrendUp(TREAND_UP_API_URL, trendCookies, 
				fromDate, toDate, trendUpSiteType, keyWord);
		String treandUpJsonData = treandUpDoc.text();
		String treandUpTotalCount = extractKeywordAmount(treandUpJsonData, TARET_TRENDUP);
		return treandUpTotalCount;
	}
	
	/**
	 * 경쟁사의 특정키워드 수집량을 반환한다. 
	 */
	private String extractSomeTrendCollectionAmounToKeyword(String keyword, String someTrendcookies, String fromDate, String toDate, String someTreandType) {
		Document someTrendDoc = parseSomeTrend(SOMETREAND_UP_API_URL, someTrendcookies, fromDate, toDate, someTreandType, keyword);
		String someTrendData = someTrendDoc.text();
		String someTrendTotalCount = extractKeywordAmount(someTrendData,TARET_SOMETREND);
		return someTrendTotalCount;
	}
	
	/**
	 * 키워드별 특정기간에 수집량을 조사하여 이상치를 확인한다.
	 * 
	 * */
	private void checkCollectionAmount(String keyword, String trendUpSiteType, String fromDate, String toDate, Double targetAmountm, 
			String trendCookies, String someTrendcookies, boolean monthlySurvey) {
			String[][] dataTDA = new String[3][3];
			dataTDA[0][0] = keyword;
			
			String trendUpAmountStr = extractTrendUpCollectionAmounToKeyword(keyword,trendCookies,fromDate,toDate,trendUpSiteType);
			dataTDA[1][1] = trendUpAmountStr;
			
			String someTreandType = transformTUSiteTypeToSTSiteType(trendUpSiteType);
			String someTrendAmountStr = extractSomeTrendCollectionAmounToKeyword(keyword, someTrendcookies, fromDate, toDate, someTreandType);
			dataTDA[1][2] = someTrendAmountStr;
			Double resultAmount = CompareAmount(trendUpAmountStr, someTrendAmountStr);
			
			if(monthlySurvey) {
				if(resultAmount < targetAmountm) {
					String resultFormat = String.format("[확인] 키워드 : %s, 매채 : %s,  수집률: %.2f [trendUp,someTrend]\n %s\n %s",
							dataTDA[0][0], 
							trendUpSiteType,
							resultAmount,
							dataTDA[1][1],
							dataTDA[1][2]
							);
					System.out.println(resultFormat);
				}else {
					String resultFormat = String.format("키워드 : %s, 매채 : %s,  수집률: %.2f [trendUp,someTrend]\n %s\n %s",
							dataTDA[0][0], 
							trendUpSiteType,
							resultAmount,
							dataTDA[1][1],
							dataTDA[1][2]
							);
					System.out.println(resultFormat);
				}
			}else {
				if(resultAmount < targetAmountm) {
					String resultFormat = String.format("[확인] 키워드 : %s, 매채 : %s, trendUp : %s, someTrand : %s, 수집률: %.2f",
							dataTDA[0][0], 
							trendUpSiteType,
							dataTDA[1][1],
							dataTDA[1][2],
							resultAmount
							);
					System.out.println(resultFormat);
				}else {
					String resultFormat = String.format("키워드 : %s, 매채 : %s, trendUp : %s, someTrand : %s, 수집률: %.2f",
							dataTDA[0][0], 
							trendUpSiteType,
							dataTDA[1][1],
							dataTDA[1][2],
							resultAmount
							);
					System.out.println(resultFormat);
				}	
			}

	}
		
	/**
	 * 자사의 키워드 수집량과 경쟁사 수집량을 비교하여 수집률을 반환한다.
	 */
	private Double CompareAmount(String trendUpAmountStr, String someTrendAmountStr) {
		Double resultAmount = 0D;
		
		Double trendUpAmountDbl= Double.valueOf(trendUpAmountStr);
		Double someTrendAmountDbl = Double.valueOf(someTrendAmountStr);
		
		resultAmount = Double.valueOf(String.format("%.2f", (trendUpAmountDbl/someTrendAmountDbl) * 100));
		
		return resultAmount;
	}
	
	public static void main(String[] args) {
		SomeTrendCollectionAmountProgram pr = new SomeTrendCollectionAmountProgram();
		boolean isMonthlySurvey = false;
		String fromDate = "20240621";
		String toDate = "20240627";
		String trendUpSiteType = "I"; //insta, twitter, news, blog
		Double targetAmountm = 60D;
		String trendCookies = "JSESSIONID=8F9FFB00B211CB8FF5137A1CB555E2F8";
		String someTrendcookies = "_fwb=24A8gpRmyJullEPEgzxiz.1717554454724; _gcl_au=1.1.795220251.1717554455; _ga=GA1.1.2124567963.1717554455; _fbp=fb.2.1717554455467.247077424297125882; _hjSessionUser_3745614=eyJpZCI6ImQzYmQ0MThmLTlmODctNTRjYi05NGIwLWRjMzYyODIzMGYwNSIsImNyZWF0ZWQiOjE3MTc1NTQ0NTU1MTIsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_3745614=eyJpZCI6IjI3MWRkMjg1LTFjMGItNDdiNC1iNmFkLTUwMzlhNzljYzc0MSIsImMiOjE3MjA0MjM3NDI3OTgsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; OAuth_Token_Request_State=7a7a9575-9d42-4330-9220-e63dcfc632ff; SESSION=MzU2MjRkMzktNzA0YS00Y2EyLWIzMjktMGFiMmRkZDk1YjE5; wcs_bt=s_29fb873cc748:1720423786; JSESSIONID=C02C504C2DC497975A96833205F64E71; _ga_72PTYVSTQR=GS1.1.1720423742.16.1.1720423790.12.0.0";
		List<String> keywordList = pr.readKeywordList();
		
		if(isMonthlySurvey) {
			String someTreandType = pr.transformTUSiteTypeToSTSiteType(trendUpSiteType);
			String JsonData = pr.parseSomeTrend(pr.SOMETREAND_UP_API_URL, someTrendcookies, fromDate, toDate, someTreandType, keywordList.get(0)).text();
			String someTreandTotalCount = pr.extractTotalAmount(JsonData);
			System.out.println("someTreandTotalCount: " + someTreandTotalCount);
		}
		
		for(String keyword : keywordList) {			
			pr.checkCollectionAmount(keyword, trendUpSiteType, fromDate, toDate, targetAmountm, trendCookies, someTrendcookies, isMonthlySurvey);
		}

	}

}
