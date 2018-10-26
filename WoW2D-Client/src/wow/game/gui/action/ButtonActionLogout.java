package wow.game.gui.action;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.util.SettingsConfiguration;

/**
 * "Logout" of the game.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ButtonActionLogout extends IAction {

	@Override
	public void performAction(GameContainer container, StateBasedGame sbg) {
		SettingsConfiguration.save();
		container.exit();
	}
}
