package wow.game.objects.mob.player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.WoW;
import wow.game.objects.mob.player.race.IRace;
import wow.game.util.PlayerController;
import wow.game.util.SettingsConfiguration;
import wow.game.util.manager.NetworkManager;

/**
 * A local player-class.
 * @author Xolitude (October 26, 2018)
 *
 */
public class MobPlayer extends IMobPlayer {

	private PlayerController controller;

	public MobPlayer(String username, IRace race) {
		super(username, race);
		controller = new PlayerController(this);
		bounds = new Rectangle(0, 0, race.getNorthAnimation().getWidth(), race.getNorthAnimation().getHeight());
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setFont(container.getDefaultFont());
		
		if (isMovingDown) {
			race.getSouthAnimation().draw(x, y);
		} else if (isMovingUp) {
			race.getNorthAnimation().draw(x, y);
		} else if (isMovingRight) {
			race.getEastAnimation().draw(x, y);
		} else if (isMovingLeft) {
			race.getWestAnimation().draw(x, y);
		} else {
			switch (direction) {
			case NORTH:
				race.getNorthIdleSprite().draw(x, y);
				break;
			case SOUTH:
				race.getSouthIdleSprite().draw(x, y);
				break;
			case EAST:
				race.getEastIdleSprite().draw(x, y);
				break;
			case WEST:
				race.getWestIdleSprite().draw(x, y);
				break;
			case NORTH_EAST:
				race.getNorthIdleSprite().draw(x, y);
				break;
			case SOUTH_EAST:
				race.getSouthIdleSprite().draw(x, y);
				break;
			case SOUTH_WEST:
				race.getSouthIdleSprite().draw(x, y);
				break;
			case NORTH_WEST:
				race.getNorthIdleSprite().draw(x, y);
				break;
			}
		}
		
		if (SettingsConfiguration.shouldRenderMyName()) {
			graphics.setColor(new Color(169, 215, 216));
			if (isGM)
				graphics.drawString("[GM]"+username, (bounds.getX() + (bounds.getWidth() / 2 - graphics.getFont().getWidth("[GM]"+username) / 2)), bounds.getY() - graphics.getFont().getLineHeight());
			else
				graphics.drawString(username, (bounds.getX() + (bounds.getWidth() / 2 - graphics.getFont().getWidth(username) / 2)), bounds.getY() - graphics.getFont().getLineHeight());
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		controller.update(container, sbg);
		
		if (isMovingUp) {
			direction = Direction.NORTH;
		}
		
		if (isMovingRight) {
			direction = Direction.EAST;
			race.getEastAnimation().update(delta);
		}
		
		if (isMovingUp && isMovingRight) {
			direction = Direction.NORTH_EAST;
		}
		
		if (isMovingUp || isMovingUp && isMovingRight || isMovingUp && isMovingLeft) {
			race.getNorthAnimation().update(delta);
		}
		
		if (isMovingDown) {
			direction = Direction.SOUTH;
		} 
		
		if (isMovingDown && isMovingRight) {
			direction = Direction.SOUTH_EAST;
		}
		
		if (isMovingLeft) {
			direction = Direction.WEST;
			race.getWestAnimation().update(delta);
		} 
		
		if (isMovingUp && isMovingLeft) {
			direction = Direction.NORTH_WEST;
		}
		
		if (isMovingDown && isMovingLeft) {
			direction = Direction.SOUTH_WEST;
		}
		
		if (isMovingDown || isMovingDown && isMovingRight || isMovingDown && isMovingLeft) {
			race.getSouthAnimation().update(delta);
		}
		
		bounds.setLocation(x, y);
		
		if (isMovingUp || isMovingDown || isMovingLeft || isMovingRight) {
			if (hasSentMovingPacket)
				hasSentMovingPacket = false;
			NetworkManager.sendMovement(direction.getDirection(), true); 
		} else {
			if (!hasSentMovingPacket) {
				race.getNorthAnimation().restart();
				race.getEastAnimation().restart();
				race.getSouthAnimation().restart();
				race.getWestAnimation().restart();
				hasSentMovingPacket = true;
				NetworkManager.sendMovement(direction.getDirection(), false);
			}
			
		}
	}
	
	public void setNetworkPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
