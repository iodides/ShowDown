package iodides.showdown.torrent;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import iodides.showdown.DB;
import iodides.showdown.object.Torrent;

public class Torrentube {

    private final static Logger log = Logger.getLogger(TorrentSearch.class);

    private static String siteName = "TORRENTUBE";
    private static String baseUrl = "";

    public void init() {

        if (getBaseUrl()) {
            if (checkSite()) {
                getTorrent();
            }
        }

    }

    private boolean getBaseUrl() {
        boolean result = false;
        try {
            baseUrl = DB.getTorrentSiteUrl(siteName).baseUrl;
            result = true;
        } catch (final SQLException e) {
            log.error(siteName + " DB에러 - 사이트 주소를 찾을 수 없음", e);
        }
        return result;
    }

    public static boolean checkSite() {

        boolean result = false;
        Document doc = null;
        try {
            doc = Jsoup.connect(baseUrl).get();
            String contents = doc.toString();
            if (contents.contains("영화")) {
                result = true;
                log.info(siteName + " 연결 성공");
            } else {
                log.info(siteName + " 연결 실패");
            }
        } catch (IOException e) {
            log.error(siteName + " 연결 실패", e);
        }
        return result;
    }

    private void getTorrent() {
        int max_page = 10;
		String pageUrl = "kt/list?p&page=";
        
		// 페이지 검색
		for(int i=1; i<=max_page; ) {
            String web_url = baseUrl + pageUrl + i;
	        try {
                Document doc = null;
                doc = Jsoup.connect(web_url).userAgent(
                        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                        .referrer("http://www.google.com").get();

                Elements list = doc.select("script");

                String pageItems = getPageItem(list);
                // log.info(pageItems);
                JSONArray array = getArray(pageItems);

                int insertCnt = 0;
                // 항목 검색
				for(int j=0; j<array.size(); j++) {
				
					JSONObject json = (JSONObject) array.get(j);
					String id = (String) json.get("id");
					String title = (String) json.get("fn");
					String url = baseUrl + "/kt/read?p="+ id;
                    String magnet = "magnet:?xt=urn:btih:" +(String) json.get("hs");
                    

                    Torrent torrent = new Torrent(siteName, id, title, url, magnet);
                    try{
                        if(torrent.save()){
                            log.info("토렌트 추가 : "+ torrent);
                            insertCnt++;
                        }
                    }catch(Exception e){
                        log.error("DB 에러 : "+ torrent +" 추가", e);
                    }
                    
					
				}
				log.info(siteName +" "+ i + "페이지 검색 ("+ insertCnt +"/"+ array.size() +") 추가");
				try {Thread.sleep(1000*2);} catch (InterruptedException e) {}
				if(array.size()==insertCnt) {
					i++;
				}else {
					i = 9999;
				}
			}catch(Exception e) {
				i++;
				e.printStackTrace();
			};
		}
    }
    
    private static String getPageItem(Elements list) {
		for(int j=0; j<list.size(); j++) {
			if(list.get(j).toString().contains("pageItems")) {
				String str = list.get(j).toString();
				
				str = str.substring(str.indexOf("pageItems")+12);
				//log.info(str.indexOf(";"));

                str = str.substring(0, str.indexOf(";"));
				
				str = str.replaceAll("'dt': '", "\"dt\": \"");
				str = str.replaceAll("'fn': '", "\"fn\": \"");
				str = str.replaceAll("'fn'", "\"fn\"");
				str = str.replaceAll("'hs': '", "\"hs\": \"");
				str = str.replaceAll("'id': '", "\"id\": \"");
				str = str.replaceAll("'px'", "\"px\"");
				str = str.replaceAll("'sz'", "\"sz\"");
				str = str.replaceAll("'tv': '", "\"tv\": \"");
				str = str.replaceAll("'td'", "\"td\"");
				str = str.replaceAll("'tu'", "\"tu\"");
				str = str.replaceAll("',", "\",");
				str = str.replaceAll("'}", "\"}");
				return str;
			}
		}
		return "";
    }
    
    
    
    private static JSONArray getArray(String str) {
		
		JSONParser parser = new JSONParser();
		JSONArray array = new JSONArray();
		
		
		try {
			//log.info(str);
			array = (JSONArray) parser.parse(str);
		
		}catch(Exception e) {e.printStackTrace();}
		return array;
		
    }
    
}