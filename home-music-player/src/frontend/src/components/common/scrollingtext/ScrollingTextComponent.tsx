import {FC} from "react";

import "./ScrollingTextComponent.css"

export type ScrollingTextComponentProps = {
    text: string,
    scrolling?: boolean,
    blinking?: boolean
}

const ScrollingTextComponent: FC<ScrollingTextComponentProps> = ({ text, scrolling = true, blinking = false }) => {
    return <div className={`scrolling_text ${blinking ? "blinkable" : ""}`}>
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