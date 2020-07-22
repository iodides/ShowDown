package iodides.showdown.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import iodides.showdown.DB;
import iodides.showdown.Showdown;

public class ObjShowList {

    private static Logger log = Logger.getLogger(Showdown.class);
    ArrayList<ObjShow> showList = new ArrayList<ObjShow>();

    public int size() {
        return showList.size();
    }

    public ObjShow get(int idx) {
        return showList.get(idx);
    }

    public void add(ObjShow show){
        showList.add(show);
    }

	public void getUpdateList() {
        try {
            String sql = " SELECT SID, STYPE, TITLE, SEASON, AIRDATE, EPICOUNT, COMPANY, SCHEDULE, STATUS, GENRE, COMMENT, URL, THUMB FROM SHOW_LIST "
                    + " WHERE COMP = false AND STATUS != 3 ORDER BY ATIME ";
            PreparedStatement pstmt = DB.conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ObjShow show = new ObjShow();

                show.sid = rs.getString(1);
                show.stype = rs.getString(2);
                show.title = rs.getString(3);
                show.season = rs.getInt(4);
                show.airDate = rs.getString(5);
                show.epiCount = rs.getInt(6);
                show.company = rs.getString(7);
                show.schedule = rs.getString(8);
                show.status = rs.getInt(9);
                show.genre = rs.getString(10);
                show.comment = rs.getString(11);
                show.url = rs.getString(12);
                show.thumb = rs.getString(13);
                showList.add(show);
            }
        } catch (SQLException e) {
            log.error("DB조회에러", e);
            e.printStackTrace();
        }
	}

}
