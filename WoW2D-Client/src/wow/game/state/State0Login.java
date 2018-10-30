package wow.game.state;

import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.gui.GuiButton;
import wow.game.gui.GuiCheckbox;
import wow.game.gui.GuiTextField;
import wow.game.gui.notification.GuiNotificationBasic;
import wow.game.gui.notification.GuiNotificationConfirmation;
import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.LogonConnection.LogonStatus;
import wow.game.util.SettingsConfiguration;
import wow.game.util.SettingsConfiguration.Keys;
import wow.game.util.manager.NetworkManager;

/**
 * The login state.
 * @author Xolitude (October 26, 2018)
 *
 */
public class State0Login extends BasicGameState {
	
	public static final int ID = 0;
	
	private final String label_AccountName = "Account Name";
	private final String label_AccountPassword = "Account Password";
	private final String label_Copyright = "Copyright 2018 Xolitude.";
	private final String label_BlizzCopyright = "Copyright 2004-2018 Blizzard Entertainment. All Rights Reserved.";
	private String label_LastRealm = null;
	
	private GuiButton button_Login;
	private GuiButton button_Quit;
	
	private GuiTextField textfield_AccountName;
	private GuiTextField textfield_AccountPassword;
	
	private GuiNotificationBasic basicNotification;
	private GuiNotificationConfirmation confirmationNotification;
	
	private GuiCheckbox rememberCheckbox;
	
	private boolean isRememberedAccountSet = false;

	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		button_Login = new GuiButton("Login");
		button_Login.setLocation(container.getWidth() / 2 - button_Login.getButtonWidth() / 2, container.getHeight() / 2 + 90);
		
		button_Quit = new GuiButton("Quit");
		button_Quit.setLocation(container.getWidth() - button_Quit.getButtonWidth() - 25, container.getHeight() - button_Quit.getButtonHeight() - 50);
		
		textfield_AccountName = new GuiTextField(container);
		textfield_AccountName.setLocation(container.getWidth() / 2 - textfield_AccountName.getTextboxWidth() / 2, container.getHeight() / 2 - 42 - textfield_AccountName.getTextboxHeight() / 2);
		
		textfield_AccountPassword = new GuiTextField(container);
		textfield_AccountPassword.setLocation(container.getWidth() / 2 - textfield_AccountPassword.getTextboxWidth() / 2, container.getHeight() / 2 - textfield_AccountPassword.getTextboxHeight() / 2 + 36);		
	
		rememberCheckbox = new GuiCheckbox(container, "Remember Account Name");
		rememberCheckbox.setLocation(25, container.getHeight() - 100);
		
