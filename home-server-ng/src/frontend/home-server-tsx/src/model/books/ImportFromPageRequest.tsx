import Serie from "./Serie";

export default class ImportFromPageRequest {
    constructor(public bookScrapperSource: string, public seriePageUrl: string, public serie: Serie) { }
}