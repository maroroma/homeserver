package maroroma.homeserverng.tools.helpers;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Classe utilitaire pour la gestion des streams.
 * @author RLEVEXIE
 *
 */
@Slf4j
public abstract class StreamHLP {

	/**
	 * Permet de fermer une implémentation de {@link Closeable}.
	 * <br /> cette version trappe les exceptions liées à la fermeture du {@link Closeable}.
	 * <br /> a n'utiliser que lorsqu'une erreur ne doit pas être prise en compte.
	 * @param toClose -
	 */
	public static void safeClose(final Closeable toClose) {
		if (toClose != null) {
			try {
				toClose.close();
			} catch (IOException e) {
				log.warn("Erreur rencontrée lors de la fermeture du closeable");
			}
		}
	}
	
	/**
	 * Permet de flusher une implémentation de {@link Flushable}.
	 * <br /> cette version trappe les exceptions liées à la fermeture du {@link Flushable}.
	 * <br /> a n'utiliser que lorsqu'une erreur ne doit pas être prise en compte.
	 * @param toFlush -
	 */
	public static void safeFlush(final Flushable toFlush) {
		if (toFlush != null) {
			try {
				toFlush.flush();
			} catch (IOException e) {
				log.warn("Erreur rencontrée lors du flush du flushable");
			}
		}
	}
	
}
