import {FC} from "react";
import {Upload} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";


const ActionUploadButton: FC<ActionBlockingButtonProps> = (props) => {

    return ActionBlockingButton(
        {
            ...props,
            variant: BootstrapVariants.Info,
            icon: <Upload />
        }
    );

}

export default ActionUploadButton;