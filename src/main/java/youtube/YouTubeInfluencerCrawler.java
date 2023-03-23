package youtube;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.tapacross.sns.entity.TBProxy;
import com.tapacross.sns.parser.JSoupFactory;
import com.tapacross.sns.util.ThreadUtil;

/**
 * 
 * @author hyukg
 * @since 2023-02-21
 * @See 소셜러스의 유튜브 순위(1~6000)를 수집하여 파일로 저장한다.
 *
 */
public class YouTubeInfluencerCrawler {

	int rank = 0;
	TBProxy proxy = new TBProxy();
	
	// 실행 메소드
	public void main() {
		proxy.setIp("49.238.161.152");
		proxy.setPort(23545);

		for(int i=1; i<302; i++) {
			String url = "";
			parseData(url);
		}
	}
		
	private void parseData(String url) {
		YouTubeInfluencerData data = new YouTubeInfluencerData();
		int follower = 0;
		int listed = 0;
		String siteName = null;
		String picture = null;
		String bio = null;
		String views = null;
		String siteCategory = null;
		String siteCategoryCode;
		try {
			Document doc = JSoupFactory.getJsoupSecureConnection(url)
					.proxy(this.proxy.getIp(), this.proxy.getPort())
					.timeout(0)
					.get();
			ThreadUtil.sleep(5 *1000);
		
			Elements els = doc.select("ul.list_ranking > li");
			
			for (Element el : els) {	
				/* rank */
				rank ++;
				data.setRank(rank);
				
				/* siteCategory*/
				siteCategory = el.select("span.category").text();
				System.out.println("siteCategory : " + siteCategory);
				siteCategoryCode =changeSiteCategory(siteCategory);
				System.out.println("siteCategory변환후 : "+ siteCategoryCode);
				data.setSiteCategory(siteCategoryCode);
							
				/* siteName */
				siteName = el.select("span.title").text();	
				
				data.setSiteName(siteName);
				
				/* follower */
				follower = Integer.parseInt(
						el.select("span.num.subscriber").text()
						.replaceAll("\\,", "")			
						.replaceAll(",", "")
						);
				data.setFollower(follower);
				
				/* Listed */
				listed = Integer.parseInt(						
						el.select("span.num.video").text()
						.replaceAll("\\,", "")	
						.replaceAll(",", "")
						);
				data.setListed(listed);
				
				/* Picture */
				picture = el.select("span.thumb_img > img").attr("src");
				data.setPicture(picture);
				
				/* url */
				url = el.select("div.ranking_info").attr("onclick");
				url = "" + url.substring(17, url.length()-1);
				
				/* channelsDetail Document 생성 */
				// jsoup connect timeout 발생으로 연결이 끊기면, 다시 접속(100번 시도)
				Document channelsDoc = null;
				int cnt = 0;
				while (channelsDoc == null) {
					try {
						cnt++;
						if (cnt > 100) {
							break;
						}
						channelsDoc = channelsDetail(url);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				url = crawlChannelsUrl(channelsDoc);
				data.setUrl(url);
				
				/* bio */
				bio = crawlChannelsBio(channelsDoc)
						.replaceAll(",", "")
						;
				data.setBio(bio);
				
				/* views */
				views = el.select("span.num.views").text()
						.replaceAll(",", "")
						;
				data.setViews(views);							
				System.out.println(data.toString());
				saveYouTubeData(data); // 데이터를 파일에 저장				      
			}
		} catch (IOException e) {
			e.printStackTrace();
		}      
	}
	
	private Document channelsDetail(String url) {
		Document doc = null;
		try {
			doc = JSoupFactory.getJsoupSecureConnection(url)
					.proxy(this.proxy.getIp(), this.proxy.getPort())
					.timeout(0)
					.get();
			ThreadUtil.sleep(5 *1000);
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private String crawlChannelsUrl(Document doc) {
		
		String channelsUrl = doc.select("div.btn_business.group > a").attr("href");	
		return channelsUrl;
	}
	
	private String crawlChannelsBio(Document doc) {
		String bio = doc.select("div.channel_detail_txt").text();			
		return bio;
	}		
	
	private String changeSiteCategory (String siteCategory) {
		String siteCategoryCode=null;
		
		switch (siteCategory) {
		case "1":
			siteCategoryCode = "1017";		
			break;
			
		case "2":
			siteCategoryCode = "1020";
			break;
			
		case "3":
			siteCategoryCode = "Examine";
			break;
			
		case "4":
			siteCategoryCode = "1020";
			break;
			
		case "5":
			siteCategoryCode = "1020";
			break;
			
		case "6":
			siteCategoryCode = "1020";
			break;
			
		case "7":
			siteCategoryCode = "1004";
			break;
			
		case "8":
			siteCategoryCode = "1005";
			break;
			
		case "9":
			siteCategoryCode = "1012";
			break;
			
		case "10":
			siteCategoryCode = "1012";
			break;
			
		case "11":
			siteCategoryCode = "1020";
			break;
			
		case "12":
			siteCategoryCode = "1006";
			break;
			
		case "13":
			siteCategoryCode = "1012";
			break;
			
		case "14":
			siteCategoryCode = "1011";
			break;
			
		case "15":
			siteCategoryCode = "1020";
			break;
			
		case "16":
			siteCategoryCode = "1020";
			break;
			
		case "17":
			siteCategoryCode = "1010";
			break;
			
		case "18":
			siteCategoryCode = "1002";
			break;
			
		case "19":
			siteCategoryCode = "1012";
			break;
			
		case "20":
			siteCategoryCode = "Examine";
			break;

		default:
			siteCategoryCode = "NotIdentified";
			break;
		}
		
		return siteCategoryCode;
	}
	
	private void saveYouTubeData(YouTubeInfluencerData data) throws IOException {
		
		String filePathYouTubeList = "C:\\Users\\hyukg\\Desktop\\YouTubeData.txt";
		String filePathReviewList = "C:\\Users\\hyukg\\Desktop\\YouTubeData_Check.txt";
		
        File youTubeList = new File(filePathYouTubeList);
        File checkList = new File(filePathReviewList);
        
        if(!youTubeList.exists()){ 
				youTubeList.createNewFile();
        }
        
		BufferedWriter youTubeListWriter = new BufferedWriter(new FileWriter(youTubeList, true));
		BufferedWriter reviewListWriter = new BufferedWriter(new FileWriter(checkList, true));
		String categoryCi = data.getSiteCategory();
		
		// 검토가 필요한 카테고리나 식별할 수 없는 카테고리는 YouTubeData_Check.txt에 저장한다.
		// 추후 수동으로 등록
		if(categoryCi.equals("Examine") || categoryCi.equals("NotIdentified")) {
			
	        if(!checkList.exists()){ 
	        	checkList.createNewFile();
        }
	        // ㏂ 을통해 엑셀해서 데이터를 분활한다.
	        // 엑셀로 복사 붙여넣기 후 ->데이터-> 텍스트 나누기 -> 구분기호로 분리됨 -> 기타 ㏂-> 열데이터서식[텍스트]
	        // 추후 =CONCATENATE 함수를 통해 YouTubeChannelRegistration에서 읽을 수 있는 형식으로 치환한다. (=CONCATENATE(B2,"|||",C2,"|||",D2...)
	        reviewListWriter.write("[rank:" + data.getRank()+"㏂"+"siteName: " + data.getSiteName()+"㏂"
					+"follower: " + data.getFollower()+"㏂"+"listed: " + data.getListed()+"㏂"
	        		+"picture: " + data.getPicture()+"㏂"+"url: " + data.getUrl()+"㏂"+"siteCategory: " +data.getSiteCategory() +"㏂"
					+"bio: " + data.getBio()+"㏂"+"views: " + data.getViews()+"]");
	        reviewListWriter.newLine();
	        reviewListWriter.flush(); 
	        
		}else {
			// YouTubeChannelRegistration 식별하는 구분자 |||
			youTubeListWriter.write("[rank:" + data.getRank()+"|||"+"siteName: " + data.getSiteName()+"|||"
					+"follower: " + data.getFollower()+"|||"+"listed: " + data.getListed()+"|||"
	        		+"picture: " + data.getPicture()+"|||"+"url: " + data.getUrl()+"|||"+"siteCategory: " +data.getSiteCategory() +"|||"
					+"bio: " + data.getBio()+"|||"+"views: " + data.getViews()+"]");
			youTubeListWriter.newLine();
			youTubeListWriter.flush(); 
			
		}	
		reviewListWriter.close();
	    youTubeListWriter.close(); 
	}

}
