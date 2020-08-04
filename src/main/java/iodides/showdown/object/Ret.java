package iodides.showdown.object;

import java.util.HashMap;

public class Ret {

    private HashMap<String, Object> hashMap = new HashMap<String, Object>();

	public boolean isResult() {
		return (boolean) hashMap.get("result");
    }
    
    public String getHash() {
        return (String) hashMap.get("hash");
    }

    public void set(String key, Object value) {
        hashMap.put(key, value);
    }


    
}