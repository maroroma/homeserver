import {ReactElement} from "react";
import FileDescriptor from "./FileDescriptor";

export default interface FileIconResolver {
    toIcon(fileDescriptor: FileDescriptor): ReactElement;
}