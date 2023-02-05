package maroroma.homeserverng.book.services.bookscrappers.google.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IsbnIdentifier {
    private String type;
    private String identifier;
}
