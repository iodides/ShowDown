package iodides.showdown.match;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.DB;
import iodides.showdown.Showdown;

import iodides.showdown.object.Episode;
import iodides.showdown.object.Show;

public class MatchThread extends Thread {

    private static Logger log = Logger.getLogger(Showdown.class);
    int interval = 10 * 60; // 10분

    public void run() {
        while (true) {
            log.info("=== Matching 시작");
            find();
            download();
            status();
            rename();
            move();
            del();
            log.info("=== Matching 완료 : " + Com.nextTime(interval) + " 에 다시 시작");
            Com.sleep(interval);
        }

    }

    private void find() {
        log.info("= Find 시작");
        for (Show show : DB.getMatchList()) {
            if (show.isMonitor()) {
                for (Episode episode : DB.getEpisodeList(show)) {
                    if (episode.isMonitor() && !episode.isFind()) {
                        episode.find();
                    }
                }
            }
        }
        log.info("= Find 완료");
    }

    private void download() {
        log.info("= Download 시작");
        for (Show show : DB.getMatchList()) {
            if (show.isMonitor()) {
                for (Episode episode : DB.getEpisodeList(show)) {
                    if (episode.isMonitor() && episode.isFind() && !episode.isDown()) {
                        episode.download();
                    }
                }
            }
        }
        log.info("= Download 완료");
    }

    private void status() {
        log.info("= Status 시작");
        for (Show show : DB.getMatchList()) {
            if (show.isMonitor()) {
                for (Episode episode : DB.getEpisodeList(show)) {
                    if (episode.isMonitor() && episode.isDown() && !episode.isComp()) {
                        episode.status();
                    }
                }
            }
        }
        log.info("= Status 완료");
    }

    private void rename() {
        log.info("= Rename 시작");
        for (Show show : DB.getMatchList()) {
            if(show.isMonitor()) {
                for (Episode episode : DB.getEpisodeList(show)) {
                    if (episode.isMonitor() && episode.isComp() && !episode.isRename()) {
                        episode.rename();
                    }
                }
            }
        }
        log.info("= Rename 완료");
    }

    private void move() {
        log.info("= Move 시작");
        for (Show show : DB.getMatchList()) {
            if (show.isMonitor()) {
                for (Episode episode : DB.getEpisodeList(show)) {
                    if (episode.isMonitor() && episode.isRename() && !episode.isMove()) {
                        episode.move();
                    }
                }
            }
        }
        log.info("= Move 완료");
    }

    private void del() {
        log.info("= Del 시작");
        for (Show show : DB.getMatchList()) {
            if (show.isMonitor()) {
                for (Episode episode : DB.getEpisodeList(show)) {
                    if (episode.isMonitor() && episode.isMove() && !episode.isDel()) {
                        episode.del();
                    }
                }
            }
        }
        log.info("= Del 완료");
    }
}