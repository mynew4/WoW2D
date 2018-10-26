package wow.server.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.logon.PacketWorldRequest;
import wow.net.packet.logon.PacketWorldResponse;
import wow.server.net.WoWServer;
import wow.server.util.IPacketHandler;

/**
 * Handles world-enter requests.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerWorldRequest extends IPacketHandler {

	private PacketWorldRequest pWorldReq;
	
	public PacketHandlerWorldRequest(PacketWorldRequest pWorldReq) {
		this.pWorldReq = pWorldReq;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		String accountName = pWorldReq.AccountName;
		
		if (WoWServer.isUserOnline(accountName)) {
			String characterName = pWorldReq.CharacterName;
			String characterLevel = pWorldReq.CharacterLevel;
			String characterLocation = pWorldReq.CharacterLocation;
			String characterRace = pWorldReq.CharacterRace;
			
			WoWServer.addWorldConnection(accountName, characterName, characterLevel, characterLocation, characterRace);
			
			connection.sendTCP(new PacketWorldResponse());
			connection.close();
		}
	}
}
