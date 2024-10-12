import {FC, ReactElement} from "react";
import {NavDropdown} from "react-bootstrap";
import SimpleMenuButton from "./SimpleMenuButton";

export class DropDownButton {
    constructor(public title: string, public icon: ReactElement, public path: string) { }
    public static of(title: string, icon: ReactElement, path: string): DropDownButton {
        return new DropDownButton(title, icon, path);
    }

    public static unique(title: string, icon: ReactElement, path: string): DropDownButton[] {
        return [DropDownButton.of(title, icon, path)]
    }
}


export type DropDownMenuButtonProps = {
    title: string,
    icon: ReactElement,
    path: string,
    dropDownButtons: DropDownButton[],
    onClick:() => void

}

const DropDownMenuButton: FC<DropDownMenuButtonProps> = ({ icon, title, path, dropDownButtons = [], onClick }) => {
    return <NavDropdown title={<>{icon} {title}</>} id="navbarScrollingDropdown" data-bs-theme="dark">
        <NavDropdown.Item  data-bs-theme="light"><SimpleMenuButton icon={icon} label={title} path={path} onClick={() => onClick()}></SimpleMenuButton></NavDropdown.Item>
        {dropDownButtons.map((aMenuButtonDesriptor, index) => <NavDropdown.Item key={index}>
            <SimpleMenuButton  icon={aMenuButtonDesriptor.icon} label={aMenuButtonDesriptor.title} path={aMenuButtonDesriptor.path} onClick={() => onClick()}></SimpleMenuButton>
            </NavDropdown.Item>)}
    </NavDropdown>
}

export default DropDownMenuButton;