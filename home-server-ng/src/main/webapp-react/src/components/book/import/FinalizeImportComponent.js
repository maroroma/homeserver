import React, {useEffect} from 'react';
import {useDisplayList} from '../../../tools/displayList';
import {useImportBookContext} from './ImportBookContext';


export default function FinalizeImportComponent() {

    const { serie, importTitlePrefix, proposalResults, dispatchNewImportPrefix, owner, dispatchNewOwner } = useImportBookContext();

    const [selectedProposals, setSelectedProposals] = useDisplayList();

    useEffect(() => {
        setSelectedProposals({ ...selectedProposals.update(proposalResults.raw) })
    }, [proposalResults]);



    return <div>
        <div>{proposalResults.getSelectedItems().length} livre(s) sélectionné(s) pour l'import dans la série <span className="strong">{serie.title}</span></div>
        <div className="input-field col s6">
            <input placeholder="Préfixe pour le sous titre" id="prefix_input" type="text" className="validate" value={importTitlePrefix} onChange={(event) => dispatchNewImportPrefix(event.target.value)}></input>
            <label htmlFor="prefix_input" className="active">Préfixe</label>
        </div>
        <div className="input-field col s6">
            <input placeholder="Proprio" id="owner_input" type="text" className="validate" value={owner} onChange={(event) => dispatchNewOwner(event.target.value)}></input>
            <label htmlFor="owner_input" className="active">A qui c'est ?</label>
        </div>
        <table>
            <thead>
                <tr>
                    <th>Titre</th>
                    <th>Sous Titre</th>
                    <th>Proprio</th>
                </tr>
            </thead>
            <tbody>
                {proposalResults.getSelectedItems().map(oneBookProposal => <tr key={oneBookProposal.number}>
                    <td>{serie.title}</td>
                    <td>{`${importTitlePrefix} ${oneBookProposal.number}`}</td>
                    <td>{owner}</td>
                </tr>)}
            </tbody>
        </table>
    </div>
}