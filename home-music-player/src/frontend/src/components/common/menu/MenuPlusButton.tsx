import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {Plus} from "react-bootstrap-icons";

const MenuPlusButton: FC<MenuClick> = ({onClick}) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()}><Plus size={40} /></Button>
}

export default MenuPlusButton;
