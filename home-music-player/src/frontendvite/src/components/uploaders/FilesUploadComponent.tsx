import { useEffect, useRef, useState, type FC } from "react";
import { Button, FloatingLabel, Form, InputGroup } from "react-bootstrap";
import ThumbIconComponent from "../thumb/ThumbIconComponent";
import { FileEarmarkPlus, Trash, type Icon } from "react-bootstrap-icons";
import CssTools from "../../tools/CssTools";

import "./ImageUploadComponent.css"

type FilesUploadComponentProps = {
    title: string;
    icon?: React.ReactElement<Icon>;
    onFilesUpdated?: (updatedFiles: FileWithNewName[]) => void
}

export type FileWithNewName = {
    file: File|undefined,
    newName: string
}

const FilesUploadComponent: FC<FilesUploadComponentProps> = ({ title, icon = <FileEarmarkPlus />, onFilesUpdated = () => { } }) => {

    const fileInputRef = useRef<HTMLInputElement>(null);

    const [filesToUpload, setFilesToUpload] = useState<FileWithNewName[]>([]);

    const updateFilesToUpload = (files: File[]) => {
        const filesWithNewName = files.map(aFile => {
            return {
                file: aFile,
                newName: aFile.name
            }
        });

        setFilesToUpload(filesWithNewName);
    }

    const updateOneFileName = (input: FileWithNewName, newName: string) => {
        setFilesToUpload(
            filesToUpload.map(aFile => {
                if (aFile.file?.name === input.file?.name) {
                    aFile.newName = newName;
                }
                return aFile;
            })
        )
    }

    const removeOneFile = (input: FileWithNewName) => {
        setFilesToUpload(
            filesToUpload.filter(aFile =>
                aFile.file?.name !== input.file?.name
            )
        );
    }

    useEffect(() => {
        onFilesUpdated(filesToUpload);
    }, [filesToUpload])

    return <>

        <Form.Group>
            <Form.Label>{title}</Form.Label>
        </Form.Group>
        <div onClick={() => { fileInputRef.current?.click() }} className={CssTools.of().clickable().css()}>
            <ThumbIconComponent
                icon={icon}
                size="xsmall"
                className={CssTools.of().ifElse(filesToUpload.length === 0, "red", "green").css()} />
        </div>

        <input ref={fileInputRef} multiple type="file" className="hidden-input-file" onChange={(event: any) => updateFilesToUpload(Array.from(event.target.files))} />

        <div className={CssTools.of().defaultPadding().css()}>
            {filesToUpload.map(aFile =>
                <InputGroup key={aFile.file?.name}>
                    <FloatingLabel
                        key={aFile.file?.name}
                        label={aFile.file?.name}
                        className="mb-3"
                    >
                        <Form.Control placeholder={aFile.file?.name} value={aFile.newName} onChange={(event) => updateOneFileName(aFile, event.target.value)} />
                    </FloatingLabel>
                    <Button className="mb-3" variant="danger" onClick={() => removeOneFile(aFile)}><Trash /></Button>
                </InputGroup>
            )}
        </div>
    </>
}


export default FilesUploadComponent;