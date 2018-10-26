package wow.game.util;

/**
 * A class to handle chat messages.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ChatMessage {

	private String username;
	private String message;
	
	public ChatMessage(String username, String message) {
		this.username = username;
		this.message = message;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getMessage() {
		return message;
	}
}
