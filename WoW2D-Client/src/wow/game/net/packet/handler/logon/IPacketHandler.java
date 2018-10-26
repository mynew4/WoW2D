package wow.game.net.packet.handler.logon;

import com.esotericsoftware.kryonet.Client;

/**
 * An extendable class for packet handling classes.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class IPacketHandler {

	public abstract void handlePacket(Client client);
}
