package maroroma.homeserverng.tools.kodi.responses.getitem;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class ItemContent {

//	"item": {
//    "album": "",
//    "artist": [],
//    "episode": -1,
//    "fanart": "image://http%3a%2f%2fimage.tmdb.org%2ft%2fp%2foriginal%2fkvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg/",
//    "file": "smb://MELCHIOR/SHARE/Videos/Films/Ant.Man.2015.TRUEFRENCH.DVDRip.XViD-FUNKKY.avi",
//    "id": 205,
//    "label": "Ant-Man",
//    "season": -1,
//    "showtitle": "",
//    "streamdetails": {
//        "audio": [],
//        "subtitle": [],
//        "video": []
//    },
//    "thumbnail": "image://http%3a%2f%2fimage.tmdb.org%2ft%2fp%2foriginal%2fD6e8RJf2qUstnfkTslTXNTUAlT.jpg/",
//    "title": "Ant-Man",
//    "tvshowid": -1,
//    "type": "movie"
//}
	
	private String album;
	private int episode;
	private String fanart;
	private String file;
	private int id;
	private String label;
	private int season;
	private String showtitle;
	private String thumbnail;
	private String title;
	private int tvshowid;
	private String type;
	
	private List<String> artist;
	
}
