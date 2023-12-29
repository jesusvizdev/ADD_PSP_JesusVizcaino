package es.florida.main;

import es.florida.controlador.Controlador;
import es.florida.modelo.Modelo;
import es.florida.vista.Vista;
import es.florida.vista.VistaRecords;

/**
 * Clase principal Main
 */
public class Main {

	/**
	 * Metodo ejecutable que crea una instancia para cada clase del Modelo Vista
	 * Controlador.
	 * 
	 * @param args Parametro propio del metodo ejecutable como un array de String.
	 */
	public static void main(String[] args) {

		Vista vista = new Vista();
		VistaRecords vistaRecords = new VistaRecords();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(vista, vistaRecords, modelo);
	}

}
