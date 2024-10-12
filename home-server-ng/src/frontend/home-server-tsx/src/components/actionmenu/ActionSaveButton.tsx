import {FC} from "react";
import {Floppy} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionSaveButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton({
        ...props,
        variant: BootstrapVariants.Info,
        icon: <Floppy />
    })
}

export default ActionSaveButton;