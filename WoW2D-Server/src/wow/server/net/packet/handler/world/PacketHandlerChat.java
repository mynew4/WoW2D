package wow.server.net.packet.handler.world;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.world.PacketChatMessage;
import wow.net.packet.world.PacketChatMessageToAll;
import wow.server.net.WoWServer;
import wow.server.net.WorldConnection;
import wow.server.net.commands.AbstractCommand;
import wow.server.util.IPacketHandler;

/**
 * Handles chat packets.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerChat extends IPacketHandler {

	private PacketChatMessage pChatMessage;
	private PacketChatMessageToAll pChatMessageToAll;
	
	public PacketHandlerChat(PacketChatMessage pChatMessage) {
		this.pChatMessage = pChatMessage;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		String message = pChatMessage.Message;
		WorldConnection worldConnection = WoWServer.getProfileFromConnection(connection.getID());
		
		if (message.startsWith(".")) {
			parseCommand(message, worldConnection, server, connection);
		} else {
			sendChatMessage(server, message, worldConnection);
		}
	}
	
	private void parseCommand(String message, WorldConnection worldConnection, Server server, Connection connection) {
		message = message.substring(1);
		String[] cmdParts = message.split(" ");
		String cmd = cmdParts[0];
		
		ArrayList<AbstractCommand> commands = WoWServer.getUserCommands();
		for (int i = 0; i < commands.size(); i++) {
			AbstractCommand command = commands.get(i);
			if (command.getPrefix().equalsIgnoreCase(cmd)) {
				if (worldConnection.getAccountLevel().getLevelId() >= command.getLevel().getLevelId()) {
					command.performCommand(server, connection, worldConnection, cmdParts);
				} else {
					PacketChatMessageToAll pError = new PacketChatMessageToAll();
					pError.Tag = "server";
					pError.Username = null;
					pError.ChatMessage = "Incorrect account level.";
					server.sendToTCP(worldConnection.getConnectionId(), pError);
				}
			}
		}
	}
	
	private void sendChatMessage(Server server, String message, WorldConnection worldConnection) {
		String username = worldConnection.getCharacterName();
		boolean isGM = worldConnection.isGM();
		pChatMessageToAll = new PacketChatMessageToAll();
		if (isGM)
			pChatMessageToAll.Tag = "GM";
		else
			pChatMessageToAll.Tag = "player";
		pChatMessageToAll.Username = username;
		pChatMessageToAll.ChatMessage = message;
		server.sendToAllTCP(pChatMessageToAll);
	}
}
