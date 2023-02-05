package maroroma.homeserverng.book.services.bookscrappers.sanctuary.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneBook {
    private String url;
    private String label;
    private String imgsrc;
}
