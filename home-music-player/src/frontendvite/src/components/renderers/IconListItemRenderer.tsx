import type {FC} from "react";
import ListItemRenderer from "./ListItemRenderer";
import type {ListItemRendererProps} from "./ListItemRendererProps";

import "./ListItemRenderer.css";
import type {Icon} from "react-bootstrap-icons";
import ThumbIconComponent from "../thumb/ThumbIconComponent";
import type {ThumbProps} from "../thumb/ThumbProps";

type IconListItemRendererProps = {
  icon: React.ReactElement<Icon>;
};

const IconListItemRenderer: FC<
  IconListItemRendererProps & ListItemRendererProps & ThumbProps
> = (props) => {
  return ListItemRenderer({
    ...props,
    thumb: <ThumbIconComponent icon={props.icon} size={props.size}/>,
  });
};

export default IconListItemRenderer;
