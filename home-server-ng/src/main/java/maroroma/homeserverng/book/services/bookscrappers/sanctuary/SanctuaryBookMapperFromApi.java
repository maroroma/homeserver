package maroroma.homeserverng.book.services.bookscrappers.sanctuary;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;
import maroroma.homeserverng.book.services.bookscrappers.sanctuary.api.OneBook;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class SanctuaryBookMapperFromApi {

    public Book mapBooksFromSanctuaryResponse(OneBook bookFromSanctuary) {
        return Book.builder()
                .id(UUID.randomUUID().toString())
                .title(bookFromSanctuary.getLabel())
                .initialImageLink(bookFromSanctuary.getImgsrc())
                .scrapperSource(BookScrapperSource.SANCTUARY.name())
                .build();
    }

}
