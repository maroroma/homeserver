import React, {useEffect, useState} from 'react';
import {administrationApi} from '../../../apiManagement/AdministrationApi';
import {when} from '../../../tools/when';
import {useImportBookContext} from './ImportBookContext';

export default function SetUrlComponent() {

    const { seriePageUrl, dispatchNewUrl } = useImportBookContext();

    const [sanctuaryUrl, setSanctuaryUrl] = useState("");


    useEffect(() => {
        administrationApi().getOneProperty("homeserver.books.sanctuary.api.url")
            .then(result => setSanctuaryUrl(result.value));
    }, []);


    const updateUrl = (event) => {
        dispatchNewUrl(event.target.value);
    }


    return <div>
        <a className={when(sanctuaryUrl === "").thenDisableElement("waves-effect waves-light btn-small")} href={sanctuaryUrl} target="_blank"><i className="material-icons right">help</i>sanctuary</a>
        <div className="input-field">
            <input placeholder="Url pour l'import" id="url_for_import" type="text" className="validate" value={seriePageUrl} onChange={updateUrl}></input>
            <label htmlFor="url_for_import" className="active">Url pour l'import</label>
        </div>
    </div>
}