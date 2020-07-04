package iodides.showdown.torrent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.Log;

public class TorrentSearch extends Thread {

    private Logger log = Log.setLog("torrent.log");

    public void run(){

        while (true) {
            log.info("=== Torrent 검색 시작");

            Torrentube t1 = new Torrentube();
            t1.init();



            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Calendar cal= Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR,10);
            String nextTime = sd.format(cal.getTime());
            log.info("=== Torrent 검색 완료 : "+ nextTime +" 에 다시 시작");

           
            Com.sleep("10h");
        }
    }
    
}