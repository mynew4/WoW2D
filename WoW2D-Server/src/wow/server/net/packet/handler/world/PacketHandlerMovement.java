package wow.server.net.packet.handler.world;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.world.PacketMovementToAll;
import wow.net.packet.world.PacketMovement_C;
import wow.net.packet.world.PacketMovement_S;
import wow.server.net.WoWServer;
import wow.server.net.WorldConnection;
import wow.server.util.IPacketHandler;

/**
 * Handle movement packets.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerMovement extends IPacketHandler {

	private PacketMovement_C pMovement;
	
	private final int NORTH = 0;
	private final int SOUTH = 1;
	private final int EAST = 2;
	private final int WEST = 3;
	private final int NORTH_EAST = 4;
	private final int SOUTH_EAST = 5;
	private final int SOUTH_WEST = 6;
	private final int NORTH_WEST = 7;
	
	private final float SPEED = 1.25f;
	
	public PacketHandlerMovement(PacketMovement_C pMovement) {
		this.pMovement = pMovement;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		int direction = pMovement.Direction;
		boolean isMoving = pMovement.isMoving;
		
		WorldConnection worldConnection = WoWServer.getProfileFromConnection(connection.getID());
		if (worldConnection != null) {
			// TODO: implement some kind of timed-loop with a delta for smoother movement?
			float xCurrent = worldConnection.getX();
			float yCurrent = worldConnection.getY();
			if (isMoving) {
				switch (direction) {
				case NORTH:
					yCurrent -= SPEED;
					break;
				case SOUTH:
					yCurrent += SPEED;
					break;
				case EAST:
					xCurrent += SPEED;
					break;
				case WEST:
					xCurrent -= SPEED;
					break;
				case NORTH_EAST:
					yCurrent -= SPEED;
					xCurrent += SPEED;
					break;
				case SOUTH_EAST:
					yCurrent += SPEED;
					xCurrent += SPEED;
					break;
				case SOUTH_WEST:
					yCurrent += SPEED;
					xCurrent -= SPEED;
					break;
				case NORTH_WEST:
					yCurrent -= SPEED;
					xCurrent -= SPEED;
					break;
				}
				worldConnection.setLocation(xCurrent, yCurrent, direction);
				PacketMovement_S pMovement_S = new PacketMovement_S();
				pMovement_S.X = xCurrent;
				pMovement_S.Y = yCurrent;
				connection.sendUDP(pMovement_S);
			}
			String characterName = WoWServer.setCharacterPosition(connection.getID(), xCurrent, yCurrent, direction);
			
			PacketMovementToAll pMovementToAll = new PacketMovementToAll();
			pMovementToAll.CharacterName = characterName;
			pMovementToAll.X = xCurrent;
			pMovementToAll.Y = yCurrent;
			pMovementToAll.Direction = direction;
			pMovementToAll.isMoving = isMoving;
			server.sendToAllExceptUDP(connection.getID(), pMovementToAll);
		}
		
		/*
		String characterName = WoWServer.setCharacterPosition(connection.getID(), x, y, direction);
		
		PacketMovementToAll pMovementToAll = new PacketMovementToAll();
		pMovementToAll.CharacterName = characterName;
		pMovementToAll.X = x;
		pMovementToAll.Y = y;
		pMovementToAll.Direction = direction;
		pMovementToAll.isMoving = isMoving;
		server.sendToAllExceptUDP(connection.getID(), pMovementToAll);
		*/
	}
}
