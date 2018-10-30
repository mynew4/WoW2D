package wow.game.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The configuration class to handle the settings.
 * @author Xolitude (October 26, 2018)
 *
 */
public class SettingsConfiguration {
	
	public enum Keys {
		Realm,
		RememberAccount,
		AccountName,
		RenderMyName,
		RenderPlayerNames,
		RenderMobNames
	}

	private static final File configFile = new File("resources/client.conf");
	private static SettingsConfiguration settingsConfiguration;
	
	private static HashMap<Keys, String> keyValues;
	
	public static SettingsConfiguration newInstance() {
		if (settingsConfiguration == null) 
			settingsConfiguration = new SettingsConfiguration();
		keyValues = new HashMap<Keys, String>();
		
		loadConfig();
		
		return settingsConfiguration;
	}
	
	/**
	 * Load the configuration and put all values into a map.
	 */
	private static void loadConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
			String data = br.readLine();
			if (data != null) {
				if (data.contains(";")) {
					String[] dataSplit = data.split(";");
					keyValues.put(Keys.Realm, dataSplit[0]);
					keyValues.put(Keys.RememberAccount, dataSplit[1]);
					keyValues.put(Keys.AccountName, dataSplit[2]);
					keyValues.put(Keys.RenderMyName, dataSplit[3]);
					keyValues.put(Keys.RenderPlayerNames, dataSplit[4]);
					keyValues.put(Keys.RenderMobNames, dataSplit[5]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the value of a key.
	 * @param key
	 * @param value
	 */
	public static void setSettingValue(Keys key, Object value) {
		keyValues.replace(key, value.toString());
	}
	
	// TODO: save when buttons are pressed.
	/**
	 * Save the setting map.
	 */
	public static void save() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(keyValues.get(Keys.Realm)+";");
		buffer.append(keyValues.get(Keys.RememberAccount)+";");
		buffer.append(keyValues.get(Keys.AccountName)+";");
		buffer.append(keyValues.get(Keys.RenderMyName)+";");
		buffer.append(keyValues.get(Keys.RenderPlayerNames)+";");
		buffer.append(keyValues.get(Keys.RenderMobNames));

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(configFile))) {
			bw.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the realm we last connected to.
	 * @return the last realm we were on
	 */
	public static String getLastRealm() {
		for (Map.Entry<Keys, String> keyvalues : keyValues.entrySet()) {
			if (keyvalues.getKey().equals(Keys.Realm)) {
				return keyvalues.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get the value pointing to whether or not we should remember the account name.
	 * @return the remember account value
	 */
	public static boolean shouldRememberAccount() {
		return Boolean.valueOf(keyValues.get(Keys.RememberAccount));
	}
	
	/**
	 * Get the account name we remembered.
	 * @return account name we used
	 */
	public static String getAccount() {
		return keyValues.get(Keys.AccountName);
	}
	
	/**
	 * Get whether or not we should render our name.
	 * @return true if so, false otherwise
	 */
	public static boolean shouldRenderMyName() {
		return Boolean.valueOf(keyValues.get(Keys.RenderMyName));
	}
	
	/**
	 * Get whether or not we should render player names.
	 * @return true if so, false otherwise.
	 */
	public static boolean shouldRenderPlayerNames() {
		return Boolean.valueOf(keyValues.get(Keys.RenderPlayerNames));
	}
	
	public static boolean shouldRenderMobNames() {
		return Boolean.valueOf(keyValues.get(Keys.RenderMobNames));
	}
}
