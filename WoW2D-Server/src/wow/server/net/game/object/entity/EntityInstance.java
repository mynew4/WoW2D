package wow.server.net.game.object.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wow.server.system.ai.Script;
import wow.server.system.ai.Script.ScriptType;

/**
 * Create an instance of an entity and initialize their respective script classes.
 * @author Xolitude (October 26, 2018)
 *
 */
public class EntityInstance {

	private int id;
	private int instanceId;
	private String name;
	
	private float x, y;
	
	private ArrayList<Script> scripts;
	
	public EntityInstance(int id, String name, HashMap<Integer, ArrayList<String>> scriptMap, float x, float y) {
		this.id = id;
		this.name = name;
		
		this.x = x;
		this.y = y;
		
		this.scripts = new ArrayList<Script>();
		
		try {
			for (Map.Entry<Integer, ArrayList<String>> map : scriptMap.entrySet()) {
				int scriptType = map.getKey();
				
				for (ScriptType type : ScriptType.values()) {
					if (type.getScriptType() == scriptType) {
						Script script = (Script) type.getScriptClass().newInstance();
						ArrayList<String> scriptSettings = map.getValue();						
						script.setScriptSettings(scriptSettings);
						scripts.add(script);
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		for (Script script : this.scripts) {
			script.initEntityInstance(this);
			script.start();
		}
	}
	
	public void update() {
		for (Script script : scripts) {
			script.run();
		}
	}
	
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getInstanceId() {
		return instanceId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setX(float newX) {
		this.x = newX;
	}
	
	public void setY(float newY) {
		this.y = newY;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
