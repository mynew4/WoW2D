package wow.server.net.packet.handler.logon;

import java.util.LinkedList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.logon.PacketCharsComplete;
import wow.net.packet.logon.PacketCharsRequest;
import wow.net.packet.logon.PacketCharsResponse;
import wow.server.net.WoWServer;
import wow.server.util.IPacketHandler;

/**
 * Handles sending character lists.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerCharacters extends IPacketHandler {

	private PacketCharsRequest pCharsReq;
	
	public PacketHandlerCharacters(PacketCharsRequest pCharsReq) {
		this.pCharsReq = pCharsReq;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		String accountName = pCharsReq.AccountName;
		int realmId = pCharsReq.RealmID;
		
		LinkedList<String> characters = WoWServer.getCharacters(accountName, realmId);
		if (characters != null) {
			for (int i = 0; i < characters.size(); i++) {
				PacketCharsResponse pCharsResp = new PacketCharsResponse();
				pCharsResp.Name = characters.get(i);
				connection.sendTCP(pCharsResp);
			}
			connection.sendTCP(new PacketCharsComplete());
		}
	}
}
