package maroroma.homeserverng.watch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebcamEmitterKey {
	private String webcamId;
	private String subscriberId;

	@Override
	public String toString() {
		return this.webcamId+"#"+this.subscriberId;
	}

	public static WebcamEmitterKey fromString(String key) {
		String[] keys = key.split("#");
		return new WebcamEmitterKey(keys[0], keys[1]);
	}
}
