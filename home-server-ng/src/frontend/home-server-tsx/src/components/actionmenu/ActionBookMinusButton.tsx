import {FC} from "react";
import {BookmarkDash} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionBookMinusButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton({
        ...props,
        variant: BootstrapVariants.Danger,
        icon: <BookmarkDash />
    })
}

export default ActionBookMinusButton;