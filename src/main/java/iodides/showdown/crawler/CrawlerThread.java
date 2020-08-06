package iodides.showdown.crawler;

import org.apache.log4j.Logger;

import iodides.showdown.Log;
import iodides.showdown.com.Utils;

public class CrawlerThread extends Thread {

    private static Logger log = Logger.getLogger(Log.class);
    int interval = 1 * 60 * 60;    // 10시간

    public void run(){

        Torrentube torrentube = new Torrentube();

        while (true) {
            log.info("=== Torrent 검색 시작");
            
            torrentube.init();

            if (Utils.torrentFlag) {
                log.info("=== Torrent 검색 완료 : "+ Utils.nextTime(interval) +" 에 다시 시작");
                Utils.sleep(interval);

            } else {
                log.info("=== Torrent 검색 완료");
                break;
            }
        }
    }
    
}