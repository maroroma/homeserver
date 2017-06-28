package maroroma.homeserverng.photo.model;

import java.time.LocalDate;

import lombok.Data;

/**
 * Correspond au retour de l'upload d'une photo.
 * @author rlevexie
 *
 */
@Data
public class UploadedPhoto {

	/**
	 * Date estimée de la prise de la photo.
	 */
	private LocalDate estimatedDate;
	
	/**
	 * Nom (identifiant) du fichier.
	 */
	private String name;
}
