package maroroma.homeserverng.filemanager.model;

import lombok.Data;

import java.util.List;

@Data
public class UrlListToUpload {
    private List<String> urlList;
}
