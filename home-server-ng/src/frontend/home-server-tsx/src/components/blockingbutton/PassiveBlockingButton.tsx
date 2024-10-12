import {FC} from "react";
import {Button} from "react-bootstrap";

import "./BlockingButton.css";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";

export type BlockingButtonProps = {
    label: string
    variant?: BootstrapVariants,
    onClick?: () => void
}


const PassiveBlockingButton: FC<BlockingButtonProps> = ({ label, variant = "primary", onClick = () => { } }) => {

    const { workInProgress } = useHomeServerContext();


    return <Button
        disabled={workInProgress}
        variant={variant}
        onClick={() => {
            onClick();
        }}>{label}</Button>
}

export default PassiveBlockingButton;