package iodides.showdown;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class Log {
	// 전역 로그

	public static Logger setConsoleLog() {
		//Logger log = Logger.getLogger(Log.class);
		Logger log = Logger.getRootLogger();
		try {
			String layout = "%d [%-15C{1}][%-5p] %m%n";
			//String layout = "%d   %m%n";
			
			// String datePattern = ".yyyy-MM-dd";
			// String logFileName = "showdown.log";
			PatternLayout patternlayout = new PatternLayout(layout);
			//DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(patternlayout, logFileName, datePattern);
			//log.addAppender(fileAppender);

			ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
			consoleAppender.setThreshold(Level.DEBUG);
		    log.addAppender(consoleAppender);
			
			//log.setLevel(Level.INFO);
		} catch (Exception e) {}

		return log;
	}

	public static Logger setLog(String logFileName) {
		//Logger log = Logger.getLogger(Log.class);
		Logger log = Logger.getRootLogger();
		try {
			String layout = "%d [%-15C{1}][%-5p] %m%n";
			PatternLayout patternlayout = new PatternLayout(layout);
			
			String datePattern = ".yyyy-MM-dd";
			DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(patternlayout, logFileName, datePattern);
			fileAppender.setThreshold(Level.DEBUG);
			log.addAppender(fileAppender);
			
			//ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
			//consoleAppender.setThreshold(Level.INFO);
		    //log.addAppender(consoleAppender);
	    	
		    //log.setLevel(Level.DEBUG);
		} catch (Exception e) {}

		return log;
	}

	public static Logger setLog() {
		//Logger log = Logger.getLogger(Log.class);
		Logger log = Logger.getRootLogger();
		try {
			String layout = "%d [%-15C{1}][%-5p] %m%n";
			PatternLayout patternlayout = new PatternLayout(layout);
			
			String datePattern = ".yyyy-MM-dd";
			DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(patternlayout, "showdown.log", datePattern);
			fileAppender.setThreshold(Level.INFO);
			log.addAppender(fileAppender);
			
			ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
			consoleAppender.setThreshold(Level.INFO);
		    log.addAppender(consoleAppender);
	    	
		    //log.setLevel(Level.DEBUG);
		} catch (Exception e) {}

		return log;
	}
    
}