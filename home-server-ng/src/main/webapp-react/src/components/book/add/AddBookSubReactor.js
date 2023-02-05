import eventReactor from "../../../eventReactor/EventReactor";


export const SEARCH_RESULT_RECEIVED = 'SEARCH_RESULT_RECEIVED';
export const BOOK_TO_ADD_CANDIDATE_UPDATED = 'BOOK_TO_ADD_CANDIDATE_UPDATED';
export const SERIE_SELECTED = 'SERIE_SELECTED';
export const SKIP_SERIE = 'SKIP_SERIE';
export const BOOK_ADD_REQUEST_UPDATED = 'BOOK_ADD_REQUEST_UPDATED';


export function addBookSubReactor() {
    // retour d'une recherche
    const searchResultReceived = (searchResults) => eventReactor().emit(SEARCH_RESULT_RECEIVED, searchResults);
    const onSearchResultReceived = (eventHandler) => eventReactor().subscribe(SEARCH_RESULT_RECEIVED, eventHandler);

    // modification de la sélection des libres à rajouter
    const bookToAddCandidateUpdated = (selectedBook) => eventReactor().emit(BOOK_TO_ADD_CANDIDATE_UPDATED, selectedBook);
    const onBookToAddCandidateUpdated = (eventHandler) => eventReactor().subscribe(BOOK_TO_ADD_CANDIDATE_UPDATED, eventHandler);

    // sélection de la série associée
    const serieSelected = (selectedSerie) => eventReactor().emit(SERIE_SELECTED, selectedSerie);
    const onSerieSelected = (eventHandler) => eventReactor().subscribe(SERIE_SELECTED, eventHandler);

    // pas de sélection de série
    const skipSerie = () => eventReactor().emit(SKIP_SERIE);
    const onSkipSerie = (eventHandler) => eventReactor().subscribe(SKIP_SERIE, eventHandler);

    // maj de la requête en préparation
    const bookAddRequestUpdated = (updatedRequest) => eventReactor().emit(BOOK_ADD_REQUEST_UPDATED, updatedRequest);
    const onBookAddRequestUpdated = (eventHandler) => eventReactor().subscribe(BOOK_ADD_REQUEST_UPDATED, eventHandler);

    return {
        searchResultReceived: searchResultReceived,
        onSearchResultReceived: onSearchResultReceived,
        bookToAddCandidateUpdated: bookToAddCandidateUpdated,
        onBookToAddCandidateUpdated: onBookToAddCandidateUpdated,
        serieSelected: serieSelected,
        onSerieSelected: onSerieSelected,
        skipSerie: skipSerie,
        onSkipSerie: onSkipSerie,
        bookAddRequestUpdated: bookAddRequestUpdated,
        onBookAddRequestUpdated: onBookAddRequestUpdated
    }
};
