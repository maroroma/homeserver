package maroroma.homeserverng.exceptions;

import maroroma.homeserverng.tools.exceptions.DisableModuleException;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.streaming.ouput.StreamingFileSenderException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gestion des exceptions connues.
 * @author RLEVEXIE
 *
 */
@ControllerAdvice
public class RestExceptionHandler {

	/**
	 * Gestion des mauvais arguments.
	 * @param iae -
	 * @return -
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody ErrorInfo handleIllegalArgumentException(final IllegalArgumentException iae) {
		return new ErrorInfo(HttpStatus.BAD_REQUEST, iae);
	}

	/**
	 * Gestion des erreurs internes.
	 * @param hse -
	 * @return -
	 */
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = HomeServerException.class)
	@ResponseBody ErrorInfo handleHomeServerException(final HomeServerException hse) {
		return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, hse);
	}


	/**
	 * Gestion des erreurs pour les appels sur les modules désactivés.
	 * @param dme -
	 * @return -
	 */
	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(value = DisableModuleException.class)
	@ResponseBody ErrorInfo handleDisabledModuleException(final DisableModuleException dme) {
		return new ErrorInfo(HttpStatus.SERVICE_UNAVAILABLE, dme);
	}

	/**
	 * Gestion des erreurs pour le streaming.
	 * @param response -
	 * @param ex -
	 * @throws IOException -
	 */
	@ExceptionHandler(value = StreamingFileSenderException.class)
	void handleIllegalArgumentException(final HttpServletResponse response, final StreamingFileSenderException ex) throws IOException {
		//		System.out.println("YOLO");
		response.sendError(ex.getHttpStatus().value());
		if (ex.getSpecificHeader() != null) {
			response.addHeader(ex.getSpecificHeader().getItem1(), ex.getSpecificHeader().getItem2());
		}
	}

}
