package wow.server.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.logon.PacketCharCreateRequest;
import wow.net.packet.logon.PacketCharCreateResponse;
import wow.server.net.WoWServer;
import wow.server.util.IPacketHandler;
import wow.server.util.OpCodes;

/**
 * Handles character creation packets.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerCharacterCreate extends IPacketHandler {

	private PacketCharCreateRequest pCharCreateReq;
	private PacketCharCreateResponse pCharCreateResp;
	
	public PacketHandlerCharacterCreate(PacketCharCreateRequest pCharCreateReq) {
		this.pCharCreateReq = pCharCreateReq;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		pCharCreateResp = new PacketCharCreateResponse();
		
		String accountName = pCharCreateReq.AccountName;
		int realmId = pCharCreateReq.RealmID;
		String characterName = pCharCreateReq.Name;
		
		if (WoWServer.doesCharacterExist(characterName)) {
			pCharCreateResp.Code = OpCodes.AUTH_CHAR_INVALID;
		} else {
			if (WoWServer.isCharacterNameBanned(characterName)) {
				pCharCreateResp.Code = OpCodes.AUTH_CHAR_INVALID;
			} else {
				pCharCreateResp.Code = OpCodes.AUTH_CHAR_OK;
				WoWServer.addCharacterToAccount(accountName, characterName, realmId);
			}
		}
		connection.sendTCP(pCharCreateResp);
	}
}
