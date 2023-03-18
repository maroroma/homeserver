import React from 'react';
import {when} from '../../../tools/when';
import {useImportBookContext} from './ImportBookContext';
import './BookProposalRenderer.scss';

export default function BookProposalRenderer({ bookProposal }) {

    // pour afficher la selction plus fort
    // box-shadow: 0 3px 3px 0 rgba(0,150,0,0.5),0 3px 1px -2px rgba(0,150,0,0.5),0 1px 5px 0 rgba(0,150,0,0.2);

    const {dispatchSelectOneProposal} = useImportBookContext();



    return <div className={when().selected(bookProposal).css("book-selected", "card")}>
        <div className="card-image">
            <img src={bookProposal.initialImageLink}></img>
            {bookProposal.alreadyInCollection ? <></> : <a className={when().selected(bookProposal).thenPlopSelectedItem("btn-floating halfway-fab waves-effect waves-light")}
                onClick={() => dispatchSelectOneProposal(bookProposal)}>
                <i className="material-icons">{bookProposal.selected ? "done" : "add"}</i>
            </a>}
        </div>
        <div className="card-content">
            <p>{bookProposal.title} - {bookProposal.number}</p>
        </div>
    </div>
}