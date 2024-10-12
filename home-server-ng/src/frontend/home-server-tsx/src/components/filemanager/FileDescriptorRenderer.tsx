import {FC} from "react";
import {Form, ListGroupItem} from "react-bootstrap";
import CssTools from "../bootstrap/CssTools";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import SelectableItem from "../../model/SelectableItem";
import FileDescriptor from "../../model/filemanager/FileDescriptor";
import FileIconResolver from "../../model/filemanager/FileIconResolver";


export type FileDescriptorRendererProps = {
    selectableFileDescriptor: SelectableItem<FileDescriptor>,
    onCheck: (selectableFileDescriptor: SelectableItem<FileDescriptor>) => void,
    onClick: (selectableFileDescriptor: SelectableItem<FileDescriptor>) => void,
    fileIconResolver: FileIconResolver
}


const FileDescriptorRenderer: FC<FileDescriptorRendererProps> = ({ selectableFileDescriptor, onCheck, onClick, fileIconResolver }) => {

    return <ListGroupItem
        action
        variant={selectableFileDescriptor.map(BootstrapVariants.Success, BootstrapVariants.None)}
    >
        <div className="file-descriptor">
            <div className="h3">
                <Form.Check
                    type="checkbox"
                    size={30}
                    inline={true}
                    disabled={selectableFileDescriptor.item.isProtected}
                    checked={selectableFileDescriptor.selected}
                    onChange={(event) => onCheck(selectableFileDescriptor)}
                />
            </div>
            <div className={CssTools.of("h3").then("file-descriptor-icon").uppercase().css()} onClick={() => { onClick(selectableFileDescriptor) }}>
                {fileIconResolver.toIcon(selectableFileDescriptor.item)}
            </div>
            <div className={CssTools.of("h3").uppercase().css()} onClick={() => { onClick(selectableFileDescriptor) }}>
                {selectableFileDescriptor.item.name}
            </div>
        </div>
    </ListGroupItem>
}

export default FileDescriptorRenderer;