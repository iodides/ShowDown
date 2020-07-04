package iodides.showdown.daum;

import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.DB;
import iodides.showdown.Log;
import iodides.showdown.object.DaumCategory;
import iodides.showdown.object.DaumShow;

public class DaumSearch extends Thread {

    //private final static Logger log = Logger.getLogger(Log.class);
    private Logger log = Log.setLog("daum.log");

    private String[] dramaCategory = { "월화드라마", "수목드라마", "금요/주말드라마", "일일/아침드라마" };
    private String[] enterCategory = { "월요일예능", "화요일예능", "수요일예능", "목요일예능", "금요일예능", "토요일예능", "일요일예능" };

    public void run() {

        while (true) {

            add();
            
            update();
            Com.sleep("10h");
        }
    }

    private void add(){
        log.info("=== 신규 프로그램 검색 시작");
        for(DaumCategory cat : getCategoryList()){
            if(cat.parse()){
                for(DaumShow show : cat.getShowList()){
                    show.add();
                }
            }
            Com.sleep("5s");
        }
        log.info("=== 신규 프로그램 검색 완료");
    }

    private void update(){
        log.info("=== 프로그램 정보 업데이트 시작");
        try {
            ArrayList<DaumShow> sl = DB.getUpdateShowList();
            for(DaumShow show_db : sl){
                DaumShow show_web = new DaumShow(show_db);
                if(show_web.parse()){
                    show_web.compare(show_db);
                }
                Com.sleep("5s");
            }
        } catch (SQLException e) {
            log.error("DB에러", e);
        }
        log.info("=== 프로그램 정보 업데이트 완료");
    }

    

    private ArrayList<DaumCategory> getCategoryList(){
        ArrayList<DaumCategory> cl = new ArrayList<DaumCategory>();

        for(String category : dramaCategory){
            DaumCategory cat = new DaumCategory("DRAMA", category);
            cl.add(cat);
        }

        for(String category : enterCategory){
            DaumCategory cat = new DaumCategory("ENTER",category);
            cl.add(cat);
        }
        return cl;
    }
}