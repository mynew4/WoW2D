package wow.game.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import wow.game.WoW.RaceType;
import wow.game.objects.mob.player.race.IRace;
import wow.game.objects.mob.player.race.RaceUndead;

/**
 * Handles everything pertaining to the player-mob's character.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PlayerCharacter {

	private String name;
	private String level;
	private String location;
	private RaceType raceType;
	private IRace race;
	
	private boolean isSelected = false;
	
	public PlayerCharacter(String name, RaceType raceType) throws SlickException {
		this.name = name;
		this.level = "Level 1 Warrior";
		this.location = "Orgrimmar";
		this.raceType = raceType;
		switch (raceType) {
		case Undead:
			race = new RaceUndead();
			break;
		}
	}
	
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLevel() {
		return level;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getRaceType() {
		return raceType.name();
	}
	
	public IRace getRace() {
		return race;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
}
