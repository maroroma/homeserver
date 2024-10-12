import {FC} from "react";
import {Download} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionDownloadButton: FC<ActionBlockingButtonProps> = (props) => {
    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Info,
            icon: <Download />
        }
    );
}

export default ActionDownloadButton;