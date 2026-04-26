package maroroma.homemusicplayer.model.library.api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenameOneFileRequest {
    private String initialFileName;
    private String newFileName;
}
