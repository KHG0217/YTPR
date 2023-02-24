package youtube.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import youtube.YouTubeInfluencerData;

@Repository
public class YouTubeInfluencerCrawlerDaoImpl implements YouTubeInfluencerCrawlerDao {
	@Resource(name = "sqlSessionTemplate3")
	private SqlSession sqlSession;
	
	@Override
	public int insertYouTubeChannel(YouTubeInfluencerData entity) throws DataAccessException {
		return sqlSession.insert("sql.resources.youtubeinfluencerdao.insertYouTubeChannel", entity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<YouTubeInfluencerData> selectDuplicationYouTubeChannelList() throws DataAccessException {
		return sqlSession.selectList("sql.resources.youtubeinfluencerdao.selectDuplicationYouTubeChannelList");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<YouTubeInfluencerData> selectNotDuplicatedYouTubeChannelList() throws DataAccessException {
		return sqlSession.selectList("sql.resources.youtubeinfluencerdao.selectNotDuplicatedYouTubeChannelList");
	}

	@Override
	public int updateYouTubeChannelPriority(String url) throws DataAccessException {
		return sqlSession.update("sql.resources.youtubeinfluencerdao.updateYouTubeChannelPriority",url);
	}

	@Override
	public int updateYouTubeChannelStatus(String url) throws DataAccessException {
		return sqlSession.update("sql.resources.youtubeinfluencerdao.updateYouTubeChannelStatus",url);
	}

	@Override
	public int updateYouTubeChannelPriorityStatus(String url) throws DataAccessException {
		return sqlSession.update("sql.resources.youtubeinfluencerdao.updateYouTubeChannelPriorityStatus",url);
	}

}
