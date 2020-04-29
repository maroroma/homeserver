import React from 'react';
import { ModalPopupComponent } from './ModalPopupComponent';
import { useState, useEffect } from 'react';



export default function YesNoModalPopupComponent({ driver }) {

    // pilotage d'une popup
    const [popupDriver, setPopupDriver] = useState({
        id: 'popup',
        open: driver.open,
        title: driver.title,
        okLabel: driver.okLabel ? driver.okLabel : 'Ok',
        cancelLabel: driver.cancelLabel ? driver.cancelLabel : 'Annuler',
        updateData: () => { },
        onOk: driver.onOk,
        data: {}
    });

    useEffect(() => {
        console.log('yesno!!!!');
        setPopupDriver({
            ...popupDriver,
            open: driver.open,
            title: driver.title,
            okLabel: driver.okLabel ? driver.okLabel : 'Ok',
            cancelLabel: driver.cancelLabel ? driver.cancelLabel : 'Annuler',
            onOk: driver.onOk
        });
    }, [driver])


    return (
        <ModalPopupComponent popupId="popupCreateFolder" driver={popupDriver}>
            <p>
                {driver.yesNoQuestion}
            </p>
        </ModalPopupComponent>
    );
}