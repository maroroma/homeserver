package maroroma.homeserverng.book.model.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    private String id;
    private String title;
    private String subtitle;
    private List<String> isbns;
    private String author;
    private int pageCount;
    private String initialImageLink;
    private SerieInfo serieInfo;
    private String pictureFileId;
    private String owner;
    private String scrapperSource;

    public Book associateToSerie(SerieInfo serieInfo) {
        return this.associateToSerie(serieInfo, Optional.ofNullable(this.serieInfo).map(SerieInfo::getOrderInSerie).orElse(null));
    }

    public Book associateToSerie(SerieInfo serieInfo, String orderInSerie) {
        this.serieInfo = SerieInfo.builder()
                .serieName(serieInfo.getSerieName())
                .serieId(serieInfo.getSerieId())
                .orderInSerie(orderInSerie)
                .build();
        return this;
    }

    public boolean hasUsableSerieInfo() {
        return Objects.nonNull(this.serieInfo) && Objects.nonNull(this.serieInfo.getSerieId()) && Objects.nonNull(this.serieInfo.getSerieName());
    }

}
