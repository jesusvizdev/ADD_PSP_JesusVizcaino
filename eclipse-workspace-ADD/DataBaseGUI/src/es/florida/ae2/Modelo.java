package es.florida.ae2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  La clase Modelo contiene todos los métodos para realizar operaciones en la aplicación.
 */
public class Modelo {
	
	private Connection connection;
	private static List<String> usersList = new ArrayList<>();
	private static List<String> passwordsList = new ArrayList<>();
	private static List<String> privilegesList = new ArrayList<>();
	private String nameToLogin;
	private String queryResult;
	private String queryError = null;
	
	
	/**
	 * Es un simple constructor de la clase Modelo que no recibe parámetros. Inicializa la conexión a la Base de Datos.
	 */
	public Modelo() {
		openConnectionDB();
	}
	
	/**
	 * Método que permite logear al usuario. Para ello, compara los datos introducidos por el usuario con las listas que almacenan los usuarios y contraseñas válidas.
	 * @param user Recibe como parámetro el usuario ingresado por el usuario.
	 * @param password Recibe como parámetro la contraseña ingresada por el usuario.
	 * @return Se retorna el tipo de usuario que se va a logear, o null si no existe, o "" si la conexión no está activa.
	 */
	public String loginUser(String user, String password) {
		
			try {
				
				nameToLogin = null;
				
				if (connection.isClosed()) return "";
				
				getUsersDBInformation(connection);
				
				String encryptedPassword = encryptPassword(password);
				
				for (String passwordUser : passwordsList) {
					if (passwordUser.equals(encryptedPassword)) {
						for (String userName : usersList) {
							if (userName.equals(user)) {
								nameToLogin = user;		
							}
						}

					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			} 
			
			return nameToLogin;
	}
	
	/**
	 * Obtiene la información de los usuarios XML y los mete en un Array. Estos usuarios sirven para realizar la conexión con la BBDD. En un principio se inicia mediante client.
	 * @param userToLogin Recibe como parámetro el usuario para el que se quiere realizar la conexión a la BBDD.
	 * @return Retorna un Array con la información del usuario.
	 */
	public String[] getXmlInformation(String userToLogin) {
		
		String[] data = new String[3];
		
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document;
			if (userToLogin.equals("client")) { document = dBuilder.parse(new File("client_config.xml"));}
			else { document = dBuilder.parse(new File("admin_config.xml"));	}
			
			Element configElement = document.getDocumentElement();
			
			String url = configElement.getElementsByTagName("url").item(0).getTextContent();
			String user = configElement.getElementsByTagName("user").item(0).getTextContent();
			String password = configElement.getElementsByTagName("password").item(0).getTextContent();
			
			data[0] = url;
			data[1] = user;
			data[2] = password;
			
		} catch (Exception e) {
	        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

		}
		
		return data;
	}
	
	/**
	 * Método que obtiene la información de los usuarios adminsitrador1 y client1. Almacena sus datos en unas listas.
	 * @param connection Se recibe como parámetro la conexión activa de la BBDD.
	 */
	public void getUsersDBInformation(Connection connection) {
		
		try {
			
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM users");

				while(rs.next()) {
									
					String name = rs.getString(2);
					usersList.add(name);
					String password = rs.getString(3);
					passwordsList.add(password);
					String privilege = rs.getString(4);
					privilegesList.add(privilege);
					
				    }
				
				rs.close();
				
		} catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Método que encripta un string encriptado, ya que las contraseñas de los usuarios de la BBDD se encuentran encriptadas y es necesario realizar la comparación.
	 * @param password Recibe como parámetro la contraseña a encriptar.
	 * @return Retorna la contraseña encriptada.
	 */
	public String encryptPassword(String password) {
			String hass = DigestUtils.md5Hex(password);
			return hass;
	}
	
	/**
	 * Obtiene la información XML del usuario administrador y se cambia la conexión a admin, con más permisos.
	 */
	public void runAdminDB() {
		
		try {
			if (!connection.isClosed()) {
				String[] data = getXmlInformation("admin");
				connection.close();
				connection = DriverManager.getConnection(data[0],data[1],data[2]);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Este método identifica el tipo de consulta SQL. Lanza un método dependiendo de la operación a realizar: GET,INSERT,UPDATE o DELETE.
	 * @param query
	 */
	public void typeOfQuery(String query) {
		String queryLower = query.toLowerCase();
		if (queryLower.contains("select")) {
			getDataQuery(query);
		} else if (queryLower.contains("insert") || queryLower.contains("update") || queryLower.contains("delete")) {
			alterDataQuery(query);
		}
	}
	
	/**
	 * Método que hace un GET en la Base de Datos. Una operación para la que todos los usuarios tienen permisos. Si hay un error se muestra en pantalla.
	 * @param query Recibe como parámetro la consulta GET.
	 */
	public void getDataQuery(String query){

		try {
			queryResult = "";
			queryError = null;
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int numColumns = metaData.getColumnCount();
			String nameColumns;
			while (rs.next()) {
				for (int i=1; i<= numColumns; i++) {
					nameColumns = metaData.getColumnName(i);
					queryResult += nameColumns + ":  " + rs.getString(i) + "\n";
				}
				queryResult += "\n";
			}	
			rs.close();
			stmt.close();
		} catch (Exception e) {
			queryError = e.getMessage();
	        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	/**
	 * Método que realiza la manipulación en la Base de Datos para las operaciones INSERT, DELETE y UPDATE. Cualquier error se muestra por pantalla.
	 * @param query Recibe como parámetro la secuencia SQL que el usuario escribe.
	 */
	public void alterDataQuery(String query) {
		queryResult = "";
		queryError = null;
		int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to alter the Data Base?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
		if (confirmation == JOptionPane.YES_OPTION) {
			try {
				PreparedStatement psAlter = connection.prepareStatement(query);
				psAlter.executeUpdate();
				psAlter.close();
		        JOptionPane.showMessageDialog(null, "Changues have been saved successfully.", "CHANGES", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e) {
				queryError = e.getMessage();
		        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

			}
		}
		
	}
	
	/**
	 * Método que controla los permisos del usuario cliente. 
	 * @param query Recibe por parámetro la consulta query, para analizar si hace uso de las operaciones INSERT, UPDATE O DELETE.
	 * @return Si la consulta no hace uso de las operaciones prohibidas se retorna true, de lo contrario false y se lanza un error.
	 */
	public boolean checkError(String query) {
			String queryLower = query.toLowerCase();
			if ((queryLower.contains("insert") || queryLower.contains("update") || queryLower.contains("delete")) && nameToLogin.equals("client1")) {
		        return false;
			}
			return true;
	}
	
	/**
	 * @return Retorna la variable privada queryResult para poder hacer algún uso de interfaz desde Vista. En este caso, el resultado de una consulta query.
	 */
	public String getQueryResult() {
		return queryResult;
	}
	
	/**
	 * @return Retorna la variable privada queryError para poder hacer algún uso de interfaz desde Vista de algún posible error.
	 */
	public String getQueryError() {
		return queryError;
	}
	
	
	/**
	 * Método que cierra la conexión con la Base de Datos siempre y cuando se haya inicializado.
	 */
	public void closeConnectionDB() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que abre la conexión con la Base de Datos siempre y cuando se haya cerrado.
	 */
	public void openConnectionDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String[] data = getXmlInformation("client");
			connection = DriverManager.getConnection(data[0],data[1],data[2]);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
}
