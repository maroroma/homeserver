import {FC, useEffect, useState} from "react";
import {CloseButton, Form, Modal} from "react-bootstrap";
import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import EditableTextFile from "../../../model/filemanager/EditableTextFile";
import FileManagerRequester from "../../../api/FileManagerRequester";

import "./TextEditorModal.css";
import PassiveBlockingButton from "../../blockingbutton/PassiveBlockingButton";
import {BootstrapVariants} from "../../bootstrap/BootstrapVariants";
import BlockingButton from "../../blockingbutton/BlockingButton";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import EndWIPAction from "../../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";

export type TextEditorModalProps = {
  show: boolean;
  onHide: () => void;
  textToEdit: FileDescriptor;
};

const TextEditorModal: FC<TextEditorModalProps> = ({
  show,
  onHide,
  textToEdit,
}) => {
  const { workInProgress, dispatch } = useHomeServerContext();

  const [editableFile, setEditableFile] = useState(EditableTextFile.empty());

  useEffect(() => {
    FileManagerRequester.getEditableText(textToEdit).then((response) =>
      setEditableFile(response)
    );
  }, [textToEdit]);

  const saveFile = () => {
    FileManagerRequester.saveEditableText(editableFile)
      .then((response) => {
        dispatch(new EndWIPAction("Sauvegarde terminée"));
        setEditableFile(response);
      })
      .catch((error) =>
        dispatch(
          new EndWIPInErrorAction(
            "Erreur recontrée lors de la sauvegarde du fichier"
          )
        )
      );
  };

  return (
    <Modal show={show} fullscreen={true} onHide={() => onHide()}>
      <Modal.Body>
        <Form.Group className="text-editor-group">
          <Form.Label>Edition de {editableFile.fileDescriptor.name}</Form.Label>
          <Form.Control
            className="text-editor-textarea"
            as="textarea"
            value={editableFile.content}
            onChange={(event) =>
              setEditableFile({
                ...editableFile,
                content: event.target.value,
              })
            }
          ></Form.Control>

          <div className="image-viewer-close-button">
            <CloseButton onClick={() => onHide()} disabled={workInProgress} />
          </div>
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
        <PassiveBlockingButton
          variant={BootstrapVariants.Secondary}
          onClick={() => onHide()}
          label="Annuler"
          disabled={workInProgress}
        />
        <BlockingButton
          label="Sauvegarder"
          variant={BootstrapVariants.Primary}
          onClick={() => {saveFile()}}
          toastMessage="Sauvegarde En cours"
        />
      </Modal.Footer>
    </Modal>
  );
};

export default TextEditorModal;
