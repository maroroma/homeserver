import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {ArrowClockwise} from "react-bootstrap-icons";

const MenuReloadButton: FC<MenuClick> = ({onClick}) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()}><ArrowClockwise size={35} /></Button>
}

export default MenuReloadButton;
