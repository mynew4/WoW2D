package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.gui.action.ButtonActionConfirmInterfaceOptions;
import wow.game.util.SettingsConfiguration;

/**
 * A class to handle the interface menu of the options.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiOptionsInterface {

	private Rectangle optionsUi;
	private GuiCheckbox myNameCheckbox;
	private GuiCheckbox playerNamesCheckbox;
	private GuiCheckbox mobNamesCheckbox;
	private GuiOptionsButton okayButton;
	
	private boolean isVisible = false;
	
	private final String[] optionTypes = new String[] {
		"Names:"	
	};
	
	public GuiOptionsInterface(GameContainer container) throws SlickException {
		optionsUi = new Rectangle(0, 0, 380, 470);
		optionsUi.setLocation(container.getWidth() / 2 - optionsUi.getWidth() / 2, container.getHeight() / 2 - optionsUi.getHeight() / 2);
		
		myNameCheckbox = new GuiCheckbox(container, "My name");
		myNameCheckbox.setLocation(optionsUi.getLocation().x + 10, optionsUi.getLocation().y + 75);
		myNameCheckbox.setToggled(SettingsConfiguration.shouldRenderMyName());
		
		playerNamesCheckbox = new GuiCheckbox(container, "Player names");
		playerNamesCheckbox.setLocation(optionsUi.getLocation().x + 10, optionsUi.getLocation().y + 95);
		playerNamesCheckbox.setToggled(SettingsConfiguration.shouldRenderPlayerNames());
		
		mobNamesCheckbox = new GuiCheckbox(container, "Mob names");
		mobNamesCheckbox.setLocation(optionsUi.getLocation().x + 10, optionsUi.getLocation().y + 115);
		mobNamesCheckbox.setToggled(SettingsConfiguration.shouldRenderMobNames());
		
		okayButton = new GuiOptionsButton("Okay", new ButtonActionConfirmInterfaceOptions());
		okayButton.setLocation(optionsUi.getLocation().x + 25, optionsUi.getLocation().y + optionsUi.getHeight() - okayButton.getHeight() - 10);
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		if (isVisible) {
			graphics.setFont(WoW.getSmallFont());
			graphics.setColor(Color.yellow);
			graphics.drawString("Interface Options", optionsUi.getLocation().x + optionsUi.getWidth() / 2 - graphics.getFont().getWidth("Interface Options") / 2, optionsUi.getLocation().y - graphics.getFont().getHeight("Interface Options"));
			graphics.setColor(new Color(0, 0, 0, 0.75f));
			graphics.fillRect(optionsUi.getX(), optionsUi.getY(), optionsUi.getWidth(), optionsUi.getHeight());
			graphics.setColor(Color.gray);
			graphics.draw(optionsUi);
			
			graphics.setColor(Color.yellow);
			graphics.drawString(optionTypes[0], optionsUi.getLocation().x + 10, optionsUi.getLocation().y + 50);
			myNameCheckbox.render(container, sbg, graphics);
			playerNamesCheckbox.render(container, sbg, graphics);
			mobNamesCheckbox.render(container, sbg, graphics);
			
			graphics.setColor(Color.white);
			okayButton.render(container, sbg, graphics);
		}
	}
	
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if (isVisible) {
			myNameCheckbox.update(container, sbg);
			playerNamesCheckbox.update(container, sbg);
			mobNamesCheckbox.update(container, sbg);
			okayButton.update(container, sbg, delta);
		}
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public boolean shouldRenderMyName() {
		return myNameCheckbox.isToggled();
	}
	
	public boolean shouldRenderPlayerNames() {
		return playerNamesCheckbox.isToggled();
	}
	
	public boolean shouldRenderMobNames() {
		return mobNamesCheckbox.isToggled();
	}
}
