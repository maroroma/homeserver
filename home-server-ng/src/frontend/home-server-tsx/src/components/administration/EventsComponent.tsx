import {FC, useEffect, useState} from "react";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {AdministrationRequester} from "../../api/AdministrationRequester";
import {AdministrationLoadedEventsAction} from "../../context/actions/administration/AdministrationLoadedEventsAction";
import {Table} from "react-bootstrap";
import PeristentNotification from "../../model/administration/PeristentNotification";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionDeleteButton from "../actionmenu/ActionDeleteButton";
import ActionUpdateButton from "../actionmenu/ActionUpdateButton";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import StartWIPAction from "../../context/actions/StartWIPAction";


const EventsComponent: FC = () => {

    const { administrationSubState, searchString, dispatch } = useHomeServerContext();

    const [filteredEvents, setFilteredEvents] = useState<PeristentNotification[]>([]);

    useEffect(() => {
        reloadEvents();
    }, [])

    const reloadEvents = () => {
        dispatch(new StartWIPAction("Chargements des events"))
        AdministrationRequester.getAllEvents()
            .then(response => {
                dispatch(new AdministrationLoadedEventsAction(response))
            })
            .catch(error => dispatch(new EndWIPInErrorAction("erreur recontrée lors du chargement des events")))
    }

    useEffect(() => {

        if (searchString !== "") {
            setFilteredEvents(administrationSubState.allLogEvents.persistantNotifications.filter(aProperty => aProperty.title.toLocaleLowerCase().includes(searchString.toLocaleLowerCase())))
        } else {
            setFilteredEvents([...administrationSubState.allLogEvents.persistantNotifications])
        }

    }, [administrationSubState.allLogEvents, searchString])

    const deleteEvents = () => {
        AdministrationRequester.deleteAllEvents(administrationSubState.allLogEvents)
            .then(response => AdministrationRequester.getAllEvents())
            .then(response => dispatch(new AdministrationLoadedEventsAction(response)))
            .catch(error => dispatch(new EndWIPInErrorAction("erreur recontrée lors de la suppression des events")))
    };

    if (filteredEvents.length === 0) {
        return <><h1>Aucun events</h1>
            <ActionMenuComponent alreadyOpen={true}>
                <ActionUpdateButton blockingButton={true} onClick={() => reloadEvents()} toastMessage="Rechargement des events en cours"/>
            </ActionMenuComponent>
        </>
    }


    return <div>

        <Table striped>
            <thead>
                <tr className="table-primary text-uppercase">
                    <th>timestamp</th>
                    <th>title</th>
                    <th>message</th>
                </tr>
            </thead>
            {filteredEvents.map((anEvent, index) =>
                <tr key={index}
                    className="text-start large-row"
                    onClick={() => { }}>
                    <td>{anEvent.creationDate}</td>
                    <td>{anEvent.title}</td>
                    <td className="text-break-anywhere">{anEvent.message}</td>
                </tr>
            )}
        </Table>

        <ActionMenuComponent alreadyOpen={true}>
            <ActionUpdateButton blockingButton={true} onClick={() => reloadEvents()} toastMessage="Rechargement des events en cours"/>
            <ActionDeleteButton blockingButton={true} onClick={() => deleteEvents()} toastMessage="Suppression des events en cours"/>
        </ActionMenuComponent>

    </div>
}

export default EventsComponent;