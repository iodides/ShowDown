package iodides.showdown.daum;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.Log;
import iodides.showdown.com.Utils;
import iodides.showdown.object.ObjCategory;
import iodides.showdown.object.ObjCategoryList;
import iodides.showdown.object.ObjShow;
import iodides.showdown.object.ObjShowList;

public class DaumThread extends Thread {

    //private final static Logger log = Logger.getLogger(Log.class);
    private Logger log = Log.setLog("daum.log");
    int interval = 12 * 60 * 60;    // 12시간

    public void run() {

        while (true) {
            log.info("=== Daum 검색 시작");
            // 카테고리 리스트 가져옴
            // 카테고리 리스트 중 1개 파싱
            // 쇼 리스트 가져옴
            // 쇼 리스트중 1개 파싱
            // 쇼 인서트
            // 쇼 업데이트

           add();
            
            update();

            log.info("=== Daum 검색 완료 : "+ Com.nextTime(interval) +" 에 다시 시작");
            Com.sleep(interval);
        }
    }

    private void add(){
        log.info("=== 신규 프로그램 검색 시작");
        // 카테고리 리스트 조회
        // 카테고리별 파싱
        // 파싱한 결과 = 쇼리스트
        // 쇼 별로 인서트
        ObjCategoryList categoryList = new ObjCategoryList();
        for(int i=0; i<categoryList.size(); i++){
            ObjCategory category = categoryList.get(i);
            ObjShowList showList = category.parse();
            for(int j=0; j<showList.size(); j++){
                ObjShow show = showList.get(j);
                show.insert();
            }
            Utils.sleep(2);
        }
        log.info("=== 신규 프로그램 검색 완료");
    }

    private void update(){
        log.info("=== 프로그램 정보 업데이트 시작");
        ObjShowList showList = new ObjShowList();
        showList.getUpdateList();
        for(int i=0; i<showList.size(); i++){
            ObjShow show_db = showList.get(i);
            ObjShow show_web = new ObjShow();
            show_web.set(show_db.sid, show_db.stype, show_db.title, show_db.url);
            show_web.parse();
            compare(show_db, show_web);
            Utils.sleep(2);
        }
        log.info("=== 프로그램 정보 업데이트 완료");
    }

    private void compare(ObjShow db, ObjShow web){

        if(!web.title.equals(db.title) && !web.title.equals("")){
            if(web.updateTitle()) log.info(db + " 제목 업데이트 : " + db.title + " > " + web.title);
        }
        if(!web.airDate.equals(db.airDate) && !web.airDate.equals("")){
            if(web.updateAirDate()) log.info(db + " 방영일 업데이트 : " + db.airDate + " > " + web.airDate);
        }
        if(!web.company.equals(db.company) && !web.company.equals("")){
            if(web.updateCompany()) log.info(db + " 방송사 업데이트 : " + db.company + " > " + web.company);
        }
        if(!web.schedule.equals(db.schedule) && !web.schedule.equals("")) {
            if(web.updateSchedule()) log.info(db + " 스케쥴 업데이트 : " + db.schedule + " > " + web.schedule);
        }
        if (!web.genre.equals(db.genre) && !web.genre.equals("")) {
            if(web.updateGenre()) log.info(db + " 장르 업데이트 : " + db.genre + " > " + web.genre);
        }
        if (!web.comment.equals(db.comment) && !web.comment.equals("")) {
            if(web.updateComment()) log.info(db + " 설명 업데이트");
        } 
        if (!web.thumb.equals(db.thumb) && !web.thumb.equals("")) {
            if(web.updateThumb()) log.info(db + " 썸네일 업데이트");
        }
        if (web.status != db.status) {
            if(web.updateStatus()) log.info(db + " 상태 업데이트 : " + db.status + " > " + web.status);
        } 
        if (web.epiCount != db.epiCount && web.epiCount > 0) {
            if(web.updateEpiCount()) {
                log.info(db + " 에피소드 업데이트 : " + db.epiCount + " > " + web.epiCount);
                web.updateEpisodes();
            }
            
        }


    }

}