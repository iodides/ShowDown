package iodides.showdown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import iodides.showdown.object.DaumEpisode;
import iodides.showdown.object.DaumShow;
import iodides.showdown.object.Torrent;
import iodides.showdown.object.TorrentSite;

public class DB {

	public static Connection conn = null;

	public static void Connection() throws ClassNotFoundException, SQLException {

		Config.DB.get();
		String DBHOST = Config.DB.host;
		String DBPORT = Config.DB.port;
		String DBNAME = Config.DB.name;
		String DBUSER = Config.DB.user;
		String DBPASS = Config.DB.pass;

		String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
		String DB_URL = "jdbc:mysql:// " + DBHOST + ":" + DBPORT + "/" + DBNAME
				+ "?useSSL=false&characterEncoding=utf8";

		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

	}

	public boolean addShow() {
		boolean result = false;
		return result;
	}
    
    public static ArrayList<DaumShow> getUpdateShowList() throws SQLException {
        ArrayList<DaumShow> sl = new ArrayList<DaumShow>();

        String sql =    " SELECT    SID, STYPE, TITLE, SEASON, AIRDATE, EPICOUNT, COMPANY, SCHEDULE, STATUS, GENRE, COMMENT, URL, THUMB "+
                        " FROM      SHOW_LIST "+
                        " WHERE     COMPLETE = false "+
                        " AND       STATUS != 3 ";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            DaumShow s = new DaumShow();
            s.sid = rs.getString(1);
            s.type = rs.getString(2);
            s.title = rs.getString(3);
            s.season = rs.getInt(4);
            s.airDate = rs.getString(5);
            s.epiCount = rs.getInt(6);
            s.company = rs.getString(7);
            s.schedule = rs.getString(8);
            s.status = rs.getInt(9);
            s.genre = rs.getString(10);
            s.comment = rs.getString(11);
            s.url = rs.getString(12);
            s.thumb = rs.getString(13);                        
            sl.add(s);
        }
        return sl;
    }


    public static boolean insertNewShow(DaumShow show) throws SQLException {
        String sql = " INSERT IGNORE INTO SHOW_LIST(SID, STYPE, TITLE, KWORD, URL) " + " VALUES(?, ?, ?, ?, ?) ";
        int cnt = 0;
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, show.sid);
        pstmt.setString(2, show.type);
        pstmt.setString(3, show.title);
        pstmt.setString(4, show.title);
        pstmt.setString(5, show.url);
        cnt = pstmt.executeUpdate();
        if (cnt > 0) return true;
        else return false;
    }

    public static boolean updateShowTitle(String sid, String title) throws SQLException {

        int cnt = 0;
        String sql = " UPDATE SHOW_LIST SET TITLE=? WHERE SID = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, sid);
        cnt = ps.executeUpdate();
        if (cnt > 0) return true;
        else return false;
    }

	public static boolean updateShowAirDate(String sid, String airDate) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET AIRDATE=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, airDate);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean updateShowCompany(String sid, String company) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET COMPANY=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, company);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

   	public static boolean updateShowSchedule(String sid, String schedule) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET SCHEDULE=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, schedule);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean updateShowGenre(String sid, String genre) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET GENRE=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, genre);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean updateShowComment(String sid, String comment) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET COMMENT=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, comment);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean updateShowThumb(String sid, String thumb) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET THUMB=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, thumb);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean updateShowStatus(String sid, int status) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET STATUS=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, status);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean updateShowEpiCount(String sid, int epiCount) throws SQLException {
		int cnt = 0;
		String sql = " UPDATE SHOW_LIST SET EPICOUNT=? WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, epiCount);
		ps.setString(2, sid);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;
	}

	public static boolean insertNewEpisode(DaumEpisode episode) throws SQLException {
		int cnt = 0;
		String sql = " INSERT IGNORE INTO EPISODE_LIST(SID, TITLE, EPINUM, AIR) " +
					 " VALUES(?, ?, ?, ?) ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, episode.sid);
		ps.setString(2, episode.title);
		ps.setInt(3, episode.epiNum);
		ps.setString(4, episode.air);
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;					 
	}

	public static DaumEpisode selectEpisode(String sid) throws SQLException {
		DaumEpisode episode = new DaumEpisode();
		String sql = " SELECT TITLE, EPINUM, AIR FROM EPISODE_LIST"+
					 " WHERE SID = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, sid);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			episode.sid = sid;
			episode.title = rs.getString(1);
			episode.epiNum = rs.getInt(2);
			episode.air = rs.getString(3);
		}
		return episode;
	}

	public static boolean updateEpisode(DaumEpisode episode) {
		return false;
	}

	public static TorrentSite getTorrentSiteUrl(String name) throws SQLException {
		TorrentSite ts = new TorrentSite();
		String sql = " SELECT ID, BASE_URL FROM TORRENT_SITE_LIST WHERE NAME = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			ts.id = rs.getInt(1);
			ts.name = name;
			ts.baseUrl = rs.getString(2);
		}
		return ts;
	}

	public static boolean insertNewTorrent(Torrent torrent) throws SQLException {
		int cnt = 0;
		String sql = " INSERT IGNORE INTO TORRENT_LIST(SITENAME, ID, TITLE, NAME, EPI1, EPI2, AIRDATE, QUALITY, URL, MAGNET) " +
					 " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, torrent.siteName);
		ps.setString(2, torrent.id);
		ps.setString(3, torrent.title);
		ps.setString(4, torrent.name);
		ps.setInt(5, torrent.epi1);
		ps.setInt(6, torrent.epi2);
		ps.setString(7, torrent.airDate);
		ps.setString(8, torrent.quality);
		ps.setString(9, torrent.url);
		ps.setString(10, torrent.magnet);
		
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;			
	}



}