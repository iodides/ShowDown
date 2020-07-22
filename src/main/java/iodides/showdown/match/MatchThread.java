package iodides.showdown.match;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.Log;
import iodides.showdown.object.Match;
import iodides.showdown.object.MatchList;

public class MatchThread extends Thread {

    private Logger log = Log.setLog("match.log");
    int interval = 10 * 60; // 10분

    public void run() {
        while (true) {
            log.info("=== Matching 시작");
            Find();
            Download();
            Process();
            log.info("=== Matching 완료 : " + Com.nextTime(interval) + " 에 다시 시작");
            Com.sleep(interval);
        }

    }

    private void Find() {
        ArrayList<Match> matchList = MatchList.getList();
        for (Match match : matchList) {
            match.findTorrent();



        
        }

    }

    private void Process() {
    }

    private void Download() {
    }

}