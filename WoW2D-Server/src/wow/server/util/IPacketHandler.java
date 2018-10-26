package wow.server.util;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

/**
 * An extendable class for packets.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class IPacketHandler {

	public abstract void handlePacket(Server server, Connection connection);
}
