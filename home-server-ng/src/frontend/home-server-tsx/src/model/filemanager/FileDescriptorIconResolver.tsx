import {ReactElement} from "react";
import FileDescriptor from "./FileDescriptor";
import FileIconResolver from "./FileIconResolver";
import FileExtension from "./FileExtension";

export default class FileDescriptorIconResolver implements FileIconResolver {
    toIcon(fileDescriptor: FileDescriptor): ReactElement {
        return FileExtension.resolve(fileDescriptor).icon;
    }


}