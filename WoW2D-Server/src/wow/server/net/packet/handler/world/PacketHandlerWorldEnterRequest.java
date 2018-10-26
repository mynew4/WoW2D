package wow.server.net.packet.handler.world;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.world.PacketChatMessageToAll;
import wow.net.packet.world.PacketWorldEnterRequest;
import wow.net.packet.world.PacketWorldEnterResponse;
import wow.net.packet.world.PacketWorldEnterToAll;
import wow.server.gui.ServerGUI.LogType;
import wow.server.net.WoWServer;
import wow.server.net.WorldConnection;
import wow.server.util.IPacketHandler;
import wow.server.util.Vector2f;

/**
 * Handle world-enter requests.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerWorldEnterRequest extends IPacketHandler {

	private PacketWorldEnterRequest pWorldEnterReq;
	
	private PacketWorldEnterResponse pWorldEnterResp;
	private PacketChatMessageToAll pMotd;
	private PacketWorldEnterToAll pWorldNewPlayer;
	
	public PacketHandlerWorldEnterRequest(PacketWorldEnterRequest pWorldEnterReq) {
		this.pWorldEnterReq = pWorldEnterReq;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		String accountName = pWorldEnterReq.AccountName;
		String characterName = pWorldEnterReq.CharacterName;
		
		WorldConnection worldConnection = null;
		if ((worldConnection = WoWServer.isUserOnWorld(accountName)) != null) {
			Vector2f vector = WoWServer.getCharacterPosition(characterName);
			
			worldConnection.setConnectionId(connection.getID());
			worldConnection.setLocation(vector.getX(), vector.getY(), vector.getDirection());
			
			pWorldEnterResp = new PacketWorldEnterResponse();
			pWorldEnterResp.Code = 0;
			pWorldEnterResp.X = vector.getX();
			pWorldEnterResp.Y = vector.getY();
			pWorldEnterResp.Direction = vector.getDirection();
			connection.sendTCP(pWorldEnterResp);
			WoWServer.writeMessage(LogType.World, String.format("%s has joined the world!", characterName));
			WoWServer.addPlayerToGUI(characterName);
			
			pMotd = new PacketChatMessageToAll();
			pMotd.Username = "Server";
			pMotd.ChatMessage = WoWServer.getMOTD();
			connection.sendTCP(pMotd);
			
			pWorldNewPlayer = new PacketWorldEnterToAll();
			pWorldNewPlayer.CharacterName = characterName;
			pWorldNewPlayer.CharacterRace = worldConnection.getCharacterRace();
			pWorldNewPlayer.X = worldConnection.getX();
			pWorldNewPlayer.Y = worldConnection.getY();
			pWorldNewPlayer.Direction = worldConnection.getDirection();
			server.sendToAllExceptTCP(worldConnection.getConnectionId(), pWorldNewPlayer);
		}
	}
}
