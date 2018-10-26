package wow.server.gui.commands;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import wow.server.gui.ServerGUI.LogType;
import wow.server.mysql.DB;
import wow.server.net.WoWServer;
import wow.server.util.BCrypt;

/**
 * Handles account creation.
 * @author Xolitude (October 26, 2018)
 *
 */
public class CommandAccountCreate extends ICommand {

	public CommandAccountCreate() {
		super("account create");
	}
	
	@Override
	public void performCommand(String args) {
		String[] arguments = args.split(" ");
		String username = arguments[2].toUpperCase();
		String password = arguments[3];
		
		if (DB.createAccount(username, password)) {
			WoWServer.writeMessage(LogType.Logon, "Account created: "+username);
		} else {
			WoWServer.writeMessage(LogType.Logon, "Unable to create account: "+username);
		}
	}
}
