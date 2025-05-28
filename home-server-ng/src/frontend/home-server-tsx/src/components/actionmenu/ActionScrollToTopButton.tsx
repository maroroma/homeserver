import {FC, useEffect, useState} from "react";
import {ArrowUpCircle} from "react-bootstrap-icons";
import ActionBlockingButton, {ActionBlockingButtonProps} from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import WindowTool from "../layouts/WindowTool";


const ActionScrollToTopButton: FC<ActionBlockingButtonProps> = (props) => {

    const [pageY, setPageY] = useState(0);

    useEffect(() => {
        return WindowTool.onScroll(event => setPageY(event));
    }, [])


    return ActionBlockingButton({
        ...props,
        variant: BootstrapVariants.Primary,
        icon: <ArrowUpCircle />,
        hidden: pageY < 50,
        onClick : () => WindowTool.scrollToTop()
    })
}

export default ActionScrollToTopButton;