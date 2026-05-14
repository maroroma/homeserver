import type { FC } from "react";
import { Image } from "react-bootstrap";
import type { LibraryListItemRendererProps } from "../renderers/LibraryListItemRenderer";
import ThumbComponent from "./ThumbComponent";

import "./ThumbComponent.css";
import "./VynilComponent.css";
import CssTools from "../../tools/CssTools";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";
import type { Album } from "../../api/model/library/Album";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";

type VynilComponentProps = {
    artistArt?: LibraryItemArts;
    album: Album;
}

const VynilComponent: FC<LibraryListItemRendererProps & VynilComponentProps> = ({ libraryItemArts }) => {

    const { playerStatus } = useMusicPlayerContext();

    return (
        <div className="vynil-container">
            <div className={CssTools.of("vynil-component")
                .if(playerStatus.playerStatus !== "PLAYING", "vynil-component-paused")
                .css()}>
                <Image
                    src="/vynil.svg"
                    className={
                        CssTools.of("disk")
                            .if(playerStatus.playerStatus === "PLAYING", "endless-rotation")
                            .if(playerStatus.playerStatus !== "PLAYING", "disk-paused")
                            .css()} />
                <Image src="/icochan.png" className={
                    CssTools.of("center-art")
                        .if(playerStatus.playerStatus === "PLAYING", "endless-rotation")
                        .if(playerStatus.playerStatus !== "PLAYING", "center-art-paused")
                        .css()} />
                <ThumbComponent libraryItemArts={libraryItemArts} size="xl" rounded={false} additionalCss="album-art" />
                <div className={CssTools.of("pin")
                    .if(playerStatus.playerStatus !== "PLAYING", "pin-paused")
                    .css()}></div>
            </div>
        </div>
    )

}

export default VynilComponent;