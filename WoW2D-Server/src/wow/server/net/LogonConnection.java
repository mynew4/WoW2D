package wow.server.net;

/**
 * Handles a logon-connection
 * @author Xolitude (October 26, 2018)
 *
 */
public class LogonConnection {

	private int id;
	private int connectionId;
	private String username;
	private int user_level;
	
	public LogonConnection(int id, String username, int user_level) {
		this.id = id;
		this.username = username;
		this.user_level = user_level;
	}
	
	public int getUserId() {
		return id;
	}
	
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
	
	public int getConnectionId() {
		return connectionId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getUserLevel() {
		return user_level;
	}
}
