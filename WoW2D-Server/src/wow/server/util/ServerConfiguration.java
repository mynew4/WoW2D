package wow.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to handle the server's configuration.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ServerConfiguration {

	private static ServerConfiguration serverConfiguration;
	private static final File configurationFile = new File("server.conf");
	private static HashMap<String, String> keyValues;
	
	public enum Keys {
		GUI("gui"),
		AUTH_PORT("auth_port"),
		MOTD("motd"),
		SCRIPTS("scripts"),
		DB_USER("db_user"),
		DB_PASS("db_pass"),
		DB_AUTH("db_auth"),
		DB_WORLD("db_world"),
		DB_HOST("db_host"),
		DB_PORT("db_port"),
		USE_SSL("use_ssl");
		
		private String keyName;
		
		Keys(String keyName) {
			this.keyName = keyName;
		}
	}
	
	public static ServerConfiguration newInstance() {
		if (serverConfiguration == null) 
			serverConfiguration = new ServerConfiguration();
		keyValues = new HashMap<String, String>();
		
		loadConfig();
		
		return serverConfiguration;
	}
	
	/**
	 * Load the configuration.
	 */
	private static void loadConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader(configurationFile))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (!line.contains("=")) // TODO: more configs (check config file)
					continue;
				String[] settingSplitter = line.split("=");
				String key = settingSplitter[0];
				String value = settingSplitter[1];
				
				for (Keys k : Keys.values()) {
					if (k.keyName.equalsIgnoreCase(key)) {
						keyValues.put(k.keyName, value);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Should we use the gui?
	 * @return true if so, false otherwise
	 */
	public static boolean shouldUseGUI() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("gui")) {
				return Boolean.valueOf(keyvalues.getValue().toLowerCase());
			}
		}
		return false;
	}
	
	/**
	 * Get the auth-server port.
	 * @return the auth-server port
	 */
	public static int getAuthenticationPort() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("auth_port")) {
				return Integer.valueOf(keyvalues.getValue());
			}
		}
		return -1;
	}
	
	/**
	 * Get the motd.
	 * @return the motd
	 */
	public static String getMOTD() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("motd")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the entity-scripts folder.
	 * @return the script folder
	 */
	public static File getScriptsFolder() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("scripts")) {
				return new File(keyvalues.getValue());
			}
		}
		return null;
	}
	
	/**
	 * Get the database username.
	 * @return our database username
	 */
	public static String getDatabaseUsername() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("db_user")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the database password.
	 * @return our database password.
	 */
	public static String getDatabasePassword() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("db_pass")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the authentication database name.
	 * @return the database name
	 */
	public static String getAuthDatabase() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("db_auth")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the world database name.
	 * @return the database name
	 */
	public static String getWorldDatabase() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("db_world")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the database ip.
	 * @return the database ip
	 */
	public static String getDatabaseHost() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("db_host")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the database port.
	 * @return the database port
	 */
	public static String getDatabasePort() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("db_port")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Should we use SSL?
	 * @return true if so, false otherwise
	 */
	public static String shouldUseSSL() {
		for (Map.Entry<String, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equalsIgnoreCase("use_ssl")) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
}
