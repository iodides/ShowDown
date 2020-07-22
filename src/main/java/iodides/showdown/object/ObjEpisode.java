package iodides.showdown.object;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import iodides.showdown.DB;
import iodides.showdown.Showdown;

public class ObjEpisode {
	private static Logger log = Logger.getLogger(Showdown.class);
	public String sid = "";
	public String title = "";
	public int epiNum;
	public String air;

	public ObjEpisode(String sid, String title, int epiNum, String air) {
		this.sid = sid;
		this.title = title;
		this.epiNum = epiNum;
		this.air = air;
	}

	public void insert() {
		try {
            int cnt = 0;
            String sql = " INSERT IGNORE INTO EPISODE_LIST(SID, EPINUM, AIR, QUALITY) VALUES(?, ?, ?, ?) ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ps.setString(1, sid);
            ps.setInt(2, epiNum);
            ps.setString(3, air);
            ps.setString(4, "HD");
            cnt = cnt + ps.executeUpdate();
            ps.setString(4, "FHD");
            cnt = cnt + ps.executeUpdate();
            if (cnt > 0)
                log.info(this + " 에피소드 추가");
        } catch (Exception e) {
            log.error("DB추가 에러 - " + this, e);
        }
	}

	@Override
    public String toString(){
        if (epiNum < 10) {
            return sid + " " + title + " E0" + epiNum + " " + air;
        } else {
            return sid + " " + title + " E" + epiNum + " " + air;
        }
    }



}
