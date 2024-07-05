import {FC} from "react";

import "./ScrollingTextComponent.css"

export type ScrollingTextComponentProps = {
    text: string,
    scrolling?: boolean
}

const ScrollingTextComponent: FC<ScrollingTextComponentProps> = ({ text, scrolling = true}) => {
    return <div className="scrolling_text">
        <div className={`text ${scrolling ? "" : "scrolling-off"}`}>
            <span>{text}</span>
            <span>{text}</span>
            <span>{text}</span>
            <span>{text}</span>
        </div>
        <div className={`text ${scrolling ? "" : "scrolling-off"}`}>
            <span>{text}</span>
            <span>{text}</span>
            <span>{text}</span>
            <span>{text}</span>
        </div>
    </div>
}

export default ScrollingTextComponent;