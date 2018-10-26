package wow.game.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.gui.GuiChat;
import wow.game.gui.GuiFPS;
import wow.game.gui.GuiHotbar;
import wow.game.gui.GuiOptions;
import wow.game.objects.PlayerCharacter;
import wow.game.util.Camera;
import wow.game.util.manager.NetworkManager;
import wow.game.util.system.RenderSystem;

/**
 * The game state.
 * @author Xolitude (October 26, 2018)
 *
 */
public class State3Game extends BasicGameState {
	
	public final static int ID = 3;
	
	private Camera camera;
	
	private GuiChat chatGui;
	private GuiOptions optionsGui;
	private GuiFPS fpsGui;
	private GuiHotbar hotbarGui;
	
	private RenderSystem renderSystem;
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		chatGui = new GuiChat(container);
		optionsGui = new GuiOptions(container);
		fpsGui = new GuiFPS(container);
		hotbarGui = new GuiHotbar();
		hotbarGui.setLocation(container.getWidth() / 2, container.getHeight());
		camera = new Camera();	
		
		renderSystem = new RenderSystem(container, sbg);
	}
	
	public void init(PlayerCharacter character) {
		NetworkManager.initPlayerObject(character.getName(), character.getRace());
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics graphics) throws SlickException {
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, container.getWidth(), container.getHeight());
		camera.translate(NetworkManager.getPlayer().getX(), NetworkManager.getPlayer().getY(), graphics);	
		renderSystem.run();
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		NetworkManager.getPlayer().update(container, sbg, delta);
		if (NetworkManager.getPlayers().size() > 0) {
			for (int i = 0; i < NetworkManager.getPlayers().size(); i++) {
				NetworkManager.getPlayers().get(i).update(container, sbg, delta);
			}
		}
		
		Input input = container.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			if (optionsGui.isInterfaceVisible())
				optionsGui.setInterfaceOptionsVisible(false);
			optionsGui.setVisible(!optionsGui.isVisible());
		}
		chatGui.update(container, sbg, delta);
		optionsGui.update(container, sbg, delta);
	}
	
	public void spawnPlayer(float x, float y, int direction) {
		NetworkManager.getPlayer().setLocation(x, y, direction);
	}
	
	public void sendChat() {
		chatGui.sendChat();
	}
	
	public GuiChat getChatGui() {
		return chatGui;
	}
	
	public GuiOptions getOptionsGui() {
		return optionsGui;
	}
	
	public GuiFPS getFPSGui() {
		return fpsGui;
	}
	
	public GuiHotbar getHotbarGui() {
		return hotbarGui;
	}

	@Override
	public int getID() {
		return ID;
	}
}
