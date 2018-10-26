package wow.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.state.State0Login;
import wow.game.state.State1CharSelect;
import wow.game.state.State2CharCreate;
import wow.game.util.SettingsConfiguration;
import wow.game.util.manager.EntityManager;
import wow.game.util.manager.NetworkManager;
import wow.game.util.manager.RealmManager;
import wow.net.util.Logger;

/* Personal note: When exporting, do not export kryonet and wow2d_net!!! Also delete .classpath and .project from exported file*/
/**
 * The main class.
 * @author Xolitude (October 26, 2018)
 *
 */
public class WoW extends StateBasedGame {
	
	public enum RaceType {
		Undead
	}
	
	public static final int Width = 1280;
	public static final int Height = 720;
	
	private static final String Version = "a5.0.0";
	private static final String Title = String.format("World of Warcraft 2D (%s)", Version);
	
	private static AngelCodeFont SmallerFont;
	
	private final File DataDirectory = new File("data");
	private final File RealmlistFile = new File(String.format("%s/realmlist.wtf", DataDirectory));
		
	public WoW() {
		super(Title);
		
		Logger.newClientInstance();
		SettingsConfiguration.newInstance();
		RealmManager.newInstance();
		NetworkManager.newInstance();
		EntityManager.init();
		
		new Thread("loading") {
			public void run() {
				checkDirectories();
			}
		}.start();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		container.setDefaultFont(new AngelCodeFont("res/ui/font/wow_font.fnt", "res/ui/font/wow_font.png"));
		SmallerFont = new AngelCodeFont("res/ui/font/wow_chat_font.fnt", "res/ui/font/wow_chat_font.png");
		
		this.addState(new State0Login());
		this.addState(new State1CharSelect());
		this.addState(new State2CharCreate());
	}
	
	/**
	 * Check directories and set the realmlist.
	 */
	private void checkDirectories() {
		if (!DataDirectory.exists()) {
			// No launcher currently exists, this is merely for testing at the moment.
			Logger.write("!! Data directory does not exist. Please run the launcher to obtain a fresh copy of WoW-2D. !!");
			System.exit(-1);
		}
		
		if (!RealmlistFile.exists()) {
			Logger.write("!! realmlist.wtf does not exist. Creating it... !!");
			try (PrintWriter pw = new PrintWriter(RealmlistFile, "UTF-8")) {
				pw.println("# You can also set a port along with the ip: [set realmlist ip:port]");
				pw.println("set realmlist 127.0.0.1");
				pw.println("# set realmlist 127.0.0.1:1234");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(RealmlistFile))) {
			String line = null;
			String realmip = null;
			int realmport = 1234; /** Default port **/
			while ((line = br.readLine()) != null) {
				if (line.contains("#"))
					continue;
				String[] rl = line.split(" ");
				String realmlist = rl[2];
				if (realmlist.contains(":")) {
					String[] realmlistParts = realmlist.split(":");
					realmip = realmlistParts[0];
					realmport = Integer.valueOf(realmlistParts[1]);
				} else {
					realmip = realmlist;
				}
			}
			NetworkManager.setRealmlist(realmip, realmport);
		} catch (FileNotFoundException e) {
			Logger.write("Unable to find the realmlist file: "+e.getMessage());
		} catch (IOException e) {
			Logger.write("I/O exception: "+e.getMessage());
		}
	}
	
	/**
	 * Get the version of the game.
	 * @return Version
	 */
	public static String getVersion() {
		return Version;
	}
	
	/**
	 * Get the game's smaller font.
	 * @return SmallerFont
	 */
	public static AngelCodeFont getSmallFont() {
		return SmallerFont;
	}
	
	/**
	 * Called whenever a user presses the "X" button.
	 */
	@Override
	public boolean closeRequested() {
		SettingsConfiguration.save();
		return true;
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new WoW());
		game.setShowFPS(false);
		game.setIcon("res/ui/wow_icon.png");
		game.setAlwaysRender(true);
		game.setTargetFrameRate(60);
		game.setDisplayMode(Width, Height, false);
		game.start();
	}
}
