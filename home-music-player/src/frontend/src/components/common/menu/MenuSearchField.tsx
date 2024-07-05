import {FC} from "react";
import {Button, Form} from "react-bootstrap";
import {Search, X} from "react-bootstrap-icons";

import "./HeaderMenuComponent.css"

export type MenuSearchFieldProps = {
    displaySearchField: boolean,
    searchText: string,
    onChange: (searchText: string) => void,
    onSwitch: () => void
    placeHolder: string


}


const MenuSearchField: FC<MenuSearchFieldProps> = ({ displaySearchField, searchText, onChange, onSwitch, placeHolder }) => {



    return <>
        <Form.Control
            className={`${displaySearchField ? "header-menu-search-field-show" : "header-menu-search-field-hidden"}`}
            placeholder={displaySearchField ? placeHolder : ""}
            aria-label={placeHolder}
            size="lg"
            value={searchText}
            onChange={(event) => onChange(event.target.value)}
        />
        <Button color="white" size="lg" variant="light" onClick={() => onSwitch()}>
            {displaySearchField ? <X size={40} /> : <Search size={30} />}

        </Button>
    </>




    // return <> {
    //     displaySearchField ?
    //         <>
    //             <Form.Control
    //                 placeholder={placeHolder}
    //                 aria-label={placeHolder}
    //                 size="lg"
    //                 value={searchText}
    //                 onChange={(event) => onChange(event.target.value)}
    //             />
    //             <Button color="white" size="lg" variant="light" onClick={() => onSwitch()}><X size={40} /></Button>
    //         </>
    //         :
    //         <Button color="white" size="lg" variant="light" onClick={() => onSwitch()}><Search size={40} /></Button>

    // }</>


}

export default MenuSearchField;