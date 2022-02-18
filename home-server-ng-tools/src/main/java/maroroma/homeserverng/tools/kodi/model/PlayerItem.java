package maroroma.homeserverng.tools.kodi.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerItem {
	private int id;
	private String label;
	private String type;

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

	
}
