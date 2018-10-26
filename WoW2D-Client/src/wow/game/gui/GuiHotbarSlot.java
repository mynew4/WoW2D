package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * A class to handle the slots within a hotbar.
 * @author Xolitude (October 26, 2018)
 *
 */
@SuppressWarnings("serial")
public class GuiHotbarSlot extends Rectangle {

	public GuiHotbarSlot(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void render(Graphics graphics) {
		graphics.setColor(Color.gray);
		graphics.draw(this);
	}
}
