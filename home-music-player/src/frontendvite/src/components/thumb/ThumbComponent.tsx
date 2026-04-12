import type { FC } from "react";
import type { LibraryItemArts } from "../../api/model/library/LibraryItemArts";
import { Image } from "react-bootstrap";
import { Question } from "react-bootstrap-icons";

import "./ThumbComponent.css";
import type { ThumbProps, ThumbSize } from "./ThumbProps";
import ThumbIconComponent from "./ThumbIconComponent";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import CssTools from "../../tools/CssTools";

type ThumbComponentProps = {
  libraryItemArts: LibraryItemArts;
  rounded?: boolean;
  sizeOnScroll?: ThumbSize;
};

const ThumbComponent: FC<ThumbComponentProps & ThumbProps> = ({
  libraryItemArts,
  size = "small",
  rounded = true,
  sizeOnScroll,
}) => {
  const { isScrollOnTop } = useMusicPlayerContext();

  if (
    (libraryItemArts?.thumbPath === undefined ||
      libraryItemArts.thumbPath === null)
    && (libraryItemArts.albumId === undefined || libraryItemArts.albumId === null)
  ) {
    return <ThumbIconComponent size={size} icon={<Question />} />;
  }
  return (
    <Image
      src={ libraryItemArts.thumbPath ? `/api/musicplayer/localresources/thumbs/${libraryItemArts.thumbPath}` : `/api/musicplayer/localresources/albums/${libraryItemArts.albumId}/thumb`}
      loading="lazy"
      className={CssTools.of(`thumb-${size}`)
        .if(!isScrollOnTop && sizeOnScroll !== undefined, `thumb-${sizeOnScroll}`)
        .defined(sizeOnScroll, "thumb-scroll-transitioning")
        .css()}
      rounded={rounded}
    />
  );
};

export default ThumbComponent;
