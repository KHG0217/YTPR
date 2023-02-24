package youtube.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import youtube.YouTubeInfluencerData;

public interface YouTubeInfluencerCrawlerDao {
	
	int insertYouTubeChannel(YouTubeInfluencerData entity) throws DataAccessException;	
	List<YouTubeInfluencerData> selectDuplicationYouTubeChannelList() throws DataAccessException;
	List<YouTubeInfluencerData> selectNotDuplicatedYouTubeChannelList() throws DataAccessException;
	int updateYouTubeChannelPriority(String url) throws DataAccessException;
	int updateYouTubeChannelStatus(String url) throws DataAccessException;
	int updateYouTubeChannelPriorityStatus(String url) throws DataAccessException;
}
