package maroroma.homeserverng.tools.repositories;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;

/**
 * Classe utilitaire pour la centralisation des {@link NanoRepository}.
 * @author rlevexie
 *
 */
@Component
public class NanoRepositoriesManager {

	/**
	 * Listing des {@link NanoRepository}.
	 */
	private Map<String, NanoRepository> nanoRepositories;

	/**
	 * Constructeur.
	 */
	public NanoRepositoriesManager() {
		this.nanoRepositories = new HashMap<>();
	}

	/**
	 * Ajout d'un {@link NanoRepository} dans le managerr.
	 * @param repo -
	 */
	public void add(final NanoRepository repo) {
		Assert.notNull(repo, "repo can't be null");
		Assert.isTrue(!this.nanoRepositories.containsKey(repo.getId()), repo.getId() + " déjà présent");

		this.nanoRepositories.put(repo.getId(), repo);
	}

	/**
	 * Retourne l'ensemble des {@link NanoRepository}.
	 * @return -
	 */
	public List<NanoRepository> getRepositories() {
		return new ArrayList<>(this.nanoRepositories.values());
	}

	/**
	 * Détermine si le repo d'id donné est bien présent dans la liste de {@link NanoRepository}.
	 * @param repoid -
	 * @return -
	 */
	public boolean containsRepository(final String repoid) {
		Assert.hasLength(repoid, "repoid can't be null or empty");
		return this.nanoRepositories.containsKey(repoid);
	}

	/**
	 * Retourne le {@link NanoRepository} d'identifiant donné.
	 * @param repoid -
	 * @return -
	 */
	public NanoRepository getRepository(final String repoid) {
		Assert.isTrue(this.nanoRepositories.containsKey(repoid), repoid + " n'existe pas");
		return this.nanoRepositories.get(repoid);
	}

	/**
	 * Exporte le fichier sous jacent à un {@link NanoRepository} sous forme de tableau de byte.
	 * @param repoid -
	 * @return -
	 * @throws HomeServerException -
	 */
	public byte[] exportRepository(final String repoid) throws HomeServerException {
		Assert.isTrue(this.nanoRepositories.containsKey(repoid), repoid + " n'existe pas");

		NanoRepository toExport = this.nanoRepositories.get(repoid);

		byte[] returnValue = null;

		File toDownload = toExport.generateDescriptor().getFile().createFile();
		Assert.isValidFile(toDownload);

		returnValue = FileAndDirectoryHLP.convertFileToByteArray(toDownload);

		return returnValue;
	}
	
	/**
	 * Permet de remplacer un repository.
	 * @param repoID -
	 * @param file - 
	 * @throws HomeServerException -
	 */
	public void importRepository(final String repoID, final MultipartFile file) throws HomeServerException {
		Assert.hasLength(repoID, "repoID can't be null or empty");
		Assert.notNull(file, "file can't be null");
		Assert.isTrue(this.nanoRepositories.containsKey(repoID), "repo " + repoID + " doesn't exist");

		// recup du descriptor
		NanoRepositoryDescriptor descriptor = this.nanoRepositories.get(repoID).generateDescriptor();

		// controle sur l'ancien fichier
//		Assert.isTrue(descriptor.getFile().createFile().exists(), "initial file can't be found");

		// recopie du fichier
		FileAndDirectoryHLP.convertByteArrayToFile(file, descriptor.getFile().createFile());

	}
}
