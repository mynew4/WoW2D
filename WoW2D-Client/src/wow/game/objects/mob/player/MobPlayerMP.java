package wow.game.objects.mob.player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.objects.mob.player.race.IRace;
import wow.game.util.SettingsConfiguration;

/**
 * A network-player class.
 * @author Xolitude (October 26, 2018)
 *
 */
public class MobPlayerMP extends IMobPlayer {
	
	public boolean isMoving;

	public MobPlayerMP(String username, IRace race) {
		super(username, race);
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setFont(container.getDefaultFont());

		if (!race.hasLoadedAnimations()) {
			race.loadAnimations();
			bounds = new Rectangle(0, 0, race.getNorthAnimation().getWidth(), race.getNorthAnimation().getHeight());
		}
		
		if (isMoving) {
			if (direction == Direction.NORTH) {
				race.getNorthAnimation().draw(x, y);
			} else if (direction == Direction.SOUTH) {
				race.getSouthAnimation().draw(x, y);
			} else if (direction == Direction.EAST) {
				race.getEastAnimation().draw(x, y);
			} else if (direction == Direction.WEST) {
				race.getWestAnimation().draw(x, y);
			} else if (direction == Direction.NORTH_EAST) {
				race.getNorthAnimation().draw(x, y);
			} else if (direction == Direction.SOUTH_EAST) {
				race.getSouthAnimation().draw(x, y);
			} else if (direction == Direction.SOUTH_WEST) {
				race.getSouthAnimation().draw(x, y);
			} else if (direction == Direction.NORTH_WEST) {
				race.getNorthAnimation().draw(x, y);
			}
		} else {
			if (direction == Direction.NORTH) {
				race.getNorthIdleSprite().draw(x, y);
			} else if (direction == Direction.SOUTH) {
				race.getSouthIdleSprite().draw(x, y);
			} else if (direction == Direction.EAST) {
				race.getEastIdleSprite().draw(x, y);
			} else if (direction == Direction.WEST) {
				race.getWestIdleSprite().draw(x, y);
			} else if (direction == Direction.NORTH_EAST) {
				race.getNorthIdleSprite().draw(x, y);
			} else if (direction == Direction.SOUTH_EAST) {
				race.getSouthIdleSprite().draw(x, y);
			} else if (direction == Direction.SOUTH_WEST) {
				race.getSouthIdleSprite().draw(x, y);
			} else if (direction == Direction.NORTH_WEST) {
				race.getNorthIdleSprite().draw(x, y);
			}
			
		}
		
		if (SettingsConfiguration.shouldRenderPlayerNames()) {
			graphics.setColor(new Color(169, 215, 216));
			if (isGM)
				graphics.drawString("[GM]"+username, (bounds.getX() + (bounds.getWidth() / 2 - graphics.getFont().getWidth("[GM]"+username) / 2)), bounds.getY() - graphics.getFont().getLineHeight());
			else
				graphics.drawString(username, (bounds.getX() + (bounds.getWidth() / 2 - graphics.getFont().getWidth(username) / 2)), bounds.getY() - graphics.getFont().getLineHeight());
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if (isMoving) {
			if (direction == Direction.NORTH) {
				race.getNorthAnimation().update(delta);
			} else if (direction == Direction.SOUTH) {
				race.getSouthAnimation().update(delta);
			} else if (direction == Direction.EAST) {
				race.getEastAnimation().update(delta);
			} else if (direction == Direction.WEST) {
				race.getWestAnimation().update(delta);
			} else if (direction == Direction.NORTH_EAST) {
				race.getNorthAnimation().update(delta);
			} else if (direction == Direction.SOUTH_EAST) {
				race.getSouthAnimation().update(delta);
			} else if (direction == Direction.SOUTH_WEST) {
				race.getSouthAnimation().update(delta);
			} else if (direction == Direction.NORTH_WEST) {
				race.getNorthAnimation().update(delta);
			}
		}
		if (bounds != null) {
			bounds.setLocation(x, y);
		}
	}
}
