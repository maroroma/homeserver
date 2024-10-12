import {FC} from "react";
import {PlusCircle} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionPlusButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Warning,
            icon: <PlusCircle />
        }
    );
}

export default ActionPlusButton;