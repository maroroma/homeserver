package maroroma.homeserverng.tools.streaming;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;

/**
 * Interface fonctionnelle pour l'écriture dans le flux de sortie, levant une exception de {@link IOException}.
 * @author rlevexie
 *
 */
@FunctionalInterface
public interface StreamingWriteOperation {

	/**
	 * OPération d'écriture.
	 * @param input -
	 * @param output -
	 * @throws IOException -
	 */
	void write(InputStream input, ServletOutputStream output) throws IOException;
}
