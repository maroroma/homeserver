package maroroma.homeserverng.seedbox.model.remote;

import org.springframework.util.Assert;

import lombok.Data;
import maroroma.homeserverng.tools.helpers.FluentMap;

@Data
public abstract class AbstractTransmissionRequest {

	private String method;
	private FluentMap<String, Object> arguments;
	
	public AbstractTransmissionRequest(String methodName) {
		Assert.hasLength(methodName, "methodName can't be null or empty");
		this.method = methodName;
		arguments = FluentMap.<String, Object>create();
	}
	
	
}
