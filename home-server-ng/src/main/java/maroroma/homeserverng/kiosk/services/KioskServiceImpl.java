package maroroma.homeserverng.kiosk.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import maroroma.homeserverng.kiosk.model.CurrentReading;
import maroroma.homeserverng.kiosk.model.CurrentReadingType;
import maroroma.homeserverng.kiosk.model.KioskDisplayOption;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.FluentList;
import maroroma.homeserverng.tools.kodi.requests.AbstractKodiJsonRPCMethod;
import maroroma.homeserverng.tools.kodi.requests.KodiMethods;
import maroroma.homeserverng.tools.kodi.responses.getactiveplayers.ActivePlayer;
import maroroma.homeserverng.tools.kodi.responses.getactiveplayers.GetActivePlayersResponse;
import maroroma.homeserverng.tools.kodi.responses.getitem.GetItemResponse;

/**
 * Implémentation du {@link KioskService}.
 * @author rlevexie
 *
 */
@Service
public class KioskServiceImpl implements KioskService {

	/**
	 * displayWeather.
	 */
	@Property("homeserver.kiosk.displayWeather")
	private HomeServerPropertyHolder displayWeather;

	/**
	 * displayHour.
	 */
	@Property("homeserver.kiosk.displayHour")
	private HomeServerPropertyHolder displayHour;

	/**
	 * displayCurrentReading.
	 */
	@Property("homeserver.kiosk.displayCurrentReading")
	private HomeServerPropertyHolder displayCurrentReading;

	/**
	 * displayCurrentDownload.
	 */
	@Property("homeserver.kiosk.displayCurrentDownload")
	private HomeServerPropertyHolder displayCurrentDownload;

	/**
	 * Liste des urls des plateformes kodi.
	 */
	@Property("homeserver.kiosk.kodi.urls")
	private HomeServerPropertyHolder kodiUrls;


	@Override
	public KioskDisplayOption getOptions() {

		// on retourne les options d'affichage à partir de l'ensemble des propriétés mappées sur le service.
		return KioskDisplayOption.builder()
				.displayCurrentDownload(this.displayCurrentDownload.asBoolean())
				.displayCurrentReading(this.displayCurrentReading.asBoolean())
				.displayHour(this.displayHour.asBoolean())
				.displayWeather(this.displayWeather.asBoolean())
				.build();
	}


	@Override
	public List<CurrentReading> getCurrentReadings() {

		return null;
	}

	private List<CurrentReading> getCurrentReading(String kodiInstance) {
		
		// récupération de la liste des players
		final RestTemplate rt = new RestTemplate();
		List<ActivePlayer> players = rt.postForObject(kodiInstance, KodiMethods.getActivePlayers(), GetActivePlayersResponse.class).getResult();
		
		// construction des requêtes pour la récupération des items (audio et video)
		List<AbstractKodiJsonRPCMethod> audioItemGetDetailRequest = players.stream()
				.filter(ap -> ap.isAudioPlayer())
				.map(ap -> KodiMethods.getPlayingAudioItem(ap.getPlayerid())).collect(Collectors.toList());
		
		List<AbstractKodiJsonRPCMethod> videoItemGetDetailRequest = players.stream()
				.filter(ap -> ap.isVideoPlayer())
				.map(ap -> KodiMethods.getPlayingVideoItem(ap.getPlayerid())).collect(Collectors.toList());
		
		// récupération des items audios et videos
		return FluentList.<CurrentReading>create().addAllAnd(getDetailsFor(kodiInstance, audioItemGetDetailRequest, CurrentReadingType.AUDIO))
		.addAllAnd(getDetailsFor(kodiInstance, videoItemGetDetailRequest, CurrentReadingType.VIDEO));
		
	}
	
	private List<CurrentReading> getDetailsFor(String kodiInstance, List<AbstractKodiJsonRPCMethod> requests, CurrentReadingType type) {
		final RestTemplate rt = new RestTemplate();
		return requests.stream().map(oneRequest -> 
			rt.postForObject(kodiInstance, oneRequest, GetItemResponse.class).getResult().getItem())
				.map(oneItemContent -> CurrentReading.builder()
						.kodiInstance(kodiInstance).label(oneItemContent.getLabel()).type(type).build())
				.collect(Collectors.toList());
	}

}
