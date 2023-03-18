import eventReactor from "../../../eventReactor/EventReactor"


export const URL_UPDATED = 'URL_UPDATED';
export const BOOK_PROPOSALS_UPDATED = 'BOOK_PROPOSALS_UPDATED';
export const BOOK_PROPOSAL_SELECTED = 'BOOK_PROPOSAL_SELECTED';

export function importBookSubReactor() {

    const urlUpdated = (url) => eventReactor().emit(URL_UPDATED, url);
    const onUrlUpdated = (eventListener) => eventReactor().subscribe(URL_UPDATED, eventListener);

    const bookProposalsUpdated = (bookProposals) => eventReactor().emit(BOOK_PROPOSALS_UPDATED, bookProposals);
    const onBookProposalsUpdated = (eventListener) => eventReactor().subscribe(BOOK_PROPOSALS_UPDATED, eventListener);
    
    const bookProposalSelected = (bookProposal) => eventReactor().emit(BOOK_PROPOSAL_SELECTED, bookProposal);
    const onBookProposalSelected = (eventListener) => eventReactor().subscribe(BOOK_PROPOSAL_SELECTED, eventListener);

    return {
        urlUpdated: urlUpdated,
        onUrlUpdated: onUrlUpdated,
        bookProposalsUpdated,
        onBookProposalsUpdated,
        bookProposalSelected,
        onBookProposalSelected
    }
}