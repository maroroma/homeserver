import {FC} from "react";
import {ClipboardPlus, Upload} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionFromClipBoard: FC<ActionBlockingButtonProps> = (props) => {

    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Info,
            icon: <ClipboardPlus />
        }
    );

}

export default ActionFromClipBoard;