package wow.game.net.connection;

import java.io.IOException;
import java.util.LinkedList;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import wow.game.objects.PlayerCharacter;
import wow.game.objects.PlayerCharacterMP;
import wow.game.util.ChatMessage;
import wow.game.util.manager.NetworkManager;
import wow.net.Network;
import wow.net.packet.world.PacketChatMessage;
import wow.net.packet.world.PacketChatMessageToAll;
import wow.net.packet.world.PacketGMUpdate;
import wow.net.packet.world.PacketGObject;
import wow.net.packet.world.PacketMovementToAll;
import wow.net.packet.world.PacketMovement_C;
import wow.net.packet.world.PacketMovement_S;
import wow.net.packet.world.PacketPlayerList;
import wow.net.packet.world.PacketPlayerListRequest;
import wow.net.packet.world.PacketWorldDisconnectToAll;
import wow.net.packet.world.PacketWorldEnterRequest;
import wow.net.packet.world.PacketWorldEnterResponse;
import wow.net.packet.world.PacketWorldEnterToAll;
import wow.net.util.Logger;
import wow.net.util.PlayerList;

/**
 * Handles the world-connection.
 * @author Xolitude (October 26, 2018)
 *
 */
public class WorldConnection {

	private Client client;
	
	public enum WorldStatus {
		Waiting,
		EnterWorldSuccess,
		EnterWorldDenied,
		NewPlayer,
		PlayerList,
	}
	
	private WorldStatus world_Status = WorldStatus.Waiting;
	
	private float playerX, playerY;
	private int playerDirection;
	
	/**
	 * Connect to the world-server and send a world request.
	 * @param character
	 * @param ip
	 * @param tcpPort
	 * @param udpPort
	 */
	public WorldConnection(PlayerCharacter character, String ip, int tcpPort, int udpPort) {
		client = new Client();
		Network.register(client);
		new Thread("connecting") {
			public void run() {
				if (!client.isConnected()) {
					client.start();
					try {
						client.connect(5000, ip, tcpPort, udpPort);
						PacketWorldEnterRequest pWorldReq = new PacketWorldEnterRequest();
						pWorldReq.AccountName = NetworkManager.getAccountUsername();
						pWorldReq.CharacterName = character.getName();
						client.sendTCP(pWorldReq);
					} catch (IOException e) {
						world_Status = WorldStatus.EnterWorldDenied;
						Logger.write("Unable to connect to the world: "+e.getMessage());
					}
				}
			}
		}.start();
		
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof PacketWorldEnterResponse) {
					PacketWorldEnterResponse pWorldResp = (PacketWorldEnterResponse)object;
					playerX = pWorldResp.X;
					playerY = pWorldResp.Y;
					playerDirection = pWorldResp.Direction;
					client.sendTCP(new PacketPlayerListRequest());
				} else if (object instanceof PacketPlayerList) {
					PacketPlayerList pPlayer = (PacketPlayerList)object;
					LinkedList<PlayerList> pPlayerList = pPlayer.PlayerList;
					LinkedList<PlayerCharacterMP> playerCharacters = new LinkedList<PlayerCharacterMP>();
					for (int i = 0; i < pPlayerList.size(); i++) {
						PlayerList player = pPlayerList.get(i);
						playerCharacters.add(new PlayerCharacterMP(player.CharacterName, player.CharacterRace, player.X, player.Y, player.Direction));
					}
					NetworkManager.addPlayerList(playerCharacters);
				} else if (object instanceof PacketWorldEnterToAll) {
					PacketWorldEnterToAll pWorldEnterAll = (PacketWorldEnterToAll)object;
					PlayerCharacterMP newPlayer = new PlayerCharacterMP(pWorldEnterAll.CharacterName, pWorldEnterAll.CharacterRace, pWorldEnterAll.X, pWorldEnterAll.Y, pWorldEnterAll.Direction);
					NetworkManager.addPlayer(newPlayer);
				} else if (object instanceof PacketWorldDisconnectToAll) {
					PacketWorldDisconnectToAll pDisconnectAll = (PacketWorldDisconnectToAll)object;
					String characterName = pDisconnectAll.CharacterName;
					NetworkManager.disconnectPlayer(characterName);
				} else if (object instanceof PacketMovement_S) {
					float x = ((PacketMovement_S)object).X;
					float y = ((PacketMovement_S)object).Y;
					NetworkManager.handleLocalMovement(x, y);
				} else if (object instanceof PacketMovementToAll) {
					String characterName = ((PacketMovementToAll)object).CharacterName;
					float x = ((PacketMovementToAll)object).X;
					float y = ((PacketMovementToAll)object).Y;
					int direction = ((PacketMovementToAll)object).Direction;
					boolean isMoving = ((PacketMovementToAll)object).isMoving;
					NetworkManager.updatePlayerMPLocation(x, y, characterName, direction, isMoving);
				} else if (object instanceof PacketChatMessageToAll) {
					String tag = ((PacketChatMessageToAll)object).Tag;
					String username = ((PacketChatMessageToAll)object).Username;
					String message = ((PacketChatMessageToAll)object).ChatMessage;
					NetworkManager.addChatMessage(new ChatMessage(tag, username, message));
				} else if (object instanceof PacketGMUpdate) {
					String username = ((PacketGMUpdate)object).Name;
					boolean isGM = ((PacketGMUpdate)object).isGM;
					NetworkManager.updatePlayerGM(username, isGM);
				} else if (object instanceof PacketGObject) {
					PacketGObject pGObject = (PacketGObject)object;
					NetworkManager.handleGObject(pGObject.ID, pGObject.InstanceID, pGObject.Name, pGObject.X, pGObject.Y, pGObject.Direction, pGObject.State);
				}
			}
		});
	}
	
	/**
	 * Send a movement packet.
	 * @param x
	 * @param y
	 * @param direction
	 * @param isMoving
	 */
	public void sendMovement(int direction, boolean isMoving) {
		PacketMovement_C pMovement = new PacketMovement_C();
		pMovement.Direction = direction;
		pMovement.isMoving = isMoving;
		client.sendUDP(pMovement);
	}
	
	/**
	 * Send a chat packet.
	 * @param message
	 */
	public void sendChat(String message) {
		PacketChatMessage pChat = new PacketChatMessage();
		pChat.Message = message;
		client.sendTCP(pChat);
	}
	
	/**
	 * Get our player's x-position.
	 * @return player's x
	 */
	public float getPlayerX() {
		return playerX;
	}
	
	/**
	 * Get our player's y-position.
	 * @return player's y
	 */
	public float getPlayerY() {
		return playerY;
	}
	
	/**
	 * Get our player's direction.
	 * @return player's direction (0,1,2,3)
	 */
	public int getPlayerDirection() {
		return playerDirection;
	}
	
	/** 
	 * Set the status of this world-connection.
	 * @param world_Status
	 */
	public void setStatus(WorldStatus world_Status) {
		this.world_Status = world_Status;
	}
	
	/**
	 * Get the status of this world-connection.
	 * @return connection's status.
	 */
	public WorldStatus getStatus() {
		return world_Status;
	}
}
