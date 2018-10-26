package wow.game.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A class to hold basic information about an image.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiBasicImage {

	private Image image;
	
	private float x, y;
	
	private boolean isVisible = true;
	
	public GuiBasicImage(String ref) throws SlickException {
		image = new Image(ref);
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		if (isVisible)
			graphics.drawImage(image, x, y);
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f getLocation() {
		return new Vector2f(x, y);
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
}
