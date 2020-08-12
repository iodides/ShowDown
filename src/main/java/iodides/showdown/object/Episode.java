package iodides.showdown.object;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import iodides.showdown.DB;
import iodides.showdown.com.TitleParse;
import iodides.showdown.com.Utils;
import iodides.showdown.match.MatchThread;

public class Episode {

    private static Logger log = Logger.getLogger(MatchThread.class);

    private String id;
    private String title;
    private String type;
    private int season;
    private String kword;
    private int epiNum;
    private String air;
    private String quality;
    private String relGroup;
    private boolean monitor;
    private boolean find;
    private boolean down;
    private boolean comp;
    private boolean rename;
    private boolean move;
    private boolean del;
    private String torrentMagnet;
    private String torrentName;
    private String torrentHash;
    private Long torrentStatus;
    private String torrentStatusName;
    private int torrentPercent;
    private ArrayList<String> torrentFiles;

    public Episode(String id, String title, int epiNum, String air, String quality, String relGroup) {
        this.id = id;
        this.title = title;
        this.epiNum = epiNum;
        this.air = air;
        this.quality = quality;
        this.relGroup = relGroup;
    }

    public Episode() {
    }

    @Override
    public String toString() {
        if (epiNum < 10) {
            return id +" "+ title +" E0" + epiNum + " " + air + " " + quality +" "+ relGroup;
        } else {
            return id +" "+ title +" E" + epiNum + " " + air + " " + quality +" "+ relGroup;
        }
    }

    public void find() {
        String dateType = "";
        int curAir = Integer.parseInt(Utils.currentDate());
        int epiAir = Integer.parseInt(air);
        if (curAir>epiAir) {
            dateType = "지난 방영일";
        }else if (curAir==epiAir) {
            dateType = "오늘 방영";
        }
        try {
            if (findTransmission()) { // 트랜스미션에서 검색
                log.info(dateType +" "+ this + " 트랜스미션에서 발견 - " + torrentName);
            } else if (findTorrent()) { // 없으면 토렌트DB에서 검색
                log.info(dateType +" "+ this + " 토렌트 발견 - " + torrentName);
            } else {
                log.info(dateType +" "+ this + " 토렌트 없음");
            }
        } catch (IOException e) {
            log.error("트랜스미션 에러", e);
        }
	}

    public boolean findTorrent() {
        Torrent torrent = DB.findTorrent(kword, epiNum, air, quality, relGroup);
        if (torrent != null) {
            String torrentName = torrent.getTitle();
            String torrentMagnet = torrent.getMagnet();
            if (torrentMagnet != null) {
                DB.updateEpisode(id, epiNum, quality, "TORRENTNAME", torrentName);
                DB.updateEpisode(id, epiNum, quality, "TORRENTMAGNET", torrentMagnet);
                DB.updateEpisode(id, epiNum, quality, "FIND", true);
                this.torrentName = torrentName;
                this.torrentMagnet = torrentMagnet;
                this.find = true;
                return true;
            }
        }
        return false;
    }

    public boolean findTransmission() throws IOException {
        ArrayList<Transmission> tranList;
        tranList = TransmissionAPI.status();
        for(Transmission tran : tranList) {
            String torrentName = tran.getName();
            String torrentMagnet = tran.getMagnet();
            String torrentHash = tran.getHash();

            String tName = TitleParse.getName(torrentName);
            String tQuality = TitleParse.getQuality(torrentName);
            String tAir = TitleParse.getAir(torrentName);
            String tRelGroup = TitleParse.getRelGroup(torrentName);
            int tEpi1 = TitleParse.getEpi(torrentName)[0];
            int tEpi2 = TitleParse.getEpi(torrentName)[1];
            
            
            
            if(tName.contains(kword.replaceAll(" ","")) && tQuality.equals(quality) && tAir.equals(air) && ( tEpi1==epiNum || tEpi2==epiNum) ) {
                if(!relGroup.equals("") && relGroup.equals(tRelGroup)) {
                    DB.updateEpisode(id, epiNum, quality, "TORRENTNAME", torrentName);
                    DB.updateEpisode(id, epiNum, quality, "TORRENTMAGNET", torrentMagnet);
                    DB.updateEpisode(id, epiNum, quality, "TORRENTHASH", torrentHash);
                    DB.updateEpisode(id, epiNum, quality, "FIND", true);
                    DB.updateEpisode(id, epiNum, quality, "DOWN", true);
                    this.torrentName = torrentName;
                    this.torrentMagnet = torrentMagnet;
                    this.torrentHash = torrentHash;
                    return true;
                }
            }
        }
		return false;
	}
  
