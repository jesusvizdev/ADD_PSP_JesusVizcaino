package es.florida.ae2;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;

/**
 * Clase que despega la segunda interfaz gráfica que permite realizar consultas SQL a la Base de Datos "books". También permite hacer Log out hacia la primera ventana.
 */
public class VistaQuery extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtQuery;
	private JTextPane txtResult;
	private JButton btnLaunch;
	private JButton btnLogOut;

	/**
	 * Constructor que inicializa los componentes.
	 */
	public VistaQuery() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 672, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel welcomeText = new JLabel("WELCOME TO THE LIBRARY");
		welcomeText.setForeground(Color.BLACK);
		welcomeText.setFont(new Font("Tahoma", Font.BOLD, 25));
		welcomeText.setBounds(10, 10, 362, 37);
		contentPane.add(welcomeText);
		
		JLabel subtititleTxt = new JLabel("You can consult the information you need as long as you have permissions");
		subtititleTxt.setFont(new Font("Tahoma", Font.BOLD, 12));
		subtititleTxt.setBounds(10, 40, 551, 37);
		contentPane.add(subtititleTxt);
		
		txtQuery = new JTextField();
		txtQuery.setBounds(37, 129, 586, 19);
		contentPane.add(txtQuery);
		txtQuery.setColumns(10);
		
		JLabel queryText = new JLabel("SQL QUERY");
		queryText.setFont(new Font("Tahoma", Font.BOLD, 15));
		queryText.setBounds(279, 87, 104, 37);
		contentPane.add(queryText);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 205, 624, 218);
		contentPane.add(scrollPane);
		
		txtResult = new JTextPane();
		txtResult.setEditable(false);
		scrollPane.setViewportView(txtResult);
		
		JLabel resultTxt = new JLabel("RESULT");
		resultTxt.setFont(new Font("Tahoma", Font.BOLD, 15));
		resultTxt.setBounds(292, 158, 65, 37);
		contentPane.add(resultTxt);
		
		btnLaunch = new JButton("Launch");
		btnLaunch.setBounds(538, 158, 85, 21);
		contentPane.add(btnLaunch);
		
		btnLogOut = new JButton("Sign off");
		btnLogOut.setBounds(538, 10, 85, 21);
		contentPane.add(btnLogOut);
	}
	
	/**
	 * @return Retorna un campo de texto donde se obtiene la consulta SQL.
	 */
	public JTextField getQuery() {
		return txtQuery;
	}
		
	/**
	 * @return Retorna un campo de texto donde se insertan los resultados de la consulta SQL.
	 */
	public JTextPane getResult() {
		return txtResult;
	}
	
	/**
	 * @return Retorna un botón que permite ejecutar la consulta SQL.
	 */
	public JButton getBtnLaunch() {
		return btnLaunch;
	}
	
	/**
	 * @return Retorna un botón que permite cerrar sesión.
	 */
	public JButton getBtnLogOut() {
		return btnLogOut;
	}
}
