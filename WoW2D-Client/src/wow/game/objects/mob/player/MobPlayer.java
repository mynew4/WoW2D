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
		
		if (isMovingDown) {
			velY += 0.1f * delta;
			if (velY > 1.2f)
				velY = 1.2f;
			direction = Direction.SOUTH;
			race.getSouthAnimation().update(delta);
			if (race.getNorthAnimation().getFrame() != 0) {
				race.getNorthAnimation().restart();
			}
		} else if (isMovingUp) {
			velY += -(0.1f) * delta;
			if (velY < -1.2f)
				velY = -1.2f;
			direction = Direction.NORTH;
			race.getNorthAnimation().update(delta);
			if (race.getSouthAnimation().getFrame() != 0) {
				race.getSouthAnimation().restart();
			}
		} else {
			velY = 0;
		}
		
		if (isMovingRight) {
			velX += 0.1f * delta;
			if (velX > 1.2f)
				velX = 1.2f;
			if (!isMovingUp || !isMovingDown) {
				direction = Direction.EAST;
				race.getEastAnimation().update(delta);
			}
			if (race.getWestAnimation().getFrame() != 0) {
				race.getWestAnimation().restart();
			}
		} else if (isMovingLeft) {
			velX += -(0.1f) * delta;
			if (velX < -1.2f)
				velX = -1.2f;
			if (!isMovingUp || !isMovingDown) {
				direction = Direction.WEST;
				race.getWestAnimation().update(delta);
			}
			if (race.getEastAnimation().getFrame() != 0) {
				race.getEastAnimation().restart();
			}
		} else {
			velX = 0;
		}
		
		x += velX;
		y += velY;
		
		bounds.setLocation(x, y);
		
		if (isMovingUp || isMovingDown || isMovingLeft || isMovingRight) {
			if (hasSentMovingPacket)
				hasSentMovingPacket = false;
			NetworkManager.sendMovement(x, y, direction.getDirection(), true); 
		} else {
			if (!hasSentMovingPacket) {
				race.getNorthAnimation().restart();
				race.getEastAnimation().restart();
				race.getSouthAnimation().restart();
				race.getWestAnimation().restart();
				hasSentMovingPacket = true;
				NetworkManager.sendMovement(x, y, direction.getDirection(), false);
			}
			
		}
	}
}
