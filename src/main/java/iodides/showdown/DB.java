package iodides.showdown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import iodides.showdown.com.Utils;
import iodides.showdown.object.Category;
import iodides.showdown.object.Episode;

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

	public static ArrayList<Category> getCategoryList() {
        ArrayList<Category> categoryList = new ArrayList<Category>();
        try {
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
        } catch (Exception e) {
			e.printStackTrace();
        }
        return categoryList;
	}

	public static ArrayList<String> getShowIdList() {
		ArrayList<String> idList = new ArrayList<String>();
		String sql = " SELECT ID FROM SHOW_LIST WHERE COMP=false AND AIRSTATUS !=3 ORDER BY TYPE, TITLE ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				idList.add(rs.getString("ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idList;
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

	public static ArrayList<Episode> getEpisodeList(String id, String quality) {
		String sql = " SELECT EPINUM, AIR FROM EPISODE_LIST WHERE ID=? AND QUALITY=? ";
		ArrayList<Episode> episodeList = new ArrayList<Episode>();
		try {
			PreparedStatement ps = DB.conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, quality);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				int epiNum = rs.getInt("EPINUM");
				String air = rs.getString("AIR");
				Episode episode = new Episode(epiNum, air, quality);
				episodeList.add(episode);
			}
		} catch (Exception e) {
		}
		return episodeList;
	}





	




}