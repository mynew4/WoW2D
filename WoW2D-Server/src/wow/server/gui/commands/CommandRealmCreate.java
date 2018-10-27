package wow.server.gui.commands;

import wow.server.gui.ServerGUI.LogType;
import wow.server.mysql.DB;
import wow.server.net.WoWServer;

/**
 * Handles realm creation.
 * @author MaxtorCoder (October 27, 2018)
 *
 */

public class CommandRealmCreate extends ICommand {
	
	public CommandRealmCreate() {
		super("realm create");
	}
	
	@Override
	public void performCommand(String args) {
		String[] arguments = args.split(" ");
		
		int realm_id = Integer.parseInt(arguments[2]);
		String realm_name = arguments[3].toUpperCase();
		int realm_port = Integer.parseInt(arguments[4]);
		
		if (DB.createRealm(realm_id, realm_name, realm_port)) {
			WoWServer.writeMessage(LogType.Logon, "Realm created: " + realm_name);
		} else {
			WoWServer.writeMessage(LogType.Logon, "Unable to create realm: " + realm_name);
		}
	}
}
