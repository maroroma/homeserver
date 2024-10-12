import {FC, useState} from "react";
import {Container, Form, Nav, Navbar} from "react-bootstrap";
import {ArrowLeftRight, Bell, Book, BookmarkPlus, Boxes, BoxSeam, FolderSymlink, InfoCircle, ListUl, Tools, UiRadios} from "react-bootstrap-icons";
import SimpleMenuButton from "./SimpleMenuButton";
import BootstrapBreakPoints from "../bootstrap/BootstrapBreakPoints";
import DropDownMenuButton, {DropDownButton} from "./DropDownMenuButton";
import "./MenuComponent.css";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import UpdateSearchStringAction from "../../context/actions/UpdateSearchStringAction";

const MenuComponent: FC = () => {

    const { searchString, dispatch } = useHomeServerContext()

    const [menuExpanded, setMenuExpanded] = useState(true);

    const booksMenuButtons =
        DropDownButton.unique("ajouter", <BookmarkPlus />, "/books/add/serieselection")

    const administrationMenuButtons = [
        DropDownButton.of("status", <InfoCircle />, "/administration/status"),
        DropDownButton.of("tasks", <UiRadios />, "/administration/tasks"),
        DropDownButton.of("events", <ListUl />, "/administration/events")
    ]

    const seedBoxMenuButtons = DropDownButton.unique("todo", <ArrowLeftRight />, "/seedbox/todo")


    return <Navbar bg="dark" data-bs-theme="dark" sticky="top"
        expand={BootstrapBreakPoints.EXTRA_EXTRA_LARGE.value}
        expanded={menuExpanded}
        onToggle={(value) => setMenuExpanded(value)}>

        <Container>
            <Navbar.Brand href="#home">HomeServerTSX</Navbar.Brand>
            <Navbar.Toggle></Navbar.Toggle>
            <Navbar.Collapse>
                <Nav className="me-auto">
                    <DropDownMenuButton title="Administration" icon={<Tools />} path="/administration/properties" dropDownButtons={administrationMenuButtons} onClick={() => setMenuExpanded(false)} />
                    <SimpleMenuButton icon={<Bell />} label="Buzzer" path="/buzzer" onClick={() => setMenuExpanded(false)}></SimpleMenuButton>
                    <SimpleMenuButton icon={<FolderSymlink />} label="Files" path="/files/filemanager" onClick={() => setMenuExpanded(false)}></SimpleMenuButton>
                    <SimpleMenuButton icon={<Boxes />} label="Lego" path="/buzzer" onClick={() => setMenuExpanded(false)}></SimpleMenuButton>
                    <DropDownMenuButton title="Seedbox" icon={<BoxSeam />} path="/seedbox" dropDownButtons={seedBoxMenuButtons} onClick={() => setMenuExpanded(false)} />
                    <DropDownMenuButton title="Books" icon={<Book />} path="/books/allbooks" dropDownButtons={booksMenuButtons} onClick={() => setMenuExpanded(false)} />
                </Nav>
                <Form className="d-flex" onSubmit={(event) => {
                    event.preventDefault();
                    event.stopPropagation();
                }}>
                    <Form.Control
                        type="search"
                        placeholder="Rechercher"
                        className="me-2"
                        aria-label="Search"
                        value={searchString}
                        onChange={(event) => dispatch(new UpdateSearchStringAction(event.target.value))}
                        onSubmit={(event) => {
                            event.preventDefault();
                            event.stopPropagation();
                        }}
                    />
                </Form>
            </Navbar.Collapse>
        </Container>
    </Navbar>
}

export default MenuComponent;