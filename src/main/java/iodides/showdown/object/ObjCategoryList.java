package iodides.showdown.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import iodides.showdown.DB;
import iodides.showdown.Showdown;

public class ObjCategoryList {

    private static Logger log = Logger.getLogger(Showdown.class);
    private String baseUrl = "https://search.daum.net/search";
    ArrayList<ObjCategory> categoryList = new ArrayList<ObjCategory>();

    public ObjCategoryList() {
        try {
            String sql = " SELECT CTYPE, CATEGORY FROM CATEGORY_LIST ";
            PreparedStatement ps = DB.conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ObjCategory category = new ObjCategory();
                category.ctype = rs.getString(1);
                category.category = rs.getString(2);
                category.url = baseUrl + "?nil_suggest=btn&w=tot&DA=SBC&q=" + rs.getString(2);
                categoryList.add(category);
            }
        } catch (Exception e) {
            log.debug("다음 카테고리 조회 DB에러", e);
        }
    }

    public int size() {
        return categoryList.size();
    }

    public ObjCategory get(int idx) {
        return categoryList.get(idx);
    }
    
}