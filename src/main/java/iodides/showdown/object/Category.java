package iodides.showdown.object;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import iodides.showdown.Showdown;
import iodides.showdown.com.Utils;

public class Category {

    private static Logger log = Logger.getLogger(Showdown.class);
    public String ctype = "";
    public String category = "";
    public String url = "";

    @Override
    public String toString() {
        return ctype + " " + category + " " + url;
    }

    public void parse() {
        log.info("카테고리 파싱 - " + this);
        try {
            Document doc = Jsoup.connect(url).get();
            Elements listProgram = doc.select("div.inner_article div.mg_cont div.cont_program li div.wrap_cont strong.tit_program a");

            for (Element elm : listProgram) {
                String type = ctype;
                String url = (Utils.daumBaseUrl() + URLDecoder.decode(elm.attr("href"), "UTF-8")).replace("w=tot","w=tv");
                String title = elm.text().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "");
                String id = getId(url);

                Show.insertNewShow(id, title, type, url);
            }
        } catch (IOException e) {
            log.error("파싱에러 - " + this, e);
        }
    }
    
    private String getId(String url){
        final String temp = url.substring(url.indexOf("&irk=")+5);
        return temp.substring(0,temp.indexOf("&"));
    }

}