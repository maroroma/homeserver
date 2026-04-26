import type { FC } from "react";
import { Button, InputGroup } from "react-bootstrap";
import CssTools from "../../tools/CssTools";
import { CloudUpload, DatabaseCheck, PencilSquare, Tags } from "react-bootstrap-icons";
import type { ButtonVariant } from "react-bootstrap/esm/types";
import type { FileInProgress } from "../../api/model/albumproject/FileInProgress";

type FileInProgressRendererProps = {
    fileInProgress: FileInProgress;
}

const FileInProgressRenderer: FC<FileInProgressRendererProps> = ({ fileInProgress }) => {

    const resolveVariant = (fileInProgress: FileInProgress, statusToDisplay: boolean, previousStatus: boolean): ButtonVariant => {
        if (fileInProgress.inError) {
            return "danger";
        }
        return statusToDisplay ? "success" : previousStatus === false ? "secondary" : "primary"
    }


    return (
        <InputGroup className="mb-3">

            <Button
                disabled
                className={CssTools.of().if(fileInProgress.uploaded === false, "blinkable").css()}
                variant={fileInProgress.inError ? "danger" : fileInProgress.uploaded ? "success" : "primary"}>
                <CloudUpload />
            </Button>
            <Button
                disabled
                className={CssTools.of().if(fileInProgress.renamed === false && fileInProgress.uploaded === true, "blinkable").css()}
                variant={resolveVariant(fileInProgress, fileInProgress.renamed, fileInProgress.uploaded)}>
                <PencilSquare />
            </Button>
            <Button
                disabled
                className={CssTools.of().if(fileInProgress.tagged === false && fileInProgress.renamed === true, "blinkable").css()}
                variant={resolveVariant(fileInProgress, fileInProgress.tagged, fileInProgress.renamed)}>
                <Tags />
            </Button>
            <Button
                disabled
                className={CssTools.of().if(fileInProgress.copied === false && fileInProgress.tagged === true, "blinkable").css()}
                variant={resolveVariant(fileInProgress, fileInProgress.copied, fileInProgress.tagged)}>
                <DatabaseCheck />
            </Button>
            <InputGroup.Text id="basic-addon3">
                {fileInProgress.fileWithNewName.newName}
            </InputGroup.Text>
        </InputGroup>
    )
}


export default FileInProgressRenderer;