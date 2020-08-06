package iodides.showdown.daum;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import iodides.showdown.DB;
import iodides.showdown.Log;
import iodides.showdown.com.Utils;
import iodides.showdown.object.Category;
import iodides.showdown.object.Show;

public class DaumThread extends Thread {

    private static Logger log = Logger.getLogger(Log.class);
    int interval = 12 * 60 * 60; // 12시간

    public void run() {

        while (true) {
            log.info("=== Daum 검색 시작");
            add();
            update();
            if (Utils.daumFlag) {
                log.info("=== Daum 검색 완료 : " + Utils.nextTime(interval) + " 에 다시 시작");
                Utils.sleep(interval);

            } else {
                log.info("=== Daum 검색 완료");
                break;
            }
        }
    }

    private void add() {
        log.info("=== 신규 프로그램 검색 시작");
        try {
            for (Category category : DB.getCategoryList()) {
                category.parse();
                Utils.sleep(2);
            }
        } catch (SQLException e) {
            log.error("DB에러(카테고리리스트조회)", e);
        }
        log.info("=== 신규 프로그램 검색 완료");
    }

    private void update() {
        log.info("=== 프로그램 정보 업데이트 시작");
        try {
            for (Show show : DB.getUpdateList()) {
                show.parse();
                Utils.sleep(2);
            }
        } catch (SQLException e) {
            log.error("DB에러(프로그램리스트조회)", e);
        }
        log.info("=== 프로그램 정보 업데이트 완료");
    }
}