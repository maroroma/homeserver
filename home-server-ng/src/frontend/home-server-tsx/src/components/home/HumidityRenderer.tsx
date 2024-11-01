import {FC} from "react"
import {Water} from "react-bootstrap-icons";

export type HumidityRendererProps = {
    humidity: number
}

const HumidityRenderer: FC<HumidityRendererProps> = ({ humidity }) => {
    return <h2><Water /> { `${humidity} %` }</h2>
}

export default HumidityRenderer;