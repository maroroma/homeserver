import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {CloudUpload} from "react-bootstrap-icons";

const MenuUploadButton: FC<MenuClick> = ({ onClick }) => {
    return <Button color="white" size="lg" variant="light" onClick={() => onClick()}><CloudUpload size={30} /></Button>
}

export default MenuUploadButton;
