package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;

/**
 * Similar to pfUi: A gui meant to show the time, fps and player's money.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiFPS {

	private final GuiBasicImage ui = new GuiBasicImage("res/ui/wow_fps_ui.png");
	
	public GuiFPS(GameContainer container) throws SlickException {
		ui.setLocation(container.getWidth() - ui.getWidth(), container.getHeight() - ui.getHeight());
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setFont(WoW.getSmallFont());
		graphics.setColor(Color.white);
		ui.render(container, sbg, graphics);
		graphics.drawString(String.format("%s fps", container.getFPS()), ui.getLocation().x + 25, ui.getLocation().y + ui.getHeight() / 2 - graphics.getFont().getHeight(String.format("%s fps", container.getFPS())) / 2);
	}
}
