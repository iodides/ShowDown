package iodides.showdown.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import iodides.showdown.DB;

public class MatchList {

    public static ArrayList<Match> getList() {
        ArrayList<Match> matchList = new ArrayList<Match>();
        try {
            String sql = " SELECT A.SID, A.TITLE, A.KWORD, C.EPINUM, C.AIR, B.QUALITY, C.FFIND, C.FDOWN, C.FCOMP, C.FDEL, B.RELGROUP "
                    + " FROM SHOW_LIST A, SHOW_STATUS B, EPISODE_LIST C " 
                    + " WHERE A.SID = B.SID " 
                    + " AND B.SID = C.SID "
                    + " AND B.QUALITY = C.QUALITY " 
                    + " AND A.COMP = 0 " 
                    + " AND B.MONITOR = 1 " 
                    + " AND C.FFIND = 0 ";
            PreparedStatement pstmt = DB.conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Match match = new Match();
                match.sid = rs.getString("SID");
                match.title = rs.getString("TITLE");
                match.kword = rs.getString("KWORD");
                match.epiNum = rs.getInt("EPINUM");
                match.air = rs.getString("AIR");
                match.quality = rs.getString("QUALITY");
                match.ffind = rs.getBoolean("FFIND");
                match.fdown = rs.getBoolean("FDOWN");
                match.fcomp = rs.getBoolean("FCOMP");
                match.fdel = rs.getBoolean("FDEL");
                match.relGroup = rs.getString("RELGROUP");
                matchList.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matchList;
    }
    
}