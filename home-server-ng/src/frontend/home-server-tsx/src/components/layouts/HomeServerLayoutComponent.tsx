import {FC, useEffect, useState} from "react";
import {Outlet} from "react-router-dom";
import MenuComponent from "../menu/MenuComponent";

import "./HomeServerLayoutComponent.css";
import {Toast, ToastContainer} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {AdministrationRequester} from "../../api/AdministrationRequester";
import AdministrationLoadedActiveCalendarAction from "../../context/actions/administration/AdministrationLoadedActiveCalendarAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";

const HomeServerLayoutComponent: FC = () => {

    const { toastSubState, administrationSubState, dispatch } = useHomeServerContext();

    const [displayToast, setDisplayToast] = useState(false);

    useEffect(() => {
        setDisplayToast(toastSubState.toastMessage !== "")
    }, [toastSubState]);

    useEffect(() => {
        if (administrationSubState.activeCalendarEvents === undefined) {
            AdministrationRequester.getActiveCalendarEvents()
            .then(response => dispatch(new AdministrationLoadedActiveCalendarAction(response)))
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement des events du jour")))
        }
    }, [administrationSubState])


    return <div className="home-server-layout" data-bs-theme="dark">
        <MenuComponent />
        <Outlet></Outlet>
        <ToastContainer position="middle-end">
            <Toast autohide={true} delay={3000} show={displayToast} bg={toastSubState.variant} onClose={() => setDisplayToast(false)}>
                <Toast.Body className="text-white">{toastSubState.toastMessage}</Toast.Body>
            </Toast>
        </ToastContainer>
    </div>
}

export default HomeServerLayoutComponent;