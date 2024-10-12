import {FC} from "react";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {DashSquare} from "react-bootstrap-icons";

const ActionUnselectAllButton: FC<ActionBlockingButtonProps> = (props) => {

    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Primary,
            icon: <DashSquare />
        }
    );


}

export default ActionUnselectAllButton;