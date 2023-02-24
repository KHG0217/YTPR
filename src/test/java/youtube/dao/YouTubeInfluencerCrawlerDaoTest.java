package youtube.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import youtube.YouTubeInfluencerData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
public class YouTubeInfluencerCrawlerDaoTest {
	@Autowired
	private YouTubeInfluencerCrawlerDao dao;
	
	@Test
	public void testInsertYouTubeChannel() {
		YouTubeInfluencerData testData = new YouTubeInfluencerData();
		testData.setSiteName("testtest");
		testData.setListed(1);
		testData.setUrl("http:styswefw");
		testData.setSiteCategory("1234");
		testData.setPicture("testtest");
		testData.setBio("test입니다.");
		testData.setFollower(1234);
		
		int result = dao.insertYouTubeChannel(testData);
		System.out.println(result);
		
	}
	
	@Test
	public void testSelectDuplicationYouTubeChannelList() {
		List<YouTubeInfluencerData> list = dao.selectDuplicationYouTubeChannelList();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() > 0);
	}
	
}
