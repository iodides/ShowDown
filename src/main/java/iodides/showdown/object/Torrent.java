package iodides.showdown.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import iodides.showdown.DB;
import iodides.showdown.com.TitleParse;

public class Torrent {

    public String siteName = "";
    public String id = "";
    public String title = "";
    public String url = "";
    public String magnet = "";
    public String name = "";
    public int epi1 = 0;
    public int epi2 = 0;
    public String quality = "";
    public String airDate = "";
    public String relGroup = "";


    // 웹에서 파싱된 결과를 세팅
    public void setCrawlResult(String siteName, String id, String title, String url, String magnet) {
        this.siteName = siteName;
        this.id = id;
        this.title = title;
        this.url = url;
        this.magnet = magnet;
        name = TitleParse.getName(title);
        epi1 = TitleParse.getEpi(title)[0];
        epi2 = TitleParse.getEpi(title)[1];
        quality = TitleParse.getQuality(title);
        airDate = TitleParse.getAirDate(title);
        relGroup = TitleParse.getRelGroup(title);
    }

    

    public boolean save() {
        try{
            int cnt = 0;
            String sql = " INSERT IGNORE INTO TORRENT_LIST(SITENAME, ID, TITLE, NAME, EPI1, EPI2, AIRDATE, QUALITY, RELGROUP, URL, MAGNET) " +
                         " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, siteName);
            ps.setString(2, id);
            ps.setString(3, title);
            ps.setString(4, name);
            ps.setInt(5, epi1);
            ps.setInt(6, epi2);
            ps.setString(7, airDate);
            ps.setString(8, quality);
            ps.setString(9, relGroup);
            ps.setString(10, url);
            ps.setString(11, magnet);
            
            cnt = ps.executeUpdate();
            if (cnt > 0) return true;

        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString(){
        return title;
    }
}