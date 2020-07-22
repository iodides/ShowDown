package iodides.showdown.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import iodides.showdown.DB;

public class Match {
    public String sid = "";
    //public String stype = "";
    public String title = "";
    public String kword = "";
    //public int season = 0;
    //public int epiCount = 0;
    public int epiNum = 0;
    //public int status = 0;
    public String quality = "";
    public String relGroup = "";
    
    public boolean comp = false;
    public String air = "";
    public boolean ffind = false;
    public boolean fdown = false;
    public boolean fcomp = false;
    public boolean fdel = false;
    public String magent = "";
    public String hash = "";
    
    @Override
    public String toString(){
        if (epiNum < 10) {
            return sid + " " + title + " E0" + epiNum + " " + air;
        } else {
            return sid + " " + title + " E" + epiNum + " " + air;
        }

    }

	public void findTorrent() {
        try{
            String sql = " SELECT TITLE, MAGNET FROM TORRENT_LIST "
                + " WHERE NAME LIKE ? "
                + " AND (EPI1 = ? OR EPI2 = ?) "
                + " AND AIRDATE = ? "
                + " AND QUALITY = ? "
                + " AND RELGROUP = ? ";
            PreparedStatement pstmt = DB.conn.prepareStatement(sql);
            pstmt.setString(1, "%"+kword.replaceAll(" ","").toLowerCase()+"%");
            pstmt.setInt(2, epiNum);
            pstmt.setInt(3, epiNum);
            pstmt.setString(4, air);
            pstmt.setString(5, quality);
            pstmt.setString(6, relGroup.toLowerCase());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("TITLE");
                String magnet = rs.getString("MAGNET");
                System.out.println(title +" "+ magnet);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
	}
}