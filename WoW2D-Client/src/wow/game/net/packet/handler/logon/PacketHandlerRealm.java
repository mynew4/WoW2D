package wow.game.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Client;

import wow.game.WoW;
import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.LogonConnection.LogonStatus;
import wow.game.objects.Realm;
import wow.game.util.manager.NetworkManager;
import wow.game.util.manager.RealmManager;
import wow.net.packet.logon.PacketCharsRequest;
import wow.net.packet.logon.PacketRealmResponse;

/**
 * Handles realm-get responses.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerRealm extends IPacketHandler {

	private PacketRealmResponse responsePacket;
	
	public PacketHandlerRealm(PacketRealmResponse responsePacket) {
		this.responsePacket = responsePacket;
	}
	
	@Override
	public void handlePacket(Client client) {
		LogonConnection logonConnection = NetworkManager.getLogonConnection();
		switch (responsePacket.Code) {
		case 10:
			RealmManager.addRealm(new Realm(responsePacket.ID, responsePacket.Name, responsePacket.Port));
			
			logonConnection.setStatus(LogonStatus.RequestingChars);
			
			PacketCharsRequest pCharsRequest = new PacketCharsRequest();
			pCharsRequest.AccountName = NetworkManager.getAccountUsername();
			pCharsRequest.RealmID = RealmManager.getRealm(0).getId();
			client.sendTCP(pCharsRequest);
			break;
		default:
			client.close();
			break;
		}
	}
}
