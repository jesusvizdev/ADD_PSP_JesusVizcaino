package es.florida.modelo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import static com.mongodb.client.model.Filters.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Clase que gestiona lo relativo a la logica del funcionamiento y conexiones a
 * la base de datos.
 */
public class Modelo {

	private MongoCollection<Document> coleccionImagenes;
	private MongoCollection<Document> coleccionUsuarios;
	private MongoCollection<Document> coleccionRecords;
	private String[] configuration = new String[6];

	/**
	 * Constructor de la clase Modelo recibe la configuracion para la posterior
	 * conexion al base de datos.
	 */
	public Modelo() {
		getConfiguration();
	};

	/**
	 * Metodo que obtiene los datos desde un archivo json para la conexion de la
	 * base de datos.
	 */
	public void getConfiguration() {
		File archivo = new File("config_local.json");
		try {
			FileReader fr = new FileReader(archivo);
			JSONObject jsonConfig = new JSONObject(new JSONTokener(fr));
			configuration[0] = jsonConfig.getString("ip");
			configuration[1] = String.valueOf(jsonConfig.getInt("port"));
			configuration[2] = jsonConfig.getString("database");
			configuration[3] = jsonConfig.getString("collection_users");
			configuration[4] = jsonConfig.getString("collection_records");
			configuration[5] = jsonConfig.getString("collection_images");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que permite la conexion a la base de datos y a sus colecciones.
	 */
	public void connectMongoDB() {
		MongoClient mongoClient = new MongoClient(configuration[0], Integer.parseInt(configuration[1]));
		MongoDatabase database = mongoClient.getDatabase(configuration[2]);
		coleccionUsuarios = database.getCollection(configuration[3]);
		coleccionRecords = database.getCollection(configuration[4]);
		coleccionImagenes = database.getCollection(configuration[5]);
	}

	/**
	 * Metodo que maneja los posibles fallos en el registro y permite el registro si
	 * todo ha ido bien.
	 * 
	 * @param userName   Recibe el nombre de usuario.
	 * @param pass       Recibe la contrasenia.
	 * @param passRepeat Recibe la repeticion de la contrasenia.
	 * @return Retorna un array String con "ok" si todo es valido o "no" si no fue
	 *         valido y un mensaje para mostrar en la interfaz.
	 */
	public String[] checkRegister(String userName, String pass, String passRepeat) {
		String[] results = new String[2];
		if (userName.length() < 1 || pass.length() < 1 || passRepeat.length() < 1) {
			results[0] = "You must fill al the fields";
			results[1] = "no";
			return results;
		} else if (!pass.equals(passRepeat)) {
			results[0] = "The passwords must match";
			results[1] = "no";
			return results;
		}

		MongoCursor<Document> cursor = coleccionUsuarios.find().iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			String userDB = obj.getString("user");
			if (userDB.equals(userName)) {
				results[0] = "This user alredy exists";
				results[1] = "no";
				return results;
			}
			;
		}

		results[0] = "You�ve been registered succesfully";
		results[1] = "ok";

		return results;
	}

	/**
	 * Metodo que registra el usuario a la base de datos y la contrasenia
	 * encriptada.
	 * 
	 * @param user Recibe el nombre de usuario.
	 * @param pass Recibe la contrasenia en texto plano.
	 */
	public void registerUser(String user, String pass) {
		String encryptedPass = hashSHA256(pass);
		Document doc = new Document();
		doc.append("user", user);
		doc.append("pass", encryptedPass);
		coleccionUsuarios.insertOne(doc);
	}

	/**
	 * Metodo que permite encriptar contrasenias usando el hash SHA256.
	 * 
	 * @param pass Recibe la contraseni a encriptar.
	 * @return Retorna la contrasenia encriptada.
	 */
	public String hashSHA256(String pass) {
		return DigestUtils.sha256Hex(pass);
	}

	/**
	 * Metodo que gestiona un correcto login o un fallo.
	 * 
	 * @param user Recibe el nombre de usuario.
	 * @param pass Recibe la contrasenia.
	 * @return Retorna un String para mostrar un determinado resultado en la
	 *         interfaz.
	 */
	public String loginUser(String user, String pass) {
		String result = "";
		try {
			pass = hashSHA256(pass);
			Bson query = eq("user", user);
			MongoCursor<Document> cursor = coleccionUsuarios.find(query).iterator();
			JSONObject obj = new JSONObject(cursor.next().toJson());
			String userDB = obj.getString("user");
			String passDB = obj.getString("pass");
			if (passDB.equals(pass)) {
				result = "ok";
			} else {
				result = "wrong";
			}
		} catch (NoSuchElementException e) {
			result = "notExist";
		}
		return result;
	}

	/**
	 * Obtiene las imagenes en base64 de la base de datos para posteriormente
	 * decodificarlas.
	 */
	public void getImages() {
		MongoCursor<Document> cursor = coleccionImagenes.find().iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			String imageName = obj.getString("id");
			String base64Snippet = obj.getString("base64");
			decodeBase64(base64Snippet, imageName);
		}

	}

