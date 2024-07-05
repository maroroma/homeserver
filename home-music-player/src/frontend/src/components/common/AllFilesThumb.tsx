import {FC} from "react";
import {ThumbImageType} from "./ThumbImage";
import {List} from "react-bootstrap-icons";

import "./ThumbImage.css"
import "./AllFilesThumb.css"


export type AllFilesThumbProps = {
    type?: ThumbImageType,
    className?: string,
    bordered?: boolean
}




const AllFilesThumb: FC<AllFilesThumbProps> = ({ type = "small", className, bordered = true }) => {
    return <List className={`${type}-thumb-default ${className} ${bordered ? "" : "all-files-thumb-no-border"}`} size={130} />
};


export default AllFilesThumb;
