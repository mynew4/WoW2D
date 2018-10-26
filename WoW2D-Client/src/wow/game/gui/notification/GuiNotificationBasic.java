package wow.game.gui.notification;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Basic notification gui.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiNotificationBasic extends GuiNotificationInterface {

	public GuiNotificationBasic(float x, float y, String text) throws SlickException {
		super(x, y, text);
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		if (shouldUpdate) {
			image.render(container, sbg, graphics);
			graphics.setColor(Color.yellow);
			graphics.drawString(text, (x + (image.getWidth() / 2 - graphics.getFont().getWidth(text) / 2)), (y + (image.getHeight() / 2 - graphics.getFont().getHeight(text) / 2) - 25));
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg) throws SlickException {}
}
