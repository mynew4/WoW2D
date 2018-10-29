package wow.server.mysql;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import wow.net.util.Logger;
import wow.server.net.LogonConnection;
import wow.server.net.WoWServer;
import wow.server.net.WorldConnection;
import wow.server.net.game.object.Realm;
import wow.server.net.game.object.entity.Entity;
import wow.server.util.BCrypt;
import wow.server.util.ServerConfiguration;
import wow.server.util.Vector2f;

/**
 * Handles everything related to the database's.
 * @author Xolitude (October 26, 2018)
 *
 */
public class DB {
	
	private static DB Database;
	private static Connection AuthenticationConnection;
	private static Connection WorldConnection;
	private static Connection CharacterConnection;
	private static Properties Properties;
	
	private static String AuthenticationURL = "jdbc:mysql://%s:%s/%s";
	private static String WorldURL = "jdbc:mysql://%s:%s/%s";
	private static String CharsURL = "jdbc:mysql://%s:%s/%s";
	
	static {
		Properties = new Properties();
		Properties.setProperty("User", ServerConfiguration.getDatabaseUsername());
		Properties.setProperty("Password", ServerConfiguration.getDatabasePassword());
		Properties.setProperty("useSSL", ServerConfiguration.shouldUseSSL());
		
		AuthenticationURL = String.format(AuthenticationURL, ServerConfiguration.getDatabaseHost(), ServerConfiguration.getDatabasePort(), ServerConfiguration.getAuthDatabase());
		WorldURL = String.format(WorldURL, ServerConfiguration.getDatabaseHost(), ServerConfiguration.getDatabasePort(), ServerConfiguration.getWorldDatabase());
		CharsURL = String.format(CharsURL, ServerConfiguration.getDatabaseHost(), ServerConfiguration.getDatabasePort(), ServerConfiguration.getCharacterDatabase());
	}
	
	/**
	 * Test the connections.
	 * @return a new DB instance.
	 */
	public static DB newInstance() {
		if (Database == null) {
			Database = new DB();
			
			System.out.println("Creating a new MySQL instance...");
			
			try {
				AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
				WorldConnection = DriverManager.getConnection(WorldURL, Properties);
				CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			} catch (SQLException ex) {
				Logger.write(String.format("Unable to initialize a new MySQL instance: %s", ex.getMessage()));
			} finally {
				closeQuietly();
			}
		}
		return Database;
	}
	
	/**
	 * Load the entity table from the database.
	 * @return a list of entities
	 */
	public static ArrayList<Entity> loadEntityTable() {
		String query = String.format("SELECT entity_id, entity_name, scriptFile FROM world_entities");
		Statement statement = null;
		ResultSet resultSet = null;
		
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		try {
			WorldConnection = DriverManager.getConnection(WorldURL, Properties);
			
			statement = WorldConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			if (resultSet.next()) {
				Entity entity = null;
				
				int id = resultSet.getInt("entity_id");
				String name = resultSet.getString("entity_name");
				String scriptFile = resultSet.getString("scriptFile");
				
				entity = new Entity(id, name, scriptFile);
				entities.add(entity);
			}
			return entities;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to fetch entities: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return null;
	}
	
	/**
	 * Load all used character names.
	 * @return a list of used names
	 */
	public static ArrayList<String> loadUsedNames() {
		String query = "SELECT character_name FROM user_characters";
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> usedNames = new ArrayList<String>();
		
		try {
			CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			
			statement = CharacterConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				String name = resultSet.getString("character_name");
				usedNames.add(name);
			}
			
			return usedNames;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to load used names: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return null;
	}
	
	/**
	 * Load all banned names.
	 * @return a list of banned names
	 */
	public static ArrayList<String> loadBannedNames() {
		String query = "SELECT character_name FROM auth_banned_names";
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> bannedNames = new ArrayList<String>();
		
		try {
			AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
			
			statement = AuthenticationConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				String name = resultSet.getString("character_name");
				bannedNames.add(name);
			}
			
			return bannedNames;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to load banned names: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return null;
	}
	
	/**
	 * Attempt to add a character to the database.
	 * @param user_id
	 * @param character_name
	 * @param realm_id
	 */
	public static void addCharacter(int user_id, String character_name, int realm_id) {
		String query = String.format("INSERT INTO user_characters (user_id, realm_id, character_name) VALUES (?, ?, ?)");
		PreparedStatement statement = null;
		
		try {
			CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			statement = CharacterConnection.prepareStatement(query);
			
			statement.setInt(1, user_id);
			statement.setInt(2, realm_id);
			statement.setString(3, character_name);
			statement.execute();
			
			Logger.write(String.format("Added character '%s' to realm '%s'.", character_name, realm_id));
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to add a character: %s", ex.getMessage()));
		} finally {
			closeQuietly(statement);
			closeQuietly();
		}

	}
	
	/**
	 * Attempt to delete a character from the database.
	 * @param user_id
	 * @param character_name
	 * @param realm_id
	 * @return true if deleted, false otherwise
	 */
	public static boolean deleteCharacter(int user_id, String character_name, int realm_id) {
		String query = String.format("DELETE FROM user_characters WHERE user_id = ? AND character_name = ? AND realm_id = ?");
		PreparedStatement statement = null;
		
		try {
			CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			statement = CharacterConnection.prepareStatement(query);
			
			statement.setInt(1, user_id);
			statement.setString(2, character_name);
			statement.setInt(3, realm_id);
			statement.execute();
			
			Logger.write(String.format("Deleted character '%s' from realm '%s'.", character_name, realm_id));
			return true;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to delete character: %s", ex.getMessage()));
			return false;
		} finally {
			closeQuietly(statement);
			closeQuietly();
		}
	}
	
	/**
	 * Save a character to the database.
	 * @param worldConnection
	 */
	public static void saveCharacter(WorldConnection worldConnection) {
		String query = String.format("UPDATE user_characters SET x_position = ?, y_position = ?, direction = ? WHERE character_name = ?");
		PreparedStatement statement = null;
		
		try {
			CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			statement = CharacterConnection.prepareStatement(query);
			
			statement.setFloat(1, worldConnection.getX());
			statement.setFloat(2, worldConnection.getY());
			statement.setInt(3, worldConnection.getDirection());
			statement.setString(4, worldConnection.getCharacterName());
			statement.execute();
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to save character: %s", ex.getMessage()));
		} finally {
			closeQuietly(statement);
			closeQuietly();
		}
	}
	
	/**
	 * Get a character's position from the database.
	 * @param characterName
	 * @return a vector of the character
	 */
	public static Vector2f getCharacterPosition(String characterName) {
		Vector2f vector = null;
		String query = String.format("SELECT x_position, y_position, direction FROM user_characters WHERE character_name='%s'", characterName);
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			
			statement = CharacterConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				vector = new Vector2f(resultSet.getFloat("x_position"), resultSet.getFloat("y_position"), resultSet.getInt("direction"));
			}
			return vector;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to fetch character position: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return null;
	}
	
