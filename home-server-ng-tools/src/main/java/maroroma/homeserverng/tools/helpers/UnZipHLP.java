package maroroma.homeserverng.tools.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Classe utilitaire pour le dézippage des fichiers.
 * @author rlevexie
 *
 */
@Component
public class UnZipHLP {

	/**
	 * Taille pas défaut du buffer de lecture.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * Dézippe le fichier dans le répertoire spécifié.
	 * @param zipFilePath input zip file
	 * @param outputFolderPath zip file output folder
	 * @throws HomeServerException -
	 */
	@SuppressWarnings("unchecked")
	public void unZip(final String zipFilePath, final  String outputFolderPath) throws HomeServerException {

		// buffer
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

		// fichier zip principal
		ZipFile zipFileToUnzip = null;

		try {

			// crée le répertoire de sortie si il n'existe pas
			File folder = new File(outputFolderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			// création de la stream zippée
			zipFileToUnzip = new ZipFile(new File(zipFilePath));

			// récupération de la liste des items zippés dans l'archive
			List<ZipEntry> zipEntries = (List<ZipEntry>) Collections.list(zipFileToUnzip.entries());


			// pour chacun des entrées
			for (ZipEntry zipEntry : zipEntries) {
				
				// création du fichier cible pour la zipentry
				File unzippedFileTarget = new File(outputFolderPath, zipEntry.getName());
				
				
				// si l'item du zip est un répertoire
				if (zipEntry.isDirectory()) {
					// on ne fait que créée le répertoire
					unzippedFileTarget.mkdirs();
				} else {

					// sinon, dans le cas d'un fchier, on recopie le contenu
					
					// stream à lire (zip entry)
					InputStream zipEntryInputStream = null;
					
					// stream à écrire (fichié dézippé)
					FileOutputStream fileUnezippedOutputStream = null;

					try {
						
						// on récupère le stream à lire à partir de la zipentry
						zipEntryInputStream = zipFileToUnzip.getInputStream(zipEntry);
						
						// création du stream à écrire
						fileUnezippedOutputStream = new FileOutputStream(unzippedFileTarget);             

						
						// écriture du fichier dézippé
						int len;
						while ((len = zipEntryInputStream.read(buffer)) > 0) {
							fileUnezippedOutputStream.write(buffer, 0, len);
						}

					} finally {
						StreamHLP.safeClose(zipEntryInputStream);
						StreamHLP.safeClose(fileUnezippedOutputStream);
					}

				}
			}
		} catch (IOException ex) {
			throw new HomeServerException("une erreur est survenue lors de la décompression du fichier " + zipFilePath, ex); 
		} finally {
			if (zipFileToUnzip != null) {
				try {
					zipFileToUnzip.close();
				} catch (IOException e) {
					throw new HomeServerException("une erreur est survenue lors de la fermeture du fichier " + zipFilePath, e); 
				}
			}
		}
	}    
}
