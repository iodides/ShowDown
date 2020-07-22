package iodides.showdown.crawler;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.Log;

public class CrawlerThread extends Thread {

    private Logger log = Log.setLog("torrent.log");
    int interval = 10 * 60 * 60;    // 10시간

    public void run(){

        Torrentube torrentube = new Torrentube();

        while (true) {
            log.info("=== Torrent 검색 시작");
            
            torrentube.init();

            log.info("=== Torrent 검색 완료 : "+ Com.nextTime(interval) +" 에 다시 시작");
            Com.sleep(interval);
        }
    }
    
}