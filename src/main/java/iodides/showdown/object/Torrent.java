package iodides.showdown.object;

public class Torrent {

    public String magnet = null;
    public String title;
    public String hash;

    
	public Torrent(String title, String magnet) {
        this.title = title;
        this.magnet = magnet;
	}


	public String getTitle() {
		return title;
	}

    public String getMagnet() {
        if (magnet == null)
            return "";
        if (magnet.equals(null))
            return "";
        else
            return magnet;
	}
}