	/**
	 * Convierte las imagenes en base 64 en imagenes para ser utilizadas. Se guardan
	 * en el directorio img.
	 * 
	 * @param string64
	 * @param imageName
	 */
	public void decodeBase64(String string64, String imageName) {

		try {
			byte[] btDataFile = Base64.decodeBase64(string64);
			BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(btDataFile));
			imagen.getScaledInstance(-1, 400, java.awt.Image.SCALE_SMOOTH);
			ImageIO.write(imagen, "jpg", new File("./img/" + imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que almacena las imagenes en una lista
	 * 
	 * @param listaImagenes Recibe una lista de imagenes vacaa.
	 * @return Retorna la lista de imagenes.
	 */
	public List<ImageIcon> getImageList(List<ImageIcon> listaImagenes) {
		File directorio = new File("img");
		File[] imgFiles = directorio.listFiles();
		if (imgFiles != null && imgFiles.length > 0) {
			for (File imgFile : imgFiles) {
				try {
					Image img = ImageIO.read(imgFile);
					ImageIcon icon = new ImageIcon(img);
					listaImagenes.add(icon);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return listaImagenes;
	}

	/**
	 * Metodo que obtiene una lista de imagenes para el juego 2x4. Siempre seran
	 * imagenes distintas.
	 * 
	 * @param listaImagenes Recibe la lista de imagenes vacia.
	 * @return Retorna la lista de imagenes.
	 */
	public List<ImageIcon> getReducideImageList(List<ImageIcon> listaImagenes) {
		List<ImageIcon> first4Images = new ArrayList<>();
		File directorio = new File("img");
		File[] imgFiles = directorio.listFiles();
		if (imgFiles != null && imgFiles.length > 0) {
			for (File imgFile : imgFiles) {
				try {
					Image img = ImageIO.read(imgFile);
					ImageIcon icon = new ImageIcon(img);
					listaImagenes.add(icon);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Collections.shuffle(listaImagenes);
			first4Images = listaImagenes.subList(0, 4);
		}
		return first4Images;

	}

	/**
	 * Metodo que baraja las cartas.
	 * 
	 * @param listaCartas Recibe como parametro la lista de cartas a barajar.
	 * @return Retorna la lista de cartas barajada.
	 */
	public List<ImageIcon> alterCards(List<ImageIcon> listaCartas) {
		List<ImageIcon> parte1 = new ArrayList<ImageIcon>();
		List<ImageIcon> parte2 = new ArrayList<ImageIcon>();
		for (int i = 0; i < listaCartas.size(); i++) {
			if (i < 4) {
				parte1.add(listaCartas.get(i));
			} else {
				parte2.add(listaCartas.get(i));
			}
		}
		Collections.shuffle(parte1);
		Collections.shuffle(parte2);
		listaCartas = parte1;
		listaCartas.addAll(parte2);
		return listaCartas;

	}

	/**
	 * Metodo que crea un timestamp del juego en un determinado formato
	 * 
	 * @return Retorna el timestamp en String.
	 */
	public String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss");
		String timestamp = dateFormat.format(date);
		return timestamp;
	}

	/**
	 * Metodo que guarda los records en la base de datos.
	 * 
	 * @param user      Recibe el nombre del usuario.
	 * @param level     Recibe la dificultad.
	 * @param timestamp Recibe el timestamp.
	 * @param duration  Recibe la duracion.
	 */
	public void saveRecord(String user, int level, String timestamp, int duration) {
		Document doc = new Document();
		doc.append("usuario", user);
		doc.append("dificultad", level);
		doc.append("timestamp", timestamp);
		doc.append("duracion", duration);
		coleccionRecords.insertOne(doc);
	}

	/**
	 * Obtiene los records de la base de datos. Le da un formato en string y los
	 * ordena respecto a su duracion en una lista.
	 * 
	 * @param dificultad Recibe la dificultad del juego: 8 o 16.
	 * @return Retorna la lista de records con su formato adecuado.
	 */
	public List<String> getRecords(int dificultad) {
		List<String> listaRecords = new ArrayList<>();

		Bson query = eq("dificultad", dificultad);
		MongoCursor<Document> cursor = coleccionRecords.find(query).iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			String usuario = obj.getString("usuario");
			String timestamp = obj.getString("timestamp");
			String duracion = String.valueOf(obj.getInt("duracion"));
			String record = usuario + " ---> " + duracion + " segundos " + "(" + timestamp + ")";
			listaRecords.add(record);
		}
		Collections.sort(listaRecords, Comparator.comparingInt(duration -> Integer.parseInt(duration.split(" ")[2])));
		return listaRecords;
	}

	/**
	 * Metodo que gestiona los records. Compara si el record actual es mejor que los
	 * demas.
	 * 
	 * @param gameDuration Recibe la duracion del juego, pues es la variable a
	 *                     comparar.
	 * @param dificultad   Recibe la dificultad, pues existen dos modos de juego.
	 * @return Retorna un booleano, true si el record es mejor o false si no lo es.
	 */
	public boolean isBestScore(int gameDuration, int dificultad) {
		List<Integer> listaDuraciones = new ArrayList<>();
		Bson query = eq("dificultad", dificultad);
		MongoCursor<Document> cursor = coleccionRecords.find(query).iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			int duracion = obj.getInt("duracion");
			listaDuraciones.add(duracion);
		}
		Collections.sort(listaDuraciones);
		if (gameDuration < listaDuraciones.get(0))
			return true;
		else
			return false;
	}

}
