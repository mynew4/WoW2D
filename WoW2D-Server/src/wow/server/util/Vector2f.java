package wow.server.util;

/**
 * Handles positions.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Vector2f {

	protected float x;
	protected float y;
	protected int direction;
	
	public Vector2f(float x, float y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getDirection() {
		return direction;
	}
}
