import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {Pencil} from "react-bootstrap-icons";

const MenuEditButton: FC<MenuClick> = ({onClick}) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()}><Pencil size={30} /></Button>
}

export default MenuEditButton;
