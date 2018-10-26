package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A slick2d.textfield helper class.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiTextField {

	private Image textbox = new Image("res/ui/wow_textbox.png");
	private TextField textfield;
	
	private int x, y;
	
	public GuiTextField(GameContainer container) throws SlickException {
		textfield = new TextField(container, container.getDefaultFont(), 0, 0, 150, 24);
		textfield.setBorderColor(Color.transparent);
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.drawImage(textbox, x, y);
		graphics.setColor(Color.white);
		textfield.render(container, graphics);
	}
	
	public void clear() {
		textfield.setText("");
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		textfield.setLocation(x + (textbox.getWidth() / 2 - textfield.getWidth() / 2), y + (textbox.getHeight() / 2 - textfield.getHeight() / 2) + 2);
	}
	
	public void setFocus(boolean shouldFocus) {
		textfield.setFocus(shouldFocus);
	}
	
	public void setBackgroundTransparent() {
		textfield.setBackgroundColor(Color.transparent);
	}
	
	public void setText(String text) {
		textfield.setText(text);
		textfield.setCursorPos(textfield.getText().length());
	}
	
	public Vector2f getLocation() {
		return new Vector2f(x, y);
	}
	
	public boolean hasFocus() {
		return textfield.hasFocus();
	}
	
	public String getText() { 
		return textfield.getText();
	}
	
	public int getTextboxWidth() {
		return textbox.getWidth();
	}
	
	public int getTextboxHeight() {
		return textbox.getHeight();
	}
	
	public int getTextFieldWidth() {
		return textfield.getWidth();
	}
	
	public int getTextFieldHeight() {
		return textfield.getHeight();
	}
}
