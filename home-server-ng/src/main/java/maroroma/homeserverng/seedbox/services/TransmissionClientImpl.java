package maroroma.homeserverng.seedbox.services;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.seedbox.model.remote.gettorrent.GetTorrentRequest;
import maroroma.homeserverng.seedbox.model.remote.gettorrent.GetTorrentResponse;
import maroroma.homeserverng.seedbox.model.remote.removetorrent.RemoveTorrentRequest;
import maroroma.homeserverng.seedbox.tools.TransmissionConverter;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.Streamed;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.SSEStreamableException;
import maroroma.homeserverng.tools.retry.Retryer;


/**
 * Implémentation de {@link TransmissionClient}.
 * Permet de centraliser les appels aux api web de transmission.
 * Permet notamment de gérer de manière centrale le token utilisé par transmission pour son client.
 * @author RLEVEXIE
 *
 */
@Component
public class TransmissionClientImpl implements TransmissionClient {

	/**
	 * Correspond au nom du header technique portant le token transmission.
	 */
	private static final String X_TRANSMISSION_SESSION_ID = "X-Transmission-Session-Id";

	/**
	 * Url d'accès au api web transmission.
	 */
	@Property("homeserver.seedbox.transmission.url")
	private HomeServerPropertyHolder transmissionUrl;

	/**
	 * uri Path pour la récupération des torrents.
	 */
	@Property("homeserver.seedbox.transmission.geturi")
	private HomeServerPropertyHolder transmissionGetUri;
	
	/**
	 * uri Path pour la suppression des torrents.
	 */
	@Property("homeserver.seedbox.transmission.removeuri")
	private HomeServerPropertyHolder transmissionRemoveUri;
	
	/**
	 * Correlation id / token transmission stocké à travers le service.
	 */
	private String lastCorrelationId = null;
	
	/**
	 * {@inheritDoc}
	 * <br /> Dans cette implémentation, appel les api web transmission pour récupérer la liste des différents torrents en cours.
	 */
	@Streamed(fixedDelay = @Property("homeserver.seedbox.client.stream.fixedDelay"),
			eventName = "updatedTorrents", mediaType = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public Object process()  throws SSEStreamableException {
		
		
		// client http
		RestTemplate rt = new RestTemplate();
		
		
		// réponse finale pour la conversion
		ResponseEntity<GetTorrentResponse> finalResponse;
		
		
		try {
			finalResponse = Retryer.
					<GetTorrentRequest, GetTorrentResponse>
					// tous les essais ont la même forme, la requete évolue cependant à travers le handler adapté
					tryWithRestTemplate(
					request -> {
						// récupération de la liste
						return rt.postForEntity(this.transmissionUrl.getResolvedValue()
								+ this.transmissionGetUri.getResolvedValue(), 
								request, GetTorrentResponse.class);
					})
					// sur erreur, on récupère le token transmission, et on change l'input pour le prochain essai
					.onExceptionTransformInput(HttpClientErrorException.class, (request, exception)
							-> {
								extractTransmissionSessionIdFromException(exception);
								return createTorrentRequest();
							})
					// cela ne peut planter qu'une fois
					.thenRetry(1)
					// lancement
					.apply(createTorrentRequest());
			// exception finale
		} catch (HomeServerException e) {
			throw new SSEStreamableException("Exception survenue lors de l'appel au service de torrent", e);
		}
		
		// retour des objets convertis
		return  TransmissionConverter.convert(finalResponse.getBody());
		
	}

	/**
	 * Permet de récupérer le token transmission sur une exception rencontrée lors des appels, et de le stocker
	 * dans le bean.
	 * @param exception -
	 */
	private void extractTransmissionSessionIdFromException(final HttpClientErrorException exception) {
		if (exception.getResponseHeaders().containsKey(X_TRANSMISSION_SESSION_ID)) {
			this.lastCorrelationId = exception.getResponseHeaders().getFirst(X_TRANSMISSION_SESSION_ID);
		}
	}

	/**
	 * Création de la requête pour la récupération des torrents en cours.
	 * Le {@link HttpEntity} généré prend en compte le fait que le token transmission a déjà été récupéré, ce qui empêche tout
	 * appel en erreur inutile.
	 * @return -
	 */
	private HttpEntity<GetTorrentRequest> createTorrentRequest() {
		return new HttpEntity<>(GetTorrentRequest.create(),
				generateTransmissionHeaders());
	}
	
	/**
	 * Création de la requête de suppression d'un torrent.
	 * @param id -
	 * @return -
	 */
	private HttpEntity<RemoveTorrentRequest> createRemoveTorrentRequest(final String... id) {
		return new HttpEntity<RemoveTorrentRequest>(RemoveTorrentRequest.create(id), generateTransmissionHeaders());
	}

	/**
	 * Permet de générer les headers de base pour transmission, en fonction de la présence du correlation id.
	 * @return -
	 */
	private HttpHeaders generateTransmissionHeaders() {
		HttpHeaders headers = new HttpHeaders();
		if (this.lastCorrelationId != null && !this.lastCorrelationId.isEmpty()) {
			headers.add(X_TRANSMISSION_SESSION_ID, this.lastCorrelationId);
		}
		return headers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uploadTorrent(final MultipartFile torrentToUpload) throws HomeServerException {
		// client http
				RestTemplate rt = new RestTemplate();
				
				
				// réponse finale pour la conversion
				ResponseEntity<GetTorrentResponse> finalResponse;
				
//				rt.pos
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTorrent(final String torrentId) throws HomeServerException {
		
		RestTemplate rt = new RestTemplate();

		Retryer.<RemoveTorrentRequest, String>tryWithRestTemplate(request -> {
			// récupération de la liste
			return rt.postForEntity(this.transmissionUrl.getResolvedValue()
					+ this.transmissionRemoveUri.getResolvedValue(), 
					request, String.class); 
		})
		.onExceptionTransformInput(HttpClientErrorException.class, (request, exception)
							-> {
								extractTransmissionSessionIdFromException(exception);
								return createRemoveTorrentRequest(torrentId);
							})
					// cela ne peut planter qu'une fois
					.thenRetry(1)
					.apply(createRemoveTorrentRequest(torrentId))
					.getBody();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RunningTorrent> getRunningTorrents() throws HomeServerException {
		try {
			return (List<RunningTorrent>) this.process();
		} catch (SSEStreamableException e) {
			throw new HomeServerException("Erreur survenue lors de la récupération des torrents", e);
		}
	}

}
