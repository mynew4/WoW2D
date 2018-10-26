package wow.game.objects.mob.player.race;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import wow.game.WoW.RaceType;
import wow.game.util.Spritesheet;

/**
 * An "extendable" race-class.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class IRace {

	private RaceType raceType;
	
	private Spritesheet spritesheet;
	private Animation northAnimation;
	private Animation eastAnimation;
	private Animation southAnimation;
	private Animation westAnimation;
	
	private boolean hasLoadedAnimations = false;
	
	public IRace(RaceType raceType) {
		this.raceType = raceType;
		
		switch (raceType) {
		case Undead:
			spritesheet = new Spritesheet("res/sprites/race/player/undead.png");
			break;
		}
	}
	
	public void loadAnimations() {
		southAnimation = new Animation(false);
		southAnimation.addFrame(spritesheet.getSpriteAt(0, 0), 200);
		southAnimation.addFrame(spritesheet.getSpriteAt(1, 0), 200);
		southAnimation.addFrame(spritesheet.getSpriteAt(2, 0), 200);
		southAnimation.addFrame(spritesheet.getSpriteAt(3, 0), 200);
		
		northAnimation = new Animation(false);
		northAnimation.addFrame(spritesheet.getSpriteAt(0, 1), 200);
		northAnimation.addFrame(spritesheet.getSpriteAt(1, 1), 200);
		northAnimation.addFrame(spritesheet.getSpriteAt(2, 1), 200);
		northAnimation.addFrame(spritesheet.getSpriteAt(3, 1), 200);
		
		eastAnimation = new Animation(false);
		eastAnimation.addFrame(spritesheet.getSpriteAt(0, 2), 200);
		eastAnimation.addFrame(spritesheet.getSpriteAt(1, 2), 200);
		eastAnimation.addFrame(spritesheet.getSpriteAt(2, 2), 200);
		eastAnimation.addFrame(spritesheet.getSpriteAt(3, 2), 200);
		
		westAnimation = new Animation(false);
		westAnimation.addFrame(spritesheet.getSpriteAt(0, 3), 200);
		westAnimation.addFrame(spritesheet.getSpriteAt(1, 3), 200);
		westAnimation.addFrame(spritesheet.getSpriteAt(2, 3), 200);
		westAnimation.addFrame(spritesheet.getSpriteAt(3, 3), 200);
		
		hasLoadedAnimations = true;
	}

	public RaceType getRaceType() {
		return raceType;
	}

	public Spritesheet getSpritesheet() {
		return spritesheet;
	}

	public Animation getNorthAnimation() {
		return northAnimation;
	}

	public Animation getEastAnimation() {
		return eastAnimation;
	}

	public Animation getSouthAnimation() {
		return southAnimation;
	}

	public Animation getWestAnimation() {
		return westAnimation;
	}
	
	/**
	 * Used for the first 2 states.
	 * @param scaled
	 * @return the first indexed image of the race.
	 */
	public Image getSprite(boolean scaled) {
		Image image = spritesheet.getSpriteAt(0, 0);
		image = image.getScaledCopy(32, 32);
		image.setFilter(Image.FILTER_NEAREST);
		return image;
	}
	
	public Image getNorthIdleSprite() {
		Image image = northAnimation.getImage(0).getScaledCopy(32, 32);
		image.setFilter(Image.FILTER_NEAREST);
		return image;
	}
	
	public Image getEastIdleSprite() {
		Image image = eastAnimation.getImage(0).getScaledCopy(32, 32);
		image.setFilter(Image.FILTER_NEAREST);
		return image;
	}

	public Image getSouthIdleSprite() {
		Image image = southAnimation.getImage(0).getScaledCopy(32, 32);
		image.setFilter(Image.FILTER_NEAREST);
		return image;
	}

	public Image getWestIdleSprite() {
		Image image = westAnimation.getImage(0).getScaledCopy(32, 32);
		image.setFilter(Image.FILTER_NEAREST);
		return image;
	}
	
	public boolean hasLoadedAnimations() {
		return hasLoadedAnimations;
	}
}
