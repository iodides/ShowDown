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
		final Logger log = Logger.getLogger(Log.class);
		try {
			final String layout = "%d [%-15C{1}][%-5p] %m%n";
			// String layout = "%d %m%n";
			final String logfilename = "showdown.log";
			final String datePattern = ".yyyy-MM-dd";
			final PatternLayout patternlayout = new PatternLayout(layout);
			final DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(patternlayout, logfilename,
					datePattern);

			log.addAppender(fileAppender);
			final ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
			log.addAppender(consoleAppender);
			log.setLevel(Level.INFO);
		} catch (final Exception e) {
		}
		return log;
	}
    
}