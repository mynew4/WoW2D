package wow.game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.gui.action.ButtonActionCloseOptions;
import wow.game.gui.action.ButtonActionInterfaceOptions;
import wow.game.gui.action.ButtonActionLogout;

/**
 * A class to handle the options ui.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiOptions {

	private GuiBasicImage optionsUi;
	private GuiOptionsInterface optionsInterfaceUi;
	private GuiOptionsButton button_Logout;
	private GuiOptionsButton button_Interface;
	private GuiOptionsButton button_Return;
	
	private boolean isVisible = false;
	
	public GuiOptions(GameContainer container) throws SlickException {
		optionsUi = new GuiBasicImage("res/ui/wow_options.png");
		optionsUi.setLocation(container.getWidth() / 2 - optionsUi.getWidth() / 2, container.getHeight() / 2 - optionsUi.getHeight() / 2);
		optionsInterfaceUi = new GuiOptionsInterface(container);
		
		button_Logout = new GuiOptionsButton("Logout", new ButtonActionLogout());
		button_Logout.setLocation(optionsUi.getLocation().x + optionsUi.getWidth() / 2 - button_Logout.getWidth() / 2, optionsUi.getLocation().y + optionsUi.getHeight() - button_Logout.getHeight() - 50);
		
		button_Interface = new GuiOptionsButton("Interface", new ButtonActionInterfaceOptions());
		button_Interface.setLocation(button_Logout.getLocation().x, button_Logout.getLocation().y - 40);
	
		button_Return = new GuiOptionsButton("Return", new ButtonActionCloseOptions());
		button_Return.setLocation(optionsUi.getLocation().x + optionsUi.getWidth() / 2 - button_Return.getWidth() / 2, optionsUi.getLocation().y + optionsUi.getHeight() - button_Return.getHeight() - 10);
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		if (isVisible) {
			graphics.setFont(WoW.getSmallFont());
			graphics.setColor(Color.yellow);
			graphics.drawString("Options", optionsUi.getLocation().x + optionsUi.getWidth() / 2 - graphics.getFont().getWidth("Options") / 2, optionsUi.getLocation().y - graphics.getFont().getHeight("Options"));
			optionsUi.render(container, sbg, graphics);
			button_Logout.render(container, sbg, graphics);
			button_Interface.render(container, sbg, graphics);
			button_Return.render(container, sbg, graphics);
		}
		
		if (optionsInterfaceUi.isVisible()) {
			optionsInterfaceUi.render(container, sbg, graphics);
		}
	}
	
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if (isVisible) {
			button_Logout.update(container, sbg, delta);
			button_Interface.update(container, sbg, delta);
			button_Return.update(container, sbg, delta);
		}
		
		if (optionsInterfaceUi.isVisible()) 
			optionsInterfaceUi.update(container, sbg, delta);
	}
	
	public void openInterfaceOptions() {
		isVisible = false;
		optionsInterfaceUi.setVisible(true);
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public void setInterfaceOptionsVisible(boolean isVisible) {
		optionsInterfaceUi.setVisible(isVisible);
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public boolean isInterfaceVisible() {
		return optionsInterfaceUi.isVisible();
	}
	
	public GuiOptionsInterface getInterfaceOptionsUi() {
		return optionsInterfaceUi;
	}
}
