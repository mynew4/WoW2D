package wow.server.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.logon.PacketRealmRequest;
import wow.net.packet.logon.PacketRealmResponse;
import wow.server.net.WoWServer;
import wow.server.net.game.object.Realm;
import wow.server.util.IPacketHandler;

/**
 * Handles realm requests.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerRealm extends IPacketHandler {
	
	private PacketRealmRequest pRealmReq;
	private PacketRealmResponse pRealmResp;
	
	public PacketHandlerRealm(PacketRealmRequest pRealmReq) {
		this.pRealmReq = pRealmReq;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		pRealmResp = new PacketRealmResponse();
		if (pRealmReq.Code == 10) {
			Realm realm = WoWServer.getRealm(0);
			pRealmResp.ID = realm.getId();
			pRealmResp.Name = realm.getName();
			pRealmResp.Port = realm.getPort();
			connection.sendTCP(pRealmResp);
		} else {
			connection.close();
		}
	}
}
