package maroroma.homeserverng.tools.sse;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe utilitaire pour la construction de {@link SseEventBuilder}.
 * @author rlevexie
 *
 */
public abstract class SseEventBuilderHelper {

	/**
	 * Enrichit un {@link SseEventBuilder} pour sa partie data, en sérialisant au besoin un objet dans le format 
	 * json si le media type est bien celui attendu.
	 * @param builder - builder à enrichir
	 * @param toPrepare - data à envoyer
	 * @param mediaType - type d'émission (permet de dire si on fait du json)
	 * @return -
	 * @throws JsonProcessingException -
	 */
	public static SseEventBuilder prepareForMediaType(final SseEventBuilder builder,
			final Object toPrepare, final String mediaType) throws JsonProcessingException {
		Assert.notNull(toPrepare, "toPrepare can't be null");
		MediaType mediaTypeToSend = MediaType.parseMediaType(mediaType);
		
		// si on demande du json, on sérialise directement
		if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType)) {
			ObjectMapper mapper = new ObjectMapper();
			builder.data(mapper.writeValueAsString(toPrepare), mediaTypeToSend);
		} else {
			builder.data(toPrepare, mediaTypeToSend);
		}
		
		return builder;
	}
}
