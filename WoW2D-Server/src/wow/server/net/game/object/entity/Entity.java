package wow.server.net.game.object.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import wow.server.system.ai.Script;
import wow.server.system.ai.Script.ScriptType;
import wow.server.util.ServerConfiguration;

/**
 * Handles loading entities and parsing their script-types.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Entity {

	private int id;
	private String name;
	
	private HashMap<Integer, ArrayList<String>> scriptMap;
	
	public Entity(int id, String name, String scriptFile) {
		this.id = id;
		this.name = name;
		
		this.scriptMap = new HashMap<Integer,  ArrayList<String>>();
		
		File[] scripts = ServerConfiguration.getScriptsFolder().listFiles();
		for (File f : scripts) {
			if (f.getName().equalsIgnoreCase(scriptFile)) {
				parseScripts(f);
			}
		}
	}
	
	/**
	 * Load scripts but don't instantiate them.
	 * @param scriptFile
	 */
	private void parseScripts(File scriptFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(scriptFile))) {
			String line = null;
			
			while ((line = br.readLine()) != null) {
				String[] scriptSettings = line.split(";");
				int scriptType = Integer.valueOf(scriptSettings[0]);
				
				for (ScriptType sType : ScriptType.values()) {
					if (sType.getScriptType() == scriptType) {
						ArrayList<String> settings = new ArrayList<String>();
						for (String s : scriptSettings) {
							if (!String.valueOf(scriptType).equals(s)) {
								settings.add(s);
							}
						}
						
						scriptMap.put(scriptType, settings);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public HashMap<Integer, ArrayList<String>> getScriptMap() {
		return scriptMap;
	}
}
