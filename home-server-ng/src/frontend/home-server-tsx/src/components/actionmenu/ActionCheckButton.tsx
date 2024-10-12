import {FC} from "react";
import {CheckCircle} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionCheckButton: FC<ActionBlockingButtonProps> = (props) => {

    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Success,
            icon: <CheckCircle />
        }
    );

}

export default ActionCheckButton;