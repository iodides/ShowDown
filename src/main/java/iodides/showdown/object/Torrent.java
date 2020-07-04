package iodides.showdown.object;

import java.sql.SQLException;

import iodides.showdown.DB;
import iodides.showdown.TitleParse;

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

    public Torrent(String siteName, String id, String title, String url, String magnet) {
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

    public boolean save() throws SQLException {
        if (DB.insertNewTorrent(this))
            return true;
        else
            return false;
    }

    @Override
    public String toString(){
        return siteName +" "+ title;
    }
}