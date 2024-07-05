import {FC, ReactElement} from "react";
import {Button, Modal} from "react-bootstrap";
import {ButtonVariant} from "react-bootstrap/esm/types";

export type PopupContainerComponentProps = {
    title: string,
    children: any,
    onCancel?: () => void,
    onAccept?: () => void,
    closeButton?: ReactElement | string,
    acceptButton?: ReactElement | string,
    acceptButtonVariant?: ButtonVariant,
    displayCloseButton?: boolean,
    displayApplyButton?: boolean
}

const PopupContainerComponent: FC<PopupContainerComponentProps> = ({
    children,
    title,
    onCancel = () => { },
    onAccept = () => { },
    closeButton = "Annuler",
    displayApplyButton = true,
    displayCloseButton = true,
    acceptButton = "Confirmer",
    acceptButtonVariant = "primary"
}) => {


    return <Modal fullscreen={true} show={true}>

        <Modal.Dialog fullscreen={true} >
            <Modal.Header>
                <Modal.Title>{title.toUpperCase()}</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                {children}
            </Modal.Body>

            <Modal.Footer>
                {displayCloseButton ?
                    <Button variant="secondary" size="lg" onClick={() => onCancel()}>{closeButton}</Button>
                    : <></>}

                {displayApplyButton ?
                    <Button variant={acceptButtonVariant}  size="lg" onClick={() => onAccept()}>{acceptButton}</Button> : <></>}
            </Modal.Footer>
        </Modal.Dialog>
    </Modal>





}

export default PopupContainerComponent;
