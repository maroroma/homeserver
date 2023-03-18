import React, {useEffect} from 'react';
import {useDisplayList} from '../../../tools/displayList';
import enhance from '../../../tools/enhance';
import {addBookSubReactor} from './AddBookSubReactor';
import SelectableBookRendererComponent from './SelectableBookRendererComponent';


export default function SearchForAddResultsComponent() {

    const [searchResults, setSearchResults] = useDisplayList();

    useEffect(() => {

        return addBookSubReactor().onSearchResultReceived(result => setSearchResults(
            { ...searchResults.update(result).updateItems(enhance().selectable()) })
        );


    }, []);

    const switchItemSelection = (oneBookResult, event) => {

        // sinon on réselectionne la Step correspondant à la zone que l'on vient de cliquer
        event.stopPropagation();

        const newSelectionValue = !oneBookResult.selected;

        const bookCandidates = searchResults
        .updateAllSelectableItems(false)
        .updateSelectableItems(oneBookResult.id, newSelectionValue);

        setSearchResults({ ...bookCandidates });



        addBookSubReactor().bookToAddCandidateUpdated(bookCandidates.getSelectedItems());

        // todoSubEventReactor().fileSelectionChange(newCompletedTorrents.getSelectedItems());

    }

    return <div>
        <div className="collection">
            {searchResults.displayList.map((oneBookResult, index) => 
            <SelectableBookRendererComponent 
            bookToDisplay={oneBookResult} 
            onClick={(event) => switchItemSelection(oneBookResult, event)} 
            key={index}
            useInitialImageLink={true}
            ></SelectableBookRendererComponent>)}
        </div>
    </div>
}