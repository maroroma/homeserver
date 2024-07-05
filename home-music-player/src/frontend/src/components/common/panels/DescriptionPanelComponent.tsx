import {FC} from "react";


import "./DescriptionPanelComponent.css"
import {Card} from "react-bootstrap";
import {LibraryItemArts} from "../../../api/model/library/LibraryItemArts";
import ThumbImage from "../ThumbImage";

export type DescriptionPanelComponentProps = {
    children: any,
    libraryItemArts?: LibraryItemArts
}

const DescriptionPanelComponent: FC<DescriptionPanelComponentProps> = ({ children, libraryItemArts }) => {


    return <div className="description-panel">
        <Card bg="dark" text="light" className="text-center description-card">
            <ThumbImage libraryItemArts={libraryItemArts} type="description"/>
            <Card.Body>
                <Card.Text className="description-card-text">
                    {children}
                </Card.Text>
            </Card.Body>
        </Card></div>
}


export default DescriptionPanelComponent 