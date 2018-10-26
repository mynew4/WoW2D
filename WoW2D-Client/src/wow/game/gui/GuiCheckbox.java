package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A class to handle the checkbox's in the game.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiCheckbox {

	private final GuiBasicImage checkmark = new GuiBasicImage("res/ui/checkbox/Checkmark.png");
	private final GuiBasicImage checkboxBG = new GuiBasicImage("res/ui/checkbox/CheckboxBG.png");
	private final GuiBasicImage checkboxBorder = new GuiBasicImage("res/ui/checkbox/Border.png");
	
	private String text;
	private Rectangle bounds;
	
	public GuiCheckbox(GameContainer container, String text) throws SlickException {
		this.text = text;
		this.bounds = new Rectangle(0, 0, checkboxBorder.getWidth(), checkboxBorder.getHeight());
		
		checkmark.setVisible(true);
		checkboxBG.setVisible(true);
		checkboxBorder.setVisible(true);
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		checkboxBG.render(container, sbg, graphics);
		checkboxBorder.render(container, sbg, graphics);
		checkmark.render(container, sbg, graphics);
		
		graphics.setColor(Color.yellow);
		graphics.drawString(text, checkboxBorder.getLocation().x + 25, checkboxBorder.getLocation().y + checkboxBorder.getHeight() / 2 - graphics.getFont().getHeight(text) / 2 - 2);
	}
	
	public void update(GameContainer container, StateBasedGame sbg) throws SlickException {
		Input input = container.getInput();
		
		float mouseX = input.getMouseX();
		float mouseY = input.getMouseY();
		
		if (bounds.contains(mouseX, mouseY)) {
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				checkmark.setVisible(!checkmark.isVisible());
			}
		}
	}
	
	public void setLocation(float x, float y) {
		checkmark.setLocation(x, y);
		checkboxBG.setLocation(x, y);
		checkboxBorder.setLocation(x, y);
		bounds.setLocation(x, y);
	}
	
	public void setToggled(boolean isToggled) {
		checkmark.setVisible(isToggled);
	}
	
	public boolean isToggled() {
		return checkmark.isVisible();
	}
}
