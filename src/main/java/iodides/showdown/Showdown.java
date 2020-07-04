package iodides.showdown;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import iodides.showdown.daum.DaumSearch;
import iodides.showdown.torrent.TorrentSearch;

public class Showdown {
	private static Logger log = Log.setLog();
	

	public static void main(final String[] args) { 
		
		String configFile = "./config.json";
		boolean daumFlag = false;
		boolean torrentFlag = false;
		if (args.length == 0) {
			System.out.println("Options");
			System.out.println("-c [configfile] : Config File Location");
			System.out.println("-d : Daum Show Search");
			System.out.println("-t : Torrent Search");
		} else {
			log.info("===== Showdown 시작");
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-c")) {
					configFile = args[i + 1];
					i = i + 1;
				} else if (args[i].equals("-d")) {
					daumFlag = true;
				} else if (args[i].equals("-t")) {
					torrentFlag = true;
				}
			}
			boolean config = checkConfig(configFile);
			boolean db = checkDB();
			if (config && db) { 
				log.info("메인 실행");
	
				if(daumFlag){
					DaumSearch daum = new DaumSearch();
					daum.start();
				}
				if(torrentFlag){
					TorrentSearch torrent = new TorrentSearch();
					torrent.start();
				}
	
			}else {
				log.info("프로그램을 종료합니다.");
			}
		}
	}

	// config 파일 체크
	private static boolean checkConfig(String configFile) {
		boolean result = false;
		try {
			Config.load(configFile);
			log.info("설정 파일 로드");
			result = true;
		} catch (IOException e) {
			log.info("설정파일을 읽을 수 없습니다.");
			e.printStackTrace();
		} catch (ParseException e) {
			log.info("설정파일 내용에 오류가 있습니다.");
			e.printStackTrace();
		}
		return result;
	}

	// DB 연결
	private static boolean checkDB() {
		boolean result = false;
		try {
			DB.Connection();
			log.info("DB 연결 성공");
			result = true;
		} catch (ClassNotFoundException | SQLException e) {
			log.info("DB 연결 실패");
			e.printStackTrace();
		}
		return result;
	}
}
