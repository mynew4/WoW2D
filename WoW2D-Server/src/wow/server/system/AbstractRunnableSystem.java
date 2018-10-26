package wow.server.system;

/**
 * An extendable class for runnable-systems.
 * @author Xolitude (October 26, 2018)
 *
 */
public abstract class AbstractRunnableSystem implements Runnable {
	
	public abstract void start();
	
	@Override
	public abstract void run();
}
