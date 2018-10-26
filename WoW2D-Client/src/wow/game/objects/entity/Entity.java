package wow.game.objects.entity;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Creates an entity object for a given entity.
 * @author Xolitude (October 26, 2018)
 *
 */
public class Entity {

	private BufferedImage img;
	private int id;
	
	public Entity(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				int settingType = Integer.valueOf(parts[0]);
				
				switch (settingType) {
				case EntitySettingType.Initialization:
					this.id = Integer.valueOf(parts[1]);
					this.img = ImageIO.read(new FileInputStream("resources/entityImages/"+parts[2]));
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getRawImage() {
		return img;
	}
	
	public int getId() {
		return id;
	}
}
