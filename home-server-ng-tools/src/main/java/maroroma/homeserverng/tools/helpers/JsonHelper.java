package maroroma.homeserverng.tools.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Helper pour la gestion des fichiers de sérialisation json.
 * @author rlevexie
 *
 */
public abstract class JsonHelper {

	/**
	 * Désérialize le fichier file.
	 * @param file fichier à désérializer.
	 * @param type type de sortie.
	 * @param <T> type à retourné
	 * @return -
	 * @throws HomeServerException -
	 */
	public static <T> T deserialize(final File file, final Class<T> type) throws HomeServerException {

		Assert.isValidFile(file);

		T returnValue = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			returnValue =  mapper.readValue(file, type);
		} catch (IOException e) {
			throw new HomeServerException("Erreur survenue lors de la désérialisation du fichier ["
					+ file.getAbsolutePath() + "]", e);
		}


		return returnValue;
	}

	public static byte[] serializeJsonToByteArray(final Object toSerialize) throws HomeServerException {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(baos, toSerialize);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new HomeServerException("Erreur rencontrée lors de la sérialisation", e);
		}
	}

}
