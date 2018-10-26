package wow.game.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import wow.game.WoW;
import wow.game.objects.mob.player.race.IRace;
import wow.game.objects.mob.player.race.RaceUndead;

/**
 * Handles everything pertaining to a network-player's mob-character.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PlayerCharacterMP {

	private String username;
	private IRace race;
	
	private float x;
	private float y;
	private int direction;
	
	public PlayerCharacterMP(String username, String race, float x, float y, int direction) {
		this.username = username;
		for (WoW.RaceType r : WoW.RaceType.values()) {
			if (r.name().equalsIgnoreCase(race)) {
				switch (r) {
				case Undead:
					this.race = new RaceUndead();
					break;
				}
			}
		}
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public String getUsername() {
		return username;
	}
	
	public IRace getRace() {
		return race;
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
