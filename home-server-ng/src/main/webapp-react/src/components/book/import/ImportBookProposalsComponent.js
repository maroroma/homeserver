import React, {useEffect, useState} from 'react';
import {useDisplayList} from '../../../tools/displayList';
import on from '../../../tools/on';
import CheckBoxComponent from '../../commons/CheckBoxComponent';
import BookProposalRenderer from './BookProposalRenderer';
import {useImportBookContext} from './ImportBookContext';


import './ImportBookProposalsComponent.scss';

/**
 * 
 * @returns PErmet d'afficher les livres d'une série sélectionnée via son url
 */
export default function ImportBookProposalsComponent() {

    const [internalProposalResults, setInternalProposalResults] = useDisplayList({});
    const [displayAlreadyOwnedBooks, setdisplayAlreadyOwnedBooks] = useState(false);

    const {proposalResults, proposalResultsLoading} = useImportBookContext();

    useEffect(() => {
        setInternalProposalResults({...proposalResults.updateFilter(oneProposal => displayAlreadyOwnedBooks ? true : !oneProposal.alreadyInCollection)});
    }, [proposalResults, displayAlreadyOwnedBooks]);

    return <div>
        <div>{internalProposalResults.rawList.length} livres trouvés</div>
        <div>{internalProposalResults.rawList.filter(on().selected()).length} livres sélectionnés pour l'import</div>
        <div>
            <CheckBoxComponent dataswitch={displayAlreadyOwnedBooks} onChange={setdisplayAlreadyOwnedBooks}></CheckBoxComponent>
            Afficher les {internalProposalResults.rawList.filter(oneProposal => oneProposal.alreadyInCollection).length} tomes déjà présents dans la collection
        </div>
        <div className="wrapper">
            {internalProposalResults.displayList.map(oneItem => <BookProposalRenderer bookProposal={oneItem} key={`bookproposal_${oneItem.index}`}></BookProposalRenderer>)}
        </div>
    </div>
}