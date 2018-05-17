package maroroma.homeserverng.tools.kodi.responses.getitem;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.kodi.responses.Result;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class GetItemResponse {
//
//	 "id": "VideoGetItem",
//	    "jsonrpc": "2.0",
//	    "result": {
//	        "item": {
//	            "album": "",
//	            "artist": [],
//	            "episode": -1,
//	            "fanart": "image://http%3a%2f%2fimage.tmdb.org%2ft%2fp%2foriginal%2fkvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg/",
//	            "file": "smb://MELCHIOR/SHARE/Videos/Films/Ant.Man.2015.TRUEFRENCH.DVDRip.XViD-FUNKKY.avi",
//	            "id": 205,
//	            "label": "Ant-Man",
//	            "season": -1,
//	            "showtitle": "",
//	            "streamdetails": {
//	                "audio": [],
//	                "subtitle": [],
//	                "video": []
//	            },
//	            "thumbnail": "image://http%3a%2f%2fimage.tmdb.org%2ft%2fp%2foriginal%2fD6e8RJf2qUstnfkTslTXNTUAlT.jpg/",
//	            "title": "Ant-Man",
//	            "tvshowid": -1,
//	            "type": "movie"
//	        }
//	    }
	
	private String id;
	private Result<ItemContent> result;
}
