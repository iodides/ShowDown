package iodides.showdown.object;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import iodides.showdown.DB;
import iodides.showdown.Showdown;

public class ObjShow {

    private static Logger log = Logger.getLogger(Showdown.class);

    public String sid = "";
    public String stype = "";
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
    public ObjEpisodeList episodeList = new ObjEpisodeList();
    

    @Override
    public String toString(){
        return sid +" "+ title;
    }

	public void set(String sid, String stype, String title, String url) {
        this.sid = sid;
        this.stype = stype;
        this.title = title;
        this.url = url;
	}

	public void insert() {
        try {
            int cnt = 0;
            PreparedStatement ps;
            String sql1 = " INSERT IGNORE INTO SHOW_LIST(SID, STYPE, TITLE, KWORD, URL) " + " VALUES(?, ?, ?, ?, ?) ";
            ps = DB.conn.prepareStatement(sql1);
            ps.setString(1, sid);
            ps.setString(2, stype);
            ps.setString(3, title);
            ps.setString(4, title);
            ps.setString(5, url);
            cnt = cnt + ps.executeUpdate();

            String sql2 = " INSERT IGNORE INTO SHOW_STATUS(SID, QUALITY) VALUES(?, ?)";
            ps = DB.conn.prepareStatement(sql2);
            ps.setString(1, sid);
            ps.setString(2, "HD");
            cnt = cnt + ps.executeUpdate();

            ps.setString(2, "FHD");
            cnt = cnt + ps.executeUpdate();

            if(cnt>0) log.info("신규 프로그램 추가 - "+ this);
            // else log.info("중복 - "+ this);
        } catch (SQLException e) {
            log.error("신규 프로그램 추가 DB에러 - "+ this, e);
            e.printStackTrace();
        }
	}

	public void parse() {
        log.debug(this +" 파싱 - "+ url);
        try {
            Document doc = Jsoup.connect(url).get();
            Element tv_program = doc.selectFirst("div.detail_wrap div.mg_cont div#tabCont div#tv_program"); // TV정보
            Element tv_episode = doc.selectFirst("div.detail_wrap div.mg_cont div#tabCont div#tv_episode"); // 회차 정보

            if (tv_program == null)
                throw new IOException();

            title = tv_program.select("div.tit_program strong").text().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
            thumb = "https:"+ tv_program.select("div.wrap_thumb img").attr("src");
            thumb = URLDecoder.decode(thumb, "UTF-8");
            
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
                    int epiNum= 0;
                    String air = "";
                    epiNum = Integer.parseInt("0" + elm.select(".txt_episode").text().replaceAll("[^0-9]", ""));
                    String temp = elm.attr("data-clip").replaceAll("[^0-9]", "");
                    if(temp.length()==8) air = temp.substring(2);
                    episodeList.add(new ObjEpisode(sid, title, epiNum, air));

                    

                    if (epiCount < epiNum)      // 최대 500개의 회차 정보만 보여주기 때문에 최종 에피소드 회차를 회차 카운트에 반영
                        epiCount = epiNum;
                }
            }
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
    }
    
    public boolean updateTitle() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET TITLE=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 제목 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAirDate() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET AIRDATE=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, airDate);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 방영일 업데이트", e);
            e.printStackTrace();
        }
        return false;
	}

	public boolean updateCompany() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET COMPANY=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, company);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 방송사 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSchedule() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET SCHEDULE=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, schedule);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("DB 에러 - " + this + " 스케쥴 업데이트", e);
        }
        return false;
    }

    public boolean updateGenre() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET GENRE=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, genre);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 장르 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateComment() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET COMMENT=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, comment);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 설명 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateThumb() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET THUMB=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, thumb);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 설명 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET STATUS=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 상태 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEpiCount() {
        try {
            int cnt = 0;
            String sql = " UPDATE SHOW_LIST SET EPICOUNT=? WHERE SID = ? ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setInt(1, epiCount);
            ps.setString(2, sid);
            cnt = ps.executeUpdate();
            if (cnt > 0)
                return true;
        } catch (SQLException e) {
            log.error("DB 에러 - " + this + " 에피소드 업데이트", e);
            e.printStackTrace();
        }
        return false;
    }

    public void updateEpisodes() {
        for(int i=0; i<episodeList.size(); i++){
            episodeList.get(i).insert();
        }
    }

}
