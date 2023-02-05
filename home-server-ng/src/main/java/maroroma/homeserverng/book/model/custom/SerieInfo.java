package maroroma.homeserverng.book.model.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SerieInfo {
    private String serieId;
    private String serieName;
    private String orderInSerie;

    public static SerieInfo fromSerie(Serie source) {
        return SerieInfo.builder()
                .serieId(source.getId())
                .serieName(source.getTitle())
                .build();
    }
}
