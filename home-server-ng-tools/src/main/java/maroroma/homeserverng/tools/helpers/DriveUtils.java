package maroroma.homeserverng.tools.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import maroroma.homeserverng.tools.model.Drive;
import maroroma.homeserverng.tools.model.OSType;

/**
 * Classe utilitaire pour la construction d'information sur les lecteurs logiques du système.
 * @author RLEVEXIE
 *
 */
public abstract class DriveUtils {

	/**
	 * Retourne une liste de {@link Drive} par défaut, complétée de chemin supplémentaires à analyser.
	 * @param optionnalPath -
	 * @return -
	 */
	public static List<Drive> getDrives(final String... optionnalPath) {
		if (optionnalPath != null && optionnalPath.length > 0) {
			return getDrives(Arrays.stream(optionnalPath).map(onePath -> new File(onePath)).collect(Collectors.toList()));
		} else {
			return getDrives();
		}
	}
	
	/**
	 * Retourne la liste des lecteurs par défaut de la machine hote.
	 * @return -
	 */
	public static List<Drive> getDrives() {
		return getDrives(new ArrayList<>());
	}
	
	/**
	 * Retourne une liste de {@link Drive} par défaut, complétée de chemin supplémentaires à analyser.
	 * @param optionnalPath -
	 * @return -
	 */
	public static List<Drive> getDrives(final List<File> optionnalPath) {
		List<File> rootFiles = new ArrayList<>();
		if (OSUtils.getOs().equals(OSType.WINDOWS)) {
			rootFiles.addAll(Arrays.asList(File.listRoots()));
		}
		
		if (!CollectionUtils.isEmpty(optionnalPath)) {
			rootFiles.addAll(optionnalPath);
		}
		
		return rootFiles.stream()
				.filter(oneFile -> oneFile.exists())
				.map(oneFile -> Drive.builder()
					.name(oneFile.getPath())
					.totalSpace(oneFile.getTotalSpace())
					.freeSpace(oneFile.getFreeSpace())
					.usedSpace(oneFile.getTotalSpace() - oneFile.getFreeSpace())
					.percentageUsed(CustomMath.percent(oneFile.getTotalSpace() - oneFile.getFreeSpace(), oneFile.getTotalSpace()))
							.build())
				.collect(Collectors.toList());
		
	}
	
}
