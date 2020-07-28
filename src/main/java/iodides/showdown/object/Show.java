package iodides.showdown.object;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import iodides.showdown.DB;
import iodides.showdown.Showdown;

public class Show {

    private static Logger log = Logger.getLogger(Showdown.class);

    private String id;
    private String title;
    private String type;
    private String url;

    private String kword;
    private int airStatus = 1;
    private int season = 1;
    private int lastEpi = 0;
    private int maxEpi = 0;
    private boolean monitor;
    private boolean hd;
    private boolean fhd;

    private String company = "";
    private String schedule = "";
    private String genre = "";
    private String comment = "";
    private String thumb = "";

    private boolean comp;

    public ArrayList<Episode> episodeList = new ArrayList<Episode>();

    public Show(String id, String title, String type, String url) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.url = url;
        monitor = DB.getShowMonitor(id);
        hd = DB.getShowMonitor(id);
        fhd = DB.getShowMonitor(id);
	}

	@Override
    public String toString(){
        return id +" "+ title;
    }

	public void parse() {
        log.debug(this +" 파싱 - "+ url);
        try {
            String newTitle;
            String newThumb;
            String newGenre = "";
            String newComment = "";
            String newCompany = "";
            String newSchedule = "";
            int newAirStatus;
            int newLastEpi = 0;
            int newMaxEpi;

            Document doc = Jsoup.connect(url).get();
            Element tv_program = doc.selectFirst("div.detail_wrap div.mg_cont div#tabCont div#tv_program"); // TV정보
            Element tv_episode = doc.selectFirst("div.detail_wrap div.mg_cont div#tabCont div#tv_episode"); // 회차 정보

            if (tv_program == null)
                throw new IOException();

            newTitle = tv_program.select("div.tit_program strong").text().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
            
            newThumb = URLDecoder.decode("https:"+ tv_program.select("div.wrap_thumb img").attr("src"), "UTF-8");
            
            if(tv_program.selectFirst("div.tit_program span.ico_status") != null) {
                String status_temp = tv_program.selectFirst("div.tit_program span.ico_status").text();
                if (status_temp.equals("방송종료")) newAirStatus = 3;
                else if (status_temp.equals("방송예정")) newAirStatus = 1;
                else newAirStatus = 2;
            } else newAirStatus = 2;
            
            Elements dl_comm = tv_program.select("dl.dl_comm.dl_row");
            if(dl_comm.size()>0) newGenre = dl_comm.get(0).select("dd.cont").text().replace("&nbsp;", " ");
            if(dl_comm.size()>1) newComment = dl_comm.get(1).select("dd.cont").text();

            Elements f_nb = tv_program.select("div.txt_summary span.f_nb");
            if(f_nb.size()>0) newCompany = f_nb.get(0).text();
            if(f_nb.size()>1) newSchedule = f_nb.get(1).text().replace("&nbsp", " ");
            // if(f_nb.size()>2) airDate = f_nb.get(2).text();
            
            newMaxEpi = Integer.parseInt(("0"+newGenre).replaceAll("[^0-9]",""));




            if(tv_episode != null){     // 회차 정보가 없는 프로그램이 있으므로 null 체크
                Elements episodes = tv_episode.select("ul.list_date li");
                for (Element elm : episodes) {
                    int epiNum= 0;
                    String air = "";
                    epiNum = Integer.parseInt("0" + elm.select(".txt_episode").text().replaceAll("[^0-9]", ""));
                    String temp = elm.attr("data-clip").replaceAll("[^0-9]", "");
                    if(temp.length()==8) air = temp.substring(2);

                    episodeList.add(new Episode(epiNum, air, "HD"));
                    episodeList.add(new Episode(epiNum, air, "FHD"));

                    if (lastEpi < epiNum)      // 최대 500개의 회차 정보만 보여주기 때문에 최종 에피소드 회차를 회차 카운트에 반영
                        lastEpi = epiNum;
                }
            }
            log.debug(this +" title : "+ title);
            log.debug(this +" airStatus : "+ airStatus);
            log.debug(this +" genre : "+ genre);
            log.debug(this +" comment : "+ comment);
            log.debug(this +" company : "+ company);
            log.debug(this +" schedule : "+ schedule);
            // log.debug(this +" airDate : "+ airDate);
            log.debug(this +" lastEpi : "+ lastEpi);
            log.debug(this +" maxEp : "+ maxEpi);

            updateTitle(newTitle);
            updateCompany(newCompany);
            updateSchedule(newSchedule);
            updateGenre(newGenre);
            updateComment(newComment);
            updateThumb(newThumb);
            updateAirStatus(newAirStatus);
            updateLastEpi(newLastEpi);
            updateMaxEpi(newMaxEpi);
            updateEpisode();
        } catch (IOException e) {
            log.debug("파싱 에러 - " + this, e);
        } 
    }
    
    public void updateTitle(String newTitle) {
        if (!newTitle.equals("") && !newTitle.equals(title)) {
            DB.updateShowTitle(id, newTitle);
            log.info(this + " 제목 업데이트 - " + title + " > " + newTitle);
        }
    }
    public void updateCompany(String newCompany) {
        if(!newCompany.equals("") && !newCompany.equals(company)) {
            DB.updateShowCompany(id, newCompany);
            log.info(this + " 방송사 업데이트 - " + company + " > " + newCompany);
        }
    }
    public void updateSchedule(String newSchedule) {
        if(!newSchedule.equals("") && !newSchedule.equals(schedule)) {
            DB.updateShowSchedule(id, newSchedule);
            log.info(this + " 스케쥴 업데이트 - " + schedule + " > " + newSchedule);
        }
    }
    public void updateGenre(String newGenre){
        if(!newGenre.equals("") && !newGenre.equals(genre)){
            DB.updateShowGenre(id, newGenre);
            log.info(this +" 장르 업데이트 - " + genre +" > "+ newGenre);
        }
    }
    public void updateComment(String newComment){
        if(!newComment.equals("") && !newComment.equals(comment)){
            DB.updateShowComment(id, newComment);
            log.info(this +" 업데이트 - 설명");
        }
    }
    public void updateThumb(String newThumb){
        if(!newThumb.equals("") && !newThumb.equals(thumb)){
            DB.updateShowThumb(id, newThumb);
            log.info(this +" 업데이트 - 썸네일");
        }
    }
    public void updateAirStatus(int newAirStatus) {
        if(newAirStatus != 0  && newAirStatus != airStatus) {
            DB.updateShowAirStatus(id, newAirStatus);
            String airStatusChar = "";
            String newAirStatusChar = "";
            if(airStatus==1) airStatusChar = "방영예정";
            else if(airStatus==2) airStatusChar = "방영중";
            else if(airStatus==3) airStatusChar = "방영종료";
            if(newAirStatus==1) newAirStatusChar = "방영예정";
            else if(newAirStatus==2) newAirStatusChar = "방영중";
            else if(newAirStatus==3) newAirStatusChar = "방영종료";
            log.info(this +" 상태 업데이트 - "+ airStatusChar +" > "+ newAirStatusChar);
        }
    }
    public void updateLastEpi(int newLastEpi) {
        if(newLastEpi != 0 && newLastEpi != lastEpi) {
            DB.updateShowLastEpi(id, newLastEpi);
            log.info(this +" 최종에피소드 - "+ lastEpi +" > "+ newLastEpi);
        }
    }

    public void updateMaxEpi(int newMaxEpi) {
        if(newMaxEpi != 0 && newMaxEpi != maxEpi) {
            DB.updateShowMaxEpi(id, newMaxEpi);
            log.info(this +" 전체에피소드 - "+ maxEpi +" > "+ newMaxEpi);
        }

    }

	public void updateEpisode() {
        for(Episode episode : episodeList){
            if(DB.insertEpisode(id, episode.epiNum, episode.air, episode.quality)){
                log.info(this +" "+ episode + " 추가 ");
            }

        }
    }
    


    ///////////////////////////////////////////////////////////////////////////

    public static void insertNewShow(String id, String title, String type, String url) {
        try {
            int cnt = 0;
            String sql = " INSERT IGNORE INTO SHOW_LIST(ID, TYPE, TITLE, KWORD, URL) " + " VALUES(?, ?, ?, ?, ?) ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, type);
            ps.setString(3, title);
            ps.setString(4, title);
            ps.setString(5, url);
            cnt = ps.executeUpdate();
            if(cnt>0)
                log.info("신규 프로그램 추가 - " + id + " " + type + " " + title);
        } catch (Exception e) {
            log.error("DB에러 - 신규 프로그램 추가 - " + id + " " + type + " " + title, e);
        }
    }

    public Show(String id) {
        this.id = id;
        String sql = " SELECT TYPE, TITLE, KWORD, SEASON, LASTEPI, MAXEPI, AIRSTATUS, MONITOR, HD, FHD, COMPANY, SCHEDULE, GENRE, COMMENT, URL, THUMB, COMP FROM SHOW_LIST WHERE ID=? ";
        try {
            PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
                setType(rs.getString("TYPE"));
                setTitle(rs.getString("TITLE"));
                setKword(rs.getString("KWORD"));
                setSeason(rs.getInt("SEASON"));
                setLastEpi(rs.getInt("LASTEPI"));
                setMaxEpi(rs.getInt("MAXEPI"));
                setMonitor(rs.getBoolean("MONITOR"));
                setHD(rs.getBoolean("HD"));
                setFHD(rs.getBoolean("FHD"));
                setCompany(rs.getString("COMPANY"));
                setSchedule(rs.getString("SCHEDULE"));
                setAirStatus(rs.getInt("AIRSTATUS"));
                setGenre(rs.getString("GENRE"));
                setComment(rs.getString("COMMENT"));
                setUrl(rs.getString("URL"));
                setThumb(rs.getString("THUMB"));
                setComp(rs.getBoolean("COMP"));
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getter
    public String getType() {
        return type;
    }
    public String getKword() {
        return kword;
    }
    public int getSeason() {
        return season;
    }
    public String getUrl() {
        return url;
    }
    public boolean isMonitor() {
        return monitor;
    }
    public boolean isHD() {
        return hd;
    }
    public boolean isFHD() {
        return fhd;
    }
    public boolean isComp() {
        return comp;
    }

    // setter

    private void setType(String type) {
        this.type = type;
    }
    private void setTitle(String title) {
        this.title = title;
    }
    private void setKword(String kword) {
        this.kword = kword;
    }
    private void setSeason(int season) {
        this.season = season;
    }
    private void setLastEpi(int lastEpi) {
        this.lastEpi = lastEpi;
    }
    private void setMaxEpi(int maxEpi) {
        this.maxEpi = maxEpi;
    }
    private void setMonitor(boolean monitor) {
        this.monitor = monitor;
    }
    private void setHD(boolean hd) {
        this.hd = hd;
    }
    private void setFHD(boolean fhd) {
        this.fhd = fhd;
    }
    private void setCompany(String company) {
        this.company = company;
    }
    private void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    private void setAirStatus(int airStatus) {
        this.airStatus = airStatus;
    }
    private void setGenre(String genre) {
        this.genre = genre;
    }
    private void setComment(String comment) {
        this.comment = comment;
    }
    private void setUrl(String url) {
        this.url = url;
    }
    private void setThumb(String thumb) {
        this.thumb = thumb;
    }
    private void setComp(boolean comp) {
        this.comp = comp;
    }





	
}
