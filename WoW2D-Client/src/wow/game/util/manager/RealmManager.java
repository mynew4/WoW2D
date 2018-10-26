package wow.game.util.manager;

import java.util.LinkedList;

import wow.game.objects.PlayerCharacter;
import wow.game.objects.Realm;
import wow.game.util.SettingsConfiguration;
import wow.game.util.SettingsConfiguration.Keys;

/**
 * Handles the realms.
 * @author Xolitude (October 26, 2018)
 *
 */
public class RealmManager {

	private static RealmManager realmManager;
	
	private static LinkedList<Realm> Realms;
	
	public static RealmManager newInstance() {
		if (realmManager == null)
			realmManager = new RealmManager();
		Realms = new LinkedList<Realm>();
		
		return realmManager;
	}
	
	public static void addRealm(Realm realm) {
		SettingsConfiguration.setSettingValue(Keys.Realm, realm.getName());
		Realms.add(realm);
	}
	
	public static void addCharacterToRealm(PlayerCharacter character) {
		Realms.get(0).addCharacterToRealm(character);
	}
	
	public static void removeCharacterFromRealm(String character) {
		Realms.get(0).deleteCharacterFromRealm(character);
	}
	
	public static PlayerCharacter getSelectedCharacter() {
		return Realms.get(0).getSelectedCharacter();
	}
	
	public static LinkedList<PlayerCharacter> getCharacterList() {
		return Realms.get(0).getCharacterList();
	}
	
	public static Realm getRealm(int index) {
		return Realms.get(index);
	}
	
	public static void clearRealms() {
		Realms.clear();
	}
}
