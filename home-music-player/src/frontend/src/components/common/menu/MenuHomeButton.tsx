import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {HouseDoor} from "react-bootstrap-icons";

const MenuHomeButton: FC<MenuClick> = ({ onClick }) => {
    return <Button size="lg" variant="light" onClick={() => onClick()}><HouseDoor size={30} /></Button>
}

export default MenuHomeButton;
