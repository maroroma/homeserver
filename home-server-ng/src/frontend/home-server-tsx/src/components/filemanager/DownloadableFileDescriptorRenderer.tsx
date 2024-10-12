import {FC} from "react";
import {Form, ListGroupItem} from "react-bootstrap";
import CssTools from "../bootstrap/CssTools";
import SelectableItem from "../../model/SelectableItem";
import FileDescriptor from "../../model/filemanager/FileDescriptor";
import FileIconResolver from "../../model/filemanager/FileIconResolver";


export type DownloadableFileDescriptorRendererProps = {
    selectableFileDescriptor: SelectableItem<FileDescriptor>,
    fileIconResolver: FileIconResolver,
    disabled?: boolean
}


const DownloadableFileDescriptorRenderer: FC<DownloadableFileDescriptorRendererProps> = ({ selectableFileDescriptor, disabled = false, fileIconResolver }) => {

    return <ListGroupItem
        action={!disabled}
        download={selectableFileDescriptor.item.name}
        href={FileDescriptor.downloadUrl(selectableFileDescriptor.item)}
    // variant={selectableFileDescriptor.map(BootstrapVariants.Success, BootstrapVariants.None)}
    >
        <div className="file-descriptor">
            <div className="h3">
                <Form.Check
                    type="checkbox"
                    size={30}
                    inline={true}
                    disabled={true}
                    checked={false}
                />
            </div>
            <div className={CssTools.of("h3").then("file-descriptor-icon").if(disabled, "text-dark").uppercase().css()}>
                {fileIconResolver.toIcon(selectableFileDescriptor.item)}
            </div>
            <div className={CssTools.of("h3").if(disabled, "text-dark").uppercase().css()}>
                {selectableFileDescriptor.item.name}
            </div>
        </div>
    </ListGroupItem>
}

export default DownloadableFileDescriptorRenderer;