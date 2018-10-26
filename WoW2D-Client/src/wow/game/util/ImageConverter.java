package wow.game.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import wow.net.util.Logger;

/**
 * Convert an awt image to a slick image.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ImageConverter {

	public static Image BufferedToSlick(BufferedImage image) {
		try {
			Texture imageTexture = BufferedImageUtil.getTexture("", image);
			Image slickImage = new Image(imageTexture.getImageWidth(), imageTexture.getImageHeight());
			slickImage.setTexture(imageTexture);
			slickImage = slickImage.getScaledCopy(32, 32);
			slickImage.setFilter(Image.FILTER_NEAREST);
			return slickImage;
		} catch (SlickException e) {
			Logger.write("Unable to convert bufferedimage to slick image: "+e.getMessage());
		} catch (IOException e) {
			Logger.write("I/O exception @ bufferedimage > slick image: "+e.getMessage());
		}
		return null;
	}
}
