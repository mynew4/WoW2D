package wow.net;

import java.util.LinkedList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import wow.net.packet.logon.PacketCharCreateRequest;
import wow.net.packet.logon.PacketCharCreateResponse;
import wow.net.packet.logon.PacketCharDeleteRequest;
import wow.net.packet.logon.PacketCharDeleteResponse;
import wow.net.packet.logon.PacketCharsComplete;
import wow.net.packet.logon.PacketCharsRequest;
import wow.net.packet.logon.PacketCharsResponse;
import wow.net.packet.logon.PacketLogin;
import wow.net.packet.logon.PacketLoginResponse;
import wow.net.packet.logon.PacketRealmRequest;
import wow.net.packet.logon.PacketRealmResponse;
import wow.net.packet.logon.PacketWorldRequest;
import wow.net.packet.logon.PacketWorldResponse;
import wow.net.packet.world.PacketChatMessage;
import wow.net.packet.world.PacketChatMessageToAll;
import wow.net.packet.world.PacketGMUpdate;
import wow.net.packet.world.PacketGObject;
import wow.net.packet.world.PacketMovement;
import wow.net.packet.world.PacketMovementToAll;
import wow.net.packet.world.PacketPlayerList;
import wow.net.packet.world.PacketPlayerListRequest;
import wow.net.packet.world.PacketWorldDisconnectToAll;
import wow.net.packet.world.PacketWorldEnterRequest;
import wow.net.packet.world.PacketWorldEnterResponse;
import wow.net.packet.world.PacketWorldEnterToAll;
import wow.net.util.PlayerList;

/**
 * Register all shared class-types.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Network {

	static public void register(EndPoint endpoint) {
		Kryo kryo = endpoint.getKryo();
		kryo.register(PacketLogin.class);
		kryo.register(PacketLoginResponse.class);
		kryo.register(PacketRealmRequest.class);
		kryo.register(PacketRealmResponse.class);
		kryo.register(PacketCharsRequest.class);
		kryo.register(PacketCharsResponse.class);
		kryo.register(PacketCharsComplete.class);
		kryo.register(PacketCharCreateRequest.class);
		kryo.register(PacketCharCreateResponse.class);
		kryo.register(PacketCharDeleteRequest.class);
		kryo.register(PacketCharDeleteResponse.class);
		kryo.register(PacketWorldRequest.class);
		kryo.register(PacketWorldResponse.class);
		kryo.register(PacketWorldEnterRequest.class);
		kryo.register(PacketWorldEnterResponse.class);
		kryo.register(PacketWorldEnterToAll.class);
		kryo.register(PacketWorldDisconnectToAll.class);
		kryo.register(PacketPlayerList.class);
		kryo.register(PacketPlayerListRequest.class);
		kryo.register(PlayerList.class);
		kryo.register(LinkedList.class);
		kryo.register(PacketMovement.class);
		kryo.register(PacketMovementToAll.class);
		kryo.register(PacketChatMessage.class);
		kryo.register(PacketChatMessageToAll.class);
		kryo.register(PacketGMUpdate.class);
		kryo.register(PacketGObject.class);
	}
}
