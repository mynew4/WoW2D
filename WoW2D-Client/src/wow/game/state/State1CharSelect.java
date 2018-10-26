package wow.game.state;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.gui.GuiBasicImage;
import wow.game.gui.GuiButton;
import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.LogonConnection.LogonStatus;
import wow.game.net.connection.WorldConnection.WorldStatus;
import wow.game.objects.PlayerCharacter;
import wow.game.objects.Realm;
import wow.game.util.manager.NetworkManager;
import wow.game.util.manager.RealmManager;

/**
 * The character selection state.
 * @author Xolitude (October 26, 2018)
 *
 */
public class State1CharSelect extends BasicGameState {
	
	public final static int ID = 1;
	
	private GuiButton button_EnterWorld;
	private GuiButton button_ChangeRealm;
	private GuiButton button_CreateCharacter;
	private GuiButton button_DeleteCharacter;
	private GuiButton button_Back;
	private GuiButton button_AddOns;
	
	private GuiBasicImage character_list_ui;
	private GuiBasicImage character_list_selector;
	
	private Rectangle[] character_List;
	private LinkedList<PlayerCharacter> characters;
	
	private PlayerCharacter charToBeDeleted;

	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {	
		characters = new LinkedList<PlayerCharacter>();
		
		button_EnterWorld = new GuiButton("Enter World");
		button_EnterWorld.setLocation(container.getWidth() / 2 - button_EnterWorld.getButtonWidth() / 2, container.getHeight() - button_EnterWorld.getButtonHeight() - 25);
		button_EnterWorld.setEnabled(false);
				
		character_list_ui = new GuiBasicImage("res/ui/wow_char_select.png");
		character_list_ui.setLocation(container.getWidth() - character_list_ui.getWidth() - 5, 10);
		
		button_ChangeRealm = new GuiButton("Change Realm");
		button_ChangeRealm.setLocation((character_list_ui.getLocation().x + (character_list_ui.getWidth() / 2 - button_ChangeRealm.getButtonWidth() / 2)), 40);
		button_ChangeRealm.setEnabled(false);
		
		button_CreateCharacter = new GuiButton("Create New Character");
		button_CreateCharacter.setLocation((character_list_ui.getLocation().x + (character_list_ui.getWidth() / 2 - button_CreateCharacter.getButtonWidth() / 2)), character_list_ui.getLocation().y + (character_list_ui.getHeight() - button_CreateCharacter.getButtonHeight() - 15));
	
		button_DeleteCharacter = new GuiButton("Delete Character");
		button_DeleteCharacter.setLocation(button_CreateCharacter.getLocation().x, button_EnterWorld.getLocation().y - 50);
		
		button_Back = new GuiButton("Back");
		button_Back.setLocation(button_CreateCharacter.getLocation().x, button_EnterWorld.getLocation().y);
		
		button_AddOns = new GuiButton("AddOns");
		button_AddOns.setLocation(15, button_EnterWorld.getLocation().y);
		button_AddOns.setEnabled(false);
		
		character_list_selector = new GuiBasicImage("res/ui/wow_char_selection.png");	
		
		character_List = new Rectangle[6];
		for (int i = 0; i < character_List.length; i++) {
			character_List[i] = new Rectangle(0, 0, 200, 60);
			if (i == 0) {
				character_List[i].setLocation(character_list_ui.getLocation().x + (character_list_ui.getWidth() / 2 - character_List[i].getWidth() / 2), button_ChangeRealm.getLocation().y + (button_ChangeRealm.getButtonHeight() + 30));
			} else {
				character_List[i].setLocation(character_list_ui.getLocation().x + (character_list_ui.getWidth() / 2 - character_List[i].getWidth() / 2), character_List[i-1].getLocation().y + (character_List[i-1].getHeight() + 10));
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		if (characters != null) {
			for (int i = 0; i < characters.size(); i++) {
				PlayerCharacter playerCharacter = characters.get(i);
				if (!playerCharacter.getRace().hasLoadedAnimations())
					playerCharacter.getRace().loadAnimations();
			}
		}
		
		graphics.setFont(container.getDefaultFont());
		button_EnterWorld.render(container, sbg, graphics);
		character_list_ui.render(container, sbg, graphics);
		button_ChangeRealm.render(container, sbg, graphics);
		button_CreateCharacter.render(container, sbg, graphics);
		button_DeleteCharacter.render(container, sbg, graphics);
		button_Back.render(container, sbg, graphics);
		button_AddOns.render(container, sbg, graphics);
		
		graphics.setColor(Color.gray);
		try {
			Realm realm = RealmManager.getRealm(0);
			if (realm != null)
				graphics.drawString(realm.getName(), (character_list_ui.getLocation().x + (character_list_ui.getWidth() / 2 - graphics.getFont().getWidth(realm.getName()) / 2)), 20);
		} catch (Exception e) {}
		
		for (int i = 0; i < characters.size(); i++) {
			PlayerCharacter character = characters.get(i);
			if (character.isSelected()) {
				character_list_selector.render(container, sbg, graphics);
				graphics.scale(2f, 2f);
				graphics.drawImage(character.getRace().getSprite(true), (container.getWidth() / 2 - character.getRace().getSprite(true).getWidth() / 2) / 2, (container.getHeight() / 2 - character.getRace().getSprite(true).getHeight() / 2) / 2);
				graphics.resetTransform();
				
				graphics.setColor(Color.yellow);
				graphics.drawString(character.getName(), container.getWidth() / 2 - graphics.getFont().getWidth(character.getName()) / 2, button_EnterWorld.getLocation().y - graphics.getFont().getHeight(character.getName()) - 10);
			}
		}
		
		if (characters.size() > 0) {
			for (int i = 0; i < characters.size(); i++) {
				PlayerCharacter character = characters.get(i);
				graphics.setColor(Color.yellow);
				graphics.drawString(character.getName(), character_List[i].getX() + 5, character_List[i].getY() + 3);
				graphics.setColor(Color.white);
				graphics.drawString(character.getLevel(), character_List[i].getX() + 5, character_List[i].getY() + graphics.getFont().getHeight(character.getLevel()) + 5);
				graphics.setColor(Color.red);
				graphics.drawString(character.getLocation(), character_List[i].getX() + 5, character_List[i].getY() + graphics.getFont().getHeight(character.getLevel()) * 2 + 8);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		try {
			LinkedList<PlayerCharacter> tempChars = RealmManager.getCharacterList();
			if (characters.size() != tempChars.size()) {
				characters = tempChars;
			}
		} catch (Exception e) {}
		
		button_EnterWorld.update(container, sbg);
		if (button_EnterWorld.isPressed()) {
			button_EnterWorld.setPressedFalse();
			
			PlayerCharacter character = RealmManager.getSelectedCharacter();
			if (character != null)
				NetworkManager.getLogonConnection().sendWorldRequest(character.getName(), character.getLevel(), character.getLocation(), character.getRaceType());
			container.getInput().clearKeyPressedRecord();
		}
		button_CreateCharacter.update(container, sbg);
		{
			if (button_CreateCharacter.isPressed()) {
				button_CreateCharacter.setPressedFalse();
				sbg.enterState(State2CharCreate.ID);
			}
		}
		button_DeleteCharacter.update(container, sbg);
		{
			if (button_DeleteCharacter.isPressed()) {
				button_DeleteCharacter.setPressedFalse();
				for (int i = 0; i < characters.size(); i++) {
					PlayerCharacter character = characters.get(i);
					if (character.isSelected()) {
						NetworkManager.getLogonConnection().sendDeleteCharacter(character.getName());
						charToBeDeleted = character;
					}
				}
			}
		}
		button_Back.update(container, sbg);
		{
			if (button_Back.isPressed()) {
				button_Back.setPressedFalse();
				
				NetworkManager.disconnectLogon();
				sbg.enterState(State0Login.ID);
			}
		}
		
		Input input = container.getInput();
		updateSelected(input);
		
		if (characters.size() > 0) {
			for (int i = 0; i < characters.size(); i++) {
				PlayerCharacter character = characters.get(i);
				if (character.isSelected()) {
					character_list_selector.setLocation(character_List[i].getLocation().x, character_List[i].getLocation().y);
					button_EnterWorld.setEnabled(true);
				}
			}
		} else {
			button_EnterWorld.setEnabled(false);
		}
		
		updateStatusChecks(container, sbg);
	}
	
	public void updateSelected(Input input) {
		float mouseX = input.getMouseX();
		float mouseY = input.getMouseY();
		
		// TODO: clean selected char
		try {
			if (characters.size() > 0) {
				if (character_List[0].contains(mouseX, mouseY)) {
					if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						deselectAll();
						characters.get(0).setIsSelected(true);
					}
				}
				
				if (character_List[1].contains(mouseX, mouseY)) {
					if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						deselectAll();
						characters.get(1).setIsSelected(true);

					}
				}
				
				if (character_List[2].contains(mouseX, mouseY)) {
					if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						deselectAll();
						characters.get(2).setIsSelected(true);

					}
				}
				
				if (character_List[3].contains(mouseX, mouseY)) {
					if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						deselectAll();
						characters.get(3).setIsSelected(true);

					}
				}
				
				if (character_List[4].contains(mouseX, mouseY)) {
					if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						deselectAll();
						characters.get(4).setIsSelected(true);

					}
				}
				
				if (character_List[5].contains(mouseX, mouseY)) {
					if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						deselectAll();
						characters.get(5).setIsSelected(true);
					}
				}
			}
		} catch (Exception e) {}
	}
	
	public void updateStatusChecks(GameContainer container, StateBasedGame sbg) throws SlickException {
		LogonConnection logonConnection = NetworkManager.getLogonConnection();
		if (logonConnection != null) {
			if (logonConnection.getStatus() == LogonStatus.CharDeletionSuccess) {
				RealmManager.removeCharacterFromRealm(charToBeDeleted.getName());
				NetworkManager.getLogonConnection().setStatus(LogonStatus.Waiting);
			}
			
			if (logonConnection.getStatus() == LogonStatus.EnterWorld) {
				State3Game game = null;
				for (int i = 0; i < characters.size(); i++) {
					PlayerCharacter character = characters.get(i);
					if (character.isSelected()) {
						game = new State3Game();
						game.init(character);
						game.init(container, sbg);
						sbg.addState(game);
						NetworkManager.initWorld(character);
						NetworkManager.disconnectLogon();
					}
				}
			}
		}
		
		if (NetworkManager.getWorldConnection() != null) {
			if (NetworkManager.getWorldConnection().getStatus() == WorldStatus.EnterWorldSuccess) {
				State3Game game = (State3Game)sbg.getState(State3Game.ID);
				game.spawnPlayer(NetworkManager.getWorldConnection().getPlayerX(), NetworkManager.getWorldConnection().getPlayerY(), NetworkManager.getWorldConnection().getPlayerDirection());
				NetworkManager.getWorldConnection().setStatus(WorldStatus.Waiting);
				sbg.enterState(State3Game.ID);
			}
		}
	}
	
	public void deselectAll() {
		for (int i = 0; i < characters.size(); i++) {
			PlayerCharacter character = characters.get(i);
			if (character.isSelected()) {
				character.setIsSelected(false);
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}
