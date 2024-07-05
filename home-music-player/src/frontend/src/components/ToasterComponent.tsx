import {FC} from "react";
import {Toast, ToastBody, ToastContainer} from "react-bootstrap";
import {useMusicPlayerContext} from "../state/MusicPlayerContext";
import {CloseToastAction} from "../state/actions/toastActions/CloseToastAction";
import {ToastMessage} from "../state/ToastState";

import "./ToasterComponent.css";

const ToasterComponent: FC = () => {

    const { toastState, dispatch } = useMusicPlayerContext();


    const convertMessageTypeToBg = (message: ToastMessage) => {
        if (message.type === "Info") {
            return "info";
        }

        if (message.type === "Error") {
            return "danger"
        }

        if (message.type === "Warning") {
            return "warning"
        }
    }


    return <>
        <ToastContainer position="top-center">
            {
                toastState.messages.map((aToastMessage, toastIndex) =>
                    <Toast
                        autohide={!aToastMessage.persistent}
                        animation={true}
                        delay={2000}
                        key={`toastIndex-${toastIndex}`}
                        bg={convertMessageTypeToBg(aToastMessage)}
                        onClose={() => dispatch(new CloseToastAction(aToastMessage))}>
                        <ToastBody>{aToastMessage.message}</ToastBody>
                    </Toast>)
            }
        </ToastContainer>
    </>

}


export default ToasterComponent;
