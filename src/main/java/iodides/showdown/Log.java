package iodides.showdown;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class Log {
	// 로그 세팅
	// 깃 테스트
	public static Logger setLog() {
		Logger log = Logger.getLogger(Log.class);
		try {
			String layout = "%d [%-15C{1}][%-5p] %m%n";
			//String layout = "%d   %m%n";
			String logfilename = "showdown.log";
			String datePattern = ".yyyy-MM-dd";
			PatternLayout patternlayout = new PatternLayout(layout);
			DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(patternlayout, logfilename, datePattern);
			
			log.addAppender(fileAppender);
			ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
		    log.addAppender(consoleAppender);
	    	//log.setLevel(Level.INFO);
		    log.setLevel(Level.DEBUG);
		} catch (Exception e) {}

		return log;
	}
    
}