package wow.game.util;

import org.newdawn.slick.Graphics;

import wow.game.WoW;

/**
 * The camera.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Camera {

	private float camX, camY;
	
	public void translate(float x, float y, Graphics g) {
		camX = -x + (WoW.Width / 2) - 16;
		camY = -y + (WoW.Height / 2) - 16;
		g.translate(camX, camY);
	}
}
