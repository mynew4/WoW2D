package wow.server.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import wow.net.Network;
import wow.net.packet.logon.PacketCharCreateRequest;
import wow.net.packet.logon.PacketCharDeleteRequest;
import wow.net.packet.logon.PacketCharsRequest;
import wow.net.packet.logon.PacketLogin;
import wow.net.packet.logon.PacketRealmRequest;
import wow.net.packet.logon.PacketWorldRequest;
import wow.net.packet.world.PacketChatMessage;
import wow.net.packet.world.PacketMovement_C;
import wow.net.packet.world.PacketPlayerList;
import wow.net.packet.world.PacketPlayerListRequest;
import wow.net.packet.world.PacketWorldDisconnectToAll;
import wow.net.packet.world.PacketWorldEnterRequest;
import wow.net.util.Logger;
import wow.net.util.PlayerList;
import wow.server.gui.ServerGUI;
import wow.server.gui.ServerGUI.LogType;
import wow.server.mysql.DB;
import wow.server.net.commands.AbstractCommand;
import wow.server.net.commands.CommandGM;
import wow.server.net.commands.CommandGObject;
import wow.server.net.game.object.Realm;
import wow.server.net.packet.handler.logon.PacketHandlerCharacterCreate;
import wow.server.net.packet.handler.logon.PacketHandlerCharacterDelete;
import wow.server.net.packet.handler.logon.PacketHandlerCharacters;
import wow.server.net.packet.handler.logon.PacketHandlerLogin;
import wow.server.net.packet.handler.logon.PacketHandlerRealm;
import wow.server.net.packet.handler.logon.PacketHandlerWorldRequest;
import wow.server.net.packet.handler.world.PacketHandlerChat;
import wow.server.net.packet.handler.world.PacketHandlerMovement;
import wow.server.net.packet.handler.world.PacketHandlerWorldEnterRequest;
import wow.server.util.ServerConfiguration;
import wow.server.util.Vector2f;
import wow.server.util.manager.EntityManager;

/**
 * The main server class.
 * @author Xolitude (October 26, 2018)
 *
 */
public class WoWServer {
	
	public static String VERSION = "a6.0.0";
	
	/* Used in authentication */
	public static String STATIC_SALT = "wow_2dimensions";

	private Server logonServer;
	private static Server worldServer;
	
	private static ArrayList<Realm> realms;
	private static ArrayList<LogonConnection> temporaryConnections;
	private static ArrayList<WorldConnection> worldConnections;
	
	private static ServerGUI serverGui;
	
	private static ArrayList<AbstractCommand> PlayerCommands = new ArrayList<AbstractCommand>();
	private static ArrayList<AbstractCommand> ModeratorCommands = new ArrayList<AbstractCommand>();
	private static ArrayList<AbstractCommand> GamemasterCommands = new ArrayList<AbstractCommand>();
	private static ArrayList<AbstractCommand> AdministratorCommands = new ArrayList<AbstractCommand>();
	
	private static ArrayList<String> BannedNames = new ArrayList<String>();
	private static ArrayList<String> UsedNames = new ArrayList<String>();
	
	static {
		ModeratorCommands.add(new CommandGM());
		
		GamemasterCommands.add(new CommandGM());
		GamemasterCommands.add(new CommandGObject());
		
		AdministratorCommands.add(new CommandGM());
		AdministratorCommands.add(new CommandGObject());
	}
	
	public WoWServer() {	
		Logger.newServerInstance();
		ServerConfiguration.newInstance();
		DB.newInstance();
		BannedNames = DB.loadBannedNames();
		UsedNames = DB.loadUsedNames();
		EntityManager.addEntities(DB.loadEntityTable());
		EntityManager.startScriptSystem();
		
		if (ServerConfiguration.shouldUseGUI())
			serverGui = new ServerGUI();
		
		writeMessage(LogType.Server, "WoW-2D "+VERSION);
		writeMessage(LogType.Server, String.format("Loaded %s banned names.", BannedNames.size()));
		writeMessage(LogType.Server, String.format("Loaded %s used names.", UsedNames.size()));
		writeMessage(LogType.Server, String.format("Loaded %s entities.", EntityManager.getSize()));
		
		realms = new ArrayList<Realm>();
		realms.addAll(DB.fetchRealms());
		temporaryConnections = new ArrayList<LogonConnection>();
		worldConnections = new ArrayList<WorldConnection>();
		
		logonServer = new Server();
		logonServer.start();
		
		worldServer = new Server();
		worldServer.start();
		
		Network.register(logonServer);
		Network.register(worldServer);
		try {
			logonServer.bind(ServerConfiguration.getAuthenticationPort());
			worldServer.bind(realms.get(0).getPort() + 1, realms.get(0).getPort());
		} catch (IOException e) {}
		
		authServerListener();
		worldServerListener();
		
		writeMessage(LogType.Server, "Authentication listening on: " + ServerConfiguration.getAuthenticationPort());
		writeMessage(LogType.Server, String.format("World listening on TCP: %s/UDP: %s", realms.get(0).getPort() + 1, realms.get(0).getPort()));
	}
	
