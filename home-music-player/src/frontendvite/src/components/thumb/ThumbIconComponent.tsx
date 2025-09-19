import "./ThumbComponent.css";
import type {Icon} from "react-bootstrap-icons";
import type {ThumbProps} from "./ThumbProps";
import type {FC} from "react";

type ThumbIconComponentProps = {
    icon: React.ReactElement<Icon>
}


const ThumbIconComponent : FC<ThumbIconComponentProps & ThumbProps> = ({size = "small", icon}) => {
    return <icon.type className={`thumb-${size}`}/>
}

export default ThumbIconComponent;