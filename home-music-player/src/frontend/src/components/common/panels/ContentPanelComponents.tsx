import {FC} from "react";


import "./ContentPanelComponents.css"

export type ContentPanelComponentsProps = {
    children: any
}

const ContentPanelComponents: FC<ContentPanelComponentsProps> = ({ children }) => {
    return <div className="content-panel">{children}</div>
}


export default ContentPanelComponents 