package com.khg.work;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tapacross.sns.util.DateUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
        	String url = "http://tu4.trendup.co.kr/wdata/w202";
        	String type = "T";
        	String from ="20240601";
        	String to ="20240613";
        	String keyword = "뉴스";
        	String encodeKeyword = URLEncoder.encode(keyword,"utf-8"); 
        	String requestBody = "dashId=4765"
        			+ "&media%5B%5D="
        			+ type
        			+ "&from="
        			+ from
        			+ "&to="
        			+ to
        			+ "&dateType=date&size=100&offset=0&queryType=&publisher=A&groupCode="
        			+ "&search="
        			+ encodeKeyword
        			+ "&exclude=&filter=&hash=&prox=-1";
        	String cookie = "JSESSIONID=45E319724693FE3979589FC3799A2AC1";
        	Document doc = Jsoup.connect(url)
            .header("Accept", "application/json, text/javascript, */*; q=0.01")
            .header("Accept-Encoding", "gzip, deflate")
            .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
            .header("Connection", "keep-alive")
            .header("Content-Length", "174")
            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .header("Cookie", cookie)
            .header("Host", "tu4.trendup.co.kr")
            .header("Origin", "http://tu4.trendup.co.kr")
            .header("Referer", "http://tu4.trendup.co.kr/trend/brand")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
            .header("X-Requested-With", "XMLHttpRequest")
            .requestBody(requestBody)
            .ignoreContentType(true)
            .post();   	
        	System.out.println(doc);
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

    }
}
