package wow.game.util.manager;

import java.io.File;
import java.util.ArrayList;

import wow.game.objects.entity.Entity;
import wow.game.objects.entity.EntityInstance;

/**
 * Handles entities and their instances.
 * @author Xolitude (October 26, 2018)
 *
 */
public class EntityManager {

	private static final File entityFileDirectory = new File("resources/entityFiles/");
	
	private static ArrayList<Entity> entities;
	private static ArrayList<EntityInstance> instancedEntities;
	
	public static void init() {
		entities = new ArrayList<Entity>();
		instancedEntities = new ArrayList<EntityInstance>();
		new Thread("entity_loading") {
			public void run() {
				File[] entityFiles = entityFileDirectory.listFiles();
				long start = System.currentTimeMillis();
				for (File f : entityFiles) {
					Entity e = new Entity(f);
					entities.add(e);
				}
				long stop = System.currentTimeMillis();
				System.out.println(String.format("Loaded entities in %sms", stop - start));
			}
		}.start();
	}
	
	public static void createInstanceOf(int entityId, int entityInstanceId, String name, float x, float y) {
		EntityInstance entityInstance = null;
		for (Entity e : entities) {
			if (e.getId() == entityId) {
				entityInstance = new EntityInstance(e.getId(), entityInstanceId, x, y);
				entityInstance.setImage(e.getRawImage());
				entityInstance.setName(name);
				instancedEntities.add(entityInstance);
			}
		}
	}
	
	public static void updateInstancePosition(int entityId, int entityInstanceId, float x, float y, int direction) {
		for (EntityInstance entityInstance : instancedEntities) {
			if (entityInstance.getId() == entityId) {
				if (entityInstance.getInstanceId() == entityInstanceId) {
					entityInstance.setLocation(x, y);
					entityInstance.setDirection(direction);
				}
			}
		}
	}
	
	public static ArrayList<EntityInstance> getInstancedEntities() {
		return instancedEntities;
	}
}
