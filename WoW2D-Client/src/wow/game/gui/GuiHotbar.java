package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * A class to handle the overall hotbar.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiHotbar {

	private GuiHotbarSlot[] slots;
	private Rectangle hotbarBackground;
	private final int START_SLOTS = 12;
	
	private float x, y;
	private boolean isVisible = true;
	
	public GuiHotbar() {
		slots = new GuiHotbarSlot[START_SLOTS];
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new GuiHotbarSlot(0, 0, 32, 32);
		}
	}
	
	public void render(Graphics graphics) {
		if (isVisible) {
			graphics.setColor(Color.black);
			graphics.fill(hotbarBackground);
			graphics.setColor(Color.gray);
			graphics.draw(hotbarBackground);
			for (int i = 0; i < slots.length; i++) {
				slots[i].render(graphics);
			}
		}
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		
		hotbarBackground = new Rectangle(0, 0, slots.length * 32 + slots.length * 6, 40);
		hotbarBackground.setLocation(x - hotbarBackground.getWidth() / 2, y - hotbarBackground.getHeight());
		for (int i = 0; i < slots.length; i++) {
			slots[i].setLocation(hotbarBackground.getX() + (i * 32) + (9 + (i * 5)), hotbarBackground.getY() + 4);
		}
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
}
