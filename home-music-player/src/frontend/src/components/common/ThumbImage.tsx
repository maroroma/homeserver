import {FC} from "react";
import {LibraryItemArts} from "../../api/model/library/LibraryItemArts";
import {Image} from "react-bootstrap";
import {Question} from "react-bootstrap-icons";

import "./ThumbImage.css"


export type ThumbImageType = "small" | "description" | "xsmall" | "large"

export type ThumbImageProps = {
    libraryItemArts?: LibraryItemArts,
    type?: ThumbImageType,
    className?: string,
    rounded?: boolean
}


const ThumbImage: FC<ThumbImageProps> = ({
    libraryItemArts,
    type = "small",
    className = "",
    rounded = false
}) => {

    if ((libraryItemArts?.thumbPath === undefined || libraryItemArts.thumbPath === null)) {
        return <Question className={`${type}-thumb-default ${className} ${type === "description" ? "card-img-top" :""}` } />
    }


    return <Image
        className={`${type}-thumb ${className} ${type === "description" ? "card-img-top" :""}`}
        rounded={rounded}
        src={`musicplayer/localresources/thumbs/${libraryItemArts.thumbPath}`}>
    </Image>

}



export default ThumbImage