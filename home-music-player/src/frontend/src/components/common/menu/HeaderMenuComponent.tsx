import {FC} from "react";
import {InputGroup} from "react-bootstrap";

import "./HeaderMenuComponent.css";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import MenuPlayerRunning from "./MenuPlayerRunning";

export type HeaderMenuComponentProps = {
    children: any
}





const HeaderMenuComponent: FC<HeaderMenuComponentProps> = ({ children }) => {

    const { playerSubState } = useMusicPlayerContext();

    return <div className="header-menu">
        <InputGroup className="header-menu-input-group">
            {children}
            {playerSubState.display==="small" ? <MenuPlayerRunning/> : <></>}
        </InputGroup>
    </div>



}

export default HeaderMenuComponent;
