package iodides.showdown.object;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import iodides.showdown.DB;
import iodides.showdown.Log;

public class DaumShow {

    private final static Logger log = Logger.getLogger(Log.class);

    public String sid = "";
    public String type = "";
    public String title = "";
    public String url = "";
    public String kword = "";
    public int season = 1;
    public String airDate = "";
    public int epiCount = 0;
    public String company = "";
    public String schedule = "";
    public int status = 1;
    public String genre = "";
    public String comment = "";
    public String thumb = "";
    public ArrayList<DaumEpisode> episodeList = new ArrayList<DaumEpisode>();

    public DaumShow() {

    }

    public DaumShow(String type, String sid, String title, String url) {
        this.type = type;
        this.sid = sid;
        this.title = title;
        this.url = url;
    }

    public DaumShow(DaumShow show_db) {
        this.sid = show_db.sid;
        this.title = show_db.title;
        this.url = show_db.url;
	}

	public boolean addDB() throws SQLException {
        return DB.insertNewShow(this);
    }

    public void addEpisode(DaumEpisode epi) {
        episodeList.add(epi);
    }

    public void add() {
        try {
            if (DB.insertNewShow(this))
                log.info("신규 프로그램 추가 - " + this);
            else
                log.debug("중복 프로그램 - " + this);
        } catch (SQLException e) {
            log.error("DB에러 - 신규 프로그램 추가", e);
        }

    }

    public boolean parse() {
        boolean result = false;
        Document doc = null;
        log.debug(this +" 파싱 - "+ url);
        try {
            doc = Jsoup.connect(url).get();
            Element tv_program = doc.selectFirst("div.detail_wrap div.mg_cont div#tabCont div#tv_program"); // TV정보
            Element tv_episode = doc.selectFirst("div.detail_wrap div.mg_cont div#tabCont div#tv_episode"); // 회차 정보

            if (tv_program == null)
                throw new IOException();

            title = tv_program.select("div.tit_program strong").text().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
            
            if(tv_program.selectFirst("div.tit_program span.ico_status") != null) {
                String status_temp = tv_program.selectFirst("div.tit_program span.ico_status").text();
                if (status_temp.equals("방송종료")) status = 3;
                else if (status_temp.equals("방송예정")) status = 1;
                else status = 2;
            } else status = 2;
            
            Elements dl_comm = tv_program.select("dl.dl_comm.dl_row");
            if(dl_comm.size()>0) genre = dl_comm.get(0).select("dd.cont").text().replace("&nbsp;", " ");
            if(dl_comm.size()>1) comment = dl_comm.get(1).select("dd.cont").text();

            Elements f_nb = tv_program.select("div.txt_summary span.f_nb");
            if(f_nb.size()>0) company = f_nb.get(0).text();
            if(f_nb.size()>1) schedule = f_nb.get(1).text().replace("&nbsp", " ");
            if(f_nb.size()>2) airDate = f_nb.get(2).text();

            if(tv_episode != null){     // 회차 정보가 없는 프로그램이 있으므로 null 체크
                Elements episodes = tv_episode.select("ul.list_date li");
                for (Element elm : episodes) {
                    DaumEpisode episode = new DaumEpisode();
                    episode.sid = this.sid;
                    episode.title = this.title;
                    episode.epiNum = Integer.parseInt("0" + elm.select(".txt_episode").text().replaceAll("[^0-9]", ""));
                    episode.air = elm.attr("data-clip");
                    episodeList.add(episode);

                    if (epiCount < episode.epiNum)      // 최대 500개의 회차 정보만 보여주기 때문에 최종 에피소드 회차를 회차 카운트에 반영
                        epiCount = episode.epiNum;
                }
            }
            
            result = true;

            log.debug(this +" title : "+ title);
            log.debug(this +" status : "+ status);
            log.debug(this +" genre : "+ genre);
            log.debug(this +" comment : "+ comment);
            log.debug(this +" company : "+ company);
            log.debug(this +" schedule : "+ schedule);
            log.debug(this +" airDate : "+ airDate);
            log.debug(this +" epiCount : "+ epiCount);
        } catch (IOException e) {
            log.debug("파싱 에러 - " + this, e);
        }
		return result;
    }

