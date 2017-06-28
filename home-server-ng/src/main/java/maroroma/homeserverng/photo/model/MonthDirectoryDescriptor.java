package maroroma.homeserverng.photo.model;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.photo.tools.PhotoNamesDateHLP;

/**
 * Implémentation de {@link AbstractPhotoDirectoryDescriptor} pour le type {@link PhotoDirectoryType#MONTH}.
 * <br /> porte des sous éléments de {@link DayDirectoryDescriptor}
 * @author rlevexie
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MonthDirectoryDescriptor extends AbstractPhotoDirectoryDescriptor<DayDirectoryDescriptor, MonthDirectoryDescriptor> {

	/**
	 * Mois correspondant.
	 */
	private int month;
	
	/**
	 * Constructeur.
	 */
	public MonthDirectoryDescriptor() {
		super(PhotoDirectoryType.MONTH);
	}

	/**
	 * Constructeur.
	 * @param file -
	 */
	public MonthDirectoryDescriptor(final File file) {
		super(file, PhotoDirectoryType.MONTH);
	}

	/**
	 * {@inheritDoc}. Va produire des DayDirectoryDescriptor.
	 */
	@Override
	public void populate(final List<File> asList) {
		this.populate(asList, rawFile -> new DayDirectoryDescriptor(rawFile));		
	}

	/**
	 * -.
	 */
	@Override
	protected LocalDate doParseNameToDate() {
//		String[] splitted = this.createFile().getName().split("-");
//		this.month = Integer.parseInt(splitted[1]);
//		return LocalDate.of(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), 1);
		this.month = PhotoNamesDateHLP.getMonth(this.createFile());
		return PhotoNamesDateHLP.parseFileNameToLocalDateForMonth(this.createFile());
	}

}
