import {FC} from "react";
import {ArrowClockwise} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionUpdateButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Info,
            icon: <ArrowClockwise />
        }
    );
}

export default ActionUpdateButton;