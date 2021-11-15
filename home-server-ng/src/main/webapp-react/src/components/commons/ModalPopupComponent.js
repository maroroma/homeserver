import React from 'react';
import { useState, useEffect } from 'react';

import { when } from '../../tools/when';

import './ModalPopupComponent.scss';
import eventReactor from '../../eventReactor/EventReactor';
import orElse from '../../tools/orElse';

export function ModalPopupComponent({ children, driver }) {

    const [innerPopupInstance, setInnerPopupInstance] = useState(undefined);

    useEffect(() => {
        if (!innerPopupInstance) {
            var elems = document.querySelectorAll('#' + driver.id);
            var instance = window.M.Modal.init(elems, { onCloseEnd: innerClose })[0];
            setInnerPopupInstance(instance);
        }
    }, [innerPopupInstance, driver]);


    useEffect(() => {
        if (innerPopupInstance) {
            if (driver && driver.open) {
                const wasClosed = !innerPopupInstance.isOpen;
                innerPopupInstance.open();
                if (wasClosed) { eventReactor().shortcuts().modalOpened(driver); }
            } else {
                innerPopupInstance.close();
            }
        }
    }, [driver]);


    const innerClose = () => {
        driver.open = false;
        eventReactor().shortcuts().modalClose(driver);
        if (innerPopupInstance) {
            innerPopupInstance.close();
        }
    }


    const innerOkClickHander = () => {
        driver.onOk(driver)
        driver.open = false;
        eventReactor().shortcuts().modalOk(driver);
        if (innerPopupInstance) {
            innerPopupInstance.close();
        }
    };
    const innerCancelClickHandler = () => innerClose();

    const hideCancelButton = () => driver.noCancelButton ? driver.noCancelButton : false;

    return (
        <div id={driver.id} className="modal">
            <div className="modal-content">
                <h6>{driver.title}</h6>
                {children}
            </div>
            <div className="modal-footer">
                <button className={when(driver.disableOkButton).thenDisableElement("btn waves-effect waves-green")} onClick={innerOkClickHander}>{driver.okLabel}</button>
                <button className={when(hideCancelButton).thenHideElement("btn waves-effect waves-red red")} onClick={innerCancelClickHandler}>{driver.cancelLabel}</button>
            </div>
        </div>
    );
}

export function usePopupDriver(popupStartConfig) {

    // const orElse = (optionalValue, defaultValue) => optionalValue ? optionalValue : defaultValue;


    return useState({
        id: orElse(popupStartConfig.id, 'NoDefinedId'),
        title: orElse(popupStartConfig.title, 'NoTitle'),
        okLabel: orElse(popupStartConfig.okLabel, 'Ok'),
        cancelLabel: orElse(popupStartConfig.cancelLabel, 'Annuler'),
        noCancelButton: orElse(popupStartConfig.noCancelButton, false),
        disableOkButton: orElse(popupStartConfig.disableOkButton, false),
        open: false,
        updateData: () => { },
        onOk: () => { },
        data: {}
    });
}