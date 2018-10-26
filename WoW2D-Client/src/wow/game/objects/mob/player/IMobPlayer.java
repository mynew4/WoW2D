package wow.game.objects.mob.player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.objects.mob.player.race.IRace;

/**
 * An extendable player-class.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class IMobPlayer {
	
	public enum Direction {
		NORTH(0),
		SOUTH(1),
		EAST(2),
		WEST(3);
		
		private int direction;
		
		Direction(int direction) {
			this.direction = direction;
		}
		
		public int getDirection() {
			return direction;
		}
	}
	
	protected String username;
	protected IRace race;
	
	protected float x, y;
	protected float velX, velY;
	
	protected boolean isMovingUp, isMovingDown, isMovingLeft, isMovingRight;
	protected Direction direction;
	
	protected Rectangle bounds;
	
	protected boolean hasSentMovingPacket = false;
	protected boolean isGM = false;
	
	public IMobPlayer(String username, IRace race) {
		this.username = username;
		this.race = race;
	}

	public abstract void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException;
	public abstract void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException;

	public void setLocation(float x, float y, int direction) {
		this.x = x;
		this.y = y;
		for (Direction d : Direction.values()) {
			if (d.getDirection() == direction) {
				this.direction = d;
			}
		}
	}
	
	public void setMovingUp(boolean isMovingUp) {
		if (!isMovingDown)
			this.isMovingUp = isMovingUp;
	}

	public void setMovingDown(boolean isMovingDown) {
		if (!isMovingUp)
			this.isMovingDown = isMovingDown;
	}

	public void setMovingLeft(boolean isMovingLeft) {
		if (!isMovingRight)
			this.isMovingLeft = isMovingLeft;
	}

	public void setMovingRight(boolean isMovingRight) {
		if (!isMovingLeft)
			this.isMovingRight = isMovingRight;
	}
	
	public boolean isMovingUp() {
		return isMovingUp;
	}

	public boolean isMovingDown() {
		return isMovingDown;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}
	
	public void stopMoving() {
		isMovingUp = false;
		isMovingDown = false;
		isMovingLeft = false;
		isMovingRight = false;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public IRace getRace() {
		return race;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getDirection() {
		return direction.direction;
	}
	
	public void setGM(boolean isGM) {
		this.isGM = isGM;
	}
}
