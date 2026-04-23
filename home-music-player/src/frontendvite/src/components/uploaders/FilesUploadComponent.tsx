import { useRef, useState, type FC } from "react";
import { Form } from "react-bootstrap";
import ThumbIconComponent from "../thumb/ThumbIconComponent";
import { FileEarmarkPlus, type Icon } from "react-bootstrap-icons";
import CssTools from "../../tools/CssTools";

import "./ImageUploadComponent.css"

type FilesUploadComponentProps = {
    title: string;
    icon?: React.ReactElement<Icon>;
    onImageAsBase64Loaded?: (imageAsBase64: string) => void
}

type FileWithNewName = {
    file: File,
    newName: string
}

const FilesUploadComponent: FC<FilesUploadComponentProps> = ({ title, icon = <FileEarmarkPlus />, onImageAsBase64Loaded = () => { } }) => {

    const fileInputRef = useRef<HTMLInputElement>(null);

    const [filesToUpload, setFilesToUpload] = useState<FileWithNewName[]>([]);


    const updateFilesToUpload = (files: File[]) => {
        const filesWithNewName = files.map(aFile => {
            return {
                file: aFile,
                newName: ""
            }
        });

        setFilesToUpload(filesWithNewName);
    }

    return <>

        <Form.Group>
            <Form.Label>{title}</Form.Label>
        </Form.Group>
        <div onClick={() => { fileInputRef.current?.click() }} className={CssTools.of().clickable().css()}>
            <ThumbIconComponent icon={icon} size="small" className="red" />
        </div>

        <input ref={fileInputRef} multiple type="file" className="hidden-input-file" onChange={(event: any) => updateFilesToUpload(Array.from(event.target.files))} />

        {filesToUpload.map(aFile => <>{aFile.file.name}</>)}
    </>
}


export default FilesUploadComponent;