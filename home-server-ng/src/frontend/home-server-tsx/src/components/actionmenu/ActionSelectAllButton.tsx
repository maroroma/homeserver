import {FC} from "react";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {Check2All} from "react-bootstrap-icons";

const ActionSelectAllButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Primary,
            icon: <Check2All />
        }
    );

}

export default ActionSelectAllButton;