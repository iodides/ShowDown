package iodides.showdown.object;

public class Episode {
    public String sid = "";
    public String title = "";
    public int epiNum = 0;
    public String air = "";

    @Override
    public String toString(){
        return sid +" "+ title +" E"+epiNum +" "+ air;
    }
}