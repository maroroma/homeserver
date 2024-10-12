import {FC, useEffect, useState} from "react";
import {AdministrationRequester} from "../../api/AdministrationRequester";
import {Form, Modal, Table} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {AdministrationLoadedPropertiesAction} from "../../context/actions/administration/AdministrationLoadedPropertiesAction";
import {HomeServerProperty} from "../../model/administration/HomeServerProperty";
import BlockingButton from "../blockingbutton/BlockingButton";
import PassiveBlockingButton from "../blockingbutton/PassiveBlockingButton";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const AdministractionComponent: FC = () => {


    const { administrationSubState, workInProgress, searchString, dispatch } = useHomeServerContext();

    const [editedProperty, setEditedProperty] = useState(HomeServerProperty.empty())

    const [displayEditionPopup, setDisplayEditionPopup] = useState(false);

    const [filteredProperties, setFilteredProperty] = useState<HomeServerProperty[]>([]);

    useEffect(() => {
        AdministrationRequester.getAllProperties()
            .then(response => dispatch(new AdministrationLoadedPropertiesAction(response)))
            .catch(response => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la récupération des propriétés")))
    }, []);

    const selectPropertyToEdit = (property: HomeServerProperty) => {
        setEditedProperty({ ...property });
        setDisplayEditionPopup(true);
    }

    const updatePropertyValue = (newValue: string) => {
        setEditedProperty({
            ...editedProperty,
            value: newValue
        });
    }

    const saveProperty = () => {
        AdministrationRequester.saveOneProperty(editedProperty)
            .then(response => AdministrationRequester.getAllProperties())
            .then(response => {
                dispatch(new AdministrationLoadedPropertiesAction(response))
                setDisplayEditionPopup(false);
                setEditedProperty(HomeServerProperty.empty());
            })
            .catch(response => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la suppresion ou de la récupération des propriétés")))
    }

    useEffect(() => {

        if (searchString !== "") {
            setFilteredProperty(administrationSubState.properties.filter(HomeServerProperty.filter(searchString)))
        } else {
            setFilteredProperty([...administrationSubState.properties])
        }

    }, [administrationSubState, searchString])



    return <div>
        <Table striped>
            <thead>
                <tr className="table-primary text-uppercase">
                    <th>Module</th>
                    <th>id</th>
                    <th>description</th>
                </tr>
            </thead>
            {filteredProperties.map((aProperty, index) =>
                <tr key={index}
                    className="text-start large-row"
                    onClick={() => selectPropertyToEdit(aProperty)}>
                    <td>{aProperty.id.split(".")[1]}</td>
                    <td>{aProperty.id.split(".")[2]}</td>
                    <td className="fst-italic">{aProperty.description}</td>
                </tr>
            )}
        </Table>

        <Modal show={displayEditionPopup} fullscreen={true} onHide={() => setDisplayEditionPopup(false)}>
            <Modal.Header closeButton={!workInProgress}>
                <Modal.Title className="text-break-anywhere">Edition de la propriété <strong>{editedProperty.id}</strong></Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p className="fst-italic">{editedProperty.description}</p>
                <Form.Control value={editedProperty.value} onChange={(event) => updatePropertyValue(event.target.value)} disabled={workInProgress} />
            </Modal.Body>
            <Modal.Footer>
                <PassiveBlockingButton variant={BootstrapVariants.Secondary} onClick={() => setDisplayEditionPopup(false)} label="Annuler" />
                <BlockingButton label="Sauvegarder" variant={BootstrapVariants.Primary} onClick={() => saveProperty()} />
            </Modal.Footer>
        </Modal>

    </div>
}

export default AdministractionComponent;