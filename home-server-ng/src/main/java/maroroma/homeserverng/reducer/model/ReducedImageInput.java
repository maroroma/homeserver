package maroroma.homeserverng.reducer.model;

import java.util.Base64;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entrée pour le service de sauvegarde d'une image réduite.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
public class ReducedImageInput {
	
	/**
	 * Nom original du fichier.
	 */
	private String originalName;
	
	/**
	 * Data en base 64 ou nom de fichier.
	 */
	private String data;
	
	/**
	 * Encodage du nom de fichier pour facilité les échange http.
	 */
	private String base64FullName;
	
	/**
	 * Détermine si data correspond à une image encodée en base 64.
	 * @return -
	 */
	@JsonIgnore
	public boolean isBase64() {
		return StringUtils.hasLength(this.data) && this.data.contains(",");
	}
	
	/**
	 * PErmet décoder une image en base 64 en tableau de byte.
	 * @return -
	 */
	@JsonIgnore
	public byte[] getDecodedImageAsByteArray() {
		Assert.isTrue(this.isBase64(), "l'image n'est pas une image en base 64");
		return Base64.getDecoder()
		.decode(
				this.getUseFullDataFromBase64().getBytes());
	}
	
	
	/**
	 * Permet de récupérer les données exploitables d'une image encodée en base 64.
	 * @return -
	 */
	@JsonIgnore
	public String getUseFullDataFromBase64() {
		Assert.isTrue(this.isBase64(), "l'image n'est pas une image en base 64");
		return this.getData().substring(this.getData().indexOf(",") + 1);
	}
	
}
