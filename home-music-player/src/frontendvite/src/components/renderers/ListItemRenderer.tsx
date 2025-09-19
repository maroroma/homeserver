import type {FC} from "react";

import "./ListItemRenderer.css";
import {Stack} from "react-bootstrap";
import type {ListItemRendererProps} from "./ListItemRendererProps";


const ListItemRenderer: FC<ListItemRendererProps> = ({
  label,
  thumb,
  onClick = () => {},
}) => {
  return (
    <div className="list-item-renderer clickable" onClick={() => onClick()}>
      <Stack direction="horizontal">
        <div>{thumb}</div>
        <div className="label">{label}</div>
      </Stack>
    </div>
  );
};

export default ListItemRenderer;
