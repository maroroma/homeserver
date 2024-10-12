import {FC, useEffect, useState} from "react";
import {Image} from "react-bootstrap";
import {QuestionCircle} from "react-bootstrap-icons";

import "./Thumb.css"

export type ThumbProps = {
    src: string
}

const Thumb: FC<ThumbProps> = ({ src }) => {

    const [displayErrorThumb, setDisplayErrorThumb] = useState(false)

    useEffect(() => {
        setDisplayErrorThumb(false);
    }, [src])

    return <>
        {displayErrorThumb ? <>
            <div className="thumb-default vertically-centered">
                <QuestionCircle size={"80px"} className="" /></div>
        </>
            : <Image src={src} className="thumb" rounded onError={() => setDisplayErrorThumb(true)}></Image>}
    </>
}

export default Thumb;