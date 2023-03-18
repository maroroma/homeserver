import React from 'react';
import {useImportBookContext} from './ImportBookContext';

export default function SetUrlComponent() {

    const { seriePageUrl, dispatchNewUrl } = useImportBookContext();

    const updateUrl = (event) => {
        dispatchNewUrl(event.target.value);
    }


    return <div>
        <div className="input-field">
            <input placeholder="Url pour l'import" id="url_for_import" type="text" className="validate" value={seriePageUrl} onChange={updateUrl}></input>
            <label htmlFor="url_for_import" className="active">Url pour l'import</label>
        </div>
    </div>
}