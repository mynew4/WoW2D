package wow.game.util.manager;

import java.util.LinkedList;

import wow.game.net.connection.LogonConnection;
import wow.game.net.connection.WorldConnection;
import wow.game.net.connection.WorldConnection.WorldStatus;
import wow.game.objects.PlayerCharacter;
import wow.game.objects.PlayerCharacterMP;
import wow.game.objects.mob.player.MobPlayer;
import wow.game.objects.mob.player.MobPlayerMP;
import wow.game.objects.mob.player.race.IRace;
import wow.game.util.ChatMessage;

/**
 * Handles both connections from one class.
 * @author Xolitude (October 26, 2018)
 *
 */
public class NetworkManager {

	private static NetworkManager networkManager;
	
	private static String realmlistIp;
	private static int realmlistPort;
	
	private static LogonConnection logonConnection;
	private static WorldConnection worldConnection;
	
	private static String accountUsername;
	
	private static LinkedList<MobPlayerMP> players;
	private static MobPlayer player;
	private static LinkedList<ChatMessage> chatQueue;
	
	public static NetworkManager newInstance() {
		if (networkManager == null) 
			networkManager = new NetworkManager();
		players = new LinkedList<MobPlayerMP>();
		chatQueue = new LinkedList<ChatMessage>();
		return networkManager;
	}
	
	public static void initLogon(String username, String password) {
		accountUsername = username;
		logonConnection = new LogonConnection(username, password);
	}
	
	public static void initWorld(PlayerCharacter character) {
		worldConnection = new WorldConnection(character, realmlistIp, RealmManager.getRealm(0).getPort() + 1, RealmManager.getRealm(0).getPort());
	}
	
	public static void initPlayerObject(String characterName, IRace characterRace) {
		player = new MobPlayer(characterName, characterRace);
	}
	
	public static void sendChat(String text) {
		worldConnection.sendChat(text);
	}
	
	public static void sendMovement(int direction, boolean isMoving) {
		worldConnection.sendMovement(direction, isMoving);
	}
	
	public static void addPlayer(PlayerCharacterMP character) {
		MobPlayerMP playerMp = new MobPlayerMP(character.getUsername(), character.getRace());
		playerMp.setLocation(character.getX(), character.getY(), character.getDirection());
		players.add(playerMp);
	}
	
	public static void addPlayerList(LinkedList<PlayerCharacterMP> playerCharacters) {
		for (int i = 0; i < playerCharacters.size(); i++) {
			PlayerCharacterMP playerCharacter = playerCharacters.get(i);
			MobPlayerMP playerMob = new MobPlayerMP(playerCharacter.getUsername(), playerCharacter.getRace());
			playerMob.setLocation(playerCharacter.getX(), playerCharacter.getY(), playerCharacter.getDirection());
			players.add(playerMob);
		}
		worldConnection.setStatus(WorldStatus.EnterWorldSuccess);
	}
	
	public static void addChatMessage(ChatMessage chatMessage) {
		chatQueue.add(chatMessage);
	}
	
	public static void removeChatMessage(ChatMessage chatMessage) {
		chatQueue.remove(chatMessage);
	}
	
	public static void disconnectPlayer(String characterName) {
		for (int i = 0; i < players.size(); i++) {
			MobPlayerMP playerMp = players.get(i);
			if (playerMp.getUsername().equalsIgnoreCase(characterName)) {
				players.remove(playerMp);
			}
		}
	}
	
	public static void updatePlayerMPLocation(float x, float y, String characterName, int direction, boolean isMoving) {
		for (int i = 0; i < players.size(); i++) {
			MobPlayerMP playerMp = players.get(i);
			if (playerMp.getUsername().equalsIgnoreCase(characterName)) {
				playerMp.setLocation(x, y, direction);
				playerMp.isMoving = isMoving;
			}
		}
	}
	
	public static void updatePlayerGM(String characterName, boolean isGM) {
		if (!player.getUsername().equalsIgnoreCase(characterName)) {
			for (int i = 0; i < players.size(); i++) {
				MobPlayerMP playerMp = players.get(i);
				if (playerMp.getUsername().equalsIgnoreCase(characterName)) {
					playerMp.setGM(isGM);
				}
			}
		} else {
			player.setGM(isGM);
		}
	}
	
	public static void handleGObject(int entityId, int entityInstanceId, String name, float x, float y, int direction, int state) {
		switch (state) {
		case 0:
			EntityManager.createInstanceOf(entityId, entityInstanceId, name, x, y);
			break;
		case 1:
			EntityManager.updateInstancePosition(entityId, entityInstanceId, x, y, direction);
			break;
		}
	}
	
	public static void handleLocalMovement(float x, float y) {
		player.setNetworkPosition(x, y);
	}
	
	public static void disconnectLogon() {
		RealmManager.clearRealms();
		logonConnection.close();
		logonConnection = null;
	}
	
	public static LogonConnection getLogonConnection() {
		return logonConnection;
	}
	
	public static WorldConnection getWorldConnection() {
		return worldConnection;
	}
	
	public static String getAccountUsername() {
		return accountUsername;
	}
	
	public static void setRealmlist(String ip, int port) {
		realmlistIp = ip;
		realmlistPort = port;
	}
	
	public static String getRealmlist() {
		return realmlistIp;
	}
	
	public static int getRealmlistPort() {
		return realmlistPort;
	}
	
	public static LinkedList<MobPlayerMP> getPlayers() {
		return players;
	}
	
	public static MobPlayer getPlayer() {
		return player;
	}
	
	public static LinkedList<ChatMessage> getChatQueue() {
		return chatQueue;
	}
}
