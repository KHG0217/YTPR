package youtube.service;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.ImageInputStreamImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import youtube.YouTubeInfluencerData;
import youtube.dao.YouTubeInfluencerCrawlerDao;


@Controller
public class YouTubeInfluencerCrawlerServiceImpl implements YouTubeInfluencerCrawlerService {

	@Autowired
	private YouTubeInfluencerCrawlerDao dao;
	
	@Override
	public int insertYouTubeChannel(YouTubeInfluencerData entity) throws DataAccessException {	
		return dao.insertYouTubeChannel(entity);
	}

	@Override
	public List<YouTubeInfluencerData> selectNotDuplicatedYouTubeChannelList() throws DataAccessException {
		return dao.selectNotDuplicatedYouTubeChannelList();
	}

	@Override
	public List<YouTubeInfluencerData> selectYouTubeChannelAll() throws DataAccessException {
		List<YouTubeInfluencerData> youTubeList = new ArrayList<YouTubeInfluencerData>();
		List<YouTubeInfluencerData> youTubeDuplicationOfList = selectDuplicationYouTubeChannelList();
		List<YouTubeInfluencerData> youTubeNotDuplicationOfList = selectNotDuplicatedYouTubeChannelList();
		String uniqueUrl = "";
		int cnt = 0;
			for(int e = 0; e < youTubeDuplicationOfList.size(); e ++) {
				// 중복인 url을 변환하고 해당하는 url은 읽으면 안됨
				if(!uniqueUrl.equals(youTubeDuplicationOfList.get(e).getUrl())) {
					cnt++;
					uniqueUrl = youTubeDuplicationOfList.get(e).getUrl();							
					youTubeList.add(youTubeDuplicationOfList.get(e));
				}			
			}
		
		youTubeNotDuplicationOfList.forEach(it->{
			youTubeList.add(it);
		});
	
		System.out.println("URL 중복된  Data 수 = " + youTubeDuplicationOfList.size());
		System.out.println("URL 중복값이 없는 Data 수  = " + youTubeNotDuplicationOfList.size());
		System.out.println("URL 중복제거한 후 Data 수  = " + cnt);
		System.out.println("URL 중복값이 제거된 전체 Data 수  = " + youTubeList.size());

		return youTubeList;
	}
	

	@Override
	public List<YouTubeInfluencerData> selectDuplicationYouTubeChannelList() throws DataAccessException {

		return dao.selectDuplicationYouTubeChannelList();
	}

	@Override
	public int updateYouTubeChannelPriority(String url) throws DataAccessException {
		return dao.updateYouTubeChannelPriority(url);
	}

	@Override
	public int updateYouTubeChannelStatus(String url) throws DataAccessException {
		return dao.updateYouTubeChannelStatus(url);
	}

	@Override
	public int updateYouTubeChannelPriorityStatus(String url) throws DataAccessException {
		return dao.updateYouTubeChannelPriorityStatus(url);
	}

}
