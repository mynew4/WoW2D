package wow.game.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import wow.game.objects.mob.player.MobPlayer;
import wow.game.state.State3Game;

/**
 * The player-controller.
 * @author Xolitude (October 26, 2018)
 *
 */
public class PlayerController {
	
	public enum Key {
		Move_Up(Input.KEY_W, true),
		Move_Down(Input.KEY_S, true),
		Move_Left(Input.KEY_A, true),
		Move_Right(Input.KEY_D, true),
		Chat(Input.KEY_RETURN, false);
		
		private int keyCode;
		
		private boolean isDown = false;
		private boolean isPressed = false;
		
		private boolean canBeHeld = false;
		
		Key(int keyCode, boolean canBeHeld) {
			this.keyCode = keyCode;
			this.canBeHeld = canBeHeld;
		}
		
		public String getKeyName() {
			return Input.getKeyName(keyCode);
		}
	}
	
	private MobPlayer player;

	public PlayerController(MobPlayer player) {
		this.player = player;
	}
	
	public void update(GameContainer container, StateBasedGame sbg) {
		Input input = container.getInput();
		State3Game game = (State3Game)sbg.getCurrentState();
		
		if (!game.getChatGui().hasFocus()) {
			for (Key key : Key.values()) {
				if (key.canBeHeld) {
					if (input.isKeyDown(key.keyCode)) {
						key.isDown = true;
					} else {
						key.isDown = false;
					}
				}
			}
			
			for (Key key : Key.values()) {
				if (key.canBeHeld) {
					if (key.isDown) {
						String keyName = key.getKeyName().toLowerCase();
						switch (keyName) {
						case "w":
							player.setMovingUp(true);
							break;
						case "a":
							player.setMovingLeft(true);
							break;
						case "s":
							player.setMovingDown(true);
							break;
						case "d":
							player.setMovingRight(true);
							break;
						}
					} else {
						String keyName = key.getKeyName().toLowerCase();
						switch (keyName) {
						case "w":
							player.setMovingUp(false);
							break;
						case "a":
							player.setMovingLeft(false);
							break;
						case "s":
							player.setMovingDown(false);
							break;
						case "d":
							player.setMovingRight(false);
							break;
						}
					}
				}
			}
		} else {
			if (player.isMovingUp() || player.isMovingDown() || player.isMovingLeft() || player.isMovingRight()) {
				player.stopMoving();
			}
		}
		
		updateKeyPress(container, sbg);
	}
	
	public void updateKeyPress(GameContainer container, StateBasedGame sbg) {
		Input input = container.getInput();
		
		for (Key key : Key.values()) {
			if (!key.canBeHeld) {
				if (input.isKeyPressed(key.keyCode)) {
					key.isPressed = true;
				}
			}
		}
		
		for (Key key : Key.values()) {
			if (!key.canBeHeld) {
				if (key.isPressed) {
					String keyName = key.getKeyName().toLowerCase();
					switch (keyName) {
					case "return":
						State3Game game = (State3Game)sbg.getCurrentState();
						game.sendChat();
						key.isPressed = false;
						break;
					}
				}
			}
		}
	}
}
