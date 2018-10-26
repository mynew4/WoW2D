package wow.server.gui.commands;

import wow.server.gui.ServerGUI.LogType;
import wow.server.mysql.DB;
import wow.server.net.WoWServer;

/**
 * Handles setting the gm-level of accounts.
 * @author Xolitude (October 26, 2018)
 *
 */
public class CommandAccountSetLevel extends ICommand {

	public CommandAccountSetLevel() {
		super("account set gmlevel");
	}

	@Override
	public void performCommand(String args) {
		String[] arguments = args.split(" ");
		String username = arguments[3].toUpperCase();
		int level = Integer.valueOf(arguments[4]);
		
		if (DB.setAccountLevel(username, level)) 
			WoWServer.writeMessage(LogType.Logon, String.format("Account level of '%s' set to: %s", username, level));
		else
			WoWServer.writeMessage(LogType.Logon, String.format("Unable to change account level of '%s'.", username));
	}
}
