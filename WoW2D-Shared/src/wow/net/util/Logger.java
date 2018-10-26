package wow.net.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles the different types of logs.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Logger {

	private static Logger logger;
	private static File logFile;
	
	public static Logger newClientInstance() {
		if (logger == null) 
			logger = new Logger();
		logFile = new File("client.log");
		
		return logger;
	}
	
	public static Logger newServerInstance() {
		if (logger == null)
			logger = new Logger();
		logFile = new File("server.log");
		
		return logger;
	}
	
	public static void write(String message) {
		try (BufferedWriter br = new BufferedWriter(new FileWriter(logFile, true))) {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			br.write(String.format("[%s] %s\n", formatter.format(date), message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
