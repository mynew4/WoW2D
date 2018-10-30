package wow.server.net.commands;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.world.PacketGMUpdate;
import wow.server.net.WorldConnection;
import wow.server.net.WorldConnection.AccountLevel;

/**
 * Handles setting gm status.
 * @author Xolitude (October 26, 2018)
 *
 */
public class CommandGM extends AbstractCommand {

	public CommandGM() {
		super("gm", AccountLevel.Gamemaster);
	}

	@Override
	public void performCommand(Server server, Connection connection, WorldConnection worldConnection, String[] commandParts) {
		String status = commandParts[1].toLowerCase();
		switch (status) {
		case "on":
			worldConnection.setGM(true);
			break;
		case "off":
			worldConnection.setGM(false);
			break;
		}
		PacketGMUpdate pGM = new PacketGMUpdate();
		pGM.Name = worldConnection.getCharacterName();
		pGM.isGM = worldConnection.isGM();
		server.sendToAllTCP(pGM);
	}
}
