package youtube;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class YouTubeInfluencerCrawlerMain {

	public static void main(String[] args) {
//		YouTubeInfluencerCrawler youtubeClass = new YouTubeInfluencerCrawler();
//		youtubeClass.main();
		List<String> list=addListSiteCategory();
		printCategoryCnt(list);
	}
	
	private static List<String> addListSiteCategory () {
		List<String> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(
					 new FileReader("C:\\Users\\hyukg\\Desktop\\Test1.txt"), // File Read
					 16 * 1024				 
			 );
			String str;
			 while((str = reader.readLine()) != null) {
				 String date[] = str.split("\\|\\|\\|");
				 for(int i = 0; i< date.length; i++) {
					 int idx = date[i].indexOf(":");
					switch (date[i].substring(0,idx)) {
					case "site_category": // 동작후 siteCategory 변경 
						String siteCategory = date[i].substring(idx+1)
						.replaceAll("=", "")
						.trim();
						list.add(siteCategory);
						
						break;	
						
					default:
						break;
				
					}
				 }
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static void printCategoryCnt(List<String> list) {
		int livingCnt =0;
		int enterCnt =0;
		int tripCnt =0;
		int sportsCnt =0;
		int broadcastCnt =0;
		int foodCnt =0;
		int itCnt =0;
		int carCnt =0;
		int financeCnt=0;
		int ExamineCnt=0;
		
		for(String data : list) {
			switch (data) {
			case "1017":
				livingCnt ++;
				break;
			case "1020":
				enterCnt ++;
				break;
			case "1004":
				tripCnt++;
				break;
			case "1005":
				sportsCnt++;
				break;
			case "1012":
				broadcastCnt++;
				break;
			case "1006":
				foodCnt++;
				break;
			case "1011":
				itCnt++;
				break;
			case "1010":
				carCnt++;
				break;
			case "1002":
				financeCnt++;
				break;
			case "Examine":
				ExamineCnt++;
				break;

			default:
				System.out.println(data);
				break;
			}
		}
		int total =livingCnt+enterCnt+tripCnt+sportsCnt+broadcastCnt+
				foodCnt+itCnt+carCnt+financeCnt+ExamineCnt;
		System.out.println(
				"livingCnt = " +livingCnt+System.lineSeparator()+
				"enterCnt = " +enterCnt+System.lineSeparator()+
				"tripCnt = " +tripCnt+System.lineSeparator()+
				"sportsCnt = " +sportsCnt+System.lineSeparator()+
				"broadcastCnt = " +broadcastCnt+System.lineSeparator()+
				"foodCnt = " +foodCnt+System.lineSeparator()+
				"itCnt = " +itCnt+System.lineSeparator()+
				"carCnt = " +carCnt+System.lineSeparator()+
				"financeCnt = " +financeCnt+System.lineSeparator()+
				"ExamineCnt = " +ExamineCnt+System.lineSeparator()+
				"total = "+total
				);
		
		
	}

}
