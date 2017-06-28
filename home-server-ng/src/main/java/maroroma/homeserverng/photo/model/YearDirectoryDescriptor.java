package maroroma.homeserverng.photo.model;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.photo.tools.PhotoNamesDateHLP;

/**
 * Implémentation de {@link AbstractPhotoDirectoryDescriptor} pour le type {@link PhotoDirectoryType#YEAR}.
 * <br /> porte des sous éléments de {@link MonthDirectoryDescriptor}
 * @author rlevexie
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class YearDirectoryDescriptor extends AbstractPhotoDirectoryDescriptor<MonthDirectoryDescriptor, YearDirectoryDescriptor> {

	/**
	 * Année correspondante.
	 */
	private Integer year;
	
	/**
	 * Constructeur.
	 */
	public YearDirectoryDescriptor() {
		super(PhotoDirectoryType.YEAR);
	}

	/**
	 * constructeur.
	 * @param file -
	 */
	public YearDirectoryDescriptor(final File file) {
		super(file, PhotoDirectoryType.YEAR);
		this.year  = Integer.parseInt(file.getName());
	}

	/**
	 * {@inheritDoc}. Va produite des {@link MonthDirectoryDescriptor}.
	 */
	@Override
	public void populate(final List<File> asList) {
		this.populate(asList, rawFile -> new MonthDirectoryDescriptor(rawFile));		
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected LocalDate doParseNameToDate() {
		// init d'une date au premier jour de l'année
//		return LocalDate.of(Integer.parseInt(this.createFile().getName()), 1, 1);
		return PhotoNamesDateHLP.parseFileNameToLocalDateForYear(this.createFile());
	}

	
	
}
