package iodides.showdown.object;

public class Episode {

    public int epiNum;
    public String air;
    public String quality;
    public boolean ffind;
    public boolean fdown;
    public boolean fcomp;
    public boolean fdel;
    public Torrent torrent;

    public Episode(int epiNum, String air, String quality) {
        this.epiNum = epiNum;
        this.air = air;
        this.quality = quality;
    }
    
    @Override
    public String toString() {
        if(epiNum<10){
            return "E0"+epiNum +" "+ air +" "+ quality;
        }else{
            return "E"+epiNum +" "+ air +" "+ quality;
        }
    }
}
