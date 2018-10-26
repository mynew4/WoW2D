package wow.game.util.system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.objects.entity.EntityInstance;
import wow.game.objects.entity.EntityInstance;
import wow.game.state.State3Game;
import wow.game.util.manager.EntityManager;
import wow.game.util.manager.NetworkManager;

/**
 * Handles rendering.
 * @author Xolitude (October 26, 2018)
 *
 */
public class RenderSystem extends AbstractSystem {

	public RenderSystem(GameContainer container, StateBasedGame sbg) {
		super(container, sbg);
	}

	@Override
	public void run() throws SlickException {
		renderPlayers();
		renderEntities();
		renderUserInterface();
	}
	
	private void renderPlayers() throws SlickException {
		Graphics graphics = container.getGraphics();
		
		NetworkManager.getPlayer().render(container, sbg, graphics);
		if (NetworkManager.getPlayers().size() > 0) {
			for (int i = 0; i < NetworkManager.getPlayers().size(); i++) {
				NetworkManager.getPlayers().get(i).render(container, sbg, graphics);
			}
		}
	}
	
	private void renderEntities() throws SlickException {
		Graphics graphics = container.getGraphics();
		
		if (EntityManager.getInstancedEntities().size() > 0) {
			for (EntityInstance e : EntityManager.getInstancedEntities()) {
				e.render(graphics);
			}
		}
	}
	
	private void renderUserInterface() throws SlickException {
		State3Game game = (State3Game)sbg.getCurrentState();
		Graphics graphics = container.getGraphics();
		
		graphics.resetTransform();
		if (!game.getOptionsGui().isVisible() && !game.getOptionsGui().isInterfaceVisible()) {
			game.getChatGui().render(container, sbg, graphics);
			game.getFPSGui().render(container, sbg, graphics);
			game.getHotbarGui().render(graphics);
		}
		game.getOptionsGui().render(container, sbg, graphics);
	}
}
