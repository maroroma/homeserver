import {FC} from "react";
import {ChevronRight} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionNextButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton({
        ...props,
        variant: BootstrapVariants.Primary,
        icon: <ChevronRight />
    })
}

export default ActionNextButton;