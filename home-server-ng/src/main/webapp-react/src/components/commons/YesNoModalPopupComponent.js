import React from 'react';
import { ModalPopupComponent, usePopupDriver } from './ModalPopupComponent';
import { useState, useEffect } from 'react';



export default function YesNoModalPopupComponent({ driver }) {

    // pilotage d'une popup
    const [popupDriver, setPopupDriver] = usePopupDriver({
        id: 'popup',
        open: driver.open,
        title: driver.title,
        okLabel: driver.okLabel ? driver.okLabel : 'Ok',
        cancelLabel: driver.cancelLabel ? driver.cancelLabel : 'Annuler',
        onOk: driver.onOk,
    });

    useEffect(() => {
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