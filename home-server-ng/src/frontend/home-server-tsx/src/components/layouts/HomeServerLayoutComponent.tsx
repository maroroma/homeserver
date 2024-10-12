import {FC, useEffect, useState} from "react";
import {Outlet} from "react-router-dom";
import MenuComponent from "../menu/MenuComponent";

import "./HomeServerLayoutComponent.css";
import {Toast, ToastContainer} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";

const HomeServerLayoutComponent: FC = () => {

    const { toastSubState } = useHomeServerContext();

    const [displayToast, setDisplayToast] = useState(false);

    useEffect(() => {
        setDisplayToast(toastSubState.toastMessage !== "")
    }, [toastSubState]);


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