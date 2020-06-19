package iodides.showdown.daum;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import iodides.showdown.Log;
import iodides.showdown.object.DaumCategory;
import iodides.showdown.object.DaumShowUrl;
import iodides.showdown.object.Show;

public class DaumParse {
    private final static Logger log = Logger.getLogger(Log.class);

    public static ArrayList<Show> category(DaumCategory dc) throws IOException {
        Document doc = null;
        String baseUrl = "https://m.search.daum.net/search";
        String listUrl = dc.getUrl();
        ArrayList<Show> showList = new ArrayList<Show>();

        log.debug("파싱 : " + listUrl);

        doc = Jsoup.connect(listUrl).get();

        Elements listProgram = doc.select(".list_program li .tit_program a");

        int listSize = listProgram.size();
        log.debug((listSize + 1) + "개의 프로그램 검색");

        for (int i = 0; i < listSize; i++) {
            Element elm = listProgram.get(i);
            String url = (baseUrl + URLDecoder.decode(elm.attr("href"), "UTF-8")).replace("w=tot", "w=tv");
            String title = elm.text();
            String sid = getSid(url);
            log.debug("(" + (i + 1) + "/" + listSize + ") URL : " + url);
            log.debug("(" + (i + 1) + "/" + listSize + ") TITLE : " + title);
            log.debug("(" + (i + 1) + "/" + listSize + ") SID : " + sid);
            Show show = new Show(sid, title, url);
            showList.add(show);
        }
        return showList;
    }

    public void parse(DaumShowUrl daumShowUrl) {

    }

    private static String getSid(String url) {
        String temp = url.substring(url.indexOf("&irk=")+5);
        return temp.substring(0,temp.indexOf("&"));
    }



}