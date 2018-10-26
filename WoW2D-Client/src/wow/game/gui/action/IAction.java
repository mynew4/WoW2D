package wow.game.gui.action;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * An extendable class for button actions.
 * @author Xolitude
 *
 */
public abstract class IAction {

	public abstract void performAction(GameContainer container, StateBasedGame sbg);
}
