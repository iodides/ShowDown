package iodides.showdown.object;

public class DaumShowUrl {
    String type = "";
    String url = "";
    String sid = "";
    String title = "";

    @Override
    public String toString(){
       return sid +" "+ title; 
    }
}