	/**
	 * Get a list of characters for the specified user.
	 * @param user_id
	 * @param realm_id
	 * @return a list of characters
	 */
	public static LinkedList<String> getCharactersForUser(int user_id, int realm_id) {
		LinkedList<String> chars = new LinkedList<String>();
		String query = String.format("SELECT character_name FROM user_characters WHERE user_id='%s' AND realm_id='%s'", user_id, realm_id);
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			CharacterConnection = DriverManager.getConnection(CharsURL, Properties);
			
			statement = CharacterConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				chars.add(resultSet.getString("character_name"));
			}
			return chars;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to fetch characters: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return null;
	}
	
	/**
	 * Attempt to create an account.
	 * @param username
	 * @param password
	 * @return true if success, false otherwise
	 */
	public static boolean createAccount(String username, String password) {
		username = username.toUpperCase();
		
		if (!doesAccountExist(username)) {
			String query = String.format("INSERT INTO auth_users (username, password, salt, user_level) VALUES (?, ?, ?, 0)");
			PreparedStatement statement = null;
			String bcrypt_hash = null;
			String bcrypt_salt = null;
			
			try {
				// sha-256
				MessageDigest d = MessageDigest.getInstance("SHA-256");
				byte[] hashed = d.digest(password.getBytes());
				String hashedHex = bytesToHex(hashed);
				
				// bcrypt
				bcrypt_salt = BCrypt.gensalt(12);
				bcrypt_hash = BCrypt.hashpw(hashedHex+WoWServer.STATIC_SALT, bcrypt_salt);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			try {
				AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
				statement = AuthenticationConnection.prepareStatement(query);
				
				statement.setString(1, username);
				statement.setString(2, bcrypt_hash);
				statement.setString(3, bcrypt_salt);
				statement.execute();
				
				return true;
			} catch (SQLException ex) {
				Logger.write(String.format("Unable to create account: %s", ex.getMessage()));
			} finally {
				closeQuietly(statement);
				closeQuietly();
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Attempt to create an account.
	 * @param realm_name
	 * @param realm_port
	 * @return true if success, false otherwise
	 */
	
	public static boolean createRealm(int realm_id, String realm_name, int realm_port)
	{
		if (!doesRealmExist(realm_name))
		{
			String query = String.format("INSERT INTO auth_realms (realm_id, realm_name, realm_port) VALUES (?, ?, ?)");
			PreparedStatement statement = null;
			try {
				AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
				statement = AuthenticationConnection.prepareStatement(query);
				
				statement.setInt(1, realm_id);
				statement.setString(2, realm_name);
				statement.setInt(3, realm_port);
				statement.execute();
				
				return true;
			} catch (SQLException ex) {
				Logger.write(String.format("Unable to create realm: %s", ex.getMessage()));
			} finally {
				closeQuietly(statement);
				closeQuietly();
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Attempt to set the level of an account.
	 * @param username
	 * @param level
	 * @return true if success, false otherwise
	 */
	public static boolean setAccountLevel(String username, int level) {
		username = username.toUpperCase();
		if (doesAccountExist(username)) {
			String query = String.format("UPDATE auth_users SET user_level = ? WHERE username = ?");
			PreparedStatement statement = null;
			
			try {
				AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
				statement = AuthenticationConnection.prepareStatement(query);
				
				statement.setInt(1, level);
				statement.setString(2, username);
				statement.execute();
				
				return true;
			} catch (SQLException ex) {
				Logger.write(String.format("Unable to update account level: %s", ex.getMessage()));
			} finally {
				closeQuietly(statement);
				closeQuietly();
			}
		} else {
			return false;
		}
		return false;
	}
	
	/**
	 * Does an account exist?
	 * @param username
	 * @return true if it does, false otherwise
	 */
	private static boolean doesAccountExist(String username) {
		String query = String.format("SELECT username FROM auth_users WHERE username='%s'", username);
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
			
			statement = AuthenticationConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			if (resultSet.next()) {
				return true;
			}
			return false;
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to check account existence: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return false;
	}
	
	/**
	 * Does a realm exist? If so, return a new temporary connection.
	 * @param realm_name
	 * @return a temporary connection
	 */
	
	private static boolean doesRealmExist(String realm_name)
	{
		String query = String.format("SELECT realm_name FROM auth_realms WHERE realm_name='%s'", realm_name);
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
			
			statement = AuthenticationConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			if (resultSet.next()) {
				return true;
			}
			return false;
		}
		catch (SQLException ex) {
			Logger.write(String.format("Unable to check realm existence: %s", ex.getMessage()));
		}
		finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return false;
	}
	
	/**
	 * Does an account exist? If so, return a new temporary connection.
	 * @param username
	 * @param password
	 * @return a temporary connection
	 */
	public static LogonConnection doesUserExist(String username, String password) {
		String query = String.format("SELECT user_id, username, user_level, password, salt FROM auth_users WHERE username='%s'", username);
		Statement statement = null;
		ResultSet resultSet = null;
		LogonConnection tempConnection = null;
		
		try {
			AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
			
			statement = AuthenticationConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			if (resultSet.next()) {
				String dbPassword = resultSet.getString("password");
				String dbSalt = resultSet.getString("salt");
				String hashedPw = BCrypt.hashpw(password+WoWServer.STATIC_SALT, dbSalt);
				if (dbPassword.equals(hashedPw)) {
					tempConnection = new LogonConnection(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getInt("user_level"));
					return tempConnection;
				} else
					return null;
			}
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to create a logon connection: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return null;
	}
	
	/**
	 * Get the realms from the database.
	 * @return a list of realms
	 */
	public static LinkedList<Realm> fetchRealms() {
		LinkedList<Realm> realms = new LinkedList<Realm>();
		
		String query = String.format("SELECT * FROM auth_realms");
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			AuthenticationConnection = DriverManager.getConnection(AuthenticationURL, Properties);
			
			statement = AuthenticationConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				int id = resultSet.getInt("realm_id");
				String name = resultSet.getString("realm_name");
				int port = resultSet.getInt("realm_port");
				realms.add(new Realm(id, name, port));
			}
		} catch (SQLException ex) {
			Logger.write(String.format("Unable to fetch a list of realms: %s", ex.getMessage()));
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly();
		}
		return realms;
	}
	
	/**
	 * Closes the 'resultSet' object quietly.
	 * @param resultSet
	 */
	private static void closeQuietly(ResultSet resultSet) {
		if (resultSet != null)
			try { resultSet.close(); } catch (Exception e) {}
	}
	
	/**
	 * Closes the 'sqlStatement' object quietly.
	 * @param sqlStatement
	 */
	private static void closeQuietly(Statement sqlStatement) {
		if (sqlStatement != null)
			try { sqlStatement.close(); } catch (Exception e) {}
	}
	
	/**
	 * Closes the connection objects quietly.
	 */
	private static void closeQuietly() {
		if (AuthenticationConnection != null) 
			try { AuthenticationConnection.close(); } catch (Exception e) {}
		if (WorldConnection != null)
			try { WorldConnection.close(); } catch (Exception e) {}
		if (CharacterConnection != null) 
			try { CharacterConnection.close(); } catch (Exception e) {}
	}
	
	/**
	 * Convert a byte-array to a hex-string.
	 * @param hash
	 * @return a hex-string
	 */
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
