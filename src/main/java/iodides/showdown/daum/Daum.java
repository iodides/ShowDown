package iodides.showdown.daum;

import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.DB;
import iodides.showdown.Log;
import iodides.showdown.object.Category;
import iodides.showdown.object.Show;

public class Daum extends Thread {

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
        for(Category cat : getCategoryList()){
            if(cat.parse()){
                for(Show show : cat.getShowList()){
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
            ArrayList<Show> sl = DB.getUpdateShowList();
            for(Show show_db : sl){
                Show show_web = new Show(show_db);
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

    

    private ArrayList<Category> getCategoryList(){
        ArrayList<Category> cl = new ArrayList<Category>();

        for(String category : dramaCategory){
            Category cat = new Category("DRAMA", category);
            cl.add(cat);
        }

        for(String category : enterCategory){
            Category cat = new Category("ENTER",category);
            cl.add(cat);
        }
        return cl;
    }
}