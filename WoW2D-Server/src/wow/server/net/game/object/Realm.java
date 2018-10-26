package wow.server.net.game.object;

import wow.server.gui.ServerGUI.LogType;
import wow.server.net.WoWServer;

/**
 * Handles realms.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Realm {

	private int id;
	private String name;
	private int port;
	
	public Realm(int id, String name, int port) {
		this.id = id;
		this.name = name;
		this.port = port;
		WoWServer.writeMessage(LogType.Logon, String.format("Added a new realm: %s:%s", name, port));
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPort() {
		return port;
	}
}
