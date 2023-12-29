package es.florida.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;

/**
 * Clase Vista secundaria que muestra los records en una interfaz.
 */
public class VistaRecords {

	private JFrame frame;
	private JTextArea txtResult2x4;
	private JTextArea txtResult4x4;
	private JLabel lblResultsx;

	/**
	 * Constructor que crea todos los componentes de la interfaz e inicializa la
	 * clase.
	 */
	public VistaRecords() {
		initialize();
		frame.setTitle("Hall of Fame");
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Results 2x4");
		lblNewLabel.setFont(new Font("Rockwell", Font.BOLD, 25));
		lblNewLabel.setBounds(136, 44, 152, 24);
		frame.getContentPane().add(lblNewLabel);

		lblResultsx = new JLabel("Results 4x4");
		lblResultsx.setFont(new Font("Rockwell", Font.BOLD, 25));
		lblResultsx.setBounds(533, 44, 152, 24);
		frame.getContentPane().add(lblResultsx);

		txtResult2x4 = new JTextArea();
		txtResult2x4.setEditable(false);
		txtResult2x4.setBounds(50, 78, 342, 425);
		frame.getContentPane().add(txtResult2x4);

		txtResult4x4 = new JTextArea();
		txtResult4x4.setEditable(false);
		txtResult4x4.setBounds(433, 78, 342, 425);
		frame.getContentPane().add(txtResult4x4);
		frame.setVisible(false);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 831, 572);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Metodo que hace invisible la ventana tras ejecutar la aplicacion.
	 */
	public void setVisible() {
		frame.setVisible(true);
	}

	/**
	 * @return Retorna el panel de records del juego 2x4.
	 */
	public JTextArea getTxtResult2x4() {
		return txtResult2x4;
	}

	/**
	 * @return Retorna el oanel de records del juego 4x4.
	 */
	public JTextArea getTxtResult4x4() {
		return txtResult4x4;
	}
}
