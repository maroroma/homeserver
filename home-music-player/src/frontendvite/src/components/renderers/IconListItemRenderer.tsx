import type { FC } from "react";
import ListItemRenderer from "./ListItemRenderer";
import type { ListItemRendererProps } from "./ListItemRendererProps";

import "./ListItemRenderer.css";
import type { Icon } from "react-bootstrap-icons";
import ThumbIconComponent from "../thumb/ThumbIconComponent";
import type { ThumbProps, ThumbSize } from "../thumb/ThumbProps";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import CssTools from "../../tools/CssTools";

type IconListItemRendererProps = {
  icon: React.ReactElement<Icon>;
  sizeOnScroll?: ThumbSize;
};

const IconListItemRenderer: FC<
  IconListItemRendererProps & ListItemRendererProps & ThumbProps
> = (props) => {

  const { isScrollOnTop } = useMusicPlayerContext();


  return ListItemRenderer({
    ...props,
    thumb: <ThumbIconComponent
      icon={props.icon}
      size={!isScrollOnTop && props.sizeOnScroll !== undefined ? props.sizeOnScroll : props.size}
      className={CssTools.of().defined(props.sizeOnScroll, "thumb-scroll-transitioning").css()} />,
  });
};

export default IconListItemRenderer;
