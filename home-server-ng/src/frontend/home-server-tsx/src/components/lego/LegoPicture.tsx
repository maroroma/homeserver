import {FC, useState} from "react"
import {Image} from "react-bootstrap";
import Brick from "../../model/lego/Brick";

import "./LegoPicture.css"


export type LegoPictureProps = {
    brick: Brick
}

const LegoPicture: FC<LegoPictureProps> = ({ brick }) => {

    const [usePictureId, setUsePictureId] = useState(true);



    return usePictureId ?
        <Image loading="lazy" src={Brick.pictureUrlFromId(brick)} onError={() => setUsePictureId(false)} className="lego-picture"/>
        : <Image src={brick.pictureUrl} loading="lazy" className="lego-picture"/>

}

export default LegoPicture;