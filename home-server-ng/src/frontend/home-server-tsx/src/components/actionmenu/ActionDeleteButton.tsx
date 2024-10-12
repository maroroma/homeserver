import {FC} from "react";
import {Trash} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionDeleteButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Danger,
            icon: <Trash />
        }
    );
}

export default ActionDeleteButton;