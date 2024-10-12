import {FC, useEffect, useState} from "react";
import {Button, ButtonGroup} from "react-bootstrap";
import {ThreeDotsVertical} from "react-bootstrap-icons";

import "./ActionMenuComponent.css";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import CssTools from "../bootstrap/CssTools";


export type ActionMenuComponentProps = {
    children: any,
    alreadyOpen?: boolean,
    small?: boolean
}



const ActionMenuComponent: FC<ActionMenuComponentProps> = ({ children, alreadyOpen = false, small = false }) => {


    const [displayAllButtons, setDisplayAllButtons] = useState(false);

    const switchDisplayAllButtons = () => {
        setDisplayAllButtons(!displayAllButtons);
    }

    useEffect(() => {
        setDisplayAllButtons(alreadyOpen)
    }, [alreadyOpen])


    return <div className={CssTools.of("action-menu").verticallyCentered().css()}>
        <div >
        </div>
        <ButtonGroup size={small ? "sm" : undefined}>
            {displayAllButtons ? children : <></>}
            <Button className="round-button" variant={BootstrapVariants.Dark} onClick={() => switchDisplayAllButtons()}><ThreeDotsVertical /></Button>
        </ButtonGroup>
    </div>
}

export default ActionMenuComponent;