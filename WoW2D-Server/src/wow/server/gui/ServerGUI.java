package wow.server.gui;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import wow.server.gui.commands.CommandAccountCreate;
import wow.server.gui.commands.CommandAccountSetLevel;
import wow.server.gui.commands.ICommand;
import wow.server.net.WoWServer;

/**
 * The server gui.
 * @author Xolitude (October 26, 2018)
 *
 */
public class ServerGUI {

	private JFrame frmWoW2D;
	private JTextField textField;
	private JTextArea textArea;
	
	private DefaultListModel<String> playerListModel;
	private JList<String> playerList;
	private static LinkedList<ICommand> commands = new LinkedList<ICommand>();
	
	/**
	 * The commands the server can use.
	 */
	static {
		commands.add(new CommandAccountCreate());
		commands.add(new CommandAccountSetLevel());
	}
	
	/**
	 * The different log-types.
	 */
	public enum LogType {
		Server,
		Logon,
		World
	}
	
	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWoW2D = new JFrame();
		frmWoW2D.setResizable(false);
		frmWoW2D.setTitle("WoW-2D "+WoWServer.VERSION);
		frmWoW2D.setBounds(100, 100, 554, 302);
		frmWoW2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWoW2D.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 247, 380, 20);
		frmWoW2D.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					parseCommand(textField.getText());
					textField.setText("");
				}
			}
		});
		
		playerListModel = new DefaultListModel<String>();
		playerList = new JList<String>(playerListModel);
		playerList.setBounds(400, 27, 139, 240);
		frmWoW2D.getContentPane().add(playerList);		
		
		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPlayers.setBounds(448, 11, 51, 14);
		frmWoW2D.getContentPane().add(lblPlayers);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		JScrollPane scroller = new JScrollPane();
		scroller.setViewportView(textArea);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setBounds(10, 11, 380, 225);
		frmWoW2D.getContentPane().add(scroller);
		frmWoW2D.setVisible(true);
	}
	
	private void parseCommand(String cmd) {
		for (int i = 0; i < commands.size(); i++) {
			ICommand command = commands.get(i);
			if (cmd.startsWith(command.getCommandPrefix())) {
				command.performCommand(cmd);
			}
		}
	}
	
	public void writeMessage(LogType logType, String message) {
		String logName = "";
		switch (logType) {
		case Server:
			logName = "[Server]";
			break;
		case Logon:
			logName = "[Logon]";
			break;
		case World:
			logName = "[World]";
			break;
		}
		textArea.append(logName + " " + message + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	public void addPlayer(String characterName) {
		playerListModel.addElement(characterName);
	}
	
	public void removePlayer(String characterName) {
		playerListModel.removeElement(characterName);
	}
}
