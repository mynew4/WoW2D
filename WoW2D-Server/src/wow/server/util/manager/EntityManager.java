package wow.server.util.manager;

import java.util.ArrayList;

import wow.server.net.game.object.entity.Entity;
import wow.server.net.game.object.entity.EntityInstance;
import wow.server.system.AbstractRunnableSystem;
import wow.server.system.ScriptSystem;

/**
 * Handles entities and creating entity-instances.
 * @author Xolitude (October 26, 2018)
 *
 */
public class EntityManager {
	
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static ArrayList<EntityInstance> instancedEntities = new ArrayList<EntityInstance>();
	
	private static AbstractRunnableSystem scriptSystem = new ScriptSystem();
	
	public static void addEntities(ArrayList<Entity> entityList) {
		for (Entity e : entityList)
			entities.add(e);
	}
	
	public static EntityInstance createIntstanceOf(int entityId, float x, float y) {
		EntityInstance entityInstance = null;
		
		for (Entity e : entities) {
			if (e.getId() ==  entityId) {
				entityInstance = new EntityInstance(e.getId(), e.getName(), e.getScriptMap(), x, y);
			}
		}
		
		if (entityInstance != null) {
			if (instancedEntities.size() > 0)
				entityInstance.setInstanceId(instancedEntities.get(instancedEntities.size() - 1).getInstanceId() + 1);
			else
				entityInstance.setInstanceId(1);
			instancedEntities.add(entityInstance);
		}
		
		return entityInstance;
	}
	
	public static void startScriptSystem() {
		scriptSystem.start();
	}
	
	public static ArrayList<EntityInstance> getInstancedEntities() {
		return instancedEntities;
	}
	
	public static int getSize() {
		return entities.size();
	}
}
