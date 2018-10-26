package wow.game.gui.action;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.state.State3Game;

/**
 * Open the interface options.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ButtonActionInterfaceOptions extends IAction {

	@Override
	public void performAction(GameContainer container, StateBasedGame sbg) {
		State3Game game = (State3Game) sbg.getCurrentState();
		game.getOptionsGui().openInterfaceOptions();
	}
}
