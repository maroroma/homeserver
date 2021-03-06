package maroroma.homeserverng.network.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO pour la description des serveurs du réseau local.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerDescriptor {

	/**
	 * Nom du serveur.
	 */
	private String nom;
	/**
	 * Ip du serveur.
	 */
	private String ip;
	/**
	 * Description du serveur.
	 */
	private String description;
	
	/**
	 * Liste des urls accessibles pour ce serveur.
	 */
	private List<UrlDescriptor> urls = new ArrayList<>();
	
	/**
	 * Détermine si le serveur est atteignable.
	 */
	private boolean reachable;

	/**
	 * Test l'accès au serveur sur la première url trouvée.
	 */
	public void test() {
		Assert.notEmpty(urls, "Aucune url à tester pour le serveur " + this.nom);
		RestTemplate template = new RestTemplate();
		template.getForEntity(this.urls.get(0).getUrl(), String.class);
	}
	
	/**
	 * Retourne un {@link ServerDescriptor} par défaut injoinable.
	 * @return -
	 */
	public static ServerDescriptor unreachable() {
		return ServerDescriptor.builder().reachable(false).build();
	}
	
	/**
	 * Retourne un serveur joignable d'adresse ip donnée.
	 * @param ipAddress -
	 * @return -
	 */
	public static ServerDescriptor reachable(final String ipAddress) {
		return ServerDescriptor.builder().reachable(true).ip(ipAddress).build();
	}
	
}
