package maroroma.homeserverng.tools.kodi.responses.getactiveplayers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"videoPlayer", "audioPlayer"})
@Deprecated
public class ActivePlayer {
//	"playerid": 0,
//    "type": "audio
	
	public static final String VIDEO_PLAYER = "video";
	public static final String AUDIO_PLAYER = "audio";
	
	private int playerid;
	private String type;
	
	public boolean isVideoPlayer() {
		return ActivePlayer.VIDEO_PLAYER.equals(this.type);
	}
	public boolean isAudioPlayer() {
		return ActivePlayer.AUDIO_PLAYER.equals(this.type);
	}
}
