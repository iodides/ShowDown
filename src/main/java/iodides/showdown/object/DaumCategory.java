package iodides.showdown.object;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import iodides.showdown.Log;

public class DaumCategory {

    private final static Logger log = Logger.getLogger(Log.class);

    private String baseUrl = "https://search.daum.net/search?w=tot&DA=TVS&rtmaxcoll=TVS&q=";
    private String type = "";
    private String category = "";
    private String url = "";
    private ArrayList<DaumShow> showList = new ArrayList<DaumShow>();

    public DaumCategory(String type, String category){
        this.type = type;
        this.category = category;
        this.url = baseUrl + category;
    }

    public boolean parse(){
        boolean result = false;
        log.debug(category +" 파싱 - "+ this.url);
        
        Document doc;
        try{
            doc = Jsoup.connect(url).get();
            Elements listProgram = doc.select("div.inner_article div.mg_cont div.cont_program li div.wrap_cont strong.tit_program a");
            for(Element elm : listProgram){
                String url = (baseUrl + URLDecoder.decode(elm.attr("href"), "UTF-8")).replace("w=tot", "w=tv");
                String title = elm.text().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
                String sid = getSid(url);
                DaumShow show = new DaumShow(type, sid, title, url);
                log.debug(category +" 파싱 - "+ sid + " "+ title);
                showList.add(show);
                result = true;
            }
        } catch (IOException e) {
            log.error("파싱 에러 - "+ this.url, e);
        }
        return result;
    }

    /*
    public boolean parseM(){
        boolean result = false;
        log.debug("파싱 : " + url);

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Elements listProgram = doc.select(".list_program li .tit_program a");

            int listSize = listProgram.size();
            log.debug(listSize + "개의 프로그램 검색");

            for (int i = 0; i < listSize; i++) {
                Element elm = listProgram.get(i);
                String url = (baseUrl + URLDecoder.decode(elm.attr("href"), "UTF-8")).replace("w=tot", "w=tv");
                String title = elm.text().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
                String sid = getSid(url);
                log.debug("(" + (i + 1) + "/" + listSize + ") URL : " + url);
                log.debug("(" + (i + 1) + "/" + listSize + ") TITLE : " + title);
                log.debug("(" + (i + 1) + "/" + listSize + ") SID : " + sid);
                Show show = new Show(type, sid, title, url);
                showList.add(show);
                result = true;
            }
        } catch (IOException e) {
            log.error("파싱 에러", e);
        }
        return result;
        
    }
    */

    public ArrayList<DaumShow> getShowList(){
        return showList;
    }

    private String getSid(String url){
        String temp = url.substring(url.indexOf("&irk=")+5);
        return temp.substring(0,temp.indexOf("&"));
    }
}