package es.florida.ae2;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;

import javax.swing.JPasswordField;

/**
 * Clase que desplega la primera interfaz gráfica donde introducir el nombre del usuario y contraseña. También permite cerrar la conexión la Base de Datos.
 */
public class VistaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUser;
	private JButton btnLogin;
	private JButton btnCloseConnection;
	private JPasswordField txtPassword;
	private JButton btnOpenConnection;

	/**
	 * Constructor que crea los componentes y hace visible la ventana.
	 */
	public VistaLogin() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 367, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titlePassword = new JLabel("PASSWORD");
		titlePassword.setBounds(27, 139, 91, 37);
		titlePassword.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(titlePassword);
		
		JLabel lblLoginBiblioteca = new JLabel("LOGIN LIBRARY");
		lblLoginBiblioteca.setBounds(66, 23, 253, 37);
		lblLoginBiblioteca.setForeground(new Color(0, 0, 0));
		lblLoginBiblioteca.setFont(new Font("Tahoma", Font.BOLD, 25));
		contentPane.add(lblLoginBiblioteca);
		
		txtUser = new JTextField();
		txtUser.setBounds(143, 108, 160, 19);
		txtUser.setColumns(10);
		contentPane.add(txtUser);
		
		JLabel userTitle_1 = new JLabel("USER");
		userTitle_1.setBounds(76, 97, 42, 37);
		userTitle_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(userTitle_1);
		
		btnLogin = new JButton("LOGIN");
		btnLogin.setBounds(188, 192, 115, 27);
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(btnLogin);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(145, 150, 154, 19);
		contentPane.add(txtPassword);
		
		btnCloseConnection = new JButton("Close Connection");
		btnCloseConnection.setBounds(13, 276, 160, 27);
		btnCloseConnection.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(btnCloseConnection);
		
		btnOpenConnection = new JButton("Open Connection");
		btnOpenConnection.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnOpenConnection.setBounds(183, 276, 160, 27);
		contentPane.add(btnOpenConnection);
		
		setVisible(true);
		}
	
	/**
	 * @return Método que retorna el campo de texto donde se insta el usuario.
	 */
	public JTextField getTextUser() {
		return txtUser;
	}
	
	/**
	 * @return Método que retorna el campo de texto donde se inserta la contraseña.
	 */
	public JTextField getTextPassword() {
		return txtPassword;
	}
	
	/**
	 * @return Metódo que retorna el botón para iniciar sesión.
	 */
	public JButton getBtnLogin() {
		return btnLogin;
	}
	
	/**
	 * @return Método que retorna el botón para cerrar la conexión la Base de Datos.
	 */
	public JButton getBtnCloseConnection() {
		return btnCloseConnection;
	}
	
	/**
	 * @return Método que retorna el botón para abrir la conexión a la Base de Datos. (Desde cliente).
	 */
	public JButton getBtnOpenConnection() {
		return btnOpenConnection;
	}
}
