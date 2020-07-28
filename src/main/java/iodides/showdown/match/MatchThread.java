package iodides.showdown.match;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import iodides.showdown.Com;
import iodides.showdown.DB;
import iodides.showdown.Log;
import iodides.showdown.object.Match;
import iodides.showdown.object.MatchList;
import iodides.showdown.object.Episode;
import iodides.showdown.object.Show;
import iodides.showdown.object.Torrent;

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
        try {
            for(Show show : DB.getShowIdList()){
                show.findTorrent();
                
    
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        ShowList showList = new ShowList();
        for(int i=0; i<showList.size(); i++){
            Show show = showList.get(i);
            if(show.hd.isMonitor()){
                show.hd.
            }
            showList.get(i).findTorrent();

            // if(show.hd.isMonitor()){
            //     for(int j=0; j<show.hd.episodeList.size(); j++){
            //         Episode episode = show.hd.episodeList.get(j);
            //         if(!episode.isFind()){
            //             Torrent torrent = DB.findTorrent(show.kword, episode.epiNum, episode.air, episode.quality, show.hd.relGroup);
            //             if(torrent.magnet!=null){
            //                 log.info(episode +" 토렌트 발견 - "+ torrent);
            //                 episode.setTorrent(torrent);
            //                 episode.save();
            //             }
            //         }
            //     }
            // }

            // for(int j=0; j<show.hd.episodeList.size(); j++){
            //     Episode episodeHD = show.hd.episodeList.get(j);
            //     Torrent torrent = new Torrent(show.kword, episodeHD.epiNum, episodeHD.air, episodeHD.quality);
            //     episodeHD.find(torrent);
            //     episodeHD.save();


            //     Episode episodeFHD = show.fhd.episodeList.get(j);
            // }
            // show.getStatus();
            // EpisodeList episodeList = new EpisodeList(show);
            // episodeList.getMatchList();
            // for(int j=0; j<episodeList.size(); j++){
            //     Episode episode = episodeList.get(j);
            //     episode.matchTorrent(show);


            // }



        }

        

        // ArrayList<Match> matchList = MatchList.getList();
        // for (Match match : matchList) {
        //     match.findTorrent();



        
        // }

    }

    private void Process() {
    }

    private void Download() {
    }

}