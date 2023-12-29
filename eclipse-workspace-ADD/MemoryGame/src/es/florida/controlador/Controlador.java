package es.florida.controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import es.florida.modelo.Modelo;
import es.florida.vista.Vista;
import es.florida.vista.VistaRecords;

/**
 * Clase que hace de intermediario entre la lógica y la vista de la interfaz.
 */
public class Controlador {

	Vista vista;
	VistaRecords vistaRecords;
	Modelo modelo;
	private List<JButton> cardButtons;
	private List<JButton> reduceCardButtons;
	JButton btnRegister;
	private List<ImageIcon> listaImagenes = new ArrayList<ImageIcon>();
	private List<ImageIcon> listaImagenesReducida = new ArrayList<ImageIcon>();
	private int index = 0;
	private int click = 0;
	private Icon img1;
	private Icon img2;
	private JButton button1;
	private JButton button2;
	private int numForWin4x4 = 0;
	private int numForWin4x2 = 0;
	private int gameDuration;
	private int dificultad;
	private String userName;
	private String timestamp;

	/**
	 * Constructor que maneja todos los eventos de los botones y hace de
	 * intermediario entre la Vista y el Modelo haciendo uso de sus metodos. Permite
	 * hacer cambios sobre los componentes de la interfaz y actualizar todas las
	 * variables necesarias para su correcta ejecucion. Entre los eventos mas
	 * importantes podemos destacar aquellos cuando se selecciona una plantilla 2x4
	 * o 4x4, ya que requieren un funcionamiento mas complejo.
	 * 
	 * @param vista        Parametro Vista principal.
	 * @param vistaRecords Parametro Vista secundario. Muestra una pantalla con los
	 *                     records del juego.
	 * @param modelo       Parametro de la clase Modelo para poder ejecutar los
	 *                     metodos relacionados con el funcionamiento de la
	 *                     aplicacion.
	 */
	public Controlador(Vista vista, VistaRecords vistaRecords, Modelo modelo) {

		this.vista = vista;
		this.modelo = modelo;
		this.vistaRecords = vistaRecords;

		cardButtons = vista.getListButtons();
		reduceCardButtons = vista.getListReduceButtons();

		listaImagenes = modelo.getImageList(listaImagenes);
		listaImagenes.addAll(listaImagenes);
		listaImagenesReducida = modelo.getReducideImageList(listaImagenesReducida);
		listaImagenesReducida.addAll(listaImagenesReducida);

		String[] opciones = { "Local", "Remote" };
		int respuesta = JOptionPane.showOptionDialog(null, "Select Server...", "Memory", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

		// Gestion Local o Remoto
		modelo.connectMongoDB();
		modelo.getImages();

		vista.getButtonRegister().addActionListener(new ActionListener() {
			/**
			 * Evento que permite mostrar los datos de registro.
			 * 
			 * @param arg0
			 */
			public void actionPerformed(ActionEvent arg0) {
				deleteDataLogin();
				vista.getLayeredPane().setVisible(true);
				vista.getPanelRegister().setVisible(true);
				vista.getPanelLogin().setVisible(false);
			}
		});
		vista.getButtonNewUser().addActionListener(new ActionListener() {
			/**
			 * Evento que gestiona los posibles errores en el registro y su acceso. Muestra
			 * un mensaje rojo o verde dependiendo de si hubo un error o el registro fue
			 * correcto.
			 */
			public void actionPerformed(ActionEvent arg0) {
				String userName = vista.getUserName().getText();
				String pass = vista.getPass().getText();
				String passRepeat = vista.getPassRepeat().getText();
				String[] result = modelo.checkRegister(userName, pass, passRepeat);
				vista.getResultRegister().setText(result[0]);
				if (result[1].equals("no")) {
					vista.getResultRegister().setForeground(Color.red);
				}
				if (result[1].equals("ok")) {
					vista.getResultRegister().setForeground(Color.green);
					// Registro del usuario a la base de datos.
					modelo.registerUser(userName, pass);
				}

			}
		});
		vista.getButtonLogin().addActionListener(new ActionListener() {
			/**
			 * Evento que muestra el panel para que el usuario pueda logearse.
			 * 
			 * @param arg0
			 */
			public void actionPerformed(ActionEvent arg0) {
				deleteDataRegister();
				vista.getLayeredPane().setVisible(true);
				vista.getPanelRegister().setVisible(false);
				vista.getPanelLogin().setVisible(true);
			}
		});

		vista.getButtonSave().addActionListener(new ActionListener() {
			/**
			 * Evento que guarda el record tras el juego.
			 * 
			 * @param arg0
			 */
			public void actionPerformed(ActionEvent arg0) {
				modelo.saveRecord(userName, dificultad, timestamp, gameDuration);
				vista.getButtonSave().setEnabled(false);
			}
		});

		vista.getBtnLoginUser().addActionListener(new ActionListener() {
			/**
			 * Evento que maneja el loggin de la aplicaci�n a trav�s de la base de datos
			 * MongoDB. Muestra resultados si hubo algun error.
			 * 
			 * @param arg0
			 */
			public void actionPerformed(ActionEvent arg0) {
				if (vista.getTxtPassLogin().getText().length() > 0 && vista.getTxtUserLogin().getText().length() > 0) {
					String user = vista.getTxtUserLogin().getText();
					String pass = vista.getTxtPassLogin().getText();
					String result = modelo.loginUser(user, pass);
					if (result.equals("ok")) {
						userName = user;
						vista.getTxtUserLogged().setText("Session started as: " + user.toUpperCase());
						vista.getTxtUserLogged().setVisible(true);
						vista.getResultLogin().setForeground(Color.black);
						vista.getButtonLogin().setBackground(Color.green);
						vista.getButtonRegister().setEnabled(false);
						vista.getBtnPlay().setEnabled(true);
						vista.getButtonLogin().setEnabled(false);
						vista.getLayeredPane().setVisible(false);
					} else if (result.equals("notExist")) {
						vista.getResultLogin().setText("User does not exist");
						vista.getResultLogin().setForeground(Color.red);
					} else if (result.equals("wrong")) {
						vista.getResultLogin().setText("Password is wrong");
						vista.getResultLogin().setForeground(Color.red);
					}
				} else {
					vista.getResultLogin().setText("Fields are empty");
					vista.getResultLogin().setForeground(Color.red);
				}
			}
		});

		vista.getBtnPlay().addActionListener(new ActionListener() {
			/**
			 * Es el evento mas complejo que gestiona la interfaz del juego dependiendo si
			 * elegimos una plantilla u otra. Permite hacer que dos botones desaparezcan si
			 * sus iconos no son iguales o que permanezcan visibles de lo contrario.
			 * Conlleva un contador de tiempo y un timestamp.
			 * 
			 * @param arg0
			 */
			public void actionPerformed(ActionEvent arg0) {
				String[] opciones = { "2x4", "4x4" };
				int respuesta = JOptionPane.showOptionDialog(null, "Select Board...", "Memory",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
				if (respuesta == 0) {
					dificultad = 8;
					gameDuration = 0;
					vista.getButtonSave().setEnabled(false);
					hideButtons();
					showButtons(reduceCardButtons);
					hideIcons();
					index = 0;
					click = 0;
					numForWin4x2 = 0;
					setDifferentsCards();

					for (JButton btn : reduceCardButtons) {
						ActionListener[] listeners = btn.getActionListeners();
						for (ActionListener listener : listeners) {
							btn.removeActionListener(listener);
						}
					}

					for (JButton btn : reduceCardButtons) {
						btn.setName(String.valueOf(index));
						index++;
					}
					JOptionPane.showMessageDialog(null, "Game Started", "Info", JOptionPane.INFORMATION_MESSAGE);
					long tiempoInicio = System.currentTimeMillis();

					for (JButton btn : reduceCardButtons) {
						btn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								btn.setIcon(listaImagenesReducida.get(Integer.parseInt(btn.getName())));
								click++;
								if (click == 1) {
									img1 = btn.getIcon();
									button1 = btn;
								}
								if (click == 2) {
									img2 = btn.getIcon();
									button2 = btn;
									click = 0;
									if (!img1.equals(img2)) {
										JOptionPane.showMessageDialog(null, "Try again", "Failed",
												JOptionPane.INFORMATION_MESSAGE);
										button1.setIcon(null);
										button2.setIcon(null);
									} else {
										button1.setEnabled(false);
										button2.setEnabled(false);
										numForWin4x2++;
									}
								}
								if (numForWin4x2 == 4) {
									timestamp = modelo.getTimeStamp();
									long tiempoActual = System.currentTimeMillis();
									long diferenciaTiempo = (tiempoActual - tiempoInicio) / 1000;
									gameDuration = (int) diferenciaTiempo;
									vista.getButtonSave().setEnabled(true);
									if (modelo.isBestScore(gameDuration, 8)) {
										JOptionPane.showMessageDialog(null,
												"Congratulations, you have a new record in 2x4 game. Dont forget to save your game",
												"RECORD!", JOptionPane.INFORMATION_MESSAGE);
									}

								}
							}
						});
					}

				}

				if (respuesta == 1) {
					dificultad = 16;
					gameDuration = 0;
					vista.getButtonSave().setEnabled(false);
					showButtons(cardButtons);
					hideIcons();
					Collections.shuffle(listaImagenes);
					index = 0;
					click = 0;
					numForWin4x4 = 0;

					for (JButton btn : cardButtons) {
						ActionListener[] listeners = btn.getActionListeners();
						for (ActionListener listener : listeners) {
							btn.removeActionListener(listener);
						}
					}
					for (JButton boton : cardButtons) {
						boton.setName(String.valueOf(index));
						index++;

					}
					JOptionPane.showMessageDialog(null, "Game Started", "Info", JOptionPane.INFORMATION_MESSAGE);
					long tiempoInicio = System.currentTimeMillis();

					for (JButton btn : cardButtons) {
						btn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								System.out.println(btn.getName());
								btn.setIcon(listaImagenes.get(Integer.parseInt(btn.getName())));
								click++;
								if (click == 1) {
									img1 = btn.getIcon();
									button1 = btn;
								}
								if (click == 2) {
									img2 = btn.getIcon();
									button2 = btn;
									click = 0;
									if (!img1.equals(img2) || button1.getName().equals(button2.getName())) {
										JOptionPane.showMessageDialog(null, "Try again", "Failed",
												JOptionPane.INFORMATION_MESSAGE);
										button1.setIcon(null);
										button2.setIcon(null);

									} else {
										button1.setEnabled(false);
										button2.setEnabled(false);
										numForWin4x4++;
									}
								}
								if (numForWin4x4 == 8) {
									timestamp = modelo.getTimeStamp();
									long tiempoActual = System.currentTimeMillis();
									long diferenciaTiempo = (tiempoActual - tiempoInicio) / 1000;
									gameDuration = (int) diferenciaTiempo;
									vista.getButtonSave().setEnabled(true);
									if (modelo.isBestScore(gameDuration, 16)) {
										JOptionPane.showMessageDialog(null,
												"Congratulations, you have a new record in 4x4 game. Dont forget to save your game.",
												"RECORD!", JOptionPane.INFORMATION_MESSAGE);
									}
								}
							}
						});
					}

				}
			}
		});

		vista.getButtonHallOfFame().addActionListener(new ActionListener() {
			/**
			 * Evento que btiene los records para cada una de las dificultades del juego.
			 * 
			 * @param arg0
			 */
			public void actionPerformed(ActionEvent arg0) {
				vistaRecords.getTxtResult2x4().setText("");
				vistaRecords.getTxtResult4x4().setText("");
				vistaRecords.setVisible();
				List<String> listaRecords2x4 = modelo.getRecords(8);
				List<String> listaRecords4x4 = modelo.getRecords(16);
				for (String record : listaRecords2x4) {
					vistaRecords.getTxtResult2x4().setText(vistaRecords.getTxtResult2x4().getText() + record + "\n");
				}
				for (String record : listaRecords4x4) {
					vistaRecords.getTxtResult4x4().setText(vistaRecords.getTxtResult4x4().getText() + record + "\n");
				}
			}
		});

		vista.getBtnSignOff().addActionListener(new ActionListener() {
			/**
			 * Evento que resetea el juego.
			 */
			public void actionPerformed(ActionEvent arg0) {
				resetGame();
			}
		});

	}

	/**
	 * Metodo que muestra todos los botones al iniciar el juego.
	 * 
	 * @param buttons Recibe como parametro la lista de botones.
	 */
	public void showButtons(List<JButton> buttons) {
		for (Component component : buttons) {
			component.setVisible(true);
			component.setEnabled(true);
		}
	}

	/**
	 * Metodo que oculta los botones que hacen uso de iconos para el juego.
	 */
	public void hideButtons() {
		for (Component component : cardButtons) {
			component.setVisible(false);
		}
	}

	/**
	 * Metodo que borra los datos introducidos en la ventana de Registro.
	 */
	public void deleteDataRegister() {
		vista.getPass().setText("");
		vista.getPassRepeat().setText("");
		vista.getUserName().setText("");
		vista.getResultRegister().setText("");
	}

	/**
	 * M�todo que borra los datos introducidos en la ventana de Login.
	 */
	public void deleteDataLogin() {
		vista.getTxtUserLogin().setText("");
		vista.getTxtPassLogin().setText("");
		vista.getResultLogin().setText("");
	}

	/**
	 * Metodo que baraja los iconos para que puedan ser distintos en cada partida.
	 */
	public void setDifferentsCards() {
		listaImagenesReducida.clear();
		listaImagenesReducida = modelo.getReducideImageList(listaImagenesReducida);
		listaImagenesReducida.addAll(listaImagenesReducida);
		Collections.shuffle(listaImagenesReducida);
	}

	/**
	 * Metodo que elimina los iconos asociados a cada uno de los botones.
	 */
	public void hideIcons() {
		for (JButton component : cardButtons) {
			component.setIcon(null);
		}
	}

	/**
	 * Metodo para resetear el juego. Permite volver a iniciar la aplicacion sin
	 * necesidad de cerrarla y poder volver a logearte.
	 */
	public void resetGame() {
		vista.getTxtUserLogged().setVisible(false);
		vista.getButtonRegister().setEnabled(true);
		vista.getButtonLogin().setEnabled(true);
		vista.getButtonLogin().setBackground(null);
		;
		vista.getBtnPlay().setEnabled(false);
		vista.getButtonSave().setEnabled(false);
		vista.getLayeredPane().setVisible(false);
		vista.getTxtUserLogin().setText("");
		vista.getTxtPassLogin().setText("");
		vista.getPass().setText("");
		vista.getPassRepeat().setText("");
		vista.getUserName().setText("");
		vista.getResultLogin().setText("");
		vista.getResultRegister().setText("");
		for (JButton btn : cardButtons) {
			btn.setVisible(false);
		}
	}

}
