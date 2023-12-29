package es.florida.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLayeredPane;
import java.awt.CardLayout;

/**
 * Clase Vista principal. Es la interfaz del juego. Contiene todos los metodos
 * getters para que la clase Controlador pueda hacer uso de los componentes de
 * la interfaz grafica.
 */
public class Vista {

	private JFrame frame;
	private final JPanel panelOptions = new JPanel();
	private JPanel panelGame = new JPanel();
	private List<JButton> cardButtons = new ArrayList<JButton>();
	private List<JButton> reduceCardButtons = new ArrayList<JButton>();
	private JButton btnRegister;
	private JPanel panelRegister;
	private JTextField txtUsername;
	private JButton btnNewUser;
	private JLabel txtResult;
	private JButton btnLogin;
	private JPasswordField txtPass;
	private JPasswordField txtRepeatPass;
	private JTextField txtUserLogin;
	private JPasswordField txtPassLogin;
	private JPanel panelLogin;
	private JLayeredPane layeredPane;
	private JButton btnLoginUser;
	private JLabel txtResultLogin;
	private JButton btnPlay;
	private JLabel txtUserLogged;
	private JButton btnSave;
	private JButton btnHallOfFame;
	private JButton btnSignOff;

	/**
	 * Constructor que inicializa la interfaz, le da un nombre y la hace visible.
	 */
	public Vista() {

		initialize();
		frame.setTitle("Memory Game");
		frame.setVisible(true);

	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1087, 663);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panelOptions.setForeground(new Color(0, 0, 0));
		panelOptions.setBounds(10, 10, 241, 593);
		frame.getContentPane().add(panelOptions);
		panelOptions.setLayout(null);

		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnRegister.setBounds(10, 21, 221, 35);
		panelOptions.add(btnRegister);

		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnLogin.setBounds(10, 66, 221, 35);
		panelOptions.add(btnLogin);

		btnPlay = new JButton("Play");
		btnPlay.setEnabled(false);
		btnPlay.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnPlay.setBounds(10, 137, 221, 35);
		panelOptions.add(btnPlay);

		btnHallOfFame = new JButton("Hall of Fame");
		btnHallOfFame.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnHallOfFame.setBounds(10, 548, 221, 35);
		panelOptions.add(btnHallOfFame);

		btnSave = new JButton("Save Game");
		btnSave.setEnabled(false);
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSave.setBounds(10, 503, 221, 35);
		panelOptions.add(btnSave);

		layeredPane = new JLayeredPane();
		layeredPane.setBounds(10, 189, 221, 304);
		layeredPane.setVisible(false);
		panelOptions.add(layeredPane);
		layeredPane.setLayout(new CardLayout(0, 0));

		panelRegister = new JPanel();
		layeredPane.add(panelRegister, "name_1827672131597700");
		panelRegister.setBackground(new Color(192, 192, 192));
		panelRegister.setLayout(null);

		JLabel textTitle = new JLabel("New User");
		textTitle.setFont(new Font("Arial Black", Font.BOLD, 14));
		textTitle.setBounds(10, 10, 201, 24);
		panelRegister.add(textTitle);

		txtUsername = new JTextField();
		txtUsername.setBounds(10, 68, 201, 24);
		panelRegister.add(txtUsername);
		txtUsername.setColumns(10);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblUsername.setBounds(10, 44, 201, 24);
		panelRegister.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblPassword.setBounds(10, 98, 201, 24);
		panelRegister.add(lblPassword);

		btnNewUser = new JButton("Create User");
		btnNewUser.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewUser.setBounds(10, 243, 201, 36);
		panelRegister.add(btnNewUser);

		JLabel lblRepeatPassword = new JLabel("Repeat password");
		lblRepeatPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRepeatPassword.setBounds(10, 148, 201, 24);
		panelRegister.add(lblRepeatPassword);

		txtResult = new JLabel("");
		txtResult.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtResult.setBounds(10, 209, 201, 36);
		panelRegister.add(txtResult);

		txtPass = new JPasswordField();
		txtPass.setBounds(10, 124, 201, 24);
		panelRegister.add(txtPass);

