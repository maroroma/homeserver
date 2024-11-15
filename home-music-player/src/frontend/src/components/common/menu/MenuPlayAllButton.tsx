import {FC} from "react";
import {MenuClick} from "./MenuClick";
import {Button} from "react-bootstrap";
import {CollectionPlay} from "react-bootstrap-icons";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import CssTools from "../CssTools";

const MenuPlayAllButton: FC<MenuClick> = ({ onClick, disabled }) => {

    const {playerSubState} = useMusicPlayerContext();


    return <Button
        color="white"
        size="lg"
        variant="light"
        className={CssTools.of().disableOnPlayerStatus(playerSubState).css()}
        onClick={() => onClick()}
        disabled={disabled || playerSubState.isLoading}
    >
        <CollectionPlay size={30} />
    </Button>
}

export default MenuPlayAllButton;
