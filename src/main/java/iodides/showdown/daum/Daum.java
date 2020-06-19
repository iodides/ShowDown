package iodides.showdown.daum;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import iodides.showdown.Log;
import iodides.showdown.object.DaumCategory;
import iodides.showdown.object.Show;

public class Daum extends Thread {

    private final Logger log = Logger.getLogger(Log.class);
    
    private String[] dramaCategory = { "월화드라마", "수목드라마", "금요/주말드라마", "일일/아침드라마" };
    private String[] enterCategory = { "월요일예능", "화요일예능", "수요일예능", "목요일예능", "금요일예능", "토요일예능", "일요일예능" };
    
    

    public void run() {

        while(true){
            for (DaumCategory dc : getCategoryList()) {
                ArrayList<Show> showList;
                try {
                    showList = DaumParse.category(dc);
                    for (Show show : showList) {
                        if (show.add())
                            log.info("신규 프로그램 추가 - " + show);
                        else
                            log.debug("중복 프로그램 - " + show);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sleep("3s");
            }
            sleep("10h");
        }

        
        
    }

    private void sleep(String time) {
        int sec = 0;
        if (time.contains("s")) {
            sec = Integer.parseInt(time.replace("s", ""));
        } else if (time.contains("m")) {
            sec = Integer.parseInt(time.replace("m", "")) * 60;
        } else if (time.contains("h")) {
            sec = Integer.parseInt(time.replace("h", "")) * 60 * 60;
        }

        try {
            Thread.sleep(1000 * sec);
        } catch (InterruptedException e) {
        }
    }

    private ArrayList<DaumCategory> getCategoryList(){
        ArrayList<DaumCategory> dcl = new ArrayList<DaumCategory>();
        
        for(String category : dramaCategory){
            DaumCategory dc = new DaumCategory("드라마",category);
            dcl.add(dc);
        }

        for(String category : enterCategory){
            DaumCategory dc = new DaumCategory("예능",category);
            dcl.add(dc);
        }

        return dcl;
    }
}