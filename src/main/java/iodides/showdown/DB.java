package iodides.showdown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import iodides.showdown.object.Match;
import iodides.showdown.object.Torrent;

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

	// public boolean addShow() {
	// 	boolean result = false;
	// 	return result;
	// }

	// public static ArrayList<DaumShow> getDaumShowList(String sql) throws SQLException {
	// 	String base_sql = " SELECT SID, STYPE, TITLE, SEASON, AIRDATE, EPICOUNT, COMPANY, SCHEDULE, STATUS, GENRE, COMMENT, URL, THUMB FROM SHOW_LIST ";
	// 	sql = base_sql + sql;
	// 	ArrayList<DaumShow> sl = new ArrayList<DaumShow>();
	// 	PreparedStatement pstmt = conn.prepareStatement(sql);
	// 	ResultSet rs = pstmt.executeQuery();
	// 	while (rs.next()) {
	// 		DaumShow s = new DaumShow();
	// 		s.sid = rs.getString(1);
	// 		s.stype = rs.getString(2);
	// 		s.title = rs.getString(3);
	// 		s.season = rs.getInt(4);
	// 		s.airDate = rs.getString(5);
	// 		s.epiCount = rs.getInt(6);
	// 		s.company = rs.getString(7);
	// 		s.schedule = rs.getString(8);
	// 		s.status = rs.getInt(9);
	// 		s.genre = rs.getString(10);
	// 		s.comment = rs.getString(11);
	// 		s.url = rs.getString(12);
	// 		s.thumb = rs.getString(13);
	// 		sl.add(s);
	// 	}
	// 	return sl;
	// }

	

	// 업데이트 대상 쇼 리스트 조회
	// public static ArrayList<DaumShow> getShowListUpdate() throws SQLException {
	// 	String sql = " WHERE COMP = false AND STATUS != 3 ";
	// 	return getDaumShowList(sql);
	// }
	

	// 


    // public static boolean insertNewShow(DaumShow show) throws SQLException {
    //     String sql = " INSERT IGNORE INTO SHOW_LIST(SID, STYPE, TITLE, KWORD, URL) " + " VALUES(?, ?, ?, ?, ?) ";
    //     int cnt = 0;
    //     PreparedStatement pstmt = conn.prepareStatement(sql);
    //     pstmt.setString(1, show.sid);
    //     pstmt.setString(2, show.stype);
    //     pstmt.setString(3, show.title);
    //     pstmt.setString(4, show.title);
    //     pstmt.setString(5, show.url);
    //     cnt = pstmt.executeUpdate();
    //     if (cnt > 0) return true;
    //     else return false;
    // }

	// public static boolean insertNewEpisode(DaumEpisode episode) throws SQLException {
	// 	int cnt = 0;
	// 	String sql = " INSERT IGNORE INTO EPISODE_LIST(SID, TITLE, EPINUM, AIR) " +
	// 				 " VALUES(?, ?, ?, ?) ";
	// 	PreparedStatement ps = conn.prepareStatement(sql);
	// 	ps.setString(1, episode.sid);
	// 	ps.setString(2, episode.title);
	// 	ps.setInt(3, episode.epiNum);
	// 	ps.setString(4, episode.air);
	// 	cnt = ps.executeUpdate();
	// 	if (cnt > 0) return true;
	// 	else return false;					 
	// }


	public static String getTorrentSiteUrl(String siteName) throws SQLException {
		String baseUrl = "";
		String sql = " SELECT BASEURL FROM TORRENT_SITE_LIST WHERE SITENAME = ? AND USEFLAG = TRUE ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, siteName);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			baseUrl = rs.getString(1);
		}
		return baseUrl;
	}

	public static boolean insertNewTorrent(Torrent torrent) throws SQLException {
		int cnt = 0;
		String sql = " INSERT IGNORE INTO TORRENT_LIST(SITENAME, ID, TITLE, NAME, EPI1, EPI2, AIRDATE, QUALITY, RELGROUP, URL, MAGNET) " +
					 " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, torrent.siteName);
		ps.setString(2, torrent.id);
		ps.setString(3, torrent.title);
		ps.setString(4, torrent.name);
		ps.setInt(5, torrent.epi1);
		ps.setInt(6, torrent.epi2);
		ps.setString(7, torrent.airDate);
		ps.setString(8, torrent.quality);
		ps.setString(9, torrent.relGroup);
		ps.setString(10, torrent.url);
		ps.setString(11, torrent.magnet);
		
		cnt = ps.executeUpdate();
		if (cnt > 0) return true;
		else return false;			
	}

	public static ArrayList<String> getRelGroups() throws SQLException {
		String sql = " SELECT RELGROUP FROM REL_GROUP_LIST ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ArrayList<String> relGroups = new ArrayList<String>();
		
		while(rs.next()) {
			relGroups.add(rs.getString(1));
		}
		return relGroups;
	}

	public static Torrent findTorrent(Match episode, String quality) throws SQLException {
		String sql = " SELECT SITENAME, ID, TITLE, URL, MAGNET FROM TORRENT_LIST "
				+ " WHERE NAME LIKE ? "
				+ " AND (EPI1 = ? OR EPI2 = ?) "
				+ " AND AIRDATE = ? " 
				+ " AND QUALITY = ? ";
		if(!episode.relGroup.equals("")) sql = sql + " AND RELGROUP = ? ";
		sql = sql + " ORDER BY ADDTIME DESC "
				+ " LIMIT 1 ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, "%"+ episode.kword +"%");
		ps.setInt(2, episode.epiNum);
		ps.setInt(3, episode.epiNum);
		ps.setString(4, episode.air);
		ps.setString(5, quality);
		if(!episode.relGroup.equals("")) ps.setString(6, episode.relGroup);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			String siteName = rs.getString(1);
			String id = rs.getString(2);
			String title = rs.getString(3);
			String url = rs.getString(4);
			String magnet = rs.getString(5);
			Torrent torrent = new Torrent(siteName, id, title, url, magnet);
			if(magnet.equals("")) return null;
			else return torrent;
		}

		return null;
	}





	// Category 이름을 조회
	// public static ArrayList<String> getCategory() {
	// 	ArrayList<String> categoryList = new ArrayList<String>();
	// 	try {
	// 		String sql = " SELECT CATEGORY FROM CATEGORY_LIST ORDER BY NUM ";
	// 		PreparedStatement ps;
	// 		ps = conn.prepareStatement(sql);
	// 		ResultSet rs = ps.executeQuery();
	// 		while(rs.next()) {
	// 			categoryList.add(rs.getString(1));
	// 		}
	// 	} catch (SQLException e) {
	// 		e.printStackTrace();
	// 	}
	// 	return categoryList;
	// }
}