package maroroma.homeserverng.book.model.importbatch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.book.model.custom.Serie;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportBookProposalsForSerieRequest {
    private Serie targetedSerie;
    private String importPrefix;

    private String owner;

    private List<ImportBookProposal> booksToImport;
}
