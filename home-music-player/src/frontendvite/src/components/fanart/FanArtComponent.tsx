import type {FC} from "react";

import "./FanArtComponent.css";
import type {LibraryItemArts} from "../../api/model/library/LibraryItemArts";
import {Image} from "react-bootstrap";
import CssTools from "../../tools/CssTools";

type FanArtComponentProps = {
  fanart: LibraryItemArts | undefined;
  mode?:"background"
};

const FanArtComponent: FC<FanArtComponentProps> = ({ fanart, mode = "background" }) => {
  if (
    !fanart ||
    fanart === null ||
    fanart.fanartPath === null ||
    !fanart.fanartPath
  ) {
    return <></>;
  }

  return (
    <div className={CssTools.of("fanart-panel").then(`fanart-mode-${mode}`).css()}>
      <Image
        src={`/api/musicplayer/localresources/fanarts/${fanart.fanartPath}`}
        loading="lazy"
        className="fanart"
      />
    </div>
  );
};

export default FanArtComponent;
