package maroroma.homeserverng.book.services.bookscrappers.google.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchBookResponse {
    private List<OneBook> items;
}
