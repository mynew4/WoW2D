package wow.server.net.commands;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.server.net.WorldConnection;

/**
 * An extendable class for all commands.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class AbstractCommand {
	
	private String prefix;

	public AbstractCommand(String prefix) {
		this.prefix = prefix;
	}
	
	public abstract void performCommand(Server server, Connection connection, WorldConnection worldConnection, String[] commandParts);
	
	public String getPrefix() {
		return prefix;
	}
}
