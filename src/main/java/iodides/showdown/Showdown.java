package iodides.showdown;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

public class Showdown {
	private static Logger log = Log.setLog();

	public static void main(final String[] args) { 
		log.info("===== Showdown 시작");
		boolean config = checkConfig();
		boolean db = checkDB();

		if (config && db) { 
			log.info("메인 실행");
		}else {
			log.info("프로그램을 종료합니다.");
		}

		// try {Thread.sleep(1000 * 60 * 60);} catch (Exception e) {};
		log.info("===== Showdown 종료");
	}

	// config 파일 체크
	private static boolean checkConfig() {
		boolean result = false;
		try {
			Config.load();
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
