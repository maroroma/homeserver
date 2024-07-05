import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {CollectionPlay} from "react-bootstrap-icons";

const MenuPlayAllButton: FC<MenuClick> = ({ onClick, disabled }) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()} disabled={disabled}><CollectionPlay size={30} /></Button>
}

export default MenuPlayAllButton;
