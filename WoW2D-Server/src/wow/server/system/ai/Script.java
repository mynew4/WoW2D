package wow.server.system.ai;

import java.util.ArrayList;

import wow.server.net.game.object.entity.EntityInstance;

/**
 * An extendable class for all known script-types.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class Script {

	public enum ScriptType {
		WANDER(0, WanderScript.class);
		
		private int scriptType;
		private Class scriptClass;
		
		ScriptType(int scriptType, Class scriptClass) {
			this.scriptType = scriptType;
			this.scriptClass = scriptClass;
		}
		
		public int getScriptType() {
			return scriptType;
		}
		
		public Class getScriptClass() {
			return scriptClass;
		}
	}
	
	protected EntityInstance entityInstance;
	
	public void initEntityInstance(EntityInstance entityInstance) {
		this.entityInstance = entityInstance;
	}
	
	public abstract void setScriptSettings(ArrayList<String> args);
	public abstract ArrayList<String> getScriptSettings();
	
	public abstract void start();
	public abstract void stop();
	
	public abstract void run();
}
