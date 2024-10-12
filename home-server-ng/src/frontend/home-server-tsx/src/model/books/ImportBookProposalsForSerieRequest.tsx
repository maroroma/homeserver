import ImportBookProposal from "./ImportBookProposal";
import Serie from "./Serie";

export default class ImportBookProposalsForSerieRequest {
    constructor(public targetedSerie: Serie, public importPrefix: string, public owner: string, public booksToImport: ImportBookProposal[]) { }
}