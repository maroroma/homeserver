package maroroma.homeserverng.photo.model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.photo.tools.PhotoNamesDateHLP;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;

/**
 * Implémentation de {@link AbstractPhotoDirectoryDescriptor} pour le type {@link PhotoDirectoryType#DAY}.
 * <br /> porte des sous éléments de {@link PhotoDescriptor}
 * @author rlevexie
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DayDirectoryDescriptor extends AbstractPhotoDirectoryDescriptor<PhotoDescriptor, DayDirectoryDescriptor> {

	/**
	 * Jour correspondant.
	 */
	private int day;
	
	/**
	 * Liste des extensions acceptées pour la récupération des fichiers.
	 */
	private List<String> acceptedExtensions;
	
	/**
	 * Constructeur.
	 */
	public DayDirectoryDescriptor() {
		super(PhotoDirectoryType.DAY);
		this.acceptedExtensions = new ArrayList<>();
	}

	/**
	 * Constructeur.
	 * @param file -
	 */
	public DayDirectoryDescriptor(final File file) {
		super(file, PhotoDirectoryType.DAY);
		this.acceptedExtensions = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}. Va générer des {@link PhotoDescriptor}.
	 */
	@Override
	public void populate(final List<File> asList) {
		this.populate(asList, rawFile -> new PhotoDescriptor(rawFile));//, 
//				PhotoUriHLP.resolvePath(this.createFile())));		
	}
	
	/**
	 * Met en place la liste des extensions acceptées pour les photos qui seront scannées dans le répertoire.
	 * @param extension -
	 * @return -
	 */
	public DayDirectoryDescriptor acceptedExtensions(final String... extension) {
		
		Assert.notEmpty(extension, "extension can't be empty");

		this.acceptedExtensions = Arrays.asList(extension);
		
		return returnThis();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DayDirectoryDescriptor autoPopulate() {
		
		// présence ou pas d'extensions pour le filtrage des fichiers
		if (this.acceptedExtensions.isEmpty()) {
			this.populate(CommonFileFilter.listFiles(this.createFile()));
		} else {
			this.populate(this.createFile().listFiles(
					CommonFileFilter.fileExtensionFilter(this.acceptedExtensions)));
		}
		
		
		
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDate doParseNameToDate() {
//		String[] splitted = this.createFile().getName().split("-");
//		this.day = Integer.parseInt(splitted[2]);
//		return LocalDate.of(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
		this.day = PhotoNamesDateHLP.getDay(this.createFile());
		return PhotoNamesDateHLP.parseFileNameToLocalDateForDay(this.createFile());
	}

}
