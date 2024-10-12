import {FC} from "react";
import Serie from "../../model/books/Serie";
import ActionBlockingButton from "./ActionBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {BookFill, BookHalf} from "react-bootstrap-icons";

export type ActionBookCompleteBookButtonProps = {
    serieToSwitch: Serie,
    onCompleteSerieClick: () => void,
    onUnCompleteSerieClick: () => void,
}


const ActionBookCompleteBookButton: FC<ActionBookCompleteBookButtonProps> = ({ serieToSwitch, onCompleteSerieClick, onUnCompleteSerieClick }) => {
    if (serieToSwitch.completed) {
        return <ActionBlockingButton variant={BootstrapVariants.Light} onClick={() => onUnCompleteSerieClick()} icon={<BookHalf />} />
    } else {
        return <ActionBlockingButton variant={BootstrapVariants.Light} onClick={() => onCompleteSerieClick()} icon={<BookFill />} />
    }

}

export default ActionBookCompleteBookButton;