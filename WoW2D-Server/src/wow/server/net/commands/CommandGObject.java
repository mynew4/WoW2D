package wow.server.net.commands;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import wow.net.packet.world.PacketChatMessageToAll;
import wow.net.packet.world.PacketGObject;
import wow.server.net.WorldConnection;
import wow.server.net.WorldConnection.AccountLevel;
import wow.server.net.game.object.entity.EntityInstance;
import wow.server.util.manager.EntityManager;

/**
 * Handles game-object modification.
 * @author Xolitude (October 26, 2018)
 *
 */
public class CommandGObject extends AbstractCommand {
	
	private final String ACTION_ADD = "add";

	public CommandGObject() {
		super("gobject", AccountLevel.Administrator);
	}

	@Override
	public void performCommand(Server server, Connection connection, WorldConnection worldConnection,
			String[] commandParts) {
		String action = commandParts[1];
		int id = Integer.valueOf(commandParts[2]);
		
		switch (action) {
		case ACTION_ADD:
			EntityInstance entityInstance = EntityManager.createIntstanceOf(id, worldConnection.getX(), worldConnection.getY());
			if (entityInstance != null) {
				PacketGObject pGameObject = new PacketGObject();
				pGameObject.ID = entityInstance.getId();
				pGameObject.InstanceID = entityInstance.getInstanceId();
				pGameObject.Name = entityInstance.getName();
				pGameObject.X = entityInstance.getX();
				pGameObject.Y = entityInstance.getY();
				pGameObject.State = 0; // add.
				server.sendToAllTCP(pGameObject);
			} else {
				PacketChatMessageToAll pError = new PacketChatMessageToAll();
				pError.Tag = "server";
				pError.Username = null;
				pError.ChatMessage = "Invalid entity_id specified.";
				server.sendToTCP(worldConnection.getConnectionId(), pError);
			}
			break;
		}
	}
}