    public void download() {
        try {
            String torrentHash = TransmissionAPI.download(torrentMagnet);
            if(torrentHash!=null) {
                DB.updateEpisode(id, epiNum, quality, "TORRENTHASH", torrentHash);
                DB.updateEpisode(id, epiNum, quality, "DOWN", true);
                // this.torrentHash = torrentHash;
                // this.down = true;
                log.info(this +" 다운로드 - "+ torrentName);
            } else {
                log.info(this +" 다운로드 실패");
            }
        } catch (Exception e) {
            log.error("트랜스미션에러 - "+ this);
        }
    }

    public void status() {
        try {
            Transmission tran = TransmissionAPI.status(torrentHash);
            if (tran==null) {
                log.info(this +" 트랜스미션 없음");
            } else {
                // torrentName = tran.getName();
                // torrentHash = tran.getHash();
                torrentPercent = tran.getPercent();
                torrentStatus = tran.getStatus();
                torrentStatusName = tran.getStatusName();
                // torrentFiles = tran.getFiles();
                if (torrentStatus==6) {
                    if (stop()) {
                        log.info(this +" 시딩중 > 완료");
                        DB.updateEpisode(id, epiNum, quality, "COMP", true);
                        // this.comp = true;
                    } else {
                        log.info(this +" 시딩중");
                    }
                } else if (torrentStatus==0 && torrentPercent==100) {
                    log.info(this +" 완료");
                    DB.updateEpisode(id, epiNum, quality, "COMP", true);
                    // this.comp = true;
                } else {
                    log.info(this +" "+ torrentStatusName +" / "+ torrentPercent +"%");
                }
            }
        } catch (IOException e) {
            log.error("트랜스미션 에러 - "+ this, e);
        }
	}

    public boolean stop() throws IOException {
        for(int i=0; i<5; i++) {
            TransmissionAPI.stop(torrentHash);
            Transmission tran = TransmissionAPI.status(torrentHash);
            this.torrentStatus = tran.getStatus();
            this.torrentStatusName = tran.getStatusName();
            this.torrentPercent = tran.getPercent();
            if(torrentStatus==0) {
                return true;
            }
            Utils.sleep(1);
        }
        return false;
    }
    
    public void rename() {
        try {
            torrentFiles = TransmissionAPI.status(torrentHash).getFiles();
            String newName = "";
            String qualityName = "";
            String epiName = "";
            String seasonName = "";
            String seasonFolder = "";
    
            if (quality.equals("HD")) {
                qualityName = "720P";
            } else if (quality.equals("FHD")) {
                qualityName = "1080P";
            }

            if (season<10) {
                seasonName = "S0"+season;
                seasonFolder = "Season 0"+season;
            }else {
                seasonName = "S"+season;
                seasonFolder = "Season "+season;
            }

            if (epiNum < 10) {
                epiName = "E0"+epiNum;
            } else {
                epiName = "E"+epiNum;
            }

            // 만약 폴더없이 파일이 1개면, '토렌트' 의 이름을 새로운 파일명으로 변경하고,
            // 파일 이동시 시즌폴더 이후로 이동한다.
            // 만약 폴더안에 파일이 여러개 존재하면, '토렌트' 의 이름은 시즌이름으로 변경하고,
            // 폴더내의 파일은 영상 파일을 이름을 새로운 파일명으로 변경하고,
            // 파일 이동시 쇼이름 이후로 이동한다.

            for(String fileName : torrentFiles) {
                if (fileName.contains(".mp4") || fileName.contains(".mkv") || fileName.contains(".avi")) {
                    if (fileName.contains(".mp4")) {
                        newName = title +"."+ seasonName + epiName + "." + air + "." + qualityName +"."+ relGroup + ".mp4";
                    } else if (fileName.contains(".mkv")) {
                        newName = title +"."+ seasonName + epiName + "." + air + "." + qualityName +"."+ relGroup + ".mkv";
                    } else if (fileName.contains(".avi")) {
                        newName = title +"."+ seasonName + epiName + "." + air + "." + qualityName +"."+ relGroup + ".avi";
                    }
                    if (TransmissionAPI.rename(torrentHash, fileName, newName)) {
                        DB.updateEpisode(id, epiNum, quality, "REN", true);
                        this.rename = true;
                        log.info(this +" 파일명 변경 - "+ newName);
                    }else {
                        log.info(this +" 파일명 변경 실패");
                    }
                    
                }
            }
            if(torrentFiles.size()>1) {
                if (season>1) {
                    if (TransmissionAPI.rename(torrentHash, seasonFolder)) {
                        log.info(this +" 토렌트명 변경 - "+ seasonFolder);
                    } else {
                        log.info(this +" 토렌트명 변경 실패");
                    }
                } else {
                    if (TransmissionAPI.rename(torrentHash, title)) {
                        log.info(this +" 토렌트명 변경 - "+ title);
                    } else {
                        log.info(this +" 토렌트명 변경 실패");
                    }
                }
            }
        } catch(IOException e) {
            log.error("트랜스미션 에러 - "+ this, e);
        }
    }

