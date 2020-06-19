package iodides.showdown.object;

import iodides.showdown.DB;

public class Show {
    String type = "";
    String cate = "";
    String sid = "";
    String title = "";
    String url = "";

    public Show(String type, String cate, String sid, String title, String url){
        this.type = type;
        this.cate = cate;
        this.sid = sid;
        this.title = title;
        this.url = url;
    }

    public Show(String sid, String title, String url){
        this.sid = sid;
        this.title = title;
        this.url = url;
    }

    public boolean add() {
        int cnt = DB.countShowSid(sid);

        if(cnt==0){
            return DB.insertNewShow(this);
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return sid +" "+ cate +" "+ title;
    }
}