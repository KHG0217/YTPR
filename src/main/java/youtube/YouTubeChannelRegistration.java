package youtube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;


import youtube.service.YouTubeInfluencerCrawlerServiceImpl;

	
/**
 * 텍스트 파일을 읽어 
 * YouTube 수집원 DB를 조회한뒤
 * 중복을 제거하고
 * 조건에 맞게 상태를 변환 혹은 추가한다.
 *  
 * @author hyukg
 * @since 2023-02-21
 *
 */
public class YouTubeChannelRegistration {
	private GenericXmlApplicationContext applicationContext;
	private YouTubeInfluencerCrawlerServiceImpl service;
	List<YouTubeInfluencerData> youTubeDBDataList;
	List<YouTubeInfluencerData> youTubeDataLogList = new ArrayList<>();
	
	public void init() {
		applicationContext = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");
		service = applicationContext.getBean(YouTubeInfluencerCrawlerServiceImpl.class);			
//		/* DB youTube Data ( service에서 중복을 제거)*/
		 youTubeDBDataList = service.selectYouTubeChannelAll();		
	}
	
	public void main() {
		System.out.println("Start");
		 List<YouTubeInfluencerData> readYouTubelist = readYouTubeChannels();
		 
		try {
			 for(YouTubeInfluencerData readYouTubeData: readYouTubelist) {
				 changeStateYouTubeChannels(readYouTubeData);
				 
			 }
			 createLog(youTubeDataLogList);
			 System.out.println("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public List<YouTubeInfluencerData> readYouTubeChannels() {
		List<YouTubeInfluencerData> list = new ArrayList<>();
		 List<YouTubeInfluencerData> removed = new ArrayList<>();
		 try {
			BufferedReader reader = new BufferedReader(
					/* 
					 * 파일 형식(줄바꿈 없이 한줄로)
						 [rank: "숫자"|||siteName: "사이트이름"|||follower: "숫자"|||listed: "숫자"|||
		        	     picture: "사진주소"|||url: "url"|||siteCategory:"맵핑된 카테고리 숫자+"|||
						 bio: "유튜브채널정보 설명글"|||"views: " +숫자]
						 
						 ex) [rank:1|||siteName: BLACKPINK|||follower: 84400000|||listed: 475|||
						 picture: https://yt3.ggpht.com/hZDUwjoeQqigphL4A1tkg9c6hVp5yXmbboBR7PYFUSFj5PIJSA483NB5v7b0XVoTN9GCku3tqQ=s800-c-k-c0x00ffffff-no-nd-rj
						 |||url: https://www.youtube.com/channel/UCOmHUn--16B90oW2L6FRR3A
						 |||site_category: 1020
						 |||bio: BLACKPINK Official YouTube Channel 블랙핑크 공식 유튜브 채널입니다. JISOO JENNIE ROSÉ LISA 지수 제니 로제 리사
						 |||views: 29142830344]
					  */

					 new FileReader("C:\\Users\\hyukg\\Desktop\\YouTubeData.txt"), // Read file
					 16 * 1024				 
			 );
			
			String str;
			 while((str = reader.readLine()) != null) {
				YouTubeInfluencerData data = new YouTubeInfluencerData();
				 String date[] = str.split("\\|\\|\\|");
				
				 for(int i = 0; i< date.length; i++) {
				 
					int idx = date[i].indexOf(":");
					switch (date[i].substring(0,idx)) {	
					case "[rank":
						int rank = Integer.parseInt(
								date[i].substring(idx+1)
								.replaceAll("[^0-9]", "")
								.trim()
								);
						data.setRank(rank);							
						break;
					
					case "siteName":
						String siteName = date[i].substring(idx+1)
								.trim();
						data.setSiteName(siteName);							
						break;
						
					case "follower":
						int follower = Integer.parseInt(
								date[i].substring(idx+1)
								.replaceAll("[^0-9]", "")
								.trim()
								);
						data.setFollower(follower);						
						break;
						
					case "listed":
						int listed = Integer.parseInt(
								date[i].substring(idx+1)
								.replaceAll("[^0-9]", "")
								.trim()
								);
						data.setListed(listed);						
						break;
						
					case "picture":
						String picture = date[i].substring(idx+1)
						.replaceAll("=", "")
						.trim();
						data.setPicture(picture);						
						break;
						
					case "url":
						String url = date[i].substring(idx+1)
						.replaceAll("=", "")
						.trim();
						data.setUrl(url);						
						break;
						
					case "bio":
						String bio = date[i].substring(idx+1)
						.replaceAll("=", "")
						.trim();
						data.setBio(bio);					
						break;	
						
					case "siteCategory":
						String siteCategory = date[i].substring(idx+1)
						.replaceAll("=", "")
						.trim();
						data.setSiteCategory(siteCategory);				
						break;	
						
					default:
						break;
					}
					

				 }
				 System.out.println(data.toString());

				 /*
				  * 읽은 파일의 동일한 URL이 있다면 처음 읽은 URL데이터만 list에 추가
				  * 중복되는 데이터는 removed list에 넣어 제거
				  * */
				 for(YouTubeInfluencerData existData : list) {
					 if(existData.getUrl().equals(data.getUrl())) { // 읽을 파일에 URL이 중복일 경우 삭제할 리스트에 추가
						 removed.add(data); 
					 }
				 }
				 list.add(data); // list에 우선 추가한 뒤
				 list.removeAll(removed); // 삭제

			 }
			    reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
		 
	}
		
	/*
	 * 중복으로 들어가있는 채널을 제거하여 목록을 가져옴
	 * 중복선정 여부는 site_id가 높은수(최신등록)을 기준으로 선별 
	 * 상태를 업데이트하는 코드순서 중요 Priority,Status -> Priority -> Status -> insert -> fail
	 * Priority 2 ->1 / Status F -> T / New Data Insert
	 * */
	public String changeStateYouTubeChannels(YouTubeInfluencerData readYouTubeData) throws IOException {	
	
		String result = "";
		
		for(YouTubeInfluencerData data : youTubeDBDataList ) {

			if(data.getUrl().equals(readYouTubeData.getUrl())) {
				//  Priority 2번 , Status가 F일 경우 -> 1번, T 변경
				if(data.getPriority() == 2 && data.getStatus().equals("F")) {
					updateYouTubeChannelPriorityStatus(data.getUrl());
					System.out.println("updateYouTubeChannelPriorityStatus : " + data.getSiteName());
					result = "0";
					data.setLog(result);
					youTubeDataLogList.add(data);
					return result;
										
				}						
				// Priority 2번일경우 1로 변경
				if(data.getPriority() == 2) {
					// Priority 2번을 1번으로 바꿔주는 로직 
					updateYouTubeChannelPriority(data.getUrl());
					System.out.println("updateYouTubeChannelPriority : " + data.getSiteName());
					result = "1";
					data.setLog(result);
					youTubeDataLogList.add(data);

					return result;
				}
				
				// Status가 F일 경우 T로 변경 -> pri 1 f인 data
				if(data.getStatus().equals("F")) {
					// T로 바꿔주는 로직 
					updateYouTubeChannelStatus(data.getUrl());
					System.out.println("updateYouTubeChannelStatus : " + data.getSiteName());
					result = "2";
					data.setLog(result);
					youTubeDataLogList.add(data);
					return result;
				}
				
				if(data.getStatus().equals("T") && data.getPriority() == 1) {
					result = "3";
					data.setLog(result);
					youTubeDataLogList.add(data);
					System.out.println("WorkingData : " + data.getSiteName());
					return result;
				}				
			}
		}
		
		// insert 문 
		try {
			result = "4";
			readYouTubeData.setLog(result);
			insertNewYouTubeChannel(readYouTubeData);	
			System.out.println("insertNewYouTubeChannel : " + readYouTubeData.getSiteName());
			youTubeDataLogList.add(readYouTubeData);
			return result;
		}catch (Exception e) {
			result ="5";
			e.printStackTrace();
			// fail log 남겨주기 
		}

		return result;
	}
	
	/**
	 * changeStateYouTubeChannels 메소드를 실행하여 DB에 변경된 유형에 따라 result 값을 받아온후
	 * (UpdatePriorityStatus - 0, UpdatePriority - 1, UpdateStatus - 2, NotUpdate - 3, Insert - 4, ERROR - 5)
	 * result값 에 따라 로그파일을 생성한다.
	 * 해당경로 C:\home\YouTubeLog\ 폴더에 년월일_시분초 이름의 폴더를 생성후
	 * DB 변경된 유형에 해당하는 로그를 생성한다.
	 * @param list
	 * @throws IOException
	 */
	public void createLog(List<YouTubeInfluencerData> list) throws IOException {
		BufferedWriter changePriorityStatusWriter = null;
		BufferedWriter changePriorityWriter = null;
		BufferedWriter changeStatusWriter = null;
		BufferedWriter workingDataWriter = null;
		BufferedWriter newDataWriter = null;
		BufferedWriter errorDataWriter = null;
		
		int changePriorityStatusCnt = 0;
		int changePriorityCnt = 0;
		int changeChangeStatusCnt = 0;
		int changeWorkingDataCnt = 0;
		int newDataCnt = 0;
		int errorDataDataCnt = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("YYMMdd_HHmmss");
		Date time = new Date();
		String fmtDate=format.format(time);
		
		String mkPath = "C:\\home\\YouTubeLog\\";
		String fileName = "";
				
		File folder = new File(mkPath);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		String mkPath2 =mkPath+fmtDate+"\\";
		File folder2 = new File(mkPath2);
		if(!folder2.exists()) {
			folder2.mkdir();
		}
				
		for(YouTubeInfluencerData data : list) {
		
			switch (data.getLog()) {
			case "0":
				fileName ="ChangePriorityAndStatus";
				String filePathChangePriorityStatus = mkPath2 + fileName+".txt";
				File ChangePriorityStatus = new File(filePathChangePriorityStatus);
				
		        if(!ChangePriorityStatus.exists()){ 
		        	ChangePriorityStatus.createNewFile();
		        }
		        
		        changePriorityStatusWriter = new BufferedWriter(new FileWriter(ChangePriorityStatus, true));
		        changePriorityStatusWriter.write("[siteName = " + data.getSiteName()+","+
							        		"url = " + data.getUrl()+","+
							        		"siteId = " + data.getSiteId()+"]");
		        changePriorityStatusCnt++;
		        
		        changePriorityStatusWriter.newLine();
		        changePriorityStatusWriter.flush(); 
		        changePriorityStatusWriter.close();
		        
		        
				break;
			case "1":
				fileName ="ChangePriority";
				String filePathChangePriority = mkPath2 + fileName+".txt";
				File ChangePriority = new File(filePathChangePriority);
				
		        if(!ChangePriority.exists()){ 
		        	ChangePriority.createNewFile();
		        }
		        
		        changePriorityWriter = new BufferedWriter(new FileWriter(ChangePriority, true));
		        changePriorityWriter.write("[siteName = " + data.getSiteName()+","+
		        							"url = " + data.getUrl()+","+
		        							"siteId = " + data.getSiteId()+"]");
		        changePriorityCnt++;
		        
		        changePriorityWriter.newLine();
		        changePriorityWriter.flush(); 
		        changePriorityWriter.close();
		        
		        
				break;
				
			case "2":
				fileName ="ChangeStatus";
				String filePathChangeStatusList = mkPath2 + fileName+".txt";
				File ChangeStatus = new File(filePathChangeStatusList);
				
		        if(!ChangeStatus.exists()){ 
		        	ChangeStatus.createNewFile();
		        }
		        
		        changeStatusWriter = new BufferedWriter(new FileWriter(ChangeStatus, true));
		        changeStatusWriter.write("[siteName = " + data.getSiteName()+ ","+
		        		"url = " + data.getUrl()+","+
		        		"siteId = " + data.getSiteId()+"]");
		        
		        changeChangeStatusCnt++;
		        
		        changeStatusWriter.newLine();
		        changeStatusWriter.flush(); 
		        changeStatusWriter.close();
		        
				break;
				
			case "3":
				fileName ="WorkingData";
				String filePathWorkingDataList = mkPath2 + fileName+".txt";
				File WorkingData = new File(filePathWorkingDataList);
				
		        if(!WorkingData.exists()){ 
		        	WorkingData.createNewFile();
		        }
		        
		        workingDataWriter = new BufferedWriter(new FileWriter(WorkingData, true));
		        workingDataWriter.write("[siteName = " + data.getSiteName()+ ","+
		        		"url = " + data.getUrl()+","+
		        		"siteId = " + data.getSiteId()+"]");
		        
		        changeWorkingDataCnt++;
		        
		        workingDataWriter.newLine();
		        workingDataWriter.flush(); 
		        workingDataWriter.close();
		        
				break;
				
			case "4":
				fileName ="NewData";
				String filePathNewDataList =  mkPath2 + fileName+".txt";
				File NewData = new File(filePathNewDataList);
				
		        if(!NewData.exists()){ 
		        	NewData.createNewFile();
		        }
		        
		        newDataWriter = new BufferedWriter(new FileWriter(NewData, true));
		        newDataWriter.write("[siteName = " + data.getSiteName()+ ","+
		        		"url = " + data.getUrl()+"]");
		        
		        newDataCnt++;
		        
		        newDataWriter.newLine();
		        newDataWriter.flush(); 
		        newDataWriter.close();
				break;
			case "5":
				fileName ="ErrorData";
				String filePathErrorDataList =  mkPath2 + fileName+".txt";
				File ErrorData = new File(filePathErrorDataList);
				
		        if(!ErrorData.exists()){ 
		        	ErrorData.createNewFile();
		        }
		        
		        errorDataWriter = new BufferedWriter(new FileWriter(ErrorData, true));
		        errorDataWriter.write("[siteName = " + data.getSiteName()+ ","+
		        		"url = " + data.getUrl()+","+
		        		"siteId = " + data.getSiteId()+"]");
		        
		        errorDataDataCnt++;
		        
		        errorDataWriter.newLine();
		        errorDataWriter.flush(); 
		        errorDataWriter.close();
				break;
			default:
				break;
			}
		}
		System.out.println(
				"changePriorityStatusCnt = "+changePriorityStatusCnt+"\r\n" +
				"changePriorityCnt = "+changePriorityCnt+"\r\n" + 
				"changeChangeStatusCnt = "+changeChangeStatusCnt+"\r\n" +
				"WorkingDataCnt = "+changeWorkingDataCnt+"\r\n" +
				"NewDataCnt = "+newDataCnt+"\r\n" +
				"errorDataDataCnt =" +errorDataDataCnt
				);


	}
	private void insertNewYouTubeChannel(YouTubeInfluencerData data) {
		service.insertYouTubeChannel(data);		
	}
	
	private void updateYouTubeChannelPriority(String url) {
		service.updateYouTubeChannelPriority(url);
	}
	
	private void updateYouTubeChannelStatus(String url) {
		service.updateYouTubeChannelStatus(url);
	}
	
	private void updateYouTubeChannelPriorityStatus(String url) {
		service.updateYouTubeChannelPriorityStatus(url);
	
	}
}
