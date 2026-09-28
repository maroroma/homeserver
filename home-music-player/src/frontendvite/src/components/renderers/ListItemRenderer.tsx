import type { FC } from "react";

import "./ListItemRenderer.css";
import { Stack } from "react-bootstrap";
import type { ListItemRendererProps } from "./ListItemRendererProps";
import { CloudDownload } from "react-bootstrap-icons";
import ThumbIconComponent from "../thumb/ThumbIconComponent";


const ListItemRenderer: FC<ListItemRendererProps> = ({
  label,
  thumb,
  downloadTitle = "",
  enableDownload = false,
  downloadLink = "",
  size,
  onClick = () => { },
}) => {
  return (
    <>
      {
        enableDownload
          ? <div className="list-item-renderer clickable">
            <Stack direction="horizontal">
              <a href={downloadLink} download={downloadTitle}>
                <ThumbIconComponent icon={<CloudDownload />} size={size} />
              </a>
              <div className="label">{label}</div>
            </Stack>
          </div>
          : <div className="list-item-renderer clickable" onClick={() => onClick()}>
            <Stack direction="horizontal">
              <div>{thumb}</div>
              <div className="label">{label}</div>
            </Stack>
          </div>
      }
    </>
  );
};

export default ListItemRenderer;
