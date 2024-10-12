import {FC, ReactElement} from "react";
import {Button} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";
import {Question} from "react-bootstrap-icons";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


export type ActionBlockingButtonProps = {
    onClick?: () => void,
    blockingButton?: boolean,
    icon?: ReactElement,
    variant?: BootstrapVariants,
    toastMessage?: string,
    disabled?: boolean,
    hidden?: boolean
}


const ActionBlockingButton: FC<ActionBlockingButtonProps> = ({ onClick = () => { },
    variant = "primary",
    blockingButton = false,
    icon = <Question />,
    toastMessage = "",
    disabled = false,
    hidden = false
}) => {
    const { workInProgress, dispatch } = useHomeServerContext();

    if (hidden) {
        return <></>
    }

    return <Button
        disabled={(workInProgress && blockingButton) || disabled}
        variant={variant}
        onClick={() => {
            if (blockingButton) {
                dispatch(new StartWIPAction(toastMessage))
            }
            onClick();
        }}>{icon}</Button>
}

export default ActionBlockingButton;