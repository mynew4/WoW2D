package wow.server.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.logon.PacketCharDeleteRequest;
import wow.net.packet.logon.PacketCharDeleteResponse;
import wow.server.net.WoWServer;
import wow.server.util.IPacketHandler;

/**
 * Handles character deletion packets.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerCharacterDelete extends IPacketHandler {

	private PacketCharDeleteRequest pCharDeleteReq;
	
	public PacketHandlerCharacterDelete(PacketCharDeleteRequest pCharDeleteReq) {
		this.pCharDeleteReq = pCharDeleteReq;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		String accountName = pCharDeleteReq.AccountName;
		String characterName = pCharDeleteReq.Name;
		int realmId = pCharDeleteReq.RealmID;
		
		WoWServer.deleteCharacterFromAccount(accountName, characterName, realmId);
		
		connection.sendTCP(new PacketCharDeleteResponse());
	}
}
