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

 */
public class YouTubeInfluencerCrawler {

	int rank = 0;
	TBProxy proxy = new TBProxy();
	
	// 실행 메소드
	public void main() {
		proxy.setIp("");
		proxy.setPort(0);

		for(int i=1; i<2; i++) {

		}
	}
		
	private void parseData(String url) {
		YouTubeInfluencerData data = new YouTubeInfluencerData();
		int follower = 0;
		int listed = 0;
		String site_name = null;
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
				
				/* site_category*/
				siteCategory = el.select("span.category").text();
				System.out.println("siteCategory : " + siteCategory);
				siteCategoryCode =changeSiteCategory(siteCategory);
				System.out.println("siteCategory변환후 : "+ siteCategoryCode);
				data.setSiteCategory(siteCategoryCode);
							
				/* site_name */
				site_name = el.select("span.title").text();	
				
				data.setSiteName(site_name);
				
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
				url = "https://socialerus.com/" + url.substring(17, url.length()-1);
				
				/* channelsDetail Document 생성 */
				// jsoup connect timeout 발생으로 연결이 끊기면, 다시 접속
				Document channelsDoc = null;
				int cnt = 0;
				while (channelsDoc == null) {
					try {
						cnt++;
						if (cnt > 20) {
							break;
						}
						channelsDoc = channelsDetail(url);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// retry by groovy closure
				
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
			break;
			
		case "2":
			break;
			
		case "3":
			break;
			
		case "4":
			break;
			
		case "5":
			break;
			
		case "6":
			break;
			
		case "7":
			break;
			
		case "8":
			break;
			
		case "9":
			break;
			
		case "10":
			break;
			
		case "11":
			break;
			
		case "12":
			break;
			
		case "13":
			break;
			
		case "14":
			break;
			
		case "15":
			break;
			
		case "16":
			break;
			
		case "17":
			break;
			
		case "18":
			break;
			
		case "19":
			break;
			
		case "20":
			break;

		default:
			break;
		}
		
		return siteCategoryCode;
	}
	
	private void saveYouTubeData(YouTubeInfluencerData data) throws IOException {
		
		String filePathYouTubeList = "C:\\Users\\hyukg\\Desktop\\Test.txt";
		String filePathReviewList = "C:\\Users\\hyukg\\Desktop\\review.txt";
		
        File youTubeList = new File(filePathYouTubeList); // File객체 생성
        File reviewList = new File(filePathReviewList);
        
        if(!youTubeList.exists()){ // 파일이 존재하지 않으면
				youTubeList.createNewFile();// 신규생성
        }
        
		BufferedWriter youTubeListWriter = new BufferedWriter(new FileWriter(youTubeList, true));
		BufferedWriter reviewListWriter = new BufferedWriter(new FileWriter(reviewList, true));
		String categoryCi = data.getSiteCategory();
		
		// 검토가 필요한 카테고리나 식별할 수 없는 카테고리는 review.txt에 저장한다.
		// 추후 수동으로 등록
		if(categoryCi.equals("Examine") || categoryCi.equals("NotIdentified")) {
			
	        if(!reviewList.exists()){ // 파일이 존재하지 않으면
	        	reviewList.createNewFile();// 신규생성
        }
	        reviewListWriter.write("[rank:" + data.getRank()+"|||"+"siteName: " + data.getSiteName()+"|||"
					+"follower: " + data.getFollower()+"|||"+"listed: " + data.getListed()+"|||"
	        		+"picture: " + data.getPicture()+"|||"+"url: " + data.getUrl()+"|||"+"siteCategory: " +data.getSiteCategory() +"|||"
					+"bio: " + data.getBio()+"|||"+"views: " + data.getViews()+"]");
	        reviewListWriter.newLine();
	        reviewListWriter.flush(); 
	        
		}else {

			youTubeListWriter.write("[rank:" + data.getRank()+"㏂"+"siteName: " + data.getSiteName()+"㏂"
					+"follower: " + data.getFollower()+"㏂"+"listed: " + data.getListed()+"㏂"
	        		+"picture: " + data.getPicture()+"㏂"+"url: " + data.getUrl()+"㏂"+"siteCategory: " +data.getSiteCategory() +"㏂"
					+"bio: " + data.getBio()+"㏂"+"views: " + data.getViews()+"]");
			youTubeListWriter.newLine();
			youTubeListWriter.flush(); 
			
		}	
		reviewListWriter.close();
	    youTubeListWriter.close(); 
	}

}
