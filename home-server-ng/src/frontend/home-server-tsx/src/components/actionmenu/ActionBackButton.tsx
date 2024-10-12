import {FC} from "react";
import {ChevronLeft} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionBackButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton({
        ...props,
        variant: BootstrapVariants.Primary,
        icon: <ChevronLeft />
    })
}

export default ActionBackButton;