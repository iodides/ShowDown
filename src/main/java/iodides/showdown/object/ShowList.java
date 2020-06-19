package iodides.showdown.object;

import java.net.URLDecoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ShowList {
    String baseUrl = "https://m.search.daum.net/search?w=tot&DA=TVS&rtmaxcoll=TVS&q=";
    String stype = "";  // 쇼타입.  "DRAMA", "ENTER"
    String scate = "";  // 쇼유형. "월화드라마", "수목드라마"....
    String listUrl = "";

    public ShowList(String stype, String scate){
        this.stype = stype;
        this.scate = scate;
        this.listUrl = baseUrl + scate;
    }


    public Show parseList() {


        Document doc = null;

        doc = Jsoup.connect(listUrl).get();
        
        Elements listProgram = doc.select(".list_program li .tit_program a");

        for(Element elm : listProgram){
            oDaumShow daumShow = new oDaumShow();
            daumShow.title = elm.text();
            daumShow.url = "https://m.search.daum.net/search" + URLDecoder.decode(elm.attr("href"), "UTF-8");
            daumShow.sid = "";
            daumShow.add();
        }

        for (int i = 0; i < listProgram.size(); i++) {
            Element elm = listProgram.get(i);
            String title = elm.text();
    

            String showUrl = "https://search.daum.net/search" + URLDecoder.decode(elm.attr("href"), "UTF-8");
            showUrl = showUrl.replace("w=tot","w=tv");
            String sid = showUrl;
            log.info(title + " " + showUrl);

            Show show = new Show();
            //show.parse
                
            db.addShow();

            }








        ArrayList<oDaumShow> daumShow
        
        oDaumShow daumShow = new oDaumShow();

        daumShow.sid = "";
        daumShow.title = "";
        daumShow.url = "";

        return daumShow;


    }
    
    
}