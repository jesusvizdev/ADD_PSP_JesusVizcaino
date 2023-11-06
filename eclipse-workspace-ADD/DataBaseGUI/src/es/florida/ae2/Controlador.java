package es.florida.ae2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * La clase Controlador interactua con la clase Vista y clase Modelo. Se hace uso de los componentes retornados en la clase Vista para agregarles funcionalidad y de los métodos necesarios de la clase Modelo.
 */
public class Controlador {
	
	private Modelo modelo;
	private VistaLogin vistaLogin;
	private VistaQuery vistaQuery;
	
	/**
	 * Inicializa el control interactivo de la aplicación mediante dos Vistas.
	 * @param modelo Recibe como parámetro un objeto de la Clase Modelo
	 * @param vistaLogin  Recibe como parámetro un objeto de la Clase VistaLogin
	 * @param vistaQuery Recibe como parámetro un objeto de la Clase VistaQuery
	 */
	public Controlador(Modelo modelo, VistaLogin vistaLogin, VistaQuery vistaQuery) {
		this.modelo = modelo;
		this.vistaLogin = vistaLogin;
		this.vistaQuery = vistaQuery;
		control();
	}
	
	/**
	 * Método que se ejecuta tras llamar al constructor de la clase Controlador. Controla los ActionListeners de cada uno de los botones y hace uso de los componentes de las clases Vista.
	 * El método acude a la clase Modelo siempre que es necesario para hacer uso de su información.
	 */
	public void control() {
		
		vistaLogin.getBtnOpenConnection().setEnabled(false);
		
		vistaLogin.getBtnLogin().addActionListener(
			new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String user = vistaLogin.getTextUser().getText();
				String password = vistaLogin.getTextPassword().getText();
				
				if (user.length() < 1 || password.length() < 1) {
			        JOptionPane.showMessageDialog(null, "You must fill all the fields", "Failed to Login", JOptionPane.ERROR_MESSAGE);
			        return;
				}else {
					String userLogedIn = modelo.loginUser(user, password);
					
					if (userLogedIn != null) {
							if (user.equals("administrador1")) {
								modelo.runAdminDB();
						        JOptionPane.showMessageDialog(null, "This connection has admin permissions", "ADMIN", JOptionPane.INFORMATION_MESSAGE);
							}
							vistaQuery.setVisible(true);
							vistaLogin.setVisible(false);
							vistaLogin.getTextUser().setText("");
							vistaLogin.getTextPassword().setText("");
					} else if (userLogedIn == null){
				        JOptionPane.showMessageDialog(null, "Non-existing user or password", "Failed to Login", JOptionPane.ERROR_MESSAGE);
					} 
				}	
			}
		});	 	
		
		vistaQuery.getBtnLaunch().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String query = vistaQuery.getQuery().getText();
						boolean noError = modelo.checkError(query);
						if (noError) {
							modelo.typeOfQuery(query);
							String queryResult = modelo.getQueryResult();
							if (modelo.getQueryError() != null) {
								vistaQuery.getResult().setText(modelo.getQueryError());
								vistaQuery.getQuery().setText("");
								return;
							}
							vistaQuery.getResult().setText(queryResult);	
						} else if (!noError) {
					        JOptionPane.showMessageDialog(null, "You don't have administrator permissions to do that.", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
						vistaQuery.getQuery().setText("");
					}
				});
		
		vistaQuery.getBtnLogOut().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to sign off?", "Sign Off", JOptionPane.YES_NO_CANCEL_OPTION);
						if (confirmation == JOptionPane.YES_OPTION) {
							vistaQuery.setVisible(false);
							vistaLogin.setVisible(true);
							vistaQuery.getResult().setText("");
						}
					}
				}
				);
		
		vistaLogin.getBtnCloseConnection().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the Data Base Connection? If you close you will not able to log in.", "Close DB Connection", JOptionPane.YES_NO_CANCEL_OPTION);
						if (confirmation == JOptionPane.YES_OPTION) {
								modelo.closeConnectionDB();	
						        JOptionPane.showMessageDialog(null, "The connection has been closed successfully", "Connection Closed", JOptionPane.INFORMATION_MESSAGE);						
						        vistaLogin.getBtnOpenConnection().setEnabled(true);
						        vistaLogin.getBtnCloseConnection().setEnabled(false);

						}
					}
				}
				);
		
		vistaLogin.getBtnOpenConnection().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				        JOptionPane.showMessageDialog(null, "The connection has been opened successfully", "Connection Opened", JOptionPane.INFORMATION_MESSAGE);						
						modelo.openConnectionDB();	
						vistaLogin.getBtnOpenConnection().setEnabled(false);
				        vistaLogin.getBtnCloseConnection().setEnabled(true);

						}

					}
				);
		
	}
}
