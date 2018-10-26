package wow.game.net.connection;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import wow.game.net.packet.handler.logon.PacketHandlerCharacterCreation;
import wow.game.net.packet.handler.logon.PacketHandlerCharacters;
import wow.game.net.packet.handler.logon.PacketHandlerLogin;
import wow.game.net.packet.handler.logon.PacketHandlerRealm;
import wow.game.util.manager.NetworkManager;
import wow.net.Network;
import wow.net.packet.logon.PacketCharCreateRequest;
import wow.net.packet.logon.PacketCharCreateResponse;
import wow.net.packet.logon.PacketCharDeleteRequest;
import wow.net.packet.logon.PacketCharDeleteResponse;
import wow.net.packet.logon.PacketCharsComplete;
import wow.net.packet.logon.PacketCharsResponse;
import wow.net.packet.logon.PacketLogin;
import wow.net.packet.logon.PacketLoginResponse;
import wow.net.packet.logon.PacketRealmResponse;
import wow.net.packet.logon.PacketWorldRequest;
import wow.net.packet.logon.PacketWorldResponse;
import wow.net.util.Logger;

/**
 * Handle the logon-connection.
 * @author Xolitude (October 26, 2018)
 *
 */
public class LogonConnection {

	private Client client;
	
	public enum LogonStatus {
		Waiting,
		Connecting,
		ConnectingFailed,
		Authenticating,
		AuthenticatingUnk,
		AuthenticatingLoggedIn,
		RequestingRealm,
		RequestingChars,
		EnterRealm,
		CharCreationSuccess,
		CharCreationFailed,
		CharDeletionSuccess,
		EnterWorld,
	}
	
	private static LogonStatus logonStatus = LogonStatus.Waiting;
	
	/**
	 * Attempt to create a connection and send a login packet.
	 * @param username
	 * @param password
	 */
	public LogonConnection(String username, String password) {
		client = new Client();
		Network.register(client);
		new Thread("connecting") {
			public void run() {
				logonStatus = LogonStatus.Connecting;
				if (!client.isConnected()) {
					client.start();
					try {
						client.connect(5000, NetworkManager.getRealmlist(), NetworkManager.getRealmlistPort());
						sendLogin(username, password);
					} catch (IOException e) {
						logonStatus = LogonStatus.ConnectingFailed;
						Logger.write("Unable to connect: "+e.getMessage());
					}
				}
			}
		}.start();
				
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof PacketLoginResponse) {
					PacketLoginResponse responsePacket = (PacketLoginResponse)object;
					PacketHandlerLogin pHandlerLogin = new PacketHandlerLogin(responsePacket);
					pHandlerLogin.handlePacket(client);
				} else if (object instanceof PacketRealmResponse) {
					PacketRealmResponse responsePacket = (PacketRealmResponse)object;
					PacketHandlerRealm pHandlerRealm = new PacketHandlerRealm(responsePacket);
					pHandlerRealm.handlePacket(client);
				} else if (object instanceof PacketCharsResponse) {
					PacketCharsResponse responsePacket = (PacketCharsResponse)object;
					PacketHandlerCharacters pHandlerCharacters = new PacketHandlerCharacters(responsePacket);
					pHandlerCharacters.handlePacket(client);
				} else if (object instanceof PacketCharsComplete) {
					logonStatus = LogonStatus.EnterRealm;
				} else if (object instanceof PacketCharCreateResponse) {
					PacketCharCreateResponse responsePacket = (PacketCharCreateResponse)object;
					PacketHandlerCharacterCreation pHandlerCharacterCreation = new PacketHandlerCharacterCreation(responsePacket);
					pHandlerCharacterCreation.handlePacket(client);
				} else if (object instanceof PacketCharDeleteResponse) {
					logonStatus = LogonStatus.CharDeletionSuccess;
				} else if (object instanceof PacketWorldResponse) {
					logonStatus = LogonStatus.EnterWorld;
				}
			}
		});		
	}
	
	/**
	 * Send a login packet.
	 * @param username
	 * @param password
	 */
	private void sendLogin(String username, String password) {
		logonStatus = LogonStatus.Authenticating;
		PacketLogin pLogin = new PacketLogin();
		pLogin.Username = username;
		
		String hashedPassword = null;
		try {
			MessageDigest d = MessageDigest.getInstance("SHA-256");
			byte[] hashed = d.digest(password.getBytes());
			hashedPassword = bytesToHex(hashed);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		pLogin.Password = hashedPassword;
		client.sendTCP(pLogin);
	}
	
	/**
	 * Send a character creation request.
	 * @param name
	 */
	public void sendCreateCharacter(String name) {
		PacketCharCreateRequest pCharReq = new PacketCharCreateRequest();
		pCharReq.AccountName = NetworkManager.getAccountUsername();
		pCharReq.Name = name;
		pCharReq.RealmID = 1;
		client.sendTCP(pCharReq);
	}
	
	/**
	 * Send a character deletion request.
	 * @param name
	 */
	public void sendDeleteCharacter(String name) {
		PacketCharDeleteRequest pCharDel = new PacketCharDeleteRequest();
		pCharDel.AccountName = NetworkManager.getAccountUsername();
		pCharDel.Name = name;
		pCharDel.RealmID = 1;
		client.sendTCP(pCharDel);
	}
	
	/**
	 * Send an enter-world request.
	 * @param characterName
	 * @param characterLevel
	 * @param characterLocation
	 * @param characterRace
	 */
	public void sendWorldRequest(String characterName, String characterLevel, String characterLocation, String characterRace) {
		PacketWorldRequest pWorldReq = new PacketWorldRequest();
		pWorldReq.AccountName = NetworkManager.getAccountUsername();
		pWorldReq.CharacterName = characterName;
		pWorldReq.CharacterLevel = characterLevel;
		pWorldReq.CharacterLocation = characterLocation;
		pWorldReq.CharacterRace = characterRace;
		client.sendTCP(pWorldReq);
	}
	
	/**
	 * Close this logon-connection.
	 */
	public void close() {
		logonStatus = LogonStatus.Waiting;
		client.close();
	}
	
	/**
	 * Change the status of the logon-connection.
	 * @param logon_Status
	 */
	public void setStatus(LogonStatus logon_Status) {
		logonStatus = logon_Status;
	}
	
	/**
	 * Get the status of the logon-connection.
	 * @return This logon-connection's status.
	 */
	public LogonStatus getStatus() {
		return logonStatus;
	}
	
	/**
	 * Convert a byte-array to a hex string (Really only used for auth).
	 * @param hash
	 * @return the hex-string of a given byte-array.
	 */
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
