package wow.game.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.gui.GuiButton;
import wow.game.gui.GuiTextField;
import wow.game.gui.notification.GuiNotificationConfirmation;
import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.LogonConnection.LogonStatus;
import wow.game.objects.PlayerCharacter;
import wow.game.objects.mob.player.race.RaceUndead;
import wow.game.util.manager.NetworkManager;
import wow.game.util.manager.RealmManager;

/**
 * The character creation state.
 * @author Xolitude (October 26, 2018)
 *
 */
public class State2CharCreate extends BasicGameState {
	
	public static final int ID = 2;

	private GuiButton button_Accept;
	private GuiButton button_Back;
	private GuiTextField textfield_CharacterName;
	private GuiNotificationConfirmation notification;
	
	private PlayerCharacter tempCharacter;
	
	private RaceUndead undeadRace;
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		undeadRace = new RaceUndead();
		
		button_Accept = new GuiButton("Accept");
		button_Accept.setLocation(container.getWidth() - button_Accept.getButtonWidth() - 25, container.getHeight() - button_Accept.getButtonHeight() - 65);
		
		button_Back = new GuiButton("Back");
		button_Back.setLocation(button_Accept.getLocation().x, button_Accept.getLocation().y + button_Back.getButtonHeight() + 10);
		
		textfield_CharacterName = new GuiTextField(container);
		textfield_CharacterName.setLocation(container.getWidth() / 2 - textfield_CharacterName.getTextboxWidth() / 2, (int) button_Accept.getLocation().y);
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setFont(container.getDefaultFont());
		button_Accept.render(container, sbg, graphics);
		button_Back.render(container, sbg, graphics);
		textfield_CharacterName.render(container, sbg, graphics);
		
		graphics.setColor(Color.yellow);
		graphics.drawString("Name", container.getWidth() / 2 - graphics.getFont().getWidth("Name") / 2, textfield_CharacterName.getLocation().y - graphics.getFont().getHeight("Name") - 5);
	
		graphics.scale(2f, 2f);
		graphics.drawImage(undeadRace.getSprite(true), (container.getWidth() / 2 - undeadRace.getSprite(true).getWidth() / 2) / 2, (container.getHeight() / 2 - undeadRace.getSprite(true).getHeight() / 2) / 2); //.draw((container.getWidth() / 2 - image.getWidth() / 2) / 2, (container.getHeight() / 2 - image.getHeight() / 2) / 2);
		graphics.resetTransform();
		
		if (notification != null) {
			notification.render(container, sbg, graphics);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		LogonConnection logonConnection = NetworkManager.getLogonConnection();
		
		button_Accept.update(container, sbg);
		{
			if (button_Accept.isPressed()) {
				button_Accept.setPressedFalse();
				String name = textfield_CharacterName.getText().trim();
				String race = "Undead";
				
				name = name.toLowerCase();
				String newName = name.substring(0, 1).toUpperCase() + name.substring(1);
				tempCharacter = new PlayerCharacter(newName, WoW.RaceType.valueOf(race));
				
				logonConnection.sendCreateCharacter(newName);
			}
		}
		button_Back.update(container, sbg);
		{
			if (button_Back.isPressed()) {
				button_Back.setPressedFalse();
				
				sbg.enterState(State1CharSelect.ID);
			}
		}
		
		if (logonConnection != null) {
			LogonStatus status = logonConnection.getStatus();
			switch (status) {
			case CharCreationSuccess:
				RealmManager.addCharacterToRealm(tempCharacter);
				logonConnection.setStatus(LogonStatus.Waiting);
				tempCharacter = null;
				sbg.enterState(State1CharSelect.ID);
				break;
			case CharCreationFailed:
				notification = new GuiNotificationConfirmation(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Invalid name. Please try again.");
				notification.addButton(new GuiButton("Okay"));
				break;
			case Waiting:
				notification = null;
				break;
			}
		}
		
		if (notification != null) {
			notification.update(container, sbg);
			if (notification.isButtonPressed()) {
				notification = null;
				if (logonConnection != null) {
					logonConnection.setStatus(LogonStatus.Waiting);
				}
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}
