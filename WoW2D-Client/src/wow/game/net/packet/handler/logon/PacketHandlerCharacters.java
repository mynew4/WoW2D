package wow.game.net.packet.handler.logon;

import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryonet.Client;

import wow.game.WoW;
import wow.game.objects.PlayerCharacter;
import wow.game.util.manager.RealmManager;
import wow.net.packet.logon.PacketCharsResponse;
import wow.net.util.Logger;

/**
 * Handles character-get responses.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PacketHandlerCharacters extends IPacketHandler {

	private PacketCharsResponse responsePacket;
	
	public PacketHandlerCharacters(PacketCharsResponse responsePacket) {
		this.responsePacket = responsePacket;
	}
	
	@Override
	public void handlePacket(Client client) {
		String characterName = responsePacket.Name;
		
		try {
			PlayerCharacter character = new PlayerCharacter(characterName, WoW.RaceType.valueOf("Undead"));
			RealmManager.addCharacterToRealm(character);
		} catch (SlickException e) {
			Logger.write("Unable to add character to realm: "+e.getMessage());
		}
	}
}
