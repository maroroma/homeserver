import type { FC } from "react";
import { Image } from "react-bootstrap";
import type { LibraryListItemRendererProps } from "../renderers/LibraryListItemRenderer";
import ThumbComponent from "./ThumbComponent";

import "./ThumbComponent.css";
import "./VynilComponent.css";
import CssTools from "../../tools/CssTools";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";
import type { Album } from "../../api/model/library/Album";

type VynilComponentProps = {
    artistArt?: LibraryItemArts;
    album: Album;
}

const VynilComponent: FC<LibraryListItemRendererProps & VynilComponentProps> = ({ libraryItemArts }) => {
    return (
        <div>
            <div className="vynil-component">
                <Image src="/vynil.svg" className={CssTools.of("disk").then("endless-rotation").css()} />
                <Image src="/icochan.png" className="center-art endless-rotation" />
                <ThumbComponent libraryItemArts={libraryItemArts} size="xl" rounded={false} additionalCss="album-art" />
                <div className="pin"></div>
            </div>
        </div>
    )

}

export default VynilComponent;