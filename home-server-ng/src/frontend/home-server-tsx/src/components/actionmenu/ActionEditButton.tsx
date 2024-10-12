import {FC} from "react";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {PencilSquare} from "react-bootstrap-icons";

const ActionEditButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Warning,
            icon: <PencilSquare />
        }
    );

}

export default ActionEditButton;