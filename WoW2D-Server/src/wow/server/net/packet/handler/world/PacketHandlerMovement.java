package wow.server.net.packet.handler.world;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.world.PacketMovement;
import wow.net.packet.world.PacketMovementToAll;
import wow.server.net.WoWServer;
import wow.server.util.IPacketHandler;

/**
 * Handle movement packets.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerMovement extends IPacketHandler {

	private PacketMovement pMovement;
	
	public PacketHandlerMovement(PacketMovement pMovement) {
		this.pMovement = pMovement;
	}
	
	@Override
	public void handlePacket(Server server, Connection connection) {
		float x = pMovement.X;
		float y = pMovement.Y;
		int direction = pMovement.Direction;
		boolean isMoving = pMovement.isMoving;
		
		String characterName = WoWServer.setCharacterPosition(connection.getID(), x, y, direction);
		
		PacketMovementToAll pMovementToAll = new PacketMovementToAll();
		pMovementToAll.CharacterName = characterName;
		pMovementToAll.X = x;
		pMovementToAll.Y = y;
		pMovementToAll.Direction = direction;
		pMovementToAll.isMoving = isMoving;
		server.sendToAllExceptUDP(connection.getID(), pMovementToAll);
	}
}
