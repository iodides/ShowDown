package iodides.showdown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import iodides.showdown.com.TitleParse;
import iodides.showdown.com.Utils;
import iodides.showdown.object.Category;
import iodides.showdown.object.Episode;
import iodides.showdown.object.Show;
import iodides.showdown.object.Torrent;

public class DB {

	public static Connection conn = null;
	private static Logger log = Logger.getLogger(Showdown.class);

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

	

	////////////////////////////////////////////////////////////////////////////////

	public static ArrayList<Category> getCategoryList() throws SQLException {
        ArrayList<Category> categoryList = new ArrayList<Category>();
		String sql = " SELECT CTYPE, CATEGORY FROM CATEGORY_LIST ";
		PreparedStatement ps = DB.conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Category category = new Category();
			category.ctype = rs.getString(1);
			category.category = rs.getString(2);
			category.url = Utils.daumBaseUrl() + "?nil_suggest=btn&w=tot&DA=SBC&q=" + rs.getString(2);
			categoryList.add(category);
		}
        return categoryList;
	}

	public static boolean insertEpisode(String id, int epiNum, String air, String quality) {
		try {
			int cnt = 0;
			String sql = " INSERT IGNORE INTO EPISODE_LIST(ID, EPINUM, QUALITY, AIR) VALUES(?, ?, ?, ?) ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setInt(2, epiNum);
			ps.setString(3, quality);
			ps.setString(4, air);
			cnt = cnt + ps.executeUpdate();
			if (cnt > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
		}
		return false;
	}

	public static void updateShowTitle(String id, String title) {
		String sql = " UPDATE SHOW_LIST SET TITLE=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowCompany(String id, String company) {
		String sql = " UPDATE SHOW_LIST SET COMPANY=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, company);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowSchedule(String id, String schedule){
		String sql = " UPDATE SHOW_LIST SET SCHEDULE=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, schedule);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowGenre(String id, String genre) {
		String sql = " UPDATE SHOW_LIST SET GENRE=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, genre);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowComment(String id, String comment){
		String sql = " UPDATE SHOW_LIST SET COMMENT=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, comment);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowThumb(String id, String thumb){
		String sql = " UPDATE SHOW_LIST SET THUMB=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, thumb);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowAirStatus(String id, int airStatus) {
		String sql = " UPDATE SHOW_LIST SET AIRSTATUS=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, airStatus);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowLastEpi(String id, int lastEpi) {
		String sql = " UPDATE SHOW_LIST SET LASTEPI=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, lastEpi);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowMonitor(String id, boolean monitor) {
		String sql = " UPDATE SHOW_LIST SET MONITOR=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setBoolean(1, monitor);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowHD(String id, boolean hd) {
		String sql = " UPDATE SHOW_LIST SET HD=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setBoolean(1, hd);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowFHD(String id, boolean fhd) {
		String sql = " UPDATE SHOW_LIST SET FHD=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setBoolean(1, fhd);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}
	public static void updateShowMaxEpi(String id, int maxEpi) {
		String sql = " UPDATE SHOW_LIST SET MAXEPI=? WHERE ID=? ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, maxEpi);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
		}
	}



	public static String getShowTitle(String id) {
		String sql = " SELECT TITLE FROM SHOW_LIST WHERE ID=? ";
		String result = "";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
				result = rs.getString(1);
		} catch (Exception e) {
		}
		return result;
	}
	
	public static String getShowCompany(String id) {
		String sql = " SELECT COMPANY FROM SHOW_LIST WHERE ID=? ";
		String result = "";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getString(1);
		} catch (Exception e) {
		}
		return result;
	}

	public static String getShowSchedule(String id) {
		String sql = " SELECT SCHEDULE FROM SHOW_LIST WHERE ID=? ";
		String result = "";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getString(1);
		} catch (Exception e) {
		}
		return result;
	}
	public static String getShowGenre(String id) {
		String sql = " SELECT GENRE FROM SHOW_LIST WHERE ID=? ";
		String result = "";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getString(1);
		} catch (Exception e) {
		}
		return result;
	}
	public static String getShowComment(String id) {
		String sql = " SELECT COMMENT FROM SHOW_LIST WHERE ID=? ";
		String result = "";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getString(1);
		} catch (Exception e) {
		}
		return result;
	}
	public static String getShowThumb(String id) {
		String sql = " SELECT THUMB FROM SHOW_LIST WHERE ID=? ";
		String result = "";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getString(1);
		} catch (Exception e) {
		}
		return result;
	}
	public static int getShowAirStatus(String id) {
		String sql = " SELECT AIRSTATUS FROM SHOW_LIST WHERE ID=? ";
		int result = 0;
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getInt(1);
		} catch (Exception e) {
		}
		return result;
	}
	
	public static int getShowLastEpi(String id) {
		String sql = " SELECT LASTEPI FROM SHOW_LIST WHERE ID=? ";
		int result = 0;
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getInt(1);
		} catch (Exception e) {
		}
		return result;
	}

	public static boolean getShowMonitor(String id) {
		String sql = " SELECT MONITOR FROM SHOW_LIST WHERE ID=? ";
		boolean result = false;
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getBoolean(1);
		} catch (Exception e) {
		}
		return result;
	}
	public static boolean getShowHD(String id) {
		String sql = " SELECT HD FROM SHOW_LIST WHERE ID=? ";
		boolean result = false;
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getBoolean(1);
		} catch (Exception e) {
		}
		return result;
	}
	public static boolean getShowFHD(String id) {
		String sql = " SELECT FHD FROM SHOW_LIST WHERE ID=? ";
		boolean result = false;
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				result = rs.getBoolean(1);
		} catch (Exception e) {
		}
		return result;
	}

	public static int getshowMaxEpi(String id) {
		String sql = " SELECT MAXEPI FROM SHOW_LIST WHERE ID=? ";
		int result = 0;
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	public static ArrayList<Episode> getEpisodeList(Show show) {
		String id = show.getId();
		String title = show.getTitle();
		String type = show.getType();
		int season = show.getSeason();
		String kword = show.getKword();
		String quality = show.getQuality();
		String relGroup = show.getRelGroup();
		String sql = " SELECT EPINUM, AIR, MONITOR, FIND, DOWN, COMP, REN, MOVE, DEL, TORRENTMAGNET, TORRENTNAME, TORRENTHASH FROM EPISODE_LIST WHERE ID=? AND QUALITY=? ";
		ArrayList<Episode> episodeList = new ArrayList<Episode>();
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, quality);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Episode episode = new Episode();
				episode.setId(id);
				episode.setTitle(title);
				episode.setType(type);
				episode.setSeason(season);
				episode.setKword(kword);
				episode.setEpiNum(rs.getInt("EPINUM"));
				episode.setAir(rs.getString("AIR"));
				episode.setQuality(quality);
				episode.setRelGroup(relGroup);
				episode.setMonitor(rs.getBoolean("MONITOR"));
				episode.setFind(rs.getBoolean("FIND"));
				episode.setDown(rs.getBoolean("DOWN"));
				episode.setComp(rs.getBoolean("COMP"));
				episode.setRename(rs.getBoolean("REN"));
				episode.setMove(rs.getBoolean("MOVE"));
				episode.setDel(rs.getBoolean("DEL"));
				episode.setTorrentMagnet(rs.getString("TORRENTMAGNET"));
				episode.setTorrentName(rs.getString("TORRENTNAME"));
				episode.setTorrentHash(rs.getString("TORRENTHASH"));
				episodeList.add(episode);
			}
		} catch (Exception e) {
			log.error("DB에러(프로그램리스트조회)", e);
		}
		return episodeList;
	}

	public static Torrent findTorrent(String kword, int epiNum, String air, String quality, String relGroup) {
		String sql = " SELECT TITLE, MAGNET FROM TORRENT_LIST WHERE NAME LIKE ? AND (EPI1=? OR EPI2=?) AND AIR=? AND QUALITY=? ";
		if(!relGroup.equals("")) {
			sql += " AND RELGROUP=? ";
		}
		sql += " ORDER BY ADDTIME DESC LIMIT 1";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, "%"+kword.replace(" ", "")+"%");
			ps.setInt(2, epiNum);
			ps.setInt(3, epiNum);
			ps.setString(4, air);
			ps.setString(5, quality);
			if(!relGroup.equals("")) {
				ps.setString(6, relGroup);
			}

			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				String title = rs.getString("TITLE");
				String magnet = rs.getString("MAGNET");
				Torrent torrent = new Torrent(title, magnet);
				return torrent;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean insertTorrent(String siteName, String id, String title, String url, String magnet) {
		String name = TitleParse.getName(title);
        String quality = TitleParse.getQuality(title);
        String air = TitleParse.getAir(title);
        String relGroup = TitleParse.getRelGroup(title);
        int epi1 = TitleParse.getEpi(title)[0];
        int epi2 = TitleParse.getEpi(title)[1];
        try{
            int cnt = 0;
            String sql = " INSERT IGNORE INTO TORRENT_LIST(SITENAME, ID, TITLE, NAME, EPI1, EPI2, AIR, QUALITY, RELGROUP, URL, MAGNET) " +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, siteName);
            ps.setString(2, id);
            ps.setString(3, title);
            ps.setString(4, name);
            ps.setInt(5, epi1);
            ps.setInt(6, epi2);
            ps.setString(7, air);
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

	public static boolean updateEpisode(String id, int epiNum, String quality, String col, String val) {
		try {
			String sql = " UPDATE EPISODE_LIST SET "+ col + "=? WHERE ID=? AND EPINUM=? AND QUALITY=? ";
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, val);
			ps.setString(2, id);
			ps.setInt(3, epiNum);
			ps.setString(4, quality);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean updateEpisode(String id, int epiNum, String quality, String col, int val) {
		try {
			String sql = " UPDATE EPISODE_LIST SET "+ col + "=? WHERE ID=? AND EPINUM=? AND QUALITY=? ";
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setInt(1, val);
			ps.setString(2, id);
			ps.setInt(3, epiNum);
			ps.setString(4, quality);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean updateEpisode(String id, int epiNum, String quality, String col, boolean val) {
		try {
			String sql = " UPDATE EPISODE_LIST SET "+ col + "=? WHERE ID=? AND EPINUM=? AND QUALITY=? ";
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setBoolean(1, val);
			ps.setString(2, id);
			ps.setInt(3, epiNum);
			ps.setString(4, quality);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean insertShow(String id, String quality, String title, String type, String url) throws SQLException {
		int cnt=0;
		String sql = " INSERT IGNORE INTO SHOW_LIST(ID, QUALITY, TYPE, TITLE, KWORD, URL) " + " VALUES(?, ?, ?, ?, ?, ?) ";
		PreparedStatement ps = DB.conn.prepareStatement(sql);
		ps.setString(1, id);
		ps.setString(2, quality);
		ps.setString(3, type);
		ps.setString(4, title);
		ps.setString(5, title);
		ps.setString(6, url);
		cnt = ps.executeUpdate();
		if(cnt>0) {
			return true;
		} else {
			return false;
		}
	}

	public static ArrayList<Show> getUpdateList() throws SQLException {
		ArrayList<Show> showList = new ArrayList<Show>();
        String sql = " SELECT ID, TYPE, TITLE, KWORD, RELGROUP, SEASON, LASTEPI, MAXEPI, AIRSTATUS, MONITOR, QUALITY, COMPANY, SCHEDULE, GENRE, COMMENT, URL, THUMB, COMP FROM SHOW_LIST WHERE QUALITY = 'HD' ";
		PreparedStatement ps = DB.conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()){
			Show show = new Show();
			show.setId(rs.getString("ID"));
			show.setType(rs.getString("TYPE"));
			show.setTitle(rs.getString("TITLE"));
			show.setKword(rs.getString("KWORD"));
			show.setRelGroup(rs.getString("RELGROUP"));
			show.setSeason(rs.getInt("SEASON"));
			show.setLastEpi(rs.getInt("LASTEPI"));
			show.setMaxEpi(rs.getInt("MAXEPI"));
			show.setAirStatus(rs.getInt("AIRSTATUS"));
			show.setMonitor(rs.getBoolean("MONITOR"));
			show.setQuality(rs.getString("QUALITY"));
			show.setCompany(rs.getString("COMPANY"));
			show.setSchedule(rs.getString("SCHEDULE"));
			show.setGenre(rs.getString("GENRE"));
			show.setComment(rs.getString("COMMENT"));
			show.setUrl(rs.getString("URL"));
			show.setThumb(rs.getString("THUMB"));
			show.setComp(rs.getBoolean("COMP"));
			showList.add(show);
		}
    	return showList;
	}
	public static ArrayList<Show> getMatchList() {
		ArrayList<Show> showList = new ArrayList<Show>();
		String sql = " SELECT ID, TYPE, TITLE, KWORD, RELGROUP, SEASON, LASTEPI, MAXEPI, AIRSTATUS, MONITOR, QUALITY, COMPANY, SCHEDULE, GENRE, COMMENT, URL, THUMB, COMP FROM SHOW_LIST ";
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Show show = new Show();
				show.setId(rs.getString("ID"));
				show.setType(rs.getString("TYPE"));
				show.setTitle(rs.getString("TITLE"));
				show.setKword(rs.getString("KWORD"));
				show.setRelGroup(rs.getString("RELGROUP"));
				show.setSeason(rs.getInt("SEASON"));
				show.setLastEpi(rs.getInt("LASTEPI"));
				show.setMaxEpi(rs.getInt("MAXEPI"));
				show.setAirStatus(rs.getInt("AIRSTATUS"));
				show.setMonitor(rs.getBoolean("MONITOR"));
				show.setQuality(rs.getString("QUALITY"));
				show.setCompany(rs.getString("COMPANY"));
				show.setSchedule(rs.getString("SCHEDULE"));
				show.setGenre(rs.getString("GENRE"));
				show.setComment(rs.getString("COMMENT"));
				show.setUrl(rs.getString("URL"));
				show.setThumb(rs.getString("THUMB"));
				show.setComp(rs.getBoolean("COMP"));
				showList.add(show);
			}
		} catch (Exception e) {
			log.error("DB에러(프로그램리스트조회)", e);
		}
    	return showList;
	}


}