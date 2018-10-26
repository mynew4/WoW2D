package wow.game.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Client;

import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.LogonConnection.LogonStatus;
import wow.game.util.OpCode;
import wow.game.util.manager.NetworkManager;
import wow.net.packet.logon.PacketCharCreateResponse;

/**
 * Handles character creation responses.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerCharacterCreation extends IPacketHandler {

	private PacketCharCreateResponse responsePacket;
	
	public PacketHandlerCharacterCreation(PacketCharCreateResponse responsePacket) {
		this.responsePacket = responsePacket;
	}
	
	@Override
	public void handlePacket(Client client) {
		LogonConnection logonConnection = NetworkManager.getLogonConnection();
		switch (responsePacket.Code) {
		case OpCode.AUTH_CHAR_OK:
			logonConnection.setStatus(LogonStatus.CharCreationSuccess);
			break;
		case OpCode.AUTH_CHAR_NO:
			logonConnection.setStatus(LogonStatus.CharCreationFailed);
			break;
		}
	}
}
