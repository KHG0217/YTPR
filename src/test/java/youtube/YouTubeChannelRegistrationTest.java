package youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;	

import junit.framework.Assert;
public class YouTubeChannelRegistrationTest {
	static YouTubeChannelRegistration youtubeClass = new YouTubeChannelRegistration();
	
	@BeforeClass
	public static void init() {
		youtubeClass.init();
	}
	
	@Test
	public void testCheckPriorityOneExistChannels_StatusF() throws IOException {
		YouTubeInfluencerData testData = new YouTubeInfluencerData();
		String siteName = "최병화"; 
		String url ="https://www.youtube.com/channel/UCMRrZrnVuYElSuMGtAgl6Tw"; // Priority 1 존재 Status F URL
	
		testData.setUrl(url);
		testData.setSiteName(siteName);
		
		String result= youtubeClass.changeStateYouTubeChannels(testData);
		Assert.assertEquals("2", result);

	}

	@Test
	public void testCheckPriorityOneExistChannels_StatusT() throws IOException {
		YouTubeInfluencerData testData = new YouTubeInfluencerData();
		String siteName ="한베커플 [윙과훈]"; 	
		String url ="https://www.youtube.com/channel/UCD5phvsrtYpaiuUvHKR0JNA"; // Priority 1 존재 Status T URL
		
		testData.setUrl(url);
		testData.setSiteName(siteName);
		
		String result= youtubeClass.changeStateYouTubeChannels(testData);
		Assert.assertEquals("3", result);

		
	}
	
	@Test
	public void testCheckPriorityTwoExistChannels() throws IOException {
		YouTubeInfluencerData testData = new YouTubeInfluencerData();
		String siteName = "쓸마"; 	
		String url ="https://www.youtube.com/channel/UC97St-7e4M46-alaomhgwUA"; // Priority 2 존재 Status T URL
		
		testData.setUrl(url);
		testData.setSiteName(siteName);
		
		String result= youtubeClass.changeStateYouTubeChannels(testData);
		Assert.assertEquals("1", result);
				
	}
	
	@Test
	public void testCheckNotExistChannels() throws IOException {
		YouTubeInfluencerData testData = new YouTubeInfluencerData();
		String siteName = "NotExist"; 	
		String url ="https://www.youtube.com/channel/UCsdsd97St-7e4M46-alaomhgwUA"; // NotExist URL
		
		testData.setUrl(url);
		testData.setSiteName(siteName);
		
		String result= youtubeClass.changeStateYouTubeChannels(testData);
		Assert.assertEquals("4", result);
		
	}
	
	@Test
	public void testCreateLog() {
		List<YouTubeInfluencerData> testList = new ArrayList<YouTubeInfluencerData>();
		YouTubeInfluencerData testData = new YouTubeInfluencerData();
		YouTubeInfluencerData testData2 = new YouTubeInfluencerData();
		YouTubeInfluencerData testData3 = new YouTubeInfluencerData();
		YouTubeInfluencerData testData4 = new YouTubeInfluencerData();
		
		String siteName = "test2"; 	
		String url ="test2"; // NotExist URL
		String siteId = "123";
		
		testData.setUrl(url);
		testData.setSiteName(siteName);
		testData.setSiteId(siteId);
		testData.setText("1");
		testList.add(testData);
		
		testData2.setUrl(url);
		testData2.setSiteName(siteName);
		testData2.setSiteId(siteId);
		testData2.setText("2");
		testList.add(testData2);
		
		testData3.setUrl(url);
		testData3.setSiteName(siteName);
		testData3.setSiteId(siteId);
		testData3.setText("3");
		testList.add(testData3);
		
		testData4.setUrl(url);
		testData4.setSiteName(siteName);
		testData4.setSiteId(siteId);
		testData4.setText("4");
		testList.add(testData4);
			
		try {
			youtubeClass.createLog(testList);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
