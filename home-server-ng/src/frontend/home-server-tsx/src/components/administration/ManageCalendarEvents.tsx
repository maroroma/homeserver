import {FC, useEffect, useState} from "react";
import {Button, Form, InputGroup, Modal, Table} from "react-bootstrap";
import {CalendarEvent, CalendarFixedDate} from "../../model/administration/CalendarEvent";
import {AdministrationRequester} from "../../api/AdministrationRequester";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import StartWIPAction from "../../context/actions/StartWIPAction";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionPlusButton from "../actionmenu/ActionPlusButton";
import PassiveBlockingButton from "../blockingbutton/PassiveBlockingButton";
import BlockingButton from "../blockingbutton/BlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import EndWIPAction from "../../context/actions/EndWIPAction";
import CalendarFixedDateEditor from "./CalendarFixedDateEditor";
import {BoxArrowUpRight} from "react-bootstrap-icons";
import CssTools, {CustomClassNames} from "../bootstrap/CssTools";
import {BootstrapText} from "../bootstrap/BootstrapText";
import YesNoModal from "../modals/YesNoModal";
import AdministrationLoadedActiveCalendarAction from "../../context/actions/administration/AdministrationLoadedActiveCalendarAction";


const ManageCalendarEvents: FC = () => {

    const { dispatch, workInProgress, administrationSubState } = useHomeServerContext();

    const [calendarEvents, setCalendarEvents] = useState<CalendarEvent[]>([]);

    const [displayEditPopup, setDisplayEditPopup] = useState(false);

    const [creationMode, setCreationMode] = useState(true);

    const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

    const [wipCalendarEvent, setWipCalendarEvent] = useState(CalendarEvent.default())

    useEffect(() => {

        dispatch(new StartWIPAction("Récupération des events du calendrier"));

        AdministrationRequester.getCalendarEvents()
            .then(response => {
                dispatch(new EndWIPAction());
                setCalendarEvents(response)
            })
            .catch(error => { dispatch(new EndWIPInErrorAction("Erreur survenue lors de la récupération des events du calendrier")) });


    }, []);

    const createCalendarEvent = () => {
        AdministrationRequester.addCalendarEvent(wipCalendarEvent)
            .then(response => {
                dispatch(new EndWIPAction());
                setCalendarEvents(response);
                setDisplayEditPopup(false);
                setWipCalendarEvent(CalendarEvent.default());
            })
            .then(response => AdministrationRequester.getActiveCalendarEvents())
            .then(response => dispatch(new AdministrationLoadedActiveCalendarAction(response)))
            .catch(error => { dispatch(new EndWIPInErrorAction("Erreur survenue lors de la création des events du calendrier")) });
    }

    const updateCalendarEvent = () => {
        AdministrationRequester.updateCalendarEvent(wipCalendarEvent)
            .then(response => {
                dispatch(new EndWIPAction());
                setCalendarEvents(response);
                setDisplayEditPopup(false);
                setWipCalendarEvent(CalendarEvent.default());
            })
            .then(response => AdministrationRequester.getActiveCalendarEvents())
            .then(response => dispatch(new AdministrationLoadedActiveCalendarAction(response)))
            .catch(error => { dispatch(new EndWIPInErrorAction("Erreur survenue lors de la mise à jour des events du calendrier")) });
    }

    const deleteEvent = () => {
        dispatch(new StartWIPAction("Suppression de l'event en cours"));

        AdministrationRequester.deleteCalendarEvent(wipCalendarEvent)
            .then(response => {
                dispatch(new EndWIPAction());
                setCalendarEvents(response);
                setDisplayDeletePopup(false);
                setDisplayEditPopup(false);
                setWipCalendarEvent(CalendarEvent.default());
            })
            .then(response => AdministrationRequester.getActiveCalendarEvents())
            .then(response => dispatch(new AdministrationLoadedActiveCalendarAction(response)))
            .catch(error => { dispatch(new EndWIPInErrorAction("Erreur survenue lors de la suppression d'un event du calendrier")) });


    }

    const displayPopupForCreation = () => {
        setCreationMode(true);
        setDisplayEditPopup(true);
    }

    const displayPopupForEdition = (selectedEvent: CalendarEvent) => {
        setCreationMode(false);
        setWipCalendarEvent(selectedEvent);
        setDisplayEditPopup(true);
    }

    return <div>
        <Table striped>
            <thead>
                <tr className="table-primary text-uppercase">
                    <th>Icon</th>
                    <th>Description</th>
                    <th>Dates</th>
                </tr>
            </thead>
            {
                calendarEvents.map((anEvent, index) =>
                    <tr key={anEvent.id}
                        className={
                            CssTools.of("large-row")
                                .then(BootstrapText.AlignLeft)
                                .css()}
                        onClick={() => displayPopupForEdition(anEvent)}
                    >
                        <td>{anEvent.utf8IconsCode}</td>
                        <td className={
                            CssTools.of().if(administrationSubState.activeCalendarEvents?.some(activeEvent => activeEvent.id === anEvent.id)!, "fw-bolder")
                                .css()
                        }>{anEvent.description}</td>
                        <td>
                            {`${CalendarFixedDate.toDateString(anEvent.startDate)}`}
                            {anEvent.dayEvent ? "" : ` > ${CalendarFixedDate.toDateString(anEvent.endDate)}`}</td>
                    </tr>
                )
            }
        </Table>
        <ActionMenuComponent alreadyOpen>
            <ActionPlusButton onClick={() => displayPopupForCreation()} />
        </ActionMenuComponent>


        <Modal show={displayEditPopup} fullscreen={true} onHide={() => setDisplayEditPopup(false)}>
            <Modal.Header closeButton={!workInProgress}>
                <Modal.Title className="text-break-anywhere"> {creationMode ? "Création d'un nouvel event" : "Modification d'un event"}  </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <h5>Description</h5>
                <Form.Control value={wipCalendarEvent.description} onChange={(event) => {
                    setWipCalendarEvent({
                        ...wipCalendarEvent,
                        description: event.target.value
                    })
                }} disabled={workInProgress} />

                <h5>Icon</h5>
                <InputGroup>
                    <Button href="https://utf8-icons.com/subset/miscellaneous-symbols-and-pictographs"
                        target="_blank"
                        size="lg" className={CustomClassNames.VerticallyCentered}>
                        <BoxArrowUpRight className={CustomClassNames.SpaceAfterIcon} />
                        UTF8 icons
                    </Button>
                    <Form.Control value={wipCalendarEvent.utf8IconsCode}
                        size="lg"
                        onChange={(event) => {
                            setWipCalendarEvent({
                                ...wipCalendarEvent,
                                utf8IconsCode: event.target.value
                            })
                        }} disabled={workInProgress} />
                    <InputGroup.Text><h1>{wipCalendarEvent.utf8IconsCode}</h1></InputGroup.Text>
                </InputGroup>



                <h5>Dates</h5>
                <Form.Check // prettier-ignore
                    type="switch"
                    id="custom-switch"
                    label="Jour unique"
                    checked={wipCalendarEvent.dayEvent}
                    onChange={(event) => {
                        setWipCalendarEvent({
                            ...wipCalendarEvent,
                            dayEvent: event.target.checked
                        })
                    }}

                />
                <CalendarFixedDateEditor fixedDate={wipCalendarEvent.startDate} onChange={(newFixedDate) => {
                    setWipCalendarEvent({
                        ...wipCalendarEvent,
                        startDate: newFixedDate
                    })
                }} />
                <CalendarFixedDateEditor fixedDate={wipCalendarEvent.endDate} disabled={wipCalendarEvent.dayEvent} onChange={(newFixedDate) => {
                    setWipCalendarEvent({
                        ...wipCalendarEvent,
                        endDate: newFixedDate
                    })
                }} />
            </Modal.Body>
            <Modal.Footer>
                <PassiveBlockingButton variant={BootstrapVariants.Secondary} onClick={() => setDisplayEditPopup(false)} label="Annuler" />
                <PassiveBlockingButton
                    label="Supprimer"
                    variant={BootstrapVariants.Danger}
                    onClick={() => { setDisplayDeletePopup(true) }}
                    hidden={creationMode}
                />
                <BlockingButton label="Sauvegarder" variant={BootstrapVariants.Primary} onClick={() =>
                    updateCalendarEvent()}
                    hidden={creationMode}
                />
                <BlockingButton label="Créér" variant={BootstrapVariants.Primary} onClick={() =>
                    createCalendarEvent()}
                    hidden={!creationMode}
                />
            </Modal.Footer>
        </Modal>

        <YesNoModal
            displayYesNoPopup={displayDeletePopup}
            title="Supprimer un event"
            question={`Voulez-vous vraiment supprimer l'event ${wipCalendarEvent.description} ?`}
            onYesClick={() => { deleteEvent() }}
            onNoClick={() => { setDisplayDeletePopup(false) }}
            yesLabel="Supprimer"
            noLabel="Non"
        />
    </div>
}


export default ManageCalendarEvents;