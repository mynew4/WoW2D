package wow.game.gui;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.util.ChatMessage;
import wow.game.util.manager.NetworkManager;

/**
 * A class to handle the chat gui.
 * @author Xolitude (October 26, 2018)
 *
 */
public class GuiChat {

	private Rectangle chatLog;
	private GuiBasicImage chatBox;
	private TextField chatField;
	
	private Rectangle tab_Chat;
	
	private LinkedList<ChatMessage> messages;
	
	public GuiChat(GameContainer container) throws SlickException {
		chatLog = new Rectangle(0, 0, 375, 150);
		chatLog.setLocation(0, WoW.Height - chatLog.getHeight());
		
		chatBox = new GuiBasicImage("res/ui/wow_chatfield.png");
		chatBox.setLocation(container.getWidth() / 2 - chatBox.getWidth() / 2, container.getHeight() - 100);
		chatBox.setVisible(false);

		chatField = new TextField(container, container.getDefaultFont(), (int)chatBox.getLocation().x + 5, (int)chatBox.getLocation().y + 2, chatBox.getWidth() - 10, chatBox.getHeight() - 5);
		chatField.setBorderColor(Color.transparent);
		chatField.setMaxLength(46);
		
		tab_Chat = new Rectangle(0, 0, 65, 20);
		tab_Chat.setLocation(chatLog.getLocation().x, chatLog.getLocation().y - tab_Chat.getHeight());
		
		messages = new LinkedList<ChatMessage>();
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setFont(WoW.getSmallFont());
		graphics.setColor(new Color(0f, 0f, 0f, 0.75f));
		graphics.fill(chatLog);
		graphics.setColor(Color.gray);
		graphics.drawRect(chatLog.getX(), chatLog.getY(), chatLog.getWidth(), chatLog.getHeight());
		graphics.setColor(new Color(0f, 0f, 0f, 0.75f));
		graphics.fill(tab_Chat);
		graphics.setColor(Color.gray);
		graphics.drawRect(tab_Chat.getX(), tab_Chat.getY(), tab_Chat.getWidth(), tab_Chat.getHeight());
		graphics.setColor(Color.white);
		graphics.drawString("Chat", tab_Chat.getLocation().x + tab_Chat.getWidth() / 2 - graphics.getFont().getWidth("Chat") / 2, tab_Chat.getY() + tab_Chat.getHeight() / 2 - graphics.getFont().getHeight("Chat") / 2 - 2);
		chatBox.render(container, sbg, graphics);
		
		graphics.setColor(Color.white);
		if (chatBox.isVisible())
			chatField.render(container, graphics);
		
		if (messages.size() > 0) {
			for (int i = 0; i < messages.size(); i++) {
				ChatMessage message = messages.get(i);
				String username = message.getUsername();
				String displayableMessage = null;
				if (message.getTag().equalsIgnoreCase("server"))
					graphics.setColor(Color.yellow);
				else
					graphics.setColor(Color.white);
				
				if (username == null) {
					displayableMessage = message.getMessage();
				} else {
					if (!message.getTag().equalsIgnoreCase("server") && !message.getTag().equalsIgnoreCase("player")) {
						displayableMessage = String.format("[%s][%s] %s", message.getTag(), username, message.getMessage());
					}
					
					if (message.getTag().equalsIgnoreCase("player")) {
						displayableMessage = String.format("[%s] %s", username, message.getMessage());
					}
				}
				
				if (i == 0) {
					graphics.drawString(displayableMessage, chatLog.getLocation().x, chatLog.getLocation().y);
				} else {
					graphics.drawString(displayableMessage, chatLog.getLocation().x, chatLog.getLocation().y + graphics.getFont().getLineHeight() * i);
				}
				
				if (message == messages.getLast()) {
					Rectangle r = new Rectangle(chatLog.getLocation().x, chatLog.getLocation().y + graphics.getFont().getLineHeight() * i, graphics.getFont().getWidth(displayableMessage), graphics.getFont().getLineHeight());
					
					if (r.getY() + r.getHeight() - chatLog.getLocation().getY() > chatLog.getHeight()) {
						messages.removeFirst();
					}
				}
			}
		}
	}
	
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if (!chatBox.isVisible())
			chatField.setFocus(false);
		
		for (int i = 0; i < NetworkManager.getChatQueue().size(); i++) {
			ChatMessage chatMessage = NetworkManager.getChatQueue().get(i);
			messages.add(chatMessage);
			NetworkManager.removeChatMessage(chatMessage);
		}
	}
	
	public void sendChat() {
		chatBox.setVisible(!chatBox.isVisible());
		if (!chatField.hasFocus()) {
			chatField.setFocus(true);
			return;
		}
		String text = chatField.getText();
		if (!isChatNullOrWhitespace(text)) {
			text = text.trim();
			NetworkManager.sendChat(text);
		}
		chatField.setText("");
		chatField.setFocus(false);
	}
	
	/**
	 * Check for an empty textbox.
	 * @param text
	 * @return false if text exists, true otherwise.
	 */
	private boolean isChatNullOrWhitespace(String text) {
		if (text == null) 
			return true;
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean hasFocus() {
		return chatField.hasFocus();
	}
}
