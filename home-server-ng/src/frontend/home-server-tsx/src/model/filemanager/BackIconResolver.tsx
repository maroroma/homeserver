import {ReactElement} from "react";
import FileDescriptor from "./FileDescriptor";
import FileIconResolver from "./FileIconResolver";
import {Arrow90degUp} from "react-bootstrap-icons";
import {CustomClassNames} from "../../components/bootstrap/CssTools";

export default class BackIconResolver implements FileIconResolver {
    constructor() {
        this.toIcon = this.toIcon.bind(this);
    }
    public toIcon(fileDescriptor: FileDescriptor): ReactElement {
        return <Arrow90degUp className={CustomClassNames.SpaceAfterIcon} />
    }

}