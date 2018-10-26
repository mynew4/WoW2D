package wow.server.net;

import wow.server.gui.ServerGUI.LogType;

/**
 * Handles a world-connection.
 * @author Xolitude (October 26, 2018)
 *
 */
public class WorldConnection {
	
	public enum AccountLevel {
		Player(0),
		Moderator(1),
		Gamemaster(2),
		Administrator(3);
		
		private int levelId;
		
		AccountLevel(int levelId) {
			this.levelId = levelId;
		}
		
		public int getLevelId() {
			return levelId;
		}
	}

	private int connectionId;
	private int accountId;
	private String accountName;
	private AccountLevel accountLevel;
	private String characterName, characterLocation, characterRace;
	private int characterLevel;
	private String characterClass;
	
	private boolean isGM = false;
	
	private float x, y;
	private int direction;
	
	public WorldConnection(int accountId, String accountName, int accountLevel, String characterName, String characterLevel, String characterLocation, String characterRace) {
		this.accountId = accountId;
		this.accountName = accountName;
		for (AccountLevel accountLvl : AccountLevel.values()) {
			if (accountLvl.levelId == accountLevel) {
				this.accountLevel = accountLvl;
			}
		}
		this.characterName = characterName;
		this.characterLocation = characterLocation;
		this.characterRace = characterRace;
		
		String[] classData = characterLevel.split(" ");
		this.characterLevel = Integer.valueOf(classData[1]);
		this.characterClass = classData[2];
	}
	
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
	
	public void setGM(boolean isGM) {
		this.isGM = isGM;
	}
	
	public boolean isGM() {
		return isGM;
	}
	
	public int getConnectionId() {
		return connectionId;
	}

	public int getAccountId() {
		return accountId;
	}

	public String getAccountName() {
		return accountName;
	}
	
	public AccountLevel getAccountLevel() {
		return accountLevel;
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getCharacterLocation() {
		return characterLocation;
	}

	public String getCharacterRace() {
		return characterRace;
	}

	public int getCharacterLevel() {
		return characterLevel;
	}

	public String getCharacterClass() {
		return characterClass;
	}
	
	public void setLocation(float x, float y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getDirection() {
		return direction;
	}
}
