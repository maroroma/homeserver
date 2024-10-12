import {FC, ReactElement} from "react";
import {Button} from "react-bootstrap";

import "./BlockingButton.css";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";

export type BlockingButtonProps = {
    label: string
    variant?: BootstrapVariants,
    onClick?: () => void,
    disabled?: boolean,
    icon?: ReactElement,
    toastMessage?: string
}


const BlockingButton: FC<BlockingButtonProps> = ({
    label,
    variant = "primary",
    onClick = () => { },
    disabled = false,
    icon = <></>,
    toastMessage = "" }) => {

    const { workInProgress, dispatch } = useHomeServerContext();


    return <Button
        disabled={workInProgress || disabled}
        variant={variant}
        onClick={() => {
            dispatch(new StartWIPAction(toastMessage))
            onClick();
        }}>{workInProgress === true ? <div className="loader" /> : <></>}{icon} {label}</Button>
}

export default BlockingButton;