package iodides.showdown.daum;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.DB;
import iodides.showdown.Log;
import iodides.showdown.com.Utils;
import iodides.showdown.object.Category;
import iodides.showdown.object.Show;

public class DaumThread extends Thread {

    // private final static Logger log = Logger.getLogger(Log.class);
    private Logger log = Log.setLog("daum.log");
    int interval = 12 * 60 * 60; // 12시간

    public void run() {

        while (true) {
            log.info("=== Daum 검색 시작");
            add();
            update();
            log.info("=== Daum 검색 완료 : " + Com.nextTime(interval) + " 에 다시 시작");
            Com.sleep(interval);
        }
    }

    private void add() {
        log.info("=== 신규 프로그램 검색 시작");
        for (Category category : DB.getCategoryList()) {
            category.parse();
            Utils.sleep(2);
        }
        log.info("=== 신규 프로그램 검색 완료");
    }

    private void update() {
        log.info("=== 프로그램 정보 업데이트 시작");
        for (String id : DB.getShowIdList()) {
            Show show = new Show(id);
            show.parse();
            Utils.sleep(2);
        }
        log.info("=== 프로그램 정보 업데이트 완료");
    }
}