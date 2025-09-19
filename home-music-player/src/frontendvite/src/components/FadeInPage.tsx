/* eslint-disable @typescript-eslint/no-explicit-any */
import type {FC} from "react";
import type {LibraryItemArts} from "../api/model/library/LibraryItemArts";

import "./FadeInPage.css";
import CssTools from "../tools/CssTools";
import LibraryListItemRenderer from "./renderers/LibraryListItemRenderer";
import type {Icon} from "react-bootstrap-icons";
import IconListItemRenderer from "./renderers/IconListItemRenderer";

type FadeInPageProps = {
  children: any;
  label?: string;
  libraryItemArts?: LibraryItemArts;
  icon?: React.ReactElement<Icon>;
  onClick?: () => void;
};

const FadeInPage: FC<FadeInPageProps> = ({
  children,
  label,
  libraryItemArts,
  icon,
  onClick,
}) => {
  return (
    <div className="fadein-page">
      {label && libraryItemArts ? (
        <div className="fixed-header">
          <LibraryListItemRenderer
            label={label}
            libraryItemArts={libraryItemArts}
            onClick={onClick}
            sizeOnScroll="xsmall"
          />
        </div>
      ) : (
        <></>
      )}

      {label && icon ? (
        <div className="fixed-header">
          <IconListItemRenderer
            label={label}
            icon={icon}
            onClick={onClick}
          />
        </div>
      ) : (
        <></>
      )}



      <div className={CssTools.of().defined(label, "as-body").css()}>
        {children}
      </div>
    </div>
  );
};

export default FadeInPage;
