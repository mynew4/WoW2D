package wow.server.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.logon.PacketLogin;
import wow.net.packet.logon.PacketLoginResponse;
import wow.server.gui.ServerGUI.LogType;
import wow.server.mysql.DB;
import wow.server.net.LogonConnection;
import wow.server.net.WoWServer;
import wow.server.util.IPacketHandler;
import wow.server.util.OpCodes;

/**
 * Handles account logins.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerLogin extends IPacketHandler {

	private PacketLogin pLogin;
	private PacketLoginResponse pLoginResp;
	
	public PacketHandlerLogin(PacketLogin pLogin) {
		this.pLogin = pLogin;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		String username = pLogin.Username;
		String password = pLogin.Password;
		
		pLoginResp = new PacketLoginResponse();
		
		if (WoWServer.isUserOnline(username)) {
			WoWServer.writeMessage(LogType.Logon, String.format("%s is already logged in; Disconnecting new connection.", username.toUpperCase()));
			
			pLoginResp.Code = OpCodes.AUTH_ALREADY_LOGGED_IN;
			connection.sendTCP(pLoginResp);
			connection.close();
			return;
		}
		
		LogonConnection temporaryConnection = DB.doesUserExist(username, password);
		if (temporaryConnection != null) {
			temporaryConnection.setConnectionId(connection.getID());
			
			pLoginResp.Code = OpCodes.AUTH_OK;
			connection.sendTCP(pLoginResp);
			
			WoWServer.addTemporaryConnection(temporaryConnection);
		} else {
			pLoginResp.Code = OpCodes.AUTH_UNK;
			connection.sendTCP(pLoginResp);
			connection.close();
		}
	}
}
