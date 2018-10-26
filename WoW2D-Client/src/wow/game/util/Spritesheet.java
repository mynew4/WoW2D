package wow.game.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.Image;

/**
 * Handles spritesheets.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Spritesheet {

	private BufferedImage spritesheet;
	
	public Spritesheet(String ref) {
		try {
			spritesheet = ImageIO.read(new FileInputStream(ref));
		} catch (IOException e) {
			System.err.println("Unable to load spritesheet by ref: "+e.getMessage());
		}
	}
	
	public Image getSpriteAt(int x, int y) {
		BufferedImage image = spritesheet.getSubimage(x * 16, y * 16, 16, 16);
		Image slickImage = ImageConverter.BufferedToSlick(image);
		return slickImage;
	}
}
