package es.florida.ae2;

/**
 * Clase principal que crea las instancias posibles para ejecutar el patrón Modelo-Vista-Controlador
 */
public class Principal {

	/**
	 * Método ejecutable de la clase Principal que inicializa la aplicación.
	 * @param args
	 */
	public static void main(String[] args) {
		
		VistaLogin vistaLogin = new VistaLogin();
		VistaQuery vistaQuery = new VistaQuery();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo,vistaLogin, vistaQuery);
	}

}
