import type {FC, ReactElement} from "react";
import {Button, Modal} from "react-bootstrap";
import type {ButtonVariant} from "react-bootstrap/esm/types";

type YesNoModalProps = {
  show: boolean;
  onYes?: () => void;
  onNo?: () => void;
  yesLabel?: string;
  noLabel?: string;
  yesColor?:ButtonVariant;
  noColor?:ButtonVariant;
  title: string;
  message: ReactElement;
};

const YesNoModal: FC<YesNoModalProps> = ({
  show,
  onYes = () => {},
  onNo = () => {},
  yesLabel = "Oui",
  noLabel = "Non",
  title,
  message,
  yesColor = "success",
  noColor = "danger"
}) => {
  return (
    <Modal show={show} fullscreen onHide={() => onNo()}>
      <Modal.Header closeButton>
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>
      <Modal.Body>{message}</Modal.Body>
      <Modal.Footer>
        <Button variant={yesColor} onClick={() => onYes()}>{yesLabel}</Button>
        <Button variant={noColor} onClick={() => onNo()}>{noLabel}</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default YesNoModal;
