package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.gui.action.IAction;

/**
 * A button class for the first few states.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiButton {

	private final Image button = new Image("res/ui/wow_button.png");
	private final Image button_disabled = new Image("res/ui/wow_button_disabled.png");
	private Rectangle button_Bounds;
	
	private String text;
	private float x, y;
	
	private boolean isHovering = false;
	private boolean isPressed = false;
	private boolean isEnabled = true;
	
	public GuiButton(String text) throws SlickException {
		this.text = text;
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		if (isEnabled)
			graphics.drawImage(button, x, y);
		else
			graphics.drawImage(button_disabled, x, y);
		
		if (isEnabled) {
			if (!isHovering)
				graphics.setColor(Color.yellow);
			else
				graphics.setColor(Color.white);
		} else {
			graphics.setColor(Color.gray);
		}
		graphics.drawString(text, (x + (getButtonWidth() / 2 - graphics.getFont().getWidth(text) / 2)), (y + (getButtonHeight() / 2 - graphics.getFont().getHeight(text) / 2)));
	}
	
	public void update(GameContainer container, StateBasedGame sbg) throws SlickException {
		Input input = container.getInput();
		
		float mouseX = input.getMouseX();
		float mouseY = input.getMouseY();
		
		if (isEnabled) {
			if (button_Bounds.contains(mouseX, mouseY)) {
				isHovering = true;
				if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					isPressed = true;
				}
			} else {
				isHovering = false;
			}
		}
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		button_Bounds = new Rectangle(x, y, button.getWidth(), button.getHeight());
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setPressedFalse() {
		isPressed = false;
	}
	
	public boolean isPressed() {
		return isPressed;
	}
	
	public Vector2f getLocation() {
		return new Vector2f(x, y);
	}
	
	public int getButtonWidth() {
		return button.getWidth();
	}
	
	public int getButtonHeight() {
		return button.getHeight();
	}
}
