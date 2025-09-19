import type {FC} from "react";
import type {LibraryItemArts} from "../../api/model/library/LibraryItemArts";
import ListItemRenderer from "./ListItemRenderer";
import ThumbComponent from "../thumb/ThumbComponent";
import type {ListItemRendererProps} from "./ListItemRendererProps";

import "./ListItemRenderer.css";
import type {ThumbSize} from "../thumb/ThumbProps";

type LibraryListItemRendererProps = {
  libraryItemArts: LibraryItemArts;
  sizeOnScroll?: ThumbSize;
  size?:ThumbSize;
};

const LibraryListItemRenderer: FC<
  LibraryListItemRendererProps & ListItemRendererProps
> = (props) => {
  return ListItemRenderer({
    ...props,
    thumb: <ThumbComponent size={props.size} libraryItemArts={props.libraryItemArts} sizeOnScroll={props.sizeOnScroll} />,
  });
};

export default LibraryListItemRenderer;
