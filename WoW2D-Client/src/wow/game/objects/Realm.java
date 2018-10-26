package wow.game.objects;

import java.util.LinkedList;

/**
 * Handles a given realm.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Realm {

	private int id;
	private String name;
	private int port;
	
	private LinkedList<PlayerCharacter> characters;
	
	public Realm(int id, String name, int port) {
		this.id = id;
		this.name = name;
		this.port = port;
		
		characters = new LinkedList<PlayerCharacter>();
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPort() {
		return port;
	}
	
	public void addCharacterToRealm(PlayerCharacter playerCharacter) {
		characters.add(playerCharacter);
	}
	
	public void deleteCharacterFromRealm(String characterName) {
		for (int i = 0; i < characters.size(); i++) {
			PlayerCharacter playerCharacter = characters.get(i);
			if (playerCharacter.getName().equalsIgnoreCase(characterName)) {
				characters.remove(playerCharacter);
			}
		}
	}
	
	public PlayerCharacter getSelectedCharacter() {
		for (int i = 0; i < characters.size(); i++) {
			PlayerCharacter playerCharacter = characters.get(i);
			if (playerCharacter.isSelected()) {
				return playerCharacter;
			}
		}
		return null;
	}
	
	public LinkedList<PlayerCharacter> getCharacterList() {
		return characters;
	}
}
