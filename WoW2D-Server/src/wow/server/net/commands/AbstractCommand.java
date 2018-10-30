package wow.server.net.commands;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.server.net.WorldConnection;
import wow.server.net.WorldConnection.AccountLevel;

/**
 * An extendable class for all commands.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class AbstractCommand {
	
	private String prefix;
	private AccountLevel level;
	
	public AbstractCommand(String prefix, AccountLevel level) {
		this.prefix = prefix;
		this.level = level;
	}
	
	public abstract void performCommand(Server server, Connection connection, WorldConnection worldConnection, String[] commandParts);
	
	public String getPrefix() {
		return prefix;
	}
	
	public AccountLevel getLevel() {
		return level;
	}
}
