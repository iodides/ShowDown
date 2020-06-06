package iodides.showdown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    public static Connection conn = null;

    public static void Connection() throws ClassNotFoundException, SQLException {
        Config.DB.get();
        String DBHOST = Config.DB.host;
        String DBPORT = Config.DB.port;
        String DBNAME = Config.DB.name;
        String DBUSER = Config.DB.user;
        String DBPASS = Config.DB.pass;

        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String DB_URL = "jdbc:mysql:// "+ DBHOST +":"+ DBPORT +"/"+ DBNAME +"?useSSL=false&characterEncoding=utf8";

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

	}
    
}