    public void compare(DaumShow show_db) {
        if (!title.equals(show_db.title) && !title.equals("")){
            try {
                if(DB.updateShowTitle(sid, title))
                    log.info(this +" 제목 업데이트 : "+ show_db.title +" > "+ title);
                else
                    log.info(this +" 제목 업데이트 실패 : "+ show_db.title +" > "+ title);
            } catch (SQLException e) {
                log.error("DB 에러 - "+this +" 제목 업데이트", e);
            }
        }
        if (!airDate.equals(show_db.airDate) && !airDate.equals("")) {
            try {
                if (DB.updateShowAirDate(sid, airDate))
                    log.info(this + " 방영일 업데이트 : " + show_db.airDate + " > " + airDate);
                else
                    log.info(this + " 방영일 업데이트 실패 : " + show_db.airDate + " > " + airDate);
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 방영일 업데이트", e);
            }
        }
        if (!company.equals(show_db.company) && !company.equals("")) {
            try {
                if (DB.updateShowCompany(sid, company))
                    log.info(this + " 방송사 업데이트 : " + show_db.company + " > " + company);
                else
                    log.info(this + " 방송사 업데이트 실패: " + show_db.company + " > " + company);
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 방송사 업데이트", e);
            }
            
        }
        if (!schedule.equals(show_db.schedule) && !schedule.equals("")) {
            try {
                if (DB.updateShowSchedule(sid, schedule))
                    log.info(this + " 스케쥴 업데이트 : " + show_db.schedule + " > " + schedule);
                else
                    log.info(this + " 스케쥴 업데이트 실패 : " + show_db.schedule + " > " + schedule);
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 스케쥴 업데이트", e);
            }

        }
        if (!genre.equals(show_db.genre) && !genre.equals("")) {
            try {
                if (DB.updateShowGenre(sid, genre))
                    log.info(this + " 장르 업데이트 : " + show_db.genre + " > " + genre);
                else
                    log.info(this + " 장르 업데이트 실패 : " + show_db.genre + " > " + genre);
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 장르 업데이트", e);
            }
        }
        if (!comment.equals(show_db.comment) && !comment.equals("")) {
            try {
                if (DB.updateShowComment(sid, comment))
                    log.info(this + " 설명 업데이트");
                else
                    log.info(this + " 설명 업데이트 실패");
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 설명 업데이트", e);
            }
        }
        if (!thumb.equals(show_db.thumb) && !thumb.equals("")) {
            try {
                if (DB.updateShowThumb(sid, thumb))
                    log.info(this + " 썸네일 업데이트");
                else
                    log.info(this + " 썸네일 업데이트 실패");
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 설명 업데이트", e);
            }
        }
        if (status != show_db.status) {
            try {
                if (DB.updateShowStatus(sid, status))
                    log.info(this + " 상태 업데이트 : " + show_db.status + " > " + status);
                else
                    log.info(this + " 상태 업데이트 실패 : " + show_db.status + " > " + status);
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 상태 업데이트", e);
            }
        }
        if (epiCount != show_db.epiCount && epiCount > 0) {
            try {
                if (DB.updateShowEpiCount(sid, epiCount)) {
                    log.info(this + " 에피소드 업데이트 : " + show_db.epiCount + " > " + epiCount);
                    
                    // 에피소드 리스트 추가 insert 또는 update
                    for(DaumEpisode episode : episodeList){
                        if(DB.insertNewEpisode(episode)){
                            log.info(episode + " 에피소드 추가");
                        }else{
                            DaumEpisode episode_db = DB.selectEpisode(sid);
                            if(!episode.air.equals(episode_db.air)){
                                if(DB.updateEpisode(episode)){
                                    log.info(episode + " 에피소드 업데이트 : "+ episode_db.air +" > "+ episode.air);
                                }
                            }
                        }
                    }
                    
                    
                } else
                    log.info(this + " 에피소드 업데이트 실패 : " + show_db.epiCount + " > " + epiCount);
            } catch (SQLException e) {
                log.error("DB 에러 - " + this + " 에피소드 업데이트", e);
            }
        }
    }

    @Override
    public String toString(){
        return sid +" "+ title;
    }
}