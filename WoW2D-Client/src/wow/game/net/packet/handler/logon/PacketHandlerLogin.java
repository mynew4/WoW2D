package wow.game.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Client;

import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.LogonConnection.LogonStatus;
import wow.game.util.OpCode;
import wow.game.util.manager.NetworkManager;
import wow.net.packet.logon.PacketLoginResponse;
import wow.net.packet.logon.PacketRealmRequest;

/**
 * Handles login responses.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerLogin extends IPacketHandler {

	private PacketLoginResponse responsePacket;
	
	public PacketHandlerLogin(PacketLoginResponse responsePacket) {
		this.responsePacket = responsePacket;
	}
	
	@Override
	public void handlePacket(Client client) {
		LogonConnection logonConnection = NetworkManager.getLogonConnection();
		switch (responsePacket.Code) {
		case OpCode.AUTH_OK:
			logonConnection.setStatus(LogonStatus.RequestingRealm);
			client.sendTCP(new PacketRealmRequest());
			break;
		case OpCode.AUTH_UNK:
			logonConnection.setStatus(LogonStatus.AuthenticatingUnk);
			client.close();
			break;
		case OpCode.AUTH_ALREADY_LOGGED_IN:
			logonConnection.setStatus(LogonStatus.AuthenticatingLoggedIn);
			client.close();
			break;
		}
	}
}
