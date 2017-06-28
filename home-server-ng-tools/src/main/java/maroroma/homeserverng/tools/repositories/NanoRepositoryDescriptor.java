package maroroma.homeserverng.tools.repositories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Mini descriptor de NanoRepository.
 * @author rlevexie
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NanoRepositoryDescriptor {
	/**
	 * Fichier sous jacent.
	 */
	private FileDescriptor file;
	/**
	 * Clef équivalent du repo (property).
	 */
	private String propertyKey;
	/**
	 * Détermine si le fichier sous jacent existe.
	 */
	private boolean exists;
}
