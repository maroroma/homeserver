package maroroma.homeserverng.book.model.importbatch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportBookProposal {
    private int number;
    private String initialImageLink;

    private String title;

    private boolean alreadyInCollection;

}
