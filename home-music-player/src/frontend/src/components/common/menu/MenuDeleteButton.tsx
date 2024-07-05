import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {Trash3} from "react-bootstrap-icons";

const MenuDeleteButton: FC<MenuClick> = ({ onClick, disabled = false }) => {
    return <Button color="red" size="lg" variant="light" onClick={() => onClick()} disabled={disabled}><Trash3 size={30} /></Button>
}

export default MenuDeleteButton;
