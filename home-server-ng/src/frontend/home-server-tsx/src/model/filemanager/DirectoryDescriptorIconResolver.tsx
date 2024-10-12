import {ReactElement} from "react";
import FileDescriptor from "./FileDescriptor";
import FileIconResolver from "./FileIconResolver";
import {Folder} from "react-bootstrap-icons";
import {CustomClassNames} from "../../components/bootstrap/CssTools";

export default class DirectoryDescriptorIconResolver implements FileIconResolver {
    constructor() {
        this.toIcon = this.toIcon.bind(this);
    }
    public toIcon(fileDescriptor: FileDescriptor): ReactElement {
        return <Folder className={CustomClassNames.SpaceAfterIcon} />
    }

}