import {FC, ReactElement} from "react";
import {Nav} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import UpdateSearchStringAction from "../../context/actions/UpdateSearchStringAction";

export type SimpleMenuButtonProps = {
    icon: ReactElement,
    label: string,
    path: string,
    onClick: () => void
}


const SimpleMenuButton: FC<SimpleMenuButtonProps> = ({ icon, label, path, onClick = () => { } }) => {

    const navigate = useNavigate();
    const { dispatch } = useHomeServerContext();
    return <Nav.Link onClick={() => {
        navigate(path);
        dispatch(UpdateSearchStringAction.clear());
        onClick();
    }}>{icon} {label}</Nav.Link>

}

export default SimpleMenuButton;