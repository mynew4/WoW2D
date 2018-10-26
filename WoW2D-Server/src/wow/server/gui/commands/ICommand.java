package wow.server.gui.commands;

/**
 * An extendable class for commands.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class ICommand {
	
	private String commandPrefix;
	
	public ICommand(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}

	public abstract void performCommand(String args);
	
	public String getCommandPrefix() {
		return commandPrefix;
	}
}