		txtRepeatPass = new JPasswordField();
		txtRepeatPass.setBounds(10, 168, 201, 24);
		panelRegister.add(txtRepeatPass);

		panelLogin = new JPanel();
		panelLogin.setBackground(new Color(192, 192, 192));
		layeredPane.add(panelLogin, "name_1827686691525500");
		panelLogin.setLayout(null);

		JLabel lblLogin_1_1 = new JLabel("Login");
		lblLogin_1_1.setFont(new Font("Arial Black", Font.BOLD, 14));
		lblLogin_1_1.setBounds(10, 10, 201, 24);
		panelLogin.add(lblLogin_1_1);

		JLabel lblUsername_1_1_1 = new JLabel("Username");
		lblUsername_1_1_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblUsername_1_1_1.setBounds(10, 52, 201, 24);
		panelLogin.add(lblUsername_1_1_1);

		txtUserLogin = new JTextField();
		txtUserLogin.setColumns(10);
		txtUserLogin.setBounds(10, 76, 201, 24);
		panelLogin.add(txtUserLogin);

		JLabel lblPassword_1_1_1 = new JLabel("Password");
		lblPassword_1_1_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblPassword_1_1_1.setBounds(10, 106, 201, 24);
		panelLogin.add(lblPassword_1_1_1);

		txtPassLogin = new JPasswordField();
		txtPassLogin.setBounds(10, 132, 201, 24);
		panelLogin.add(txtPassLogin);

		btnLoginUser = new JButton("Login ");
		btnLoginUser.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnLoginUser.setBounds(10, 247, 201, 36);
		panelLogin.add(btnLoginUser);

		txtResultLogin = new JLabel("");
		txtResultLogin.setBounds(10, 225, 201, 23);
		panelLogin.add(txtResultLogin);
		txtResultLogin.setFont(new Font("Tahoma", Font.BOLD, 11));

		txtUserLogged = new JLabel("");
		txtUserLogged.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtUserLogged.setBounds(10, 104, 201, 23);
		panelOptions.add(txtUserLogged);

		panelGame.setBackground(new Color(171, 171, 171));
		panelGame.setBounds(261, 21, 578, 582);
		frame.getContentPane().add(panelGame);
		panelGame.setLayout(null);

		JButton img4 = new JButton("");
		img4.setBounds(10, 10, 119, 119);
		panelGame.add(img4);

		JButton img1 = new JButton("");
		img1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		img1.setBounds(157, 10, 119, 119);
		panelGame.add(img1);

		JButton img2 = new JButton("");
		img2.setBounds(305, 10, 119, 119);
		panelGame.add(img2);

		JButton img3 = new JButton("");
		img3.setBounds(449, 10, 119, 119);
		panelGame.add(img3);

		JButton img5 = new JButton("");
		img5.setBounds(10, 154, 119, 119);
		panelGame.add(img5);

		JButton img6 = new JButton("");
		img6.setBounds(157, 154, 119, 119);
		panelGame.add(img6);

		JButton img7 = new JButton("");
		img7.setBounds(305, 154, 119, 119);
		panelGame.add(img7);

		JButton img8 = new JButton("");
		img8.setBounds(449, 154, 119, 119);
		panelGame.add(img8);

		JButton img9 = new JButton("");
		img9.setBounds(10, 302, 119, 119);
		panelGame.add(img9);

		JButton img10 = new JButton("");
		img10.setBounds(157, 302, 119, 119);
		panelGame.add(img10);

		JButton img11 = new JButton("");
		img11.setBounds(305, 302, 119, 119);
		panelGame.add(img11);

		JButton img12 = new JButton("");
		img12.setBounds(449, 302, 119, 119);
		panelGame.add(img12);

		JButton img13 = new JButton("");
		img13.setBounds(10, 450, 119, 119);
		panelGame.add(img13);

		JButton img14 = new JButton("");
		img14.setBounds(157, 450, 119, 119);
		panelGame.add(img14);

		JButton img15 = new JButton("");
		img15.setBounds(305, 450, 119, 119);
		panelGame.add(img15);

