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
	
	// TODO: Condense all commands into one arraylist and check command level against user leve,.
	private void parseCommand(String message, WorldConnection worldConnection, Server server, Connection connection) {
		message = message.substring(1).toLowerCase();
		String[] cmdParts = message.split(" ");
		String cmd = cmdParts[0];
		switch (worldConnection.getAccountLevel()) {
		case Administrator:
			for (int i = 0; i < WoWServer.getAdministratorCommands().size(); i++) {
				AbstractCommand command = WoWServer.getAdministratorCommands().get(i);
				if (command.getPrefix().equalsIgnoreCase(cmd)) {
					command.performCommand(server, connection, worldConnection, cmdParts);
				}
			}
			break;
		case Gamemaster:
			for (int i = 0; i < WoWServer.getGamemasterCommands().size(); i++) {
				AbstractCommand command = WoWServer.getGamemasterCommands().get(i);
				if (command.getPrefix().equalsIgnoreCase(cmd)) {
					command.performCommand(server, connection, worldConnection, cmdParts);
				}
			}
			break;
		case Moderator:
			for (int i = 0; i < WoWServer.getModeratorCommands().size(); i++) {
				AbstractCommand command = WoWServer.getModeratorCommands().get(i);
				if (command.getPrefix().equalsIgnoreCase(cmd)) {
					command.performCommand(server, connection, worldConnection, cmdParts);
				}
			}
			break;
		case Player:
			sendChatMessage(server, message, worldConnection);
			break;
		}
	}
	
	private void sendChatMessage(Server server, String message, WorldConnection worldConnection) {
		pChatMessageToAll = new PacketChatMessageToAll();
		pChatMessageToAll.Username = worldConnection.getCharacterName();
		pChatMessageToAll.ChatMessage = message;
		server.sendToAllTCP(pChatMessageToAll);
	}
}
