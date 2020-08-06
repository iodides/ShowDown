package iodides.showdown.object;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
// import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// import iodides.showdown.Showdown;

public class TransmissionAPI {

    // private static Logger log = Logger.getLogger(Showdown.class);

    private static String url = "http://192.168.0.100:49091/transmission/rpc/";
    private static String id = "plex";
    private static String pw = "Password!234";

    private static JSONObject tran(JSONObject send) throws IOException {
        // log.debug("Transmission send : " + send);

        String sessionID = getSessionID();
        JSONObject result = new JSONObject();
        String encoding = Base64.getEncoder().encodeToString((id + ":" + pw).getBytes("UTF-8"));

        URL urlconn = new URL(url);
        HttpURLConnection httpconn = (HttpURLConnection) urlconn.openConnection();

        httpconn.setRequestProperty("Accept", "application/json");
        httpconn.setRequestProperty("Content-type", "application/json");
        httpconn.setRequestProperty("Authorization", "Basic " + encoding);
        httpconn.setRequestProperty("X-Transmission-Session-Id", sessionID);

        httpconn.setRequestMethod("POST");

        httpconn.setDoOutput(true);
        httpconn.setDoInput(true);

        OutputStream os = httpconn.getOutputStream();

        os.write(send.toString().getBytes("UTF-8"));
        os.flush();

        InputStream is;
        is = httpconn.getInputStream();

        String receive = IOUtils.toString(is, "UTF-8");
        // log.debug("Transmission receive : " + receive);

        JSONParser parser = new JSONParser();
        try {
            result = (JSONObject) parser.parse(receive);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        is.close();
        httpconn.disconnect();

        return result;
    }

    public static String getSessionID() {

        String sessionID = "";
        try {
            String encoding = Base64.getEncoder().encodeToString((id + ":" + pw).getBytes("UTF-8"));
			
			URL urlconn = new URL(url);
			HttpURLConnection httpconn = (HttpURLConnection)urlconn.openConnection();
			
			httpconn.setRequestProperty("Accept",  "application/json");
			httpconn.setRequestProperty("Content-type",  "application/json");
			httpconn.setRequestProperty("Authorization",  "Basic " + encoding);;
			
			httpconn.setRequestMethod("POST");
			
			httpconn.setDoOutput(true);
			httpconn.setDoInput(true);
			
			//OutputStream os = httpconn.getOutputStream();
			httpconn.getOutputStream();
			
			//os.write(json.getBytes("UTF-8"));
			//os.flush();
			
			InputStream is = null;
			if(httpconn.getResponseCode() == 409) {
				is = httpconn.getErrorStream();
				sessionID = IOUtils.toString(is, "UTF-8");
				is.close();
				sessionID = sessionID.substring(sessionID.lastIndexOf("X-Transmission-Session-Id: ")+27);
				sessionID = sessionID.substring(0, sessionID.length()-11);
			}
		}catch(Exception e) {}
		
		return sessionID;
	}

    public static String download(String magnet) throws IOException {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        
        
        arguments.put("filename", magnet);
        
        hashMap.put("method", "torrent-add");
        hashMap.put("arguments", arguments);

        JSONObject send = new JSONObject(hashMap);
        JSONObject receive = tran(send);

		if(receive.get("result").equals("success")) {
            JSONObject rcvArguments = (JSONObject) receive.get("arguments");
            JSONObject temp;
            if (rcvArguments.toString().contains("torrent-added")) {
                temp = (JSONObject) rcvArguments.get("torrent-added");
            } else {
                temp = (JSONObject) rcvArguments.get("torrent-duplicate");
            }
            return (String) temp.get("hashString");
        } else {
            return null;
        }
    }
    
    public static Transmission status(String hash) throws IOException {
        Transmission tran = new Transmission();
        ArrayList<String> fields = new ArrayList<String>();
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        fields.add("name");
        fields.add("status");
        fields.add("percentDone");
        fields.add("files");

        arguments.put("fields", fields);
        arguments.put("ids", hash);

        hashMap.put("method", "torrent-get");
        hashMap.put("arguments", arguments);

        JSONObject send = new JSONObject(hashMap);
        JSONObject receive = tran(send);

        JSONObject arg = (JSONObject) receive.get("arguments");
        JSONArray torrents = (JSONArray) arg.get("torrents");
        if(torrents.size()>0) {
            JSONObject torrent = (JSONObject) torrents.get(0);
            String name = (String) torrent.get("name");
            Long status = (Long) torrent.get("status");
            JSONArray files = (JSONArray) torrent.get("files");
            int percent = 0;
            try {
                Double temp = (Double) torrent.get("percentDone");
                percent = (int) (temp * 100);
            } catch (Exception e) {
                Long temp = (Long) torrent.get("percentDone");
                percent = (int) (temp * 100);
            }

            tran.setName(name);
            tran.setStatus(status);
            tran.setHash(hash);
            tran.setPercent(percent);
            for(int i=0; i<files.size(); i++) {
                JSONObject file = (JSONObject) files.get(i);
                tran.setFile((String)file.get("name"));
            }
            return tran;
        }
        return null;
    }

    public static void stop(String torrentHash) throws IOException {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        arguments.put("ids", torrentHash);

        hashMap.put("arguments", arguments);
        hashMap.put("method", "torrent-stop");
        JSONObject send = new JSONObject(hashMap);
        tran(send);
    }

    public static ArrayList<Transmission> status() throws IOException {
        ArrayList<Transmission> tranList = new ArrayList<Transmission>();
        ArrayList<String> fields = new ArrayList<String>();
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        fields.add("name");
        fields.add("hashString");
        fields.add("magnetLink");
        fields.add("status");
        fields.add("percentDone");
        
        arguments.put("fields", fields);
        
        hashMap.put("arguments", arguments);
        hashMap.put("method", "torrent-get");
      
        JSONObject send = new JSONObject(hashMap);
        JSONObject receive = tran(send);

        JSONObject ar = (JSONObject) receive.get("arguments");
        JSONArray tr = (JSONArray) ar.get("torrents");
        for(int i=0; i<tr.size(); i++) {
            JSONObject torrent = (JSONObject) tr.get(i);
            Transmission tran = new Transmission();
            String name = (String) torrent.get("name");
            String hash = (String) torrent.get("hashString");
            String magnet = (String) torrent.get("magnetLink");
            Long status = (Long) torrent.get("status");
            int percent = 0;
            try {
                Double temp = (Double) torrent.get("percentDone");
                percent = (int) (temp * 100);
            } catch (Exception e) {
                Long temp = (Long) torrent.get("percentDone");
                percent = (int) (temp * 100);
            }
            
            tran.setName(name);
            tran.setHash(hash);
            tran.setMagnet(magnet.substring(0, magnet.indexOf("&dn=")));
            tran.setStatus(status);
            tran.setPercent(percent);
            tranList.add(tran);
        }
        return tranList;
    }

	public static boolean rename(String torrentHash, String newName) throws IOException {
        Transmission tran = status(torrentHash);
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        arguments.put("ids", torrentHash);
        arguments.put("path", tran.getName());
        arguments.put("name", newName);

        hashMap.put("method", "torrent-rename-path");
        hashMap.put("arguments", arguments);

        JSONObject send = new JSONObject(hashMap);
        JSONObject receive = tran(send);
        String result = (String) receive.get("result");
        if (result.equals("success")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean move(String torrentHash, String location) throws IOException {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        arguments.put("ids", torrentHash);
        arguments.put("location", location);
        arguments.put("move", true);

        hashMap.put("method", "torrent-set-location");
        hashMap.put("arguments", arguments);

        JSONObject send = new JSONObject(hashMap);
        JSONObject receive = tran(send);

        String result = (String) receive.get("result");
        if (result.equals("success")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean del(String torrentHash) throws IOException {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        arguments.put("ids", torrentHash);

        hashMap.put("method", "torrent-remove");
        hashMap.put("arguments", arguments);

        JSONObject send = new JSONObject(hashMap);
        JSONObject receive = tran(send);

        String result = (String) receive.get("result");
        if (result.equals("success")) {
            return true;
        } else {
            return false;
        }
    }

}