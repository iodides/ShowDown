package iodides.showdown.object;

public class DaumCategory {
    private String baseUrl = "https://m.search.daum.net/search?w=tot&DA=TVS&rtmaxcoll=TVS&q=";
    String type = "";
    String category = "";
    String url = "";

    public DaumCategory(String type, String category){
        this.type = type;
        this.category = category;
        this.url = baseUrl + category;
    }

    @Override
    public String toString(){
        return category +" "+ url;
    }

	public String getUrl() {
		return url;
	}
}