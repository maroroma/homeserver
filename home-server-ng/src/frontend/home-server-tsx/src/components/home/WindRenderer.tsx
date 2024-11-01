import {FC} from "react"
import {Wind} from "react-bootstrap-icons";

export type WindRendererProps = {
    windSpeed: number
}

const WindRenderer: FC<WindRendererProps> = ({ windSpeed }) => {
    return <h2><Wind /> { `${windSpeed} km/h` }</h2>
}

export default WindRenderer;