import {FC} from "react";
import {BookmarkPlus} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionBookPlusButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Warning,
            icon: <BookmarkPlus />
        }
    )
}

export default ActionBookPlusButton;