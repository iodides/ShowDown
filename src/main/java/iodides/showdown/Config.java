package iodides.showdown;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {

    public static JSONObject json = new JSONObject();

    public static void load() throws IOException, ParseException {
        String configFile = "config.json";

        JSONParser parser = new JSONParser();

        Reader render = new FileReader(configFile);
        json = (JSONObject) parser.parse(render);
    }

    public static class DB {
        static String host;
        static String port;
        static String name;
        static String user;
        static String pass;

        public static void get() {
            JSONObject section = (JSONObject) json.get("DB");
            host = (String) section.get("HOST");
            port = (String) section.get("PORT");
            name = (String) section.get("NAME");
            user = (String) section.get("USER");
            pass = (String) section.get("PASS");
        }
    }
}