	private void authServerListener() {
		logonServer.addListener(new Listener() {
			public void disconnected(Connection connection) {
				for (int i = 0; i < temporaryConnections.size(); i++) {
					LogonConnection logonConnection = temporaryConnections.get(i);
					if (logonConnection.getConnectionId() == connection.getID()) {
						writeMessage(LogType.Logon, String.format("%s has disconnected.", logonConnection.getUsername()));
						temporaryConnections.remove(logonConnection);
					}
				}
			}
		});
		
		logonServer.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof PacketLogin) {
					PacketHandlerLogin pHandlerLogin = new PacketHandlerLogin((PacketLogin)object);
					pHandlerLogin.handlePacket(logonServer, connection);
				}
				
				if (object instanceof PacketRealmRequest) {
					PacketHandlerRealm pHandlerRealm = new PacketHandlerRealm((PacketRealmRequest)object);
					pHandlerRealm.handlePacket(logonServer, connection);
				}
				
				if (object instanceof PacketCharsRequest) {
					PacketHandlerCharacters pHandlerChars = new PacketHandlerCharacters((PacketCharsRequest)object);
					pHandlerChars.handlePacket(logonServer, connection);
				}
				
				if (object instanceof PacketCharCreateRequest) {
					PacketHandlerCharacterCreate pHandlerCharCreate = new PacketHandlerCharacterCreate((PacketCharCreateRequest)object);
					pHandlerCharCreate.handlePacket(logonServer, connection);
				}
				
				if (object instanceof PacketCharDeleteRequest) {
					PacketHandlerCharacterDelete pHandlerCharDelete = new PacketHandlerCharacterDelete((PacketCharDeleteRequest)object);
					pHandlerCharDelete.handlePacket(logonServer, connection);
				}
				
