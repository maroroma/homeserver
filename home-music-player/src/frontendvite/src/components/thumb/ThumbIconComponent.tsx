import "./ThumbComponent.css";
import type {Icon} from "react-bootstrap-icons";
import type {ThumbProps} from "./ThumbProps";
import type {FC} from "react";
import CssTools from "../../tools/CssTools";

type ThumbIconComponentProps = {
    icon: React.ReactElement<Icon>
    className?: string
}


const ThumbIconComponent : FC<ThumbIconComponentProps & ThumbProps> = ({size = "small", icon, className = ""}) => {
    return <icon.type className={CssTools.of(`thumb-${size}`).then(className).css()}/>
}

export default ThumbIconComponent;