		rememberCheckbox.setToggled(SettingsConfiguration.shouldRememberAccount());
		if (rememberCheckbox.isToggled()) {
			textfield_AccountName.setText(SettingsConfiguration.getAccount());
			textfield_AccountPassword.setFocus(true);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setFont(container.getDefaultFont());
		graphics.setColor(Color.yellow);
		graphics.drawString(label_AccountName, container.getWidth() / 2 - graphics.getFont().getWidth(label_AccountName) / 2, container.getHeight() / 2 - 75 - graphics.getFont().getHeight(label_AccountName) / 2);
		graphics.drawString(label_AccountPassword, container.getWidth() / 2 - graphics.getFont().getWidth(label_AccountPassword) / 2, container.getHeight() / 2 - graphics.getFont().getHeight(label_AccountPassword) / 2);
		graphics.drawString(String.format("Version %s (Build 6) (dev-Alpha)", WoW.getVersion()), 0, container.getHeight() - (graphics.getFont().getLineHeight() * 2));
		graphics.drawString("Oct 29 2018", 0, container.getHeight() - graphics.getFont().getLineHeight());
		graphics.drawString(label_Copyright, container.getWidth() / 2 - graphics.getFont().getWidth(label_Copyright) / 2, container.getHeight() - graphics.getFont().getLineHeight() * 2);
		graphics.drawString(label_BlizzCopyright, container.getWidth() / 2 - graphics.getFont().getWidth(label_BlizzCopyright) / 2, container.getHeight() - graphics.getFont().getLineHeight());
		
		graphics.setColor(Color.gray);
		if (label_LastRealm != null)
			graphics.drawString(label_LastRealm, button_Quit.getLocation().getX() + button_Quit.getButtonWidth() - graphics.getFont().getWidth(label_LastRealm), button_Quit.getLocation().getY() - 80);
		
		rememberCheckbox.render(container, sbg, graphics);
		button_Login.render(container, sbg, graphics);
		button_Quit.render(container, sbg, graphics);
		textfield_AccountName.render(container, sbg, graphics);
		textfield_AccountPassword.render(container, sbg, graphics);
		
		if (basicNotification != null)
			basicNotification.render(container, sbg, graphics);
		
		if (confirmationNotification != null)
			confirmationNotification.render(container, sbg, graphics);
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_TAB)) {
			if (textfield_AccountName.hasFocus()) {
				textfield_AccountPassword.setFocus(true);
			} else {
				textfield_AccountName.setFocus(true);
			}
		}

		if (!textfield_AccountName.hasFocus() && !textfield_AccountPassword.hasFocus())
			textfield_AccountName.setFocus(true);
				
		String lastRealm = SettingsConfiguration.getLastRealm();
		if (!lastRealm.equalsIgnoreCase("null"))
			label_LastRealm = lastRealm;
		
		if (SettingsConfiguration.shouldRememberAccount()) {
			if (!isRememberedAccountSet) {
				textfield_AccountName.setText(SettingsConfiguration.getAccount());
				isRememberedAccountSet = true;
			}
		}
		
		updateButtons(container, sbg);
		updateUI(container, sbg);
	}
	
	private void updateButtons(GameContainer container, StateBasedGame sbg) throws SlickException {
		rememberCheckbox.update(container, sbg);
		
		button_Login.update(container, sbg);
		if (button_Login.isPressed()) {
			button_Login.setPressedFalse();
			
			String accountUsername = textfield_AccountName.getText().trim();
			String accountPassword = textfield_AccountPassword.getText().trim();
			
			textfield_AccountName.clear();
			textfield_AccountPassword.clear();
			
			if (accountUsername.isEmpty()) {
				confirmationNotification = new GuiNotificationConfirmation(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Please enter your account name.");
				confirmationNotification.addButton(new GuiButton("Okay"));
				return;
			}
			
			if (accountPassword.isEmpty()) {
				confirmationNotification = new GuiNotificationConfirmation(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Please enter your account password.");
				confirmationNotification.addButton(new GuiButton("Okay"));
				return;
			}
			
			SettingsConfiguration.setSettingValue(Keys.RememberAccount, rememberCheckbox.isToggled());
			if (rememberCheckbox.isToggled())
				SettingsConfiguration.setSettingValue(Keys.AccountName, accountUsername);
			isRememberedAccountSet = false;
			
			NetworkManager.initLogon(accountUsername, accountPassword);
		}
		
		button_Quit.update(container, sbg);
		if (button_Quit.isPressed())
			container.exit();
	}
	
	private void updateUI(GameContainer container, StateBasedGame sbg) throws SlickException {
		LogonConnection logonConnection = NetworkManager.getLogonConnection();
		if (logonConnection != null) {
			LogonStatus status = logonConnection.getStatus();
			if (basicNotification != null) 
				basicNotification = null;
			if (confirmationNotification != null)
				confirmationNotification = null;
			switch (status) {
			case Connecting:
				basicNotification = new GuiNotificationBasic(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Connecting");
				break;
			case ConnectingFailed:
				confirmationNotification = new GuiNotificationConfirmation(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Unable to connect. Please contact a developer.");
				confirmationNotification.addButton(new GuiButton("Okay"));
				break;
			case Authenticating:
				basicNotification = new GuiNotificationBasic(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Authenticating");
				break;
			case AuthenticatingLoggedIn:
				confirmationNotification = new GuiNotificationConfirmation(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Account is already logged in.");
				confirmationNotification.addButton(new GuiButton("Okay"));
				break;
			case AuthenticatingUnk:
				confirmationNotification = new GuiNotificationConfirmation(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Unknown account. Please try again or contact a developer.");
				confirmationNotification.addButton(new GuiButton("Okay"));
				break;
			case EnterRealm:
				basicNotification = null;
				sbg.enterState(State1CharSelect.ID);
				break;
			case RequestingChars:
				basicNotification = new GuiNotificationBasic(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Retrieving characters");
				break;
			case RequestingRealm:
				basicNotification = new GuiNotificationBasic(container.getWidth() / 2 - 480 / 2, container.getHeight() / 2 - 96 / 2, "Retrieving realm-list");
				break;
			
			}
		}
		
		if (confirmationNotification != null) {
			confirmationNotification.update(container, sbg);
			if (confirmationNotification.isButtonPressed()) {
				confirmationNotification = null;
				if (logonConnection != null) {
					if (logonConnection.getStatus() == LogonStatus.ConnectingFailed)
						NetworkManager.disconnectLogon();
					if (logonConnection.getStatus() == LogonStatus.AuthenticatingUnk)
						NetworkManager.disconnectLogon();
					if (logonConnection.getStatus() == LogonStatus.AuthenticatingLoggedIn)
						NetworkManager.disconnectLogon();
				}
			}
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}
