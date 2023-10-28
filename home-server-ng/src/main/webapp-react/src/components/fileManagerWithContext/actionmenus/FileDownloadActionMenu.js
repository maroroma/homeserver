import React, {useEffect} from 'react';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import FloatingMenuComponent from '../../commons/FloatingMenu/FloatingMenuComponent';
import keys from '../../../tools/keys';


export default function FileDownloadActionMenu() {

    const { disptachDisableDownloading } = useFileBrowserContext();




    useEffect(() => {

        const keyHandler = (event) => {
            keys(event).onEscape(() => {
                disptachDisableDownloading();
            })
        }

        keys().register(keyHandler);

        return () => {
            keys().unregister(keyHandler);
        }

    }, [disptachDisableDownloading])



    return <FloatingMenuComponent isOpen={true}>
        <li>
            <a href="#!" className="btn-floating btn-small green" onClick={() => { disptachDisableDownloading() }}>
                <i className="material-icons">undo</i>
            </a>
        </li>
    </FloatingMenuComponent >
}