package maroroma.homeserverng.tools.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Classe utilitaire pour l'execution de commandes sur le systeme.
 * @author rlevexie
 *
 */
public abstract class BashCmdExecutorHLP {

	/**
	 * Exécute la commande en entrée.
	 * @param cmd -
	 * @return la sortie de la commande.
	 * @throws HomeServerException -
	 */
	public static List<String> executeCommand(final String cmd) throws HomeServerException {

		// validation de la commande
		Assert.hasLength(cmd, "La commande ne peut être vide");

		List<String> returnValue = null;

		try {
			
			// pour pouvoir lancer une commande sur un système unix, il faut impérativement l'éxécuter à travers un bash
			Process process = 
					new ProcessBuilder(new String[] {"bash", "-c", cmd})
					.redirectErrorStream(true)
					.directory(new File("."))
					.start();

			returnValue = new ArrayList<String>();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;

			while ((line = br.readLine()) != null) {
				returnValue.add(line);
			}

			//There should really be a timeout here.
			if (0 != process.waitFor()) {
				throw new HomeServerException("Le processus ne s'est pas terminé de manière standard");
			}


		} catch (Exception e) {
			throw new HomeServerException("Exception inconnue rencontrée", e);
		}

		return returnValue;

	}

}
