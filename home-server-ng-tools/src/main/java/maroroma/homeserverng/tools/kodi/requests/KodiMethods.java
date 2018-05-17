package maroroma.homeserverng.tools.kodi.requests;

import maroroma.homeserverng.tools.helpers.FluentList;
import maroroma.homeserverng.tools.helpers.FluentMap;

/**
 * classe utilitaire pour la construction de requête vers kodi.
 * @author rlevexie
 *
 */
@Deprecated
public abstract class KodiMethods {

	/**
	 * Représentation de la version 2 de json rpc.
	 */
	public static final String JSON_RPC_V_2_0 = "2.0";


	/**
	 * Nom de la méthode pour les notifications.
	 */
	public static final String KODI_METHOD_NAME_GUI_SHOW_NOTIFICATION = "GUI.ShowNotification";
	
	/**
	 * Nom de la méthode pour la récupération des lecteurs actifs.
	 */
	public static final String KODI_METHOD_NAME_GET_ACTIVE_PLAYER = "Player.GetActivePlayers";
	
	/**
	 * Nom de la méthode pour la récupération du détail de la lecture en cours.
	 */
	public static final String KODI_METHOD_NAME_GET_ITEM = "Player.GetItem";


	/**
	 * Permet de construire une {@link KodiJsonRPCMethodIntId} pour l'affichage de notification.
	 * @param title titre
	 * @param message message
	 * @param displayTime temps d'affichage
	 * @return -
	 */
	public static AbstractKodiJsonRPCMethod guiShowNotification(final String title, final String message, final int displayTime) {
		return KodiJsonRPCMethodIntId.builder()
				.jsonrpc(KodiMethods.JSON_RPC_V_2_0)
				.id(1)
				.method(KodiMethods.KODI_METHOD_NAME_GUI_SHOW_NOTIFICATION)
				.params(
						FluentMap.<String, Object>create()
						.add("title", title)
						.add("message", message)
						.add("displaytime", displayTime))
				.build();
	}

//	public static AbstractKodiJsonRPCMethod updateVideoLibrary() {
//
//	}
	

	/**
	 * PErmet de construire une {@link AbstractKodiJsonRPCMethod} pour la récupération des lecteurs actifs.
	 * @return -
	 */
	public static AbstractKodiJsonRPCMethod getActivePlayers() {
		return KodiJsonRPCMethodIntId.builder()
				.jsonrpc(KodiMethods.JSON_RPC_V_2_0)
				.id(1)
				.method(KodiMethods.KODI_METHOD_NAME_GET_ACTIVE_PLAYER)
				.build();
	}
	
	/**
	 * Retourne la requête pour le détail de la lecture en cours.
	 * @param playerId id du plauer
	 * @param getItemType type d'item
	 * @param details détail sur les items.
	 * @return -
	 */
	public static AbstractKodiJsonRPCMethod getPlayingItem(final int playerId, final String getItemType, final String... details) {
		// {"jsonrpc": "2.0", "method": "Player.GetItem", "params": { "properties": ["title", "album", "artist", "duration", "thumbnail", "file", "fanart", "streamdetails"], "playerid": 0 }, "id": "AudioGetItem"}
		
		return KodiJsonRPCMethodStringId.builder()
				.jsonrpc(KodiMethods.JSON_RPC_V_2_0)
				.id(getItemType)
				.method(KodiMethods.KODI_METHOD_NAME_GET_ITEM)
				.params(FluentMap.<String, Object>create()
						.add("properties", details)
						.add("playerid", playerId)
						)
				.build();
	}
	
	/**
	 * Retourne la requête pour avoir les détails d'une lecture vidéo en cours.
	 * @param playerId -
	 * @return -
	 */
	public static AbstractKodiJsonRPCMethod getPlayingVideoItem(final int playerId) {
		// {"jsonrpc": "2.0", "method": "Player.GetItem", "params": { "properties": ["title", "album", "artist", "season", "episode", "duration", "showtitle", "tvshowid", "thumbnail", "file", "fanart", "streamdetails"], "playerid": 1 }, "id": "VideoGetItem"}

		return KodiMethods.getPlayingItem(playerId, "VideoGetItem", KodiMethods.generatePlayingItemParameters("season", "episode",
				"showtitle", "tvshowid"));
	}
	
	/**
	 * Retourne la requête pour avoir les détails d'une lecture audio en cours.
	 * @param playerId -
	 * @return -
	 */
	public static AbstractKodiJsonRPCMethod getPlayingAudioItem(final int playerId) {
		// {"jsonrpc": "2.0", "method": "Player.GetItem", "params": { "properties": ["title", "album", "artist", "duration", "thumbnail", "file", "fanart", "streamdetails"], "playerid": 0 }, "id": "AudioGetItem"}
		
		return KodiMethods.getPlayingItem(playerId, "AudioGetItem", KodiMethods.generatePlayingItemParameters());
	}
	
	/**
	 * Permet de générer la liste de paramètres pour la récupération du détail d'une lecture en cours.
	 * @param params -
	 * @return -
	 */
	protected static String[] generatePlayingItemParameters(final String... params) {
		return FluentList.<String>create()
				.addAnd("title")
				.addAnd("album")
				.addAnd("artist")
				.addAnd("duration")
				.addAnd("thumbnail")
				.addAnd("file")
				.addAnd("fanart")
				.addAnd("streamdetails")
				.addAllAnd(params).toArray(new String[0]);
		
	}
	
	
}
