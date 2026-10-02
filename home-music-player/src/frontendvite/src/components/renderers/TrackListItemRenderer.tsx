import type { FC } from "react";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";

import "./ListItemRenderer.css";
import type { ThumbSize } from "../thumb/ThumbProps";
import { Track } from "../../api/model/library/Track";
import IconListItemRenderer from "./IconListItemRenderer";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";
import { NameTransformer } from "../../tools/NameTransformer";
import { BookmarkPlus, Play, type Icon } from "react-bootstrap-icons";
import LibraryListItemRenderer from "./LibraryListItemRenderer";
import EqualizerComponent from "../thumb/EqualizerComponent";
import { Stack } from "react-bootstrap";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";

export type TrackDisplayMode = "Play" | "Download" | "Favorite" | "LibraryItemArts"

export type TrackListItemRendererProps = {
  track: Track;
  displayMode: TrackDisplayMode
  size?: ThumbSize;
  onPlay: () => void;
  onAddToFavorite?: () => void;
};

const TrackListItemRenderer: FC<
  TrackListItemRendererProps
> = (props) => {

  const { playerStatus } = useMusicPlayerContext();

  const resolveIcon = (displayMode: TrackDisplayMode): React.ReactElement<Icon> => {
    switch (displayMode) {
      case "Play": return <Play />
      case "Favorite": return <BookmarkPlus />
    }
    return <Play />
  }



  return <Stack direction="horizontal">
    {playerStatus.track.id === props.track.id ? <EqualizerComponent /> : <></>}
    {
      props.displayMode === "Download" || props.displayMode === "Favorite" || props.displayMode === "Play" ?
        <IconListItemRenderer
          size={props.size}
          icon={resolveIcon(props.displayMode)}
          enableDownload={props.displayMode === "Download"}
          downloadTitle={props.track.shortFileName}
          downloadLink={EmbeddedPlayerRequester.trackDownloadUrl(props.track)}
          label={NameTransformer.trackName(props.track)}
          onClick={() => {
            if (props.displayMode === "Play") {
              props.onPlay();
            }

            if (props.displayMode === "Favorite" && props.onAddToFavorite) {
              props.onAddToFavorite();
            }
          }}
        />
        : <LibraryListItemRenderer

          label={NameTransformer.trackName(props.track)}
          size={props.size}

          libraryItemArts={new LibraryItemArts(null, null, props.track.albumId)}
          onClick={() => {
            props.onPlay();
          }}
        />
    }
  </Stack>
};

export default TrackListItemRenderer;
