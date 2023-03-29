package maroroma.homeserverng.book.model.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.helpers.StringUtils;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Serie {
    private String id;
    private String title;
    private List<String> bookIds;
    private String pictureFileId;
    private boolean isCompleted;

    private String serieUrlForImport;

    public Serie appendBooksToSerie(List<Book> books) {

        // si non renseigné, on tope la couverture du premier bouquin pour lequel
        // on a enregistré quelque chose
        if (pictureFileId == null) {
            this.pictureFileId = books.stream()
                    .map(Book::getPictureFileId)
                    .filter(StringUtils::hasLength)
                    .findFirst()
                    .orElse(null);
        }


        if (this.bookIds == null) {
            this.bookIds = new ArrayList<>();
        }
        this.bookIds.addAll(books.stream().map(Book::getId).toList());
        return this;
    }

    public Serie removeBookFromSerie(Book bookToRemove) {
        // suppression de la référence
        this.bookIds.removeIf(oneBookCandidateForRemoval -> bookToRemove.getId().equals(oneBookCandidateForRemoval));

        return this;
    }
}
