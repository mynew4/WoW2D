package wow.server.system.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import wow.net.packet.world.PacketGObject;
import wow.server.net.WoWServer;

/**
 * A basic wandering script.
 * @author Xolitude (October 26, 2018)
 *
 */
public class WanderScript extends Script {
	
	private float movementTime, idleTime, speed;
	
	private boolean isRunning = false;
	private boolean shouldMove = false;
	private boolean shouldIdle = true;
	
	private long lastTimer;
	
	private int i = 0;
	
	private Random rand;
	private boolean isRandSet = false;
	private int direction = -1;

	@Override
	public void setScriptSettings(ArrayList<String> args) {
		movementTime = Float.valueOf(args.get(0));
		idleTime = Float.valueOf(args.get(1));
		speed = Float.valueOf(args.get(2));
	}

	@Override
	public void start() {
		if (isRunning)
			return;
		isRunning = true;
		lastTimer = System.currentTimeMillis();
		rand = new Random();
	}

	@Override
	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		if (isRunning) {
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				i += 1000;
            }
			
			if (shouldIdle) {
				if (isRandSet) isRandSet = false;
				if (i >= idleTime * 1000) {
					i = 0;
					shouldIdle = false;
					shouldMove = true;
				}
			}
			
			if (shouldMove) {
				if (!isRandSet)
					direction = getRandomDirection();
				moveEntity(); // TODO: implement bounds to stop them from moving all over the map. (using tiledmap)
				PacketGObject gObjectUpdate = new PacketGObject();
				gObjectUpdate.ID = super.entityInstance.getId();
				gObjectUpdate.InstanceID = super.entityInstance.getInstanceId();
				gObjectUpdate.X = super.entityInstance.getX();
				gObjectUpdate.Y = super.entityInstance.getY();
				gObjectUpdate.Direction = direction;
				gObjectUpdate.State = 1;
				WoWServer.getWorldServer().sendToAllUDP(gObjectUpdate);
				if (i >= movementTime * 1000) {
					i = 0;
					shouldIdle = true;
					shouldMove = false;
				}
			}
		}
	}
	
	private void moveEntity() {
		switch (direction) {
		case 0:
			super.entityInstance.setY(super.entityInstance.getY() - speed);
			break;
		case 1:
			super.entityInstance.setY(super.entityInstance.getY() + speed);
			break;
		case 2:
			super.entityInstance.setX(super.entityInstance.getX() + speed);
			break;
		case 3:
			super.entityInstance.setX(super.entityInstance.getX() - speed);
			break;
		}
	}
	
	private int getRandomDirection() {
		isRandSet = true;
		return rand.nextInt(4);
	}

	@Override
	public ArrayList<String> getScriptSettings() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(String.valueOf(movementTime));
		result.add(String.valueOf(idleTime));
		result.add(String.valueOf(speed));
		return result;
	}
}