		JButton img16 = new JButton("");
		img16.setBounds(449, 450, 119, 119);
		panelGame.add(img16);

		btnSignOff = new JButton("Sign Off");
		btnSignOff.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSignOff.setBounds(849, 21, 214, 35);
		frame.getContentPane().add(btnSignOff);

		getAllCardButtons();
		getReduceCardButtons();

	}

	/**
	 * Metodo que identifica los componentes que son botones dentro del panel de
	 * juego para hacerlos invisibles al iniciar la aplicacion y meterlos a una
	 * lista.
	 */
	public void getAllCardButtons() {
		for (Component component : panelGame.getComponents()) {
			if (component instanceof JButton) {
				component.setVisible(false);
				cardButtons.add((JButton) component);
			}
		}
	}

	/**
	 * Metodo que obtiene de todos los botones solo 8 para el juego 2x4. Para ello,
	 * se hace uso de otra lista reducida de botones.
	 */
	public void getReduceCardButtons() {
		int numButtons = 0;
		for (Component component : panelGame.getComponents()) {
			if (component instanceof JButton && numButtons < 8) {
				component.setVisible(false);
				reduceCardButtons.add((JButton) component);
				numButtons++;
			}
		}
	}

	/**
	 * Getter lista de botones completa.
	 */
	public List<JButton> getListButtons() {
		return cardButtons;
	}

	/**
	 * Getter lista de botones reducida.
	 */
	public List<JButton> getListReduceButtons() {
		return reduceCardButtons;
	}

	/**
	 * Getter boton registro.
	 */
	public JButton getButtonRegister() {
		return btnRegister;
	}

	/**
	 * Getter panel registro.
	 */
	public JPanel getPanelRegister() {
		return panelRegister;
	}

	/**
	 * Getter campo de texto usuario.
	 */
	public JTextField getUserName() {
		return txtUsername;
	}

	/**
	 * Getter campo texto contrasenia.
	 */
	public JPasswordField getPass() {
		return txtPass;
	}

	/**
	 * Getter campo texto contrasenia repetida.
	 */
	public JPasswordField getPassRepeat() {
		return txtRepeatPass;
	}

	/**
	 * Getter boton de nuevo usuario.
	 */
	public JButton getButtonNewUser() {
		return btnNewUser;
	}

	/**
	 * Getter texto para el resultado del registro.
	 */
	public JLabel getResultRegister() {
		return txtResult;
	}

	/**
	 * Getter boton login.
	 */
	public JButton getButtonLogin() {
		return btnLogin;
	}

	/**
	 * Getter panel login.
	 */
	public JPanel getPanelLogin() {
		return panelLogin;
	}

	/**
	 * Getter superposicion de los paneles.
	 */
	public JLayeredPane getLayeredPane() {
		return layeredPane;
	}

	/**
	 * Getter campo de texto usuario para el login.
	 */
	public JTextField getTxtUserLogin() {
		return txtUserLogin;
	}

	/**
	 * Getter campo de texto contraenia para el login.
	 */
	public JTextField getTxtPassLogin() {
		return txtPassLogin;
	}

	/**
	 * Getter boton login.
	 */
	public JButton getBtnLoginUser() {
		return btnLoginUser;
	}

	/**
	 * Getter texto para el resultado tras el login.
	 */
	public JLabel getResultLogin() {
		return txtResultLogin;
	}

	/**
	 * Getter boton para iniciar juego.
	 */
	public JButton getBtnPlay() {
		return btnPlay;
	}

	/**
	 * Getter texto del usuario logeado
	 */
	public JLabel getTxtUserLogged() {
		return txtUserLogged;
	}

	/**
	 * Getter panel de juego.
	 */
	public JPanel getPanelGame() {
		return panelGame;
	}

	/**
	 * Getter boton para guardar partida.
	 */
	public JButton getButtonSave() {
		return btnSave;
	}

	/**
	 * Getter boton para acceder a la vista de records.
	 */
	public JButton getButtonHallOfFame() {
		return btnHallOfFame;
	}

	/**
	 * Getter boton para cerra sesion.
	 */
	public JButton getBtnSignOff() {
		return btnSignOff;
	}
}