				if (object instanceof PacketWorldRequest) {
					PacketHandlerWorldRequest pHandlerWorldReq = new PacketHandlerWorldRequest((PacketWorldRequest)object);
					pHandlerWorldReq.handlePacket(logonServer, connection);
				}
			}
		});
	}
	
	private void worldServerListener() {
		worldServer.addListener(new Listener() {
			public void disconnected(Connection connection) {
				for (int i = 0; i < worldConnections.size(); i++) {
					WorldConnection worldConnection = worldConnections.get(i);
					if (connection.getID() == worldConnection.getConnectionId()) {
						writeMessage(LogType.World, String.format("%s has left the world!", worldConnection.getCharacterName()));
						PacketWorldDisconnectToAll pDisconnectAll = new PacketWorldDisconnectToAll();
						pDisconnectAll.CharacterName = worldConnection.getCharacterName();
						worldServer.sendToAllTCP(pDisconnectAll);
						if (ServerConfiguration.shouldUseGUI())
							serverGui.removePlayer(worldConnection.getCharacterName());
						DB.saveCharacter(worldConnection);
						worldConnections.remove(worldConnection);
					}
				}
			}
		});
		
		worldServer.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof PacketWorldEnterRequest) {
					PacketHandlerWorldEnterRequest pHandlerWorldEnterReq = new PacketHandlerWorldEnterRequest((PacketWorldEnterRequest)object);
					pHandlerWorldEnterReq.handlePacket(worldServer, connection);
				}
				
				if (object instanceof PacketPlayerListRequest) {
					sendPlayerList(connection.getID());
				}
				
				if (object instanceof PacketMovement_C) {
					PacketHandlerMovement pHandlerMovement = new PacketHandlerMovement((PacketMovement_C)object);
					pHandlerMovement.handlePacket(worldServer, connection);
				}
				
				if (object instanceof PacketChatMessage) {
					PacketHandlerChat pHandlerChat = new PacketHandlerChat((PacketChatMessage)object);
					pHandlerChat.handlePacket(worldServer, connection);
				}
			}
		});
	}
	
	/**
	 * Send all players to a new connection.
	 * @param connectionId
	 */
	private void sendPlayerList(int connectionId) {
		LinkedList<PlayerList> playerList = new LinkedList<PlayerList>();
		for (int i = 0; i < worldConnections.size(); i++) {
			WorldConnection worldConnection = worldConnections.get(i);
			if (worldConnection.getConnectionId() != connectionId) {
				PlayerList pPlayerList = new PlayerList();
				pPlayerList.CharacterName = worldConnection.getCharacterName();
				pPlayerList.CharacterRace = worldConnection.getCharacterRace();
				pPlayerList.X = worldConnection.getX();
				pPlayerList.Y = worldConnection.getY();
				pPlayerList.Direction = worldConnection.getDirection();
				playerList.add(pPlayerList);
			}
		}
		PacketPlayerList pPlayerList = new PacketPlayerList();
		pPlayerList.PlayerList = playerList;
		worldServer.sendToTCP(connectionId, pPlayerList);
	}
	
	/**
	 * Create a temporary connection.
	 * @param temporaryConnection
	 */
	public static void addTemporaryConnection(LogonConnection temporaryConnection) {
		temporaryConnections.add(temporaryConnection);
	}
	
	/**
	 * Create a world connection.
	 * @param accountName
	 * @param characterName
	 * @param characterLevel
	 * @param characterLocation
	 * @param characterRace
	 */
	public static void addWorldConnection(String accountName, String characterName, String characterLevel, String characterLocation, String characterRace) {
		int userId = -1;
		int userLevel = -1;
		
		for (int i = 0; i < temporaryConnections.size(); i++) {
			LogonConnection temporaryConnection = temporaryConnections.get(i);
			if (temporaryConnection.getUsername().equalsIgnoreCase(accountName)) {
				userId = temporaryConnection.getUserId();
				userLevel = temporaryConnection.getUserLevel();
				temporaryConnections.remove(temporaryConnection);
			}
		}
		
		WorldConnection worldConnection = new WorldConnection(userId, accountName, userLevel, characterName, characterLevel, characterLocation, characterRace);
		worldConnections.add(worldConnection);
	}
	
	/**
	 * Adds a player to the server gui if we use it.
	 * @param characterName
	 */
	public static void addPlayerToGUI(String characterName) {
		if (ServerConfiguration.shouldUseGUI())
			serverGui.addPlayer(characterName);
	}
	
	/**
	 * Is a user currently online?
	 * @param username
	 * @return true if they are, false otherwise
	 */
	public static boolean isUserOnline(String username) {
		if (temporaryConnections.size() > 0) {
			for (int i = 0; i < temporaryConnections.size(); i++) {
				LogonConnection logonConnection = temporaryConnections.get(i);
				if (logonConnection.getUsername().equalsIgnoreCase(username)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Is a user currently online, in the world?
	 * @param accountName
	 * @return true if they are, false otherwise
	 */
	public static WorldConnection isUserOnWorld(String accountName) {
		for (int i = 0; i < worldConnections.size(); i++) {
			WorldConnection worldConnection = worldConnections.get(i);
			if (worldConnection.getAccountName().equalsIgnoreCase(accountName)) {
				return worldConnection;
			}
		}
		return null;
	}
	
	/**
	 * Check for a banned name.
	 * @param characterName
	 * @return true if it is, false otherwise
	 */
	public static boolean isCharacterNameBanned(String characterName) {
		for (String str : BannedNames) {
			if (str.equalsIgnoreCase(characterName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check for an existing character name..
	 * @param characterName
	 * @return true if it is, false otherwise
	 */
	public static boolean doesCharacterExist(String characterName) {
		for (String str : UsedNames) {
			if (str.equalsIgnoreCase(characterName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a character to an account.
	 * @param accountName
	 * @param characterName
	 * @param realmId
	 */
	public static void addCharacterToAccount(String accountName, String characterName, int realmId) {
		for (int i = 0; i < temporaryConnections.size(); i++) {
			LogonConnection temporaryConnection = temporaryConnections.get(i);
			if (temporaryConnection.getUsername().equalsIgnoreCase(accountName)) {
				DB.addCharacter(temporaryConnection.getUserId(), characterName, realmId);
				UsedNames.add(characterName);
			}
		}
	}
	
	/**
	 * Delete a character from an account.
	 * @param accountName
	 * @param characterName
	 * @param realmId
	 */
	public static void deleteCharacterFromAccount(String accountName, String characterName, int realmId) {
		for (int i = 0; i < temporaryConnections.size(); i++) {
			LogonConnection temporaryConnection = temporaryConnections.get(i);
			if (temporaryConnection.getUsername().toLowerCase().equals(accountName.toLowerCase())) {
				DB.deleteCharacter(temporaryConnection.getUserId(), characterName, realmId);
				UsedNames.remove(characterName);
			}
		}
	}
	
	/**
	 * Get all characters for an account on a specified realm.
	 * @param accountName
	 * @param realmId
	 * @return a list of characters for an account on a realm
	 */
	public static LinkedList<String> getCharacters(String accountName, int realmId) {
		for (int i = 0; i < temporaryConnections.size(); i++) {
			LogonConnection temporaryConnection = temporaryConnections.get(i);
			if (temporaryConnection.getUsername().equalsIgnoreCase(accountName)) {
				return DB.getCharactersForUser(temporaryConnection.getUserId(), realmId);
			}
		}
		return null;
	}
	
	/**
	 * Set the position of a character in the world.
	 * @param connectionId
	 * @param x
	 * @param y
	 * @param direction
	 * @return the name of the character to send to all online players
	 */
	public static String setCharacterPosition(int connectionId, float x, float y, int direction) {
		for (int i = 0; i < worldConnections.size(); i++) {
			WorldConnection worldConnection = worldConnections.get(i);
			if (worldConnection.getConnectionId() == connectionId) {
				worldConnection.setLocation(x, y, direction);
				return worldConnection.getCharacterName();
			}
		}
		return null;
	}
	
	/**
	 * Get the position of the given character.
	 * @param characterName
	 * @return a vector of the character
	 */
	public static Vector2f getCharacterPosition(String characterName) {
		return DB.getCharacterPosition(characterName);
	}
	
	/**
	 * Get the world connection from a connection id.
	 * @param connectionId
	 * @return the connection's world connection instance
	 */
	public static WorldConnection getProfileFromConnection(int connectionId) {
		for (int i = 0; i < worldConnections.size(); i++) {
			WorldConnection worldConnection = worldConnections.get(i);
			if (worldConnection.getConnectionId() == connectionId) {
				return worldConnection;
			}
		}
		return null;
	}
	
	/**
	 * Get the world server.
	 * @return worldServer
	 */
	public static Server getWorldServer() {
		return worldServer;
	}
	
	/**
	 * Get a realm by index.
	 * @param index
	 * @return the realm at index x
	 */
	public static Realm getRealm(int index) {
		return realms.get(index);
	}
	
	/**
	 * Get the message of the day.
	 * @return motd
	 */
	public static String getMOTD() {
		return ServerConfiguration.getMOTD();
	}
	
	public static ArrayList<AbstractCommand> getPlayerCommands() {
		return PlayerCommands;
	}

	public static ArrayList<AbstractCommand> getModeratorCommands() {
		return ModeratorCommands;
	}

	public static ArrayList<AbstractCommand> getGamemasterCommands() {
		return GamemasterCommands;
	}

	public static ArrayList<AbstractCommand> getAdministratorCommands() {
		return AdministratorCommands;
	}
	
	/**
	 * Get a world-connection by character name.
	 * @param characterName
	 * @return this players world-connection instance
	 */
	public static WorldConnection getUserByName(String characterName) {
		for (WorldConnection worldConnection : worldConnections) {
			if (worldConnection.getCharacterName().equalsIgnoreCase(characterName))
				return worldConnection;
		}
		return null;
	}

	public static void writeMessage(LogType logType, String text) {
		if (ServerConfiguration.shouldUseGUI())
			serverGui.writeMessage(logType, text);
		else
			System.out.println(text);
	}
	
	public static void main(String[] args) {
		new WoWServer();
	}
}
