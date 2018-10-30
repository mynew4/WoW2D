package wow.game.gui.action;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.gui.GuiOptionsInterface;
import wow.game.state.State3Game;
import wow.game.util.SettingsConfiguration;
import wow.game.util.SettingsConfiguration.Keys;

/**
 * Save and close the interface options.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ButtonActionConfirmInterfaceOptions extends IAction {

	@Override
	public void performAction(GameContainer container, StateBasedGame sbg) {
		State3Game game = (State3Game) sbg.getCurrentState();
		GuiOptionsInterface interfaceUi = game.getOptionsGui().getInterfaceOptionsUi();
		
		boolean shouldRenderMyName = interfaceUi.shouldRenderMyName();
		boolean shouldRenderPlayerNames = interfaceUi.shouldRenderPlayerNames();
		boolean shouldRenderMobNames = interfaceUi.shouldRenderMobNames();
				
		SettingsConfiguration.setSettingValue(Keys.RenderMyName, shouldRenderMyName);
		SettingsConfiguration.setSettingValue(Keys.RenderPlayerNames, shouldRenderPlayerNames);
		SettingsConfiguration.setSettingValue(Keys.RenderMobNames, shouldRenderMobNames);
		
		game.getOptionsGui().setInterfaceOptionsVisible(false);
	}
}
