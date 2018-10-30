package wow.game.objects.entity;

import java.awt.image.BufferedImage;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import wow.game.WoW;
import wow.game.objects.mob.player.IMobPlayer.Direction;
import wow.game.util.ImageConverter;
import wow.game.util.SettingsConfiguration;

/**
 * Creates an instance of a given entity-id.
 * @author Xolitude (October 26, 2018)
 *
 */
public class EntityInstance {

	private int entityId;
	private int entityInstanceId;
	private String name;
	private float x, y;
	private Direction direction;
	private BufferedImage rawImg;
	private Image img;
	
	private Rectangle bounds;
	
	public EntityInstance(int entityId, int entityInstanceId, float x, float y) {
		this.entityId = entityId;
		this.entityInstanceId = entityInstanceId;
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics graphics) {
		graphics.setFont(WoW.getSmallFont());
		if (img == null) {
			img = ImageConverter.BufferedToSlick(rawImg);
			bounds = new Rectangle(0, 0, img.getWidth(), img.getHeight());
		}
		bounds.setLocation(x, y);
		
		if (direction != null) {
			switch (direction) {
			case NORTH:
				img.setRotation(0f);
				break;
			case EAST:
				img.setRotation(90f);
				break;
			case SOUTH:
				img.setRotation(180f);
				break;
			case WEST:
				img.setRotation(270f);
				break;
			}
		}
		graphics.setColor(Color.yellow); // TODO: change based on passive/aggressive status
		if (SettingsConfiguration.shouldRenderMobNames())
			graphics.drawString(name, (bounds.getX() + (bounds.getWidth() / 2 - graphics.getFont().getWidth(name) / 2)), bounds.getY() - graphics.getFont().getLineHeight());
		graphics.drawImage(img, x, y);
	}
	
	public int getId() {
		return entityId;
	}
	
	public int getInstanceId() {
		return entityInstanceId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setImage(BufferedImage img) {
		this.rawImg = img;
	}
	
	public void setLocation(float newX, float newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void setDirection(int newDirection) {
		for (Direction dir : Direction.values()) {
			if (dir.getDirection() == newDirection) {
				this.direction = dir;
			}
		}
	}
}
