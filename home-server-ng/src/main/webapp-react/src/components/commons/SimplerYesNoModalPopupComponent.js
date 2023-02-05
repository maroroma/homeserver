import React from 'react';
import { useState, useEffect } from 'react';
import popup from '../../tools/popup';



export default function SimplerYesNoModalPopupComponent({
    id,
    open,
    title,
    question,
    onClose = () => { },
    okLabel = "Ok",
    cancelLabel = "Annuler"
}) {


    const [innerPopupInstance, setInnerPopupInstance] = useState(undefined);

    useEffect(() => {

        const instance = popup(id, setInnerPopupInstance, innerPopupInstanceClose)

        return () => {
            instance.destroy();
        }

    }, [id]);

    useEffect(() => {
        if (innerPopupInstance) {
            if (open) {
                innerPopupInstance.open();
            } else {
                innerPopupInstance.close();
            }
        }
    }, [open]);

   



    const innerPopupInstanceClose = () => {};

    const innerClose = (result) => {
        innerPopupInstance.close();
        onClose(result);
    }


    return (
        <div id={id} className="modal">
            <div className="modal-content">
                <h6>{title}</h6>
                {question}
            </div>
            <div className="modal-footer">
                <button className="btn waves-effect waves-green" onClick={() => innerClose(true)}>{okLabel}</button>
                <button className="btn waves-effect waves-red red" onClick={() => innerClose(false)}>{cancelLabel}</button>
            </div>
        </div>
    );
}