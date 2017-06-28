package maroroma.homeserverng.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContext;

import maroroma.homeserverng.tools.annotations.DevProfile;
import maroroma.homeserverng.tools.helpers.Assert;

@RestController
@DevProfile
public class MockController {

	@RequestMapping(path = "/mocks/transmission/{fileName}/", method = {RequestMethod.POST, RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getMockForGet(final @PathVariable("fileName") String fileName, @RequestHeader(required = false, name = "X-Transmission-Session-Id") String inputHeader) throws IOException {
		
		
		ClassPathResource cpr = new ClassPathResource("mocks/"+fileName);
		File returnValue = cpr.getFile();
		Assert.isValidFile(returnValue);
		StringBuilder sb = new StringBuilder();
		Files.readAllLines(returnValue.toPath()).forEach(line -> sb.append(line));

		
		HttpHeaders outputHEaders = new HttpHeaders();
		ResponseEntity<String> httpResponse = null;
		if (inputHeader == null || inputHeader.isEmpty()) {
			outputHEaders.add("X-Transmission-Session-Id", "X-Transmission-Session-Id");
			httpResponse = new ResponseEntity<String>(sb.toString(), outputHEaders,  HttpStatus.BAD_REQUEST);
		} else {
			outputHEaders.add("X-Transmission-Session-Id", inputHeader);
			httpResponse = new ResponseEntity<String>(sb.toString(), outputHEaders, HttpStatus.OK);
		}
		
		return httpResponse;
	}
	
}
