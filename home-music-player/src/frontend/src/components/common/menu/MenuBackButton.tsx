import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {ChevronLeft} from "react-bootstrap-icons";

const MenuBackButton: FC<MenuClick> = ({ onClick }) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()}><ChevronLeft size={35} /></Button>
}

export default MenuBackButton;
