import {ReactElement} from "react";
import FileDescriptor from "./FileDescriptor";
import FileIconResolver from "./FileIconResolver";
import {Download} from "react-bootstrap-icons";
import {CustomClassNames} from "../../components/bootstrap/CssTools";

export default class DownloadIconResolver implements FileIconResolver {
    constructor() {
        this.toIcon = this.toIcon.bind(this);
    }
    public toIcon(fileDescriptor: FileDescriptor|string): ReactElement {
        return <Download className={CustomClassNames.SpaceAfterIcon} />
    }

}