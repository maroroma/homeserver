import React, {useEffect, useState} from 'react';
import {administrationApi} from '../../apiManagement/AdministrationApi';
import bookApi from '../../apiManagement/BookApi';
import eventReactor from '../../eventReactor/EventReactor';
import {useDisplayList} from '../../tools/displayList';
import enhance from '../../tools/enhance';
import keys from '../../tools/keys';
import popup from '../../tools/popup';
import {when} from '../../tools/when';

export function sendMailWithMissingBooksEventReactor() {
    const openSendMailPopup = () => eventReactor().emit("OPEN_SEND_MISSING_BOOKS_POPUP");
    const onOpenSendMailPopup = (handler) => eventReactor().subscribe("OPEN_SEND_MISSING_BOOKS_POPUP", handler);

    return {
        openSendMailPopup: openSendMailPopup,
        onOpenSendMailPopup: onOpenSendMailPopup
    }

}

export function SendMailPopupComponent() {
    const [selfPopupInstance, setSelfPopupInstance] = useState(undefined);

    const [mailingList, setMailingList] = useDisplayList();
    const [newMailToAdd, setNewMailToAdd] = useState("");


    useEffect(() => {
        // gestion de la popup
        const instance = popup("sendMailPopupInstance", setSelfPopupInstance, innerClose);




        return () => {
            instance.destroy();
        }

    }, []);

    useEffect(() => {
        const unsubscribeOnOpenPopup = sendMailWithMissingBooksEventReactor().onOpenSendMailPopup(() => {
            selfPopupInstance.open();


            // récup adresse mail par défaut
            administrationApi()
                .getOneProperty("homeserver.notifyer.mail.smtp.clients")
                .then(response => {
                    console.log(response.value
                        .split(",")
                        .map(oneRawMail => {
                            return {
                                mail: oneRawMail
                            }
                        }));
                    setMailingList({
                        ...mailingList
                            .update(

                                response.value
                                    .split(",")
                                    .map(oneRawMail => {
                                        return {
                                            mail: oneRawMail
                                        }
                                    }))
                            .updateItems(enhance().indexed())
                    })
                }
                );

        });

        return () => {
            unsubscribeOnOpenPopup();
        }

    }, [selfPopupInstance]);

    const innerClose = () => {
        setNewMailToAdd("");
        setMailingList({ ...mailingList.update([]) });
    }


    const updateNewMail = (event) => {
        setNewMailToAdd(event.target.value);
    };

    const addNewMail = () => {
        if (newMailToAdd !== "") {
            setMailingList({ ...mailingList.update([...mailingList.rawList, { mail: newMailToAdd }]).updateItems(enhance().indexed()) });
            setNewMailToAdd("");
        }

    }

    const removeMail = (index) => {
        setMailingList({ ...mailingList.update([...mailingList.rawList.filter(oneMail => oneMail.index !== index)]) })
    }

    const sendMail = () => {
        bookApi().sendMailForMissingBooks(mailingList.rawList.map(oneMail => oneMail.mail)).then(result => selfPopupInstance.close());
    }


    return <div className="modal" id="sendMailPopupInstance">
        <div className="modal-content">
            <h6>Envoyer la liste des tomes manquants à</h6>
            {mailingList.displayList.map((oneMail) =>
                <div className="row  valign-wrapper" key={oneMail.index}>
                    <div className="col s9">
                        {oneMail.mail}
                    </div>
                    <div className="col s3">
                        <a className="waves-effect waves-light btn-small red" href="#" onClick={() => removeMail(oneMail.index)}><i className="material-icons">delete</i></a>
                    </div>
                </div>
            )}
            <div className="row  valign-wrapper">
                <div className="input-field col s9">
                    <input id="addSerieInput" type="text" className="validate" value={newMailToAdd} onChange={updateNewMail} onKeyDown={(key) => keys(key).onEnter(addNewMail)}></input>
                    <label htmlFor="addSerieInput">contact</label>
                </div>
                <div className="col s3">
                    <a className={when(newMailToAdd === "").thenDisableElement("waves-effect waves-light btn-small")} href="#" onClick={addNewMail}><i className="material-icons">add</i></a>
                </div>
            </div>
        </div>
        <div className="modal-footer">
            <button className={when(mailingList.displayList.length === 0).thenDisableElement("btn waves-effect waves-green")} onClick={sendMail}>Envoyer</button>
        </div>
    </div>

}