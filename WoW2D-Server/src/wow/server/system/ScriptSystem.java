package wow.server.system;

import java.util.ArrayList;

import wow.server.net.game.object.entity.EntityInstance;
import wow.server.util.manager.EntityManager;

/**
 * Runs entity scripts.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ScriptSystem extends AbstractRunnableSystem {
	
	private Thread thr;

	@Override
	public void start() {
		thr = new Thread(this, "script_system");
		synchronized (thr) {
			thr.start();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			ArrayList<EntityInstance> instancedEntities = EntityManager.getInstancedEntities();
			if (instancedEntities.size() > 0) {
				for (EntityInstance entityInstance : instancedEntities) {
					entityInstance.update();
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
