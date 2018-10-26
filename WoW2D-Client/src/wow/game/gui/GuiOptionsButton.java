package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.gui.action.IAction;

/**
 * A class to handle the option ui's buttons.
 * @author Xolitude
 *
 */
public class GuiOptionsButton {

	private String text;
	private float x, y;
	
	private IAction action;
	
	private GuiBasicImage button;
	private Rectangle button_Bounds;
	
	private boolean isHovering = false;
	
	public GuiOptionsButton(String text, IAction action) throws SlickException {
		this.text = text;
		this.action = action;
		
		button = new GuiBasicImage("res/ui/wow_options_button.png");
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		button.render(container, sbg, graphics);
		graphics.setFont(WoW.getSmallFont());
		graphics.setColor(Color.white);
		graphics.drawString(text, x + button.getWidth() / 2 - graphics.getFont().getWidth(text) / 2, y + button.getHeight() / 2 - graphics.getFont().getHeight(text) / 2);
	
		
		if (isHovering) {
			graphics.setColor(Color.cyan);
			graphics.draw(button_Bounds);
		}
	}
	
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		Input input = container.getInput();
		
		float mouseX = input.getMouseX();
		float mouseY = input.getMouseY();
		
		if (button_Bounds.contains(mouseX, mouseY)) {
			if (!isHovering) {
				isHovering = true;
			}
		} else {
			isHovering = false;
		}
		
		if (isHovering) {
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				action.performAction(container, sbg);
			}
		}
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		button_Bounds = new Rectangle(x, y, button.getWidth(), button.getHeight());
		button.setLocation(x, y);
	}
	
	public Vector2f getLocation() {
		return new Vector2f(x, y);
	}
	
	public int getWidth() {
		return button.getWidth();
	}
	
	public int getHeight() {
		return button.getHeight();
	}
}
