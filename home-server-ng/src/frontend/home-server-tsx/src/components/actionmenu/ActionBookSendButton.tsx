import {FC} from "react";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {Send} from "react-bootstrap-icons";


const ActionBookSendButton: FC<ActionBlockingButtonProps> = (props) => {

    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Success,
            icon: <Send />
        }
    )

}

export default ActionBookSendButton;