package iodides.showdown;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import iodides.showdown.com.Utils;
import iodides.showdown.crawler.CrawlerThread;
import iodides.showdown.daum.DaumThread;
import iodides.showdown.match.MatchThread;

public class Showdown {
	private static Logger log = Log.setLog();

	public static void main(String[] args) {

		String configFile = "./config.json";
		String version = "1.0";
		boolean daumFlag = false;
		boolean torrentFlag = false;
		boolean matchFlag = false;
		if (args.length == 0) {

			System.out.println("Please Select Options");
			System.out.println("-s : Start Showdown");
			System.out.println("-v : Show version");
			// System.out.println("-c [configfile] : Config File Location");
			System.out.println("-d : Daum Show Search (1time)");
			System.out.println("-t : Torrent Search (1time)");
			System.out.println("-m : Match & Download & Process (1time)");
		} else {
			log.info("===== Showdown 시작");
			boolean config = checkConfig(configFile);
			boolean db = checkDB();
			if (config && db) { 
				log.info("메인 실행");
				if (args[0].equals("-v")) {
					System.out.println("Showdown2 Version : "+ version);
				} else if (args[0].equals("-s")) {
					daumFlag = true;
					torrentFlag = true;
					matchFlag = true;
				} else if (args[0].equals("-d")) {
					daumFlag = true;
					Utils.daumFlag = false;
				} else if (args[0].equals("-t")) {
					torrentFlag = true;
					Utils.torrentFlag = false;
				} else if (args[0].equals("-m")) {
					matchFlag = true;
					Utils.matchFlag = false;
				}

				if(daumFlag){
					DaumThread daum = new DaumThread();
					daum.start();
				}
				if(torrentFlag){
					CrawlerThread crawlerThread = new CrawlerThread();
					crawlerThread.start();
				}
				if (matchFlag){
					MatchThread match = new MatchThread();
					match.start();
				}
			} else {
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
			log.error("설정파일을 읽을 수 없습니다.", e);
		} catch (ParseException e) {
			log.error("설정파일 내용에 오류가 있습니다.", e);
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
			log.error("DB 연결 실패", e);
		}
		return result;
	}
}
