import {FC} from "react";
import {Button, Modal} from "react-bootstrap";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {BootstrapText} from "../bootstrap/BootstrapText";

export type YesNoModalProps = {
    displayYesNoPopup: boolean,
    question: string,
    title: string,
    onYesClick: () => void,
    onNoClick: () => void,
    yesLabel?: string,
    noLabel?: string,
}

const YesNoModal: FC<YesNoModalProps> = ({
    displayYesNoPopup,
    question,
    title,
    onYesClick,
    onNoClick,
    yesLabel = "Oui",
    noLabel = "Non"
}) => {


    return <Modal show={displayYesNoPopup} fullscreen={true} onHide={() => onNoClick()}>
        <Modal.Header>
            <Modal.Title className={BootstrapText.WordBreak}>{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <h1 className={BootstrapText.WordBreak}>{question}</h1>
        </Modal.Body>
        <Modal.Footer>
            <Button
                variant={BootstrapVariants.Danger}
                onClick={() => { onNoClick() }}
            >
                {noLabel}
            </Button>
            <Button
                variant={BootstrapVariants.Primary}
                onClick={() => { onYesClick() }}
            >
                {yesLabel}
            </Button>
        </Modal.Footer>
    </Modal>


}

export default YesNoModal;