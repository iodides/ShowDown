package iodides.showdown.object;

public class TorrentSite {

    public int id = 0;
    public String name = "";
    public String baseUrl = "";

    @Override
    public String toString(){
        return id +" "+ name;
    }
    
}