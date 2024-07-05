import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {People} from "react-bootstrap-icons";

const MenuAllArtistsButton: FC<MenuClick> = ({onClick}) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()}><People size={30} /></Button>
}

export default MenuAllArtistsButton;
