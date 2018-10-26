package wow.game.util.system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * An extendable class for systems.
 * @author Xolitude
 *
 */
public abstract class AbstractSystem {

	protected GameContainer container;
	protected StateBasedGame sbg;
	
	public AbstractSystem(GameContainer container, StateBasedGame sbg) {
		this.container = container;
		this.sbg = sbg;
	}
	
	public abstract void run() throws SlickException;
}