    public void move() {
        // String baseLocation = "/mnt/GDrive/My Drive/Media/Plex";
        String baseLocation = "/share/GDrive/My Drive/Media/Plex";
        String seasonFolder = "";
        String location = "";
        String libraryName = "";

        if (type.equals("드라마")) {
            libraryName = "드라마_한국";
        } else if (type.equals("예능")) {
            libraryName = "예능";
        }

        if (season<10) {
            seasonFolder = "Season 0"+season;
        } else {
            seasonFolder = "Season "+season;
        }
        if (torrentFiles.size()>1) {
            if (season==1) {
                location = baseLocation +"/"+ libraryName +"/";
            } else {
                location = baseLocation +"/"+ libraryName +"/"+ title +"/";
            }
        } else {
            if (season==1) {
                location = baseLocation +"/"+ libraryName +"/"+ title +"/";
            } else {
                location = baseLocation +"/"+ libraryName +"/"+ title +"/"+ seasonFolder +"/";
            }
        }

        try {
            if (TransmissionAPI.move(torrentHash, location)) {
                DB.updateEpisode(id, epiNum, quality, "MOVE", true);
                this.move = true;
                log.info(this +" 파일이동 - "+ location);
            }else {
                log.info(this +" 파일이동 실패");
            }
        } catch (IOException e) {
            log.error("트랜스미션 에러 - "+ this, e);
        }
    }

    public void del() {
        try {
            if (TransmissionAPI.del(torrentHash)) {
                DB.updateEpisode(id, epiNum, quality, "DEL", true);
                this.del = true;
                log.info(this + " 토렌트 삭제 - " + torrentName);
            } else {
                log.info(this + " 토렌트 삭제 실패");
            }
        } catch (IOException e) {
            log.error("트랜스미션 에러 - "+ this, e);
        }
    }

    
    // getter
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public int getSeason() {
        return season;
    }
    public String getKword() {
        return kword;
    }
    public int getEpiNum() {
        return epiNum;
    }
    public String getAir() {
        return air;
    }
    public String getQuality() {
        return quality;
    }
    public String getRelGroup() {
        return relGroup;
    }
    public boolean isMonitor() {
        return monitor;
    }
    public boolean isFind() {
        return find;
    }
    public boolean isDown() {
        return down;
    }
    public boolean isComp() {
        return comp;
    }
    public boolean isRename() {
        return rename;
    }
    public boolean isMove() {
        return move;
    }
    public boolean isDel() {
        return del;
    }
    public String getTorrentMagnet() {
        return torrentMagnet;
    }
    public String getTorrentName() {
        return torrentName;
    }
    public String getTorrentHash() {
        return torrentHash;
    }
    public Long getTorrentStatus() {
        return torrentStatus;
    }
    public String getTorrentStatusName() {
        return torrentStatusName;
    }
	public int getTorrentPercent() {
		return torrentPercent;
	}
    
    // setter
    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setSeason(int season) {
        this.season = season;
    }
    public void setKword(String kword) {
        this.kword = kword;
    }
    public void setEpiNum(int epiNum) {
        this.epiNum = epiNum;
    }
    public void setAir(String air) {
        this.air = air;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public void setRelGroup(String relGroup) {
        this.relGroup = relGroup;
    }
    public void setMonitor(boolean monitor) {
        this.monitor = monitor;
    }
    public void setFind(boolean find) {
        this.find = find;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public void setComp(boolean comp) {
        this.comp = comp;
    }
    public void setRename(boolean rename) {
        this.rename = rename;
    }
    public void setMove(boolean move) {
        this.move = move;
    }
    public void setDel(boolean del) {
        this.del = del;
    }
    public void setTorrentMagnet(String torrentMagnet) {
        this.torrentMagnet = torrentMagnet;
    }
    public void setTorrentName(String torrentName) {
        this.torrentName = torrentName;
    }
    public void setTorrentHash(String torrentHash) {
        this.torrentHash = torrentHash;
    }
    public void setTorrentPercent(int torrentPercent) {
        this.torrentPercent = torrentPercent;
